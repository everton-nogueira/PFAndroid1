package mobile.iesb.br.projetofinal.activitys

import android.arch.persistence.room.Room
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import mobile.iesb.br.projetofinal.R
import mobile.iesb.br.projetofinal.dao.AppDatabase
import mobile.iesb.br.projetofinal.entidade.Usuario
import mobile.iesb.br.projetofinal.util.ValidaUtil
import java.io.File
import java.util.*
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import java.io.ByteArrayOutputStream


class MainActivity : AppCompatActivity() {

    var db: AppDatabase? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var sessao = getSharedPreferences("username", Context.MODE_PRIVATE)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            val myIntent = Intent(this, CadastroActivity::class.java)
            startActivity(myIntent)
        }

        var esqueceuSenha = findViewById<TextView>(R.id.textViewEsqueceuSenha)

        esqueceuSenha.setOnClickListener { view ->
            val myIntent = Intent(this, EsqueceuSenhaActivity::class.java)
            startActivity(myIntent)
        }


        var botaoEntrar = findViewById<TextView>(R.id.buttonEntrar)

        botaoEntrar.setOnClickListener { view ->
            var email = findViewById<EditText>(R.id.editTextEmailLogin)
            var senha = findViewById<EditText>(R.id.editTextSenhaLogin)
            if (validaInputs(email, senha) && isUsuarioValido(email, senha)) {
                var editor = sessao.edit();
                editor.putString("emailLogin", email.text.toString())
                editor.commit()
                val myIntent = Intent(this, HomeActivity::class.java)
                startActivity(myIntent)
            }
        }

        db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "room-database"
        ).allowMainThreadQueries().build()

        cadastraUsuario()
    }

    private fun isUsuarioValido(email: EditText, senha: EditText): Boolean {
        var usuario = db?.usuarioDao()?.findByEmailSenha(email.text.toString(), senha.text.toString())
        if (usuario == null) {
            Toast.makeText(applicationContext, "Dados Incorretos", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun validaInputs(email: EditText, senha: EditText): Boolean {
        var isEmailValido = ValidaUtil.isEmailValido(email)
        var isSenhaVazia = ValidaUtil.isEmpty(senha)
        return isEmailValido && !isSenhaVazia
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun cadastraUsuario() {
        var email = "admin@admin.com"
        var senha = "senha"
        var usuarioAdmin = db?.usuarioDao()?.findByEmailSenha(email, senha)

        if (usuarioAdmin == null) {
            db?.usuarioDao()?.insertUsuario(Usuario(0, "admin", email, getImagem(), senha, 0, 6199999999))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getImagem(): String{
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.avatar)
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
        val base64 = android.util.Base64.encodeToString(stream.toByteArray(), android.util.Base64.DEFAULT)
        bitmap.recycle()
        return base64
    }



}
