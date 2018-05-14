package mobile.iesb.br.projetofinal.dao

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import mobile.iesb.br.projetofinal.converters.DateConverter
import mobile.iesb.br.projetofinal.entidade.Noticia
import mobile.iesb.br.projetofinal.entidade.Usuario

@Database(entities = [Usuario::class, Noticia::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDAO

    abstract fun noticiaDao(): NoticiaDAO
}