package baylinux01.securityDemo.filters;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import baylinux01.securityDemo.services.AppUserService;
import baylinux01.securityDemo.services.JWTService;
import baylinux01.securityDemo.services.UserDetailsServiceImplementation;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTFilter extends OncePerRequestFilter{

	private JWTService jWTService;
	private ApplicationContext applicationContext;
	private UserDetailsServiceImplementation userDetailsServiceImplementation;
	
	
	
	

	@Autowired
	public JWTFilter(JWTService jWTService, ApplicationContext applicationContext,
			UserDetailsServiceImplementation userDetailsServiceImplementation) {
		super();
		this.jWTService = jWTService;
		this.applicationContext = applicationContext;
		this.userDetailsServiceImplementation = userDetailsServiceImplementation;
	}






	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String authHeader=request.getHeader("Authorization");
		String token=null;
		String username=null;
		if(authHeader==null || !authHeader.startsWith("Bearer "))
			{
				filterChain.doFilter(request, response);
				return;
			}
		if(authHeader!=null && authHeader.startsWith("Bearer "))
		{
			//token=authHeader.substring("Bearer ".length());
			token=authHeader.substring(7);
			
				
					try {
						username=jWTService.extractUsername(token);
					} catch (JwtException | IllegalArgumentException | NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
			
		}
		
		if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null)
		{
			//UserDetails userDetails=applicationContext.getBean(MyUserDetailsService.class).loadUserByUsername(username);
			UserDetails userDetails=userDetailsServiceImplementation.loadUserByUsername(username);
			
				
					try {
						if(jWTService.isTokenValid(token))
						{
							UsernamePasswordAuthenticationToken authToken= 
									new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
							authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
							SecurityContextHolder.getContext().setAuthentication(authToken);
						}
					} catch (JwtException | IllegalArgumentException | NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
			
		}
		filterChain.doFilter(request,response);
	}

}
