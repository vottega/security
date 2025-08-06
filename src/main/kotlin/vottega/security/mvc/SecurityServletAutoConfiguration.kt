package vottega.security.mvc

import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import vottega.security.security.SecurityProps

@AutoConfiguration
@ConditionalOnClass(HttpSecurity::class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableConfigurationProperties(SecurityProps::class)
@ConditionalOnProperty(prefix = "vottega.security", name = ["enabled"], havingValue = "true", matchIfMissing = true)
class SecurityServletAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  fun customHeaderAuthenticationFilter(props: SecurityProps) =
    CustomHeaderAuthenticationFilter(props)

  @Bean
  @Order(0)
  fun securityFilterChain(
    http: HttpSecurity,
    filter: CustomHeaderAuthenticationFilter,
    props: SecurityProps
  ): SecurityFilterChain {
    http.csrf { it.disable() }
      .logout { it.disable() }
      .anonymous { it.disable() }
      .formLogin { it.disable() }
      .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
      .authorizeHttpRequests {
        if (props.permitPaths.isNotEmpty()) {
          it.requestMatchers(*props.permitPaths.toTypedArray()).permitAll()
        }
        it.requestMatchers(*props.filterPaths.toTypedArray()).authenticated()
          .anyRequest().permitAll()
      }
      .addFilterAt(filter, BasicAuthenticationFilter::class.java)
    return http.build()
  }
}