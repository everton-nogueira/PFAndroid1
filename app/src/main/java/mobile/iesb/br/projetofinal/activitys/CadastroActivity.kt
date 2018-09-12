package mobile.iesb.br.projetofinal.activitys

import android.arch.persistence.room.Room
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.content_cadastro.*
import mobile.iesb.br.projetofinal.R
import mobile.iesb.br.projetofinal.dao.AppDatabase
import mobile.iesb.br.projetofinal.entidade.Usuario
import mobile.iesb.br.projetofinal.util.ResourcesUtil
import mobile.iesb.br.projetofinal.util.ValidaUtil

class CadastroActivity : AppCompatActivity() {

    var db: AppDatabase? = null
    var mAuth: FirebaseAuth? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        buttonCadastrar.setOnClickListener {
            cadastraUsuario()
        }
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "room-database").allowMainThreadQueries().build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun cadastraUsuario() {
        val email = findViewById<EditText>(R.id.editTextEmailCadastro)
        val senha = findViewById<EditText>(R.id.editTextSenhaCadastro)
        val senhaConfirmar = findViewById<EditText>(R.id.editTextConfirmaSenhaCadastro)

        if (ValidaUtil.isEmailValido(email) && ValidaUtil.isPasswordValido(senha) && ValidaUtil.isPasswordValido(senhaConfirmar)) {
            if(!senha.text.toString().equals(senhaConfirmar.text.toString())) {
                Toast.makeText(applicationContext, "As senhas não conferem.", Toast.LENGTH_LONG).show()
                return
            }

            if (this.isEmailExistente()) {
                Toast.makeText(applicationContext, "Usuário já cadastrado.", Toast.LENGTH_LONG).show()
            } else {
                insereUsuario(email, senha, senhaConfirmar)
            }
        }
    }

    private fun isEmailExistente(): Boolean {
        return db?.usuarioDao()?.findByEmail(editTextEmailCadastro.text.toString()) != null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun insereUsuario(email: EditText, senha: EditText, senhaConfirmar: EditText) {
        mAuth = FirebaseAuth.getInstance()
        mAuth?.createUserWithEmailAndPassword(email.text.toString(), senha.text.toString())?.addOnCompleteListener(this, { task ->
            if (!task.isSuccessful) {
                Toast.makeText(applicationContext, "Ocorreu um erro ao salvar o usuario", Toast.LENGTH_LONG).show()
            }else{
                db?.usuarioDao()?.insertUsuario(Usuario(0, email.text.toString().split('@')[0], email.text.toString(), ResourcesUtil.getImagem(resources, R.drawable.avatar), senha.text.toString(), 0, 0))
                email.text.clear()
                senha.text.clear()
                senhaConfirmar.text.clear()
                Toast.makeText(applicationContext, "Usuário cadastrado!", Toast.LENGTH_LONG).show()
                finish()
            }
        })?.addOnFailureListener(this, {error ->
            Toast.makeText(applicationContext, error.message, Toast.LENGTH_LONG).show()
        })
    }

}
