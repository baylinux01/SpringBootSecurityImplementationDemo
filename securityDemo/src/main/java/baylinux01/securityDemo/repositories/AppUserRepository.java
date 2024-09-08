package baylinux01.securityDemo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import baylinux01.securityDemo.Entities.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Long>{

	AppUser findByUsername(String username);

}
