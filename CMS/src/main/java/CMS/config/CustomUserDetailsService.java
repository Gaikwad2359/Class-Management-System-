package CMS.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import CMS.DAO.UserRepo;
import CMS.Master.StudentMaster;

@Component
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepo userRepo;

	@Override
	 @Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		StudentMaster student = userRepo.findByEmail(username);
		System.out.println(student);
		if (student == null) {
			throw new UsernameNotFoundException("user not found");
		} else {
			return new CustomUser(student);
		}	

	}

}
