package mobile.iesb.br.projetofinal.activitys

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import mobile.iesb.br.projetofinal.R
import mobile.iesb.br.projetofinal.entidade.Noticia

class DetalhaNoticiaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalha_noticia)
        var item = intent.extras.get("itemSelecionado") as Noticia
        findViewById<ImageView>(R.id.imageViewNoticia).setImageBitmap(item.retornaBitMapImage())
        findViewById<TextView>(R.id.tituloNoticia).text = item.titulo
        findViewById<TextView>(R.id.corpoNoticia).text = item.texto
        findViewById<TextView>(R.id.dataNoticia).text = item.getDataString()


        var voltar = findViewById<Button>(R.id.buttonVoltar)
        voltar.setOnClickListener { view ->
            finish()
        }

    }

    public override fun onDestroy() {
        super.onDestroy()
    }
}