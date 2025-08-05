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
import vottega.security.security.RoomId


@Component
class RoomIdArgumentResolver : HandlerMethodArgumentResolver {
  override fun supportsParameter(parameter: MethodParameter): Boolean {
    return parameter.hasParameterAnnotation(RoomId::class.java)
      && parameter.parameterType == Long::class.java
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
      return authentication.roomId
    } else {
      throw BadCredentialsException(
        "Participant 권한이 없습니다."
      )
    }
  }

}