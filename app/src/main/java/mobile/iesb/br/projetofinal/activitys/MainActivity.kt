package mobile.iesb.br.projetofinal.activitys

import android.arch.persistence.room.Room
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import android.widget.Toast
import mobile.iesb.br.projetofinal.R
import mobile.iesb.br.projetofinal.dao.AppDatabase
import mobile.iesb.br.projetofinal.entidade.Usuario
import mobile.iesb.br.projetofinal.util.ValidaUtil
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.content_main.*
import mobile.iesb.br.projetofinal.util.ResourcesUtil
import java.io.ByteArrayOutputStream


class MainActivity : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null
    var db: AppDatabase? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var sessao = getSharedPreferences("username", Context.MODE_PRIVATE)

        textViewCadastro.setOnClickListener {
            val myIntent = Intent(this, CadastroActivity::class.java)
            startActivity(myIntent)
        }

        textViewEsqueceuSenha.setOnClickListener {
            val myIntent = Intent(this, EsqueceuSenhaActivity::class.java)
            startActivity(myIntent)
        }

        buttonEntrar.setOnClickListener {
            var email = findViewById<EditText>(R.id.editTextEmailLogin)
            var senha = findViewById<EditText>(R.id.editTextSenhaLogin)

            mAuth = FirebaseAuth.getInstance()
            if(validaInputs(email, senha)){
                mAuth?.signInWithEmailAndPassword(email.text.toString(), senha.text.toString())?.addOnCompleteListener(this, {task ->
                    if(!task.isSuccessful){
                        Toast.makeText(applicationContext, "Dados Incorretos", Toast.LENGTH_LONG).show()
                    }else{
                        var user: FirebaseUser = task.result.user

                        if(user != null) {
                            var editor = sessao.edit()
                            editor.putString("emailLogin", email.text.toString())
                            editor.commit()
                            val myIntent = Intent(this, HomeActivity::class.java)
                            startActivity(myIntent)
                        }
                    }
                })
            }else{
                Toast.makeText(applicationContext, "Dados Incorretos", Toast.LENGTH_LONG).show()
            }
        }

        cadastraUsuario()
    }

    private fun validaInputs(email: EditText, senha: EditText): Boolean {
        var isEmailValido = ValidaUtil.isEmailValido(email)
        var isSenhaVazia = ValidaUtil.isEmpty(senha)
        return isEmailValido && !isSenhaVazia
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun cadastraUsuario() {
        db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "room-database"
        ).allowMainThreadQueries().build()
        var email = "admin@admin.com"
        var senha = "admin"
        var usuarioAdmin = db?.usuarioDao()?.findByEmail(email)
        if (usuarioAdmin == null) {
            mAuth?.createUserWithEmailAndPassword(email, senha)?.addOnCompleteListener(this, { task ->
                if (!task.isSuccessful) {
                    Toast.makeText(applicationContext, "Ocorreu um erro ao salvar o usuario", Toast.LENGTH_LONG).show()
                }else{
                    db?.usuarioDao()?.insertUsuario(Usuario(0, "admin", email, ResourcesUtil.getImagem(resources, R.drawable.avatar), senha, 0, 6199999999))
                }
            })
        }
    }


}
