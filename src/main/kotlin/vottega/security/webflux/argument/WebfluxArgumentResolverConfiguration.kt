package vottega.security.webflux.argument

import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer

@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@ConditionalOnClass(WebFluxConfigurer::class)
class WebfluxArgumentResolverConfiguration {

  @Bean
  @ConditionalOnMissingBean
  fun userIdArgumentResolver(): UserIdArgumentResolver =
    UserIdArgumentResolver()

  @Bean
  @ConditionalOnMissingBean
  fun participantIdArgumentResolver(): ParticipantIdArgumentResolver =
    ParticipantIdArgumentResolver()

  @Bean
  @ConditionalOnMissingBean
  fun roomIdArgumentResolver(): RoomIdArgumentResolver =
    RoomIdArgumentResolver()

  @Bean
  fun mvcConfigurer(
    userId: UserIdArgumentResolver,
    participantId: ParticipantIdArgumentResolver,
    roomId: RoomIdArgumentResolver
  ) = object : WebFluxConfigurer {
    override fun configureArgumentResolvers(configurer: ArgumentResolverConfigurer) {
      configurer.addCustomResolver(userId)
      configurer.addCustomResolver(participantId)
      configurer.addCustomResolver(roomId)
    }
  }
}