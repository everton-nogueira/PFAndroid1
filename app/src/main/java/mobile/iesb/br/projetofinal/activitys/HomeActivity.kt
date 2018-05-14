package mobile.iesb.br.projetofinal.activitys

import android.arch.persistence.room.Room
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.*
import mobile.iesb.br.projetofinal.R
import mobile.iesb.br.projetofinal.dao.AppDatabase
import mobile.iesb.br.projetofinal.entidade.Noticia
import java.io.ByteArrayOutputStream
import java.util.*

class HomeActivity : AppCompatActivity() {

    var db: AppDatabase? = null
    val TEXTO = "Lorem Ipsum é simplesmente uma simulação de texto da indústria tipográfica e de impressos, e vem sendo utilizado desde o século XVI, quando um impressor desconhecido pegou uma bandeja de tipos e os embaralhou para fazer um livro de modelos de tipos. Lorem Ipsum sobreviveu não só a cinco séculos, como também ao salto para a editoração eletrônica, permanecendo essencialmente inalterado. Se popularizou na década de 60, quando a Letraset lançou decalques contendo passagens de Lorem Ipsum, e mais recentemente quando passou a ser integrado a softwares de editoração eletrônica como Aldus PageMaker."

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "room-database"
        ).allowMainThreadQueries().build()


        var noticias = cadastraNoticia()

        val listView = findViewById<ListView>(R.id.listNoticias)
        listView.adapter = NoticiaListAdapter(this, noticias!!)
        listView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
            val myIntent = Intent(this, DetalhaNoticiaActivity::class.java)
            myIntent.putExtra("itemSelecionado", adapterView.getItemAtPosition(position) as Noticia)
            startActivity(myIntent)
        }

    }

    public override fun onDestroy() {
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.editarPerfil -> {
                val myIntent = Intent(this, EditarPerfilActivity::class.java)
                startActivity(myIntent)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun cadastraNoticia(): List<Noticia>? {
        var noticias = db?.noticiaDao()?.findAll()

        if (noticias?.size == 0) {

            db?.noticiaDao()?.insertNoticia(Noticia(0, "Titulo1", Date(), TEXTO, getImagem()))
            db?.noticiaDao()?.insertNoticia(Noticia(0, "Titulo2", Date(), TEXTO, getImagem()))
            db?.noticiaDao()?.insertNoticia(Noticia(0, "Titulo3", Date(), TEXTO, getImagem()))
            db?.noticiaDao()?.insertNoticia(Noticia(0, "Titulo4", Date(), TEXTO, getImagem()))
            db?.noticiaDao()?.insertNoticia(Noticia(0, "Titulo5", Date(), TEXTO, getImagem()))
            db?.noticiaDao()?.insertNoticia(Noticia(0, "Titulo6", Date(), TEXTO, getImagem()))
            db?.noticiaDao()?.insertNoticia(Noticia(0, "Titulo7", Date(), TEXTO, getImagem()))
            db?.noticiaDao()?.insertNoticia(Noticia(0, "Titulo8", Date(), TEXTO, getImagem()))
            db?.noticiaDao()?.insertNoticia(Noticia(0, "Titulo9", Date(), TEXTO, getImagem()))
            db?.noticiaDao()?.insertNoticia(Noticia(0, "Titulo10", Date(), TEXTO, getImagem()))

            noticias = db?.noticiaDao()?.findAll()
        }

        return noticias?.sortedByDescending { it.uid }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getImagem(): String{
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.noticia)
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
        val base64 = android.util.Base64.encodeToString(stream.toByteArray(), android.util.Base64.DEFAULT)
        bitmap.recycle()
        return base64
    }

}

private class NoticiaListAdapter(paramContexto: Context, paramNoticias: List<Noticia>) : BaseAdapter() {

    private val contexto: Context
    private var noticias: List<Noticia>

    init {
        contexto = paramContexto
        noticias = paramNoticias
    }

    override fun getItem(position: Int): Any {
        return noticias.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return noticias.size
    }

    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
        val layoutInflater = LayoutInflater.from(contexto)
        val noticiaRow = layoutInflater.inflate(R.layout.content_home, viewGroup, false)

        val tituloTextView = noticiaRow.findViewById<TextView>(R.id.textViewTituloNoticia)
        tituloTextView.text = noticias.get(position).titulo

        val dataTextView = noticiaRow.findViewById<TextView>(R.id.textViewDataNoticia)
        dataTextView.text = noticias.get(position).getDataString();

        val descricaotextView = noticiaRow.findViewById<TextView>(R.id.textViewTextoNoticia)
        descricaotextView.text = noticias.get(position).texto

        val imageViewNoticia = noticiaRow.findViewById<ImageView>(R.id.imageViewImagemNoticia)
        imageViewNoticia.setImageBitmap(noticias.get(position).retornaBitMapImage())

        return noticiaRow
    }
}
