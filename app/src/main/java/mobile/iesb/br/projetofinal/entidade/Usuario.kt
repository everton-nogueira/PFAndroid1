package mobile.iesb.br.projetofinal.entidade

import android.os.Build
import android.support.annotation.RequiresApi
import java.io.File
import java.io.Serializable
import java.util.*

data class Usuario (
    var uid: String = "",
    var nome: String = "",
    var email: String = "",
//  var foto: String = "",
    var senha: String = "",
    var matricula: Long = 0,
    var telefone: Long = 0) : Serializable {

    @RequiresApi(Build.VERSION_CODES.O)
    fun converteToBase64(filePath: String) : String    {
        val bytes = File(filePath).readBytes()
        val base64 = Base64.getEncoder().encodeToString(bytes)

        return base64
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun converteBase64ParaBytes(base64: String, pathFile: String): Unit {
        val imageByteArray = Base64.getDecoder().decode(base64)
        File(pathFile).writeBytes(imageByteArray)
    }


//    fun retornaBitMapImage() : Bitmap {
//        var bytes = android.util.Base64.decode(foto, android.util.Base64.DEFAULT)
//        return BitmapFactory.decodeByteArray(bytes,0, bytes.size)
//    }
}