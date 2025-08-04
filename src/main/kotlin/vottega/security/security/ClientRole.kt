package vottega.security.security

enum class ClientRole {
  USER,
  PARTICIPANT;

  val roleName: String
    get() = "ROLE_$name"
}