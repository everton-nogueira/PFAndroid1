package mobile.iesb.br.projetofinal.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import mobile.iesb.br.projetofinal.R

/**
 * Created by everton on 15/05/18.
 */
class CarrosselView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    val timeMillisLoop: Long = 2500
    var imagens = intArrayOf(R.drawable.noticia, R.drawable.noticia2, R.drawable.noticia3)
    var index: Int = 0

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        var paint = Paint()
        val bmp = BitmapFactory.decodeResource(resources, imagens[index])
        val matrix = Matrix()

        var escalaX: Float = width.toFloat() / bmp.width.toFloat()
        var escalaY: Float = height.toFloat() / bmp.height.toFloat()

        matrix.postScale(escalaX, escalaY)
        val imagemBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, true)
        canvas.drawColor(Color.TRANSPARENT)

        canvas.drawBitmap(imagemBitmap, 0f, 0f, paint)

        postInvalidateDelayed(timeMillisLoop)

        index++
        if (index >= imagens.size) {
            index = 0
        }
    }
}