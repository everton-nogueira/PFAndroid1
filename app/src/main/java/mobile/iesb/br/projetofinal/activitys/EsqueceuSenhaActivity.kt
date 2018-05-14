package mobile.iesb.br.projetofinal.activitys

import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import mobile.iesb.br.projetofinal.R
import mobile.iesb.br.projetofinal.dao.AppDatabase
import mobile.iesb.br.projetofinal.util.ValidaUtil

class EsqueceuSenhaActivity : AppCompatActivity() {

    var db: AppDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_esqueceu_senha)


        var botaoRecuperar = findViewById<Button>(R.id.buttonRecuperar)

        botaoRecuperar.setOnClickListener { view ->
            var email = findViewById<EditText>(R.id.editTextEmailRecuperaSenha)

            if (ValidaUtil.isEmailValido(email)) {
                if (this.isEmailExistente()) {
                    Toast.makeText(applicationContext, "E-mail de recuperação enviado com sucesso.", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(applicationContext, "E-mail não cadastrado.", Toast.LENGTH_LONG).show()
                    email.text.clear()
                }
            }
        }

        db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "room-database"
        ).allowMainThreadQueries().build()
    }

    private fun isEmailExistente(): Boolean {
        var email = findViewById<EditText>(R.id.editTextEmailRecuperaSenha)

        return db?.usuarioDao()?.findByEmail(email.text.toString()) != null

    }

}
