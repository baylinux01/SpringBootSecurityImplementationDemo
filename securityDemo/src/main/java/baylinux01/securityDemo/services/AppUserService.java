package baylinux01.securityDemo.services;

import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import baylinux01.securityDemo.Entities.AppUser;
import baylinux01.securityDemo.Entities.LoginDTO;
import baylinux01.securityDemo.repositories.AppUserRepository;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.InvalidKeyException;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class AppUserService{

	AppUserRepository appUserRepository;
	AuthenticationManager authenticationManager;
	JWTService jWTService;
	PasswordEncoder passwordEncoder;
	

	


	@Autowired
	public AppUserService(AppUserRepository appUserRepository, AuthenticationManager authenticationManager,
			JWTService jWTService, PasswordEncoder passwordEncoder) {
		super();
		this.appUserRepository = appUserRepository;
		this.authenticationManager = authenticationManager;
		this.jWTService = jWTService;
		this.passwordEncoder = passwordEncoder;
	}


	public AppUser getAppUserByUsername(String username) {
		return appUserRepository.findByUsername(username);
	}
	



	public AppUser getAppUserById(long id) {
		// TODO Auto-generated method stub
		return appUserRepository.findById(id).orElse(null);
	}



	public List<AppUser> getAllAppUsers() {
		// TODO Auto-generated method stub
		return appUserRepository.findAll();
	}



	public String createAppUser(String username, String password) {
		// TODO Auto-generated method stub
		List<AppUser> appUsers=getAllAppUsers();
		int i=0;
		while(i<appUsers.size())
		{
			if(appUsers.get(i).getUsername().equals(username))
				return "appuser with this username already exists";
			i++;
		}
		AppUser appUser=new AppUser();
		appUser.setUsername(username);
		appUser.setPassword(passwordEncoder.encode(password));
		List<SimpleGrantedAuthority> authorities=new ArrayList<SimpleGrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("USER"));
		//authorities.add(new SimpleGrantedAuthority("ADMIN"));
		appUser.setAuthorities(authorities);
		appUserRepository.save(appUser);
		return "success";
	}



	public LoginDTO login(String username, String password) throws InvalidKeyException, NoSuchAlgorithmException {
		// TODO Auto-generated method stub
		Authentication authentication =authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(username,password));
		if(authentication.isAuthenticated()) 
		{
			String jwt=jWTService.generateToken(username);
			AppUser appUser=(AppUser) appUserRepository.findByUsername(username);
			if(appUser!=null)
			{
				LoginDTO dto=new LoginDTO();
				dto.setJwt(jwt);
				dto.setAppUserId(appUser.getId());
				return dto;
			}return null;
			
		}
		else return null;
	}


	public String updateAppUserPassword(HttpServletRequest request,String newPassword) {
		
/*jwt olmadan requestten kullanıcı adını alma kodları başlangıcı*/		
//		Principal pl=request.getUserPrincipal();
//		String username=pl.getName();
/*jwt olmadan requestten kullanıcı adını alma kodları sonu*/
		
/*jwt ile requestten kullanıcı adını alma kodları başlangıcı*/
		String authHeader=request.getHeader("Authorization");
		String jwt=null;
		String username=null;
		
		if(authHeader!=null && authHeader.startsWith("Bearer "))
		{
			//token=authHeader.substring("Bearer ".length());
			jwt=authHeader.substring(7);
			
					try {
						username=jWTService.extractUsername(jwt);
					} catch (JwtException | IllegalArgumentException | NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
		}
		else return "fail";
/*jwt ile requestten kullanıcı adını alma kodları sonu*/
		
		
		if(username!=null)
		{
		AppUser user=appUserRepository.findByUsername(username);
		user.setPassword(passwordEncoder.encode(newPassword));
		appUserRepository.save(user);
		return "success";
		}
		else return "fail";
		
	}


	public String updateAppUserUsername(HttpServletRequest request, String newUsername) {
/*jwt olmadan requestten kullanıcı adını alma kodları başlangıcı*/
//		Principal pl=request.getUserPrincipal();
//		String username=pl.getName();
/*jwt olmadan requestten kullanıcı adını alma kodları sonu*/	
		
/*jwt ile requestten kullanıcı adını alma kodları başlangıcı*/
		String authHeader=request.getHeader("Authorization");
		String jwt=null;
		String username=null;
		
		if(authHeader!=null && authHeader.startsWith("Bearer "))
		{
			//token=authHeader.substring("Bearer ".length());
			jwt=authHeader.substring(7);
			
					try {
						username=jWTService.extractUsername(jwt);
					} catch (JwtException | IllegalArgumentException | NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
		}
		else return "fail";
/*jwt ile requestten kullanıcı adını alma kodları sonu*/	
		
		if(username!=null)
		{
			List<AppUser> users=appUserRepository.findAll();
			for(AppUser u:users)
			{
				if(u.getUsername().equals(username)) return "username already exists";
			}
			AppUser user=appUserRepository.findByUsername(username);
			user.setUsername(username);
			appUserRepository.save(user);
			return "success";
		}
		else return "fail";
		
	}







	

}
