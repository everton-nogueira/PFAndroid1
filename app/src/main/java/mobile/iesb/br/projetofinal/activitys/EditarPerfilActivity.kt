package mobile.iesb.br.projetofinal.activitys

import android.Manifest
import android.app.AlertDialog
import android.arch.persistence.room.Room
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.Toast
import mobile.iesb.br.projetofinal.R
import mobile.iesb.br.projetofinal.dao.AppDatabase
import mobile.iesb.br.projetofinal.util.ValidaUtil
import kotlinx.android.synthetic.main.content_editar_perfil.*;
import mobile.iesb.br.projetofinal.entidade.Usuario
import java.io.ByteArrayOutputStream
import java.io.IOException

class EditarPerfilActivity : AppCompatActivity() {

    var db: AppDatabase? = null
    val CAMERA = 0
    val GALERIA = 1

    val GALLERY_REQUEST_CODE = 1
    val CAMERA_REQUEST_CODE =  2

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_perfil)

        imageView.setOnClickListener {
            showPictureDialog()
        }

        backButton.setOnClickListener{
            finish()
        }


        db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "room-database"
        ).allowMainThreadQueries().build()

        var sessao = getSharedPreferences("username", Context.MODE_PRIVATE)
        var email = sessao.getString("emailLogin", " ")
        var usuario = db?.usuarioDao()?.findByEmail(email)

        imageView.setImageBitmap(usuario?.retornaBitMapImage())
        textViewEmailUsuario.text = usuario?.email;
        editTextNomeUsuario.setText(usuario?.nome)
        editTextMatricula.setText(usuario?.matricula.toString())
        editTextTelefone.setText(usuario?.telefone.toString())

        buttonAlterar.setOnClickListener{
            alteraUsuario(usuario)
        }

    }

    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle("Selecione")
        val pictureDialogItems = arrayOf("Camera", "Galeria")
        pictureDialog.setItems(pictureDialogItems
        ) { dialog, which ->
            when (which) {
                CAMERA -> takePhotoFromCamera()
                GALERIA -> choosePhotoFromGallary()
            }
        }
        pictureDialog.show()
    }

    fun choosePhotoFromGallary() {
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest(Manifest.permission.READ_EXTERNAL_STORAGE, GALLERY_REQUEST_CODE)
        }else {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
        }
    }

    private fun takePhotoFromCamera() {
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest(Manifest.permission.CAMERA, CAMERA_REQUEST_CODE)
        }else{
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, CAMERA_REQUEST_CODE)
        }
    }

    private fun makeRequest(permissao: String, codigo: Int) {
        ActivityCompat.requestPermissions(this, arrayOf(permissao), codigo)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if (!grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent, CAMERA_REQUEST_CODE)
                } else {
                    Toast.makeText(this, "Permissão não concedida!", Toast.LENGTH_SHORT).show()
                }
                return
            }
            GALLERY_REQUEST_CODE -> {
                if (!grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
                } else {
                    Toast.makeText(this, "Permissão não concedida!", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun alteraUsuario(usuario: Usuario?) {
        var senhaAlterada = true
        if(editTextSenhaCadastro.text.isEmpty() && editTextConfirmaSenhaCadastro.text.isEmpty()){
            senhaAlterada = false
        }
        if(senhaAlterada){
            if(ValidaUtil.isPasswordValido(editTextSenhaCadastro) && ValidaUtil.isPasswordValido(editTextConfirmaSenhaCadastro)) {
                if (!editTextSenhaCadastro.text.toString().equals(editTextConfirmaSenhaCadastro.text.toString())) {
                    Toast.makeText(applicationContext, "As senhas não conferem.", Toast.LENGTH_LONG).show()
                    return
                }else {
                    usuario?.senha = editTextSenhaCadastro.text.toString()
                }
            }
        }
        usuario?.nome = editTextNomeUsuario.text.toString()
        usuario?.email = textViewEmailUsuario.text.toString()
        usuario?.foto = imageToBase64(imageView)
        usuario?.matricula = editTextMatricula.text.toString().toLong()
        usuario?.telefone = editTextTelefone.text.toString().toLong()
        db?.usuarioDao()?.alteraUsuario(usuario)

        Toast.makeText(this, "Usuário alterado com Sucesso", Toast.LENGTH_SHORT).show()

        //this.finish()
    }


    public override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_REQUEST_CODE) {
            if (data != null) {
                val contentURI = data!!.data
                try {
                    imageView!!.setImageBitmap(MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI))
                }
                catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Erro ao recuperar imagem!", Toast.LENGTH_SHORT).show()
                }
            }
        } else if (requestCode == CAMERA) {
            imageView!!.setImageBitmap(data!!.extras!!.get("data") as Bitmap)
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        when (requestCode) {
//            CAMERA_REQUEST_CODE -> {
//                if (resultCode == Activity.RESULT_OK && data != null) {
//                    imageView.setImageBitmap(data.extras.get("data") as Bitmap)
//                }
//            }
//            GALLERY_REQUEST_CODE -> {
//                if (resultCode == Activity.RESULT_OK && data != null) {
//                    imageView.setImageBitmap(data.extras.get("data") as Bitmap)
//                }
//            }
//            else -> {
//                Toast.makeText(this, "Comando não conhecido", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    fun abrirCamera(abreCamera:Intent){
        startActivityForResult(abreCamera, CAMERA_REQUEST_CODE)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun imageToBase64(image: ImageView): String {
        val bitmap = (image.drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)

        val base64 = android.util.Base64.encodeToString(stream.toByteArray(), android.util.Base64.DEFAULT)

        bitmap.recycle()
        return base64
    }

}
