package mobile.iesb.br.projetofinal.activitys

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import mobile.iesb.br.projetofinal.R
import mobile.iesb.br.projetofinal.entidade.Usuario

class ListagemUsuariosChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listagem_usuarios_chat)

        var usuarioRef = FirebaseDatabase.getInstance().getReference()
        var usuarios = mutableListOf<Usuario>()
        var mAuth = FirebaseAuth.getInstance()

        var usuario:Usuario
        usuarioRef.child("usuario").addValueEventListener(object: ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot){

                for(usuarioSnap: DataSnapshot in dataSnapshot.children) {

                    usuario = Usuario()

                    usuario.nome = usuarioSnap.child("nome").value.toString()
                    usuario.email = usuarioSnap.child("email").value.toString()

                    if(!mAuth.currentUser!!.email.toString().equals(usuario.email)) {
                        usuarios?.add(usuario!!)
                    }
                }

                onFinish(usuarios)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private val onFinish: (List<Usuario>) -> Unit = { usuarios ->
        val listRecyclerView = findViewById<RecyclerView>(R.id.recyclerViewListaUsuario)

        listRecyclerView.itemAnimator = DefaultItemAnimator()
        listRecyclerView.layoutManager = LinearLayoutManager(this)
        listRecyclerView.adapter = UsuarioListAdapter(this, usuarios!!)
    }


    public override fun onDestroy() {
        super.onDestroy()
    }
}

class UsuarioListAdapter(paramContexto: Context, paramNoticias: List<Usuario>) : RecyclerView.Adapter<UsuarioViewHolder>() {

    private val contexto: Context
    private var usuarios: List<Usuario>

    init {
        contexto = paramContexto
        usuarios = paramNoticias
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val contentHomeitemView = LayoutInflater.from(this.contexto).inflate(R.layout.content_listagem_usuarios_chat, parent, false)
        return UsuarioViewHolder(contentHomeitemView)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {

        val item = this.usuarios[position]
        holder.textViewEmailUsuario.text = item.nome +" - "+ item.email

        holder.bind(usuarios[position], itemOnClick)
    }

    private val itemOnClick: (Usuario) -> Unit = { usuario ->
        val intent = Intent(this.contexto, ChatActivity::class.java)
        intent.putExtra("usuarioSelecionado", usuario)
        this.contexto.startActivity(intent)
    }

    override fun getItemCount(): Int {
        return this.usuarios.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}

class UsuarioViewHolder(contentView: View) : RecyclerView.ViewHolder(contentView) {

    var textViewEmailUsuario: TextView
    var _contentView:View

    init{

        textViewEmailUsuario = contentView.findViewById(R.id.textViewEmailUsuario)
        _contentView = contentView
    }

    fun bind(usuario: Usuario, clickListener: (Usuario) -> Unit) {
        _contentView.setOnClickListener { clickListener(usuario)}
    }
}