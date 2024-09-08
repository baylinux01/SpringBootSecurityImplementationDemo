package baylinux01.securityDemo.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import baylinux01.securityDemo.filters.JWTFilter;



@Configuration
@EnableWebSecurity
public class SecurityConfig{
	
	private UserDetailsService userDetailsService;
	private JWTFilter jWTFilter;
	
	@Autowired
	public SecurityConfig(UserDetailsService userDetailsService,JWTFilter jWTFilter) {
		super();
		this.userDetailsService = userDetailsService;
		this.jWTFilter=jWTFilter;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
	{
		return http
				.csrf(customizer->customizer.disable())
				.authorizeHttpRequests(req->req
						.requestMatchers("/appUser/register","/appUser/login").permitAll()
						//.requestMatchers("/appUser/**").hasRole("USER")
						.anyRequest().authenticated())
			  //.formLogin(Customizer.withDefaults())
				.httpBasic(Customizer.withDefaults())
				.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jWTFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
				
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() 
	{
		DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
		provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
		provider.setUserDetailsService(userDetailsService);
		return provider;
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception
	{
		return config.getAuthenticationManager();
		
	}
	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder(12);
	}
//	@Bean
//	public UserDetailsService userDetailsService()
//	{
//		UserDetails inMemoryUser=User
//										.builder()
//										.username("inMemoryUser")
//										.password("$2a$12$RrtH5At9LG0UOipBkO2EiOnhRVSJ..6X01XJ0ZB4scs4CIHRfWKxO")
//										.roles("USER","ADMIN")
//										.build();
//		UserDetails inMemoryUser2=User
//				.builder()
//				.username("inMemoryUser2")
//				.password("$2a$12$RrtH5At9LG0UOipBkO2EiOnhRVSJ..6X01XJ0ZB4scs4CIHRfWKxO")
//				.roles("USER")
//				.build();	
//		return new InMemoryUserDetailsManager(inMemoryUser,inMemoryUser2);
//		
//	}

}