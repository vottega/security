package vottega.security.webflux.argument

import org.springframework.core.MethodParameter
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.reactive.BindingContext
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import vottega.security.security.CustomUserRoleAuthenticationToken
import vottega.security.security.UserId

@Component
class UserIdArgumentResolver : HandlerMethodArgumentResolver {
  override fun supportsParameter(parameter: MethodParameter): Boolean =
    parameter.hasParameterAnnotation(UserId::class.java)
      && parameter.parameterType == Long::class.java


  override fun resolveArgument(
    parameter: MethodParameter,
    bindingContext: BindingContext,
    exchange: ServerWebExchange
  ): Mono<Any> {
    return ReactiveSecurityContextHolder.getContext()
      .flatMap { ctx ->
        when (val auth = ctx.authentication) {
          is CustomUserRoleAuthenticationToken -> Mono.just(auth.principal)
          else -> Mono.error(
            ResponseStatusException(HttpStatus.FORBIDDEN, "User 권한이 없습니다.")
          )
        }
      }
  }
}