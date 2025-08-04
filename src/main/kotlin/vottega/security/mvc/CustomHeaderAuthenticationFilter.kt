package vottega.security.mvc

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import vottega.security.security.ClientRole
import vottega.security.security.CustomParticipantRoleAuthenticationToken
import vottega.security.security.CustomUserRoleAuthenticationToken
import vottega.security.security.SecurityProps
import java.util.*

@Component
class CustomHeaderAuthenticationFilter(
  private val props: SecurityProps
) : OncePerRequestFilter() {

  override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
    val headerRole = request.getHeader(props.headerNames.role)
    println(request.getHeader(props.headerNames.userId))
    val userId = request.getHeader(props.headerNames.userId)?.toLongOrNull()
    val participantUUID = try {
      UUID.fromString(request.getHeader(props.headerNames.participantId))
    } catch (e: Exception) {
      null
    }
    val roomId = request.getHeader(props.headerNames.roomId)?.toLongOrNull()
    val role = try {
      ClientRole.valueOf(headerRole)
    } catch (e: Exception) {
      null
    }
    if (
      role == null ||
      (role == ClientRole.PARTICIPANT && (participantUUID == null || roomId == null)) ||
      (role == ClientRole.USER && userId == null)
    ) {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
      return
    }

    val authentication: Authentication = when {
      role == ClientRole.USER -> CustomUserRoleAuthenticationToken(userId!!)
      role == ClientRole.PARTICIPANT -> CustomParticipantRoleAuthenticationToken(
        participantUUID!!,
        roomId!!
      )

      else -> {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
        return
      }
    }
    SecurityContextHolder.getContext().authentication = authentication
    filterChain.doFilter(request, response)

  }

  override fun shouldNotFilter(request: HttpServletRequest) =
    !props.filterPaths.any { request.servletPath.startsWith(it) }
}