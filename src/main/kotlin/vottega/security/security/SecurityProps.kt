package vottega.security.security

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "vottega.security")
data class SecurityProps(
  val filterPaths: List<String> = listOf("/**"),
  val permitPaths: List<String> = emptyList(),
  val headerNames: HeaderNames = HeaderNames()
) {
  data class HeaderNames(
    var role: String = "X-Client-Role",
    var userId: String = "X-User-Id",
    var participantId: String = "X-Participant-Id",
    var roomId: String = "X-Room-Id"
  )
}