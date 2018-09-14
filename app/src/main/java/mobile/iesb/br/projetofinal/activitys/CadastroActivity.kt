package mobile.iesb.br.projetofinal.activitys

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.content_cadastro.*
import mobile.iesb.br.projetofinal.R
import mobile.iesb.br.projetofinal.entidade.Usuario
import mobile.iesb.br.projetofinal.util.ValidaUtil
import java.util.*

class CadastroActivity : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        buttonCadastrar.setOnClickListener {
            cadastraUsuario()
        }

        mAuth = FirebaseAuth.getInstance()
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

            insereUsuario(email, senha, senhaConfirmar)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun insereUsuario(email: EditText, senha: EditText, senhaConfirmar: EditText) {
        mAuth?.createUserWithEmailAndPassword(email.text.toString(), senha.text.toString())?.addOnCompleteListener(this, { task ->
            if (!task.isSuccessful) {
                Toast.makeText(applicationContext, "Ocorreu um erro ao salvar o usuario", Toast.LENGTH_LONG).show()
            }else{
                val db = FirebaseDatabase.getInstance()
                val uuid = mAuth?.currentUser?.uid
                val dbRef = db.getReference("/usuario/$uuid")
                val user = Usuario(uuid.toString(), "", email.text.toString(), "", 0, 0)
                dbRef.setValue(user)

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
