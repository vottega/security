package vottega.security.mvc.argument

import org.springframework.core.MethodParameter
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import vottega.security.security.CustomParticipantRoleAuthenticationToken
import vottega.security.security.ParticipantId
import java.util.*

@Component
class ParticipantIdArgumentResolver : HandlerMethodArgumentResolver {
  override fun supportsParameter(parameter: MethodParameter): Boolean {
    return parameter.hasParameterAnnotation(ParticipantId::class.java)
      && parameter.parameterType == UUID::class.java
  }

  override fun resolveArgument(
    parameter: MethodParameter,
    mavContainer: ModelAndViewContainer?,
    webRequest: NativeWebRequest,
    binderFactory: WebDataBinderFactory?
  ): Any? {
    val authentication: Authentication? = SecurityContextHolder
      .getContext()
      .authentication
    if (authentication is CustomParticipantRoleAuthenticationToken) {
      return authentication.principal
    } else {
      throw BadCredentialsException(
        "Participant 권한이 없습니다."
      )
    }
  }

}