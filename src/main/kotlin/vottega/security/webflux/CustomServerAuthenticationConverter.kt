package vottega.security.webflux

import org.springframework.security.core.Authentication
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import vottega.security.security.ClientRole
import vottega.security.security.CustomParticipantRoleAuthenticationToken
import vottega.security.security.CustomUserRoleAuthenticationToken
import vottega.security.security.SecurityProps
import java.util.*

class CustomServerAuthenticationConverter(
  private val props: SecurityProps
) : ServerAuthenticationConverter {

  override fun convert(exchange: ServerWebExchange): Mono<Authentication> {
    val headers = exchange.request.headers
    val roleHeader = headers.getFirst(props.headerNames.role) ?: return Mono.empty()

    val role = runCatching { ClientRole.valueOf(roleHeader) }.getOrNull() ?: return Mono.empty()

    return when (role) {
      ClientRole.USER ->
        headers.getFirst(props.headerNames.userId)
          ?.toLongOrNull()
          ?.let { Mono.just(CustomUserRoleAuthenticationToken(it)) }
          ?: Mono.empty()


      ClientRole.PARTICIPANT -> {
        val participantId = headers.getFirst(props.headerNames.participantId)
          ?.let { runCatching { UUID.fromString(it) }.getOrNull() }
        val roomId = headers.getFirst(props.headerNames.roomId)?.toLongOrNull()

        if (participantId != null && roomId != null)
          Mono.just(CustomParticipantRoleAuthenticationToken(participantId, roomId))
        else Mono.empty()
      }
    }
  }

}