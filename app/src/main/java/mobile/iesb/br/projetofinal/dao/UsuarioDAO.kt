package mobile.iesb.br.projetofinal.dao

import android.arch.persistence.room.*
import mobile.iesb.br.projetofinal.entidade.Usuario

@Dao
interface UsuarioDAO {
    @Insert
    fun insertUsuario(usuario: Usuario)

    @Update
    fun alteraUsuario(usuario: Usuario?)

    @Delete
    fun removeUsuario(usuario: Usuario)

    @Query("SELECT * FROM USUARIO")
    fun findAll(): List<Usuario>

    @Query("SELECT * FROM USUARIO WHERE email like :emailParametro and senha like :senhaParametro")
    fun findByEmailSenha(emailParametro : String, senhaParametro : String) : Usuario

    @Query("SELECT * FROM USUARIO WHERE email like :emailParametro")
    fun findByEmail(emailParametro : String) : Usuario
}