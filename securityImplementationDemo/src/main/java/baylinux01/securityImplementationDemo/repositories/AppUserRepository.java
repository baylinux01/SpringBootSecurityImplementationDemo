package baylinux01.securityImplementationDemo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import baylinux01.securityImplementationDemo.entities.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Long>{

	AppUser findByUsername(String username);

}
