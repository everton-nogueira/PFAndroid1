package mobile.iesb.br.projetofinal.entidade

import java.util.Date

/**
 * Created by everton on 13/09/18.
 */
data class Mensagem(
        var texto: String = "",
        var sender: String = "",
        var data: Long = 0L
)