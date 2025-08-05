package vottega.security.mvc.argument

import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass(WebMvcConfigurer::class)
class MvcArgumentResolverAutoConfiguration {

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
  ) = object : WebMvcConfigurer {
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
      resolvers.add(userId)
      resolvers.add(participantId)
      resolvers.add(roomId)
    }
  }
}