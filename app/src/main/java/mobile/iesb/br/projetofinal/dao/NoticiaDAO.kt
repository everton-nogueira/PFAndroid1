package mobile.iesb.br.projetofinal.dao

import android.arch.persistence.room.*
import mobile.iesb.br.projetofinal.entidade.Noticia

@Dao
interface NoticiaDAO {
    @Insert
    fun insertNoticia(noticia: Noticia)

    @Update
    fun alteraNoticia(noticia: Noticia)

    @Delete
    fun removeNoticia(noticia: Noticia)

    @Query("SELECT * FROM NOTICIA")
    fun findAll(): List<Noticia>
}