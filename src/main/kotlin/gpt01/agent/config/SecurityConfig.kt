package gpt01.agent.config

import gpt01.agent.config.handler.Oauth2FailureHandler
import gpt01.agent.config.handler.Oauth2SuccessHandler
import gpt01.agent.config.jwt.JwtFilter
import gpt01.agent.service.CustomOauth2UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val customOauth2UserService: CustomOauth2UserService,
    private val successHandler : Oauth2SuccessHandler,
    private val failureHandler : Oauth2FailureHandler,
    private val jwtFilter : JwtFilter,
) {

    /*
    filterChain 구성
     */
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .cors {  }
            .authorizeHttpRequests {
                it.requestMatchers("/oauth2/**", "/auth/**").permitAll()
                    .anyRequest().authenticated()
            }
            .oauth2Login {
                it.userInfoEndpoint { endpoint ->
                    endpoint.userService(customOauth2UserService)
                }
                .successHandler(successHandler)
                .failureHandler(failureHandler)
            }
            .exceptionHandling {
                it.authenticationEntryPoint {_, response, _ ->
                    response.sendError(400)
                }
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }

    /*
    cors 관련 설정
     */
    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("https://localhost:5173")
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE")
        configuration.allowedHeaders = listOf("*")
        configuration.allowCredentials = true

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}