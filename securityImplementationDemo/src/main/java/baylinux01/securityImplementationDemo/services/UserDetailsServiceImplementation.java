package baylinux01.securityImplementationDemo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import baylinux01.securityImplementationDemo.repositories.AppUserRepository;

@Service
public class UserDetailsServiceImplementation  implements UserDetailsService{

	AppUserRepository appUserRepository;
	
	@Autowired
	public UserDetailsServiceImplementation(AppUserRepository appUserRepository) {
		super();
		this.appUserRepository = appUserRepository;
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return (UserDetails) appUserRepository.findByUsername(username);
	}

}
