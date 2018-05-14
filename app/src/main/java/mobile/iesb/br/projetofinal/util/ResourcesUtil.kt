package mobile.iesb.br.projetofinal.util

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.support.annotation.RequiresApi
import java.io.ByteArrayOutputStream

class ResourcesUtil {

    companion object {

        @RequiresApi(Build.VERSION_CODES.O)
        fun getImagem(resources: Resources, imagem: Int): String{
            val bitmap = BitmapFactory.decodeResource(resources, imagem)
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
            val base64 = android.util.Base64.encodeToString(stream.toByteArray(), android.util.Base64.DEFAULT)
            bitmap.recycle()
            return base64
        }

    }


}
