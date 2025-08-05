package vottega.security.webflux

import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.core.annotation.Order
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers
import vottega.security.security.SecurityProps

@AutoConfiguration
@ConditionalOnClass(ServerHttpSecurity::class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@EnableConfigurationProperties(SecurityProps::class)
@ConditionalOnProperty(prefix = "vottega.security", name = ["enabled"], havingValue = "true", matchIfMissing = true)
class SecurityWebFluxAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  fun headerAuthConverter(props: SecurityProps): ServerAuthenticationConverter =
    CustomServerAuthenticationConverter(props)

  @Bean
  @ConditionalOnMissingBean
  fun reactiveAuthManager(): ReactiveAuthenticationManager = PassThroughAuthManager()

  @Bean
  @Order(0)
  fun securityWebFilterChain(
    http: ServerHttpSecurity,
    converter: ServerAuthenticationConverter,
    authManager: ReactiveAuthenticationManager,
    props: SecurityProps
  ): SecurityWebFilterChain {

    val headerAuthFilter = AuthenticationWebFilter(authManager).apply {
      setServerAuthenticationConverter(converter)
      setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers(*props.filterPaths.toTypedArray()))
    }

    return http
      .csrf { it.disable() }
      .httpBasic { it.disable() }
      .formLogin { it.disable() }
      .logout { it.disable() }
      .authorizeExchange {
        it.pathMatchers(*props.filterPaths.toTypedArray()).authenticated()
        it.anyExchange().permitAll()
      }
      .addFilterAt(headerAuthFilter, SecurityWebFiltersOrder.AUTHENTICATION)
      .build()
  }
}