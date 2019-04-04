package hive.auth.security;

import hive.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  // -----------------------------------------------------------------------------------
  // hardcode some users
  private final BCryptPasswordEncoder encoder;
  // -----------------------------------------------------------------------------------

  private final UserRepository userRepository;

  @Autowired
  public UserDetailsServiceImpl(UserRepository userRepository, BCryptPasswordEncoder encoder) {
    this.userRepository = userRepository;

    // hardcode some users
    userRepository.save(new hive.entity.user.User("admin", encoder.encode("admin"), 0, 0));
    userRepository.save(new hive.entity.user.User("ped", encoder.encode("ped"), 0, 0));
    userRepository.save(new hive.entity.user.User("stu", encoder.encode("stu"), 0, 0));
    this.encoder = encoder;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    var hiveUser = userRepository.findByUsername(username);

    if (hiveUser == null) {
      throw new UsernameNotFoundException("Username: " + username + " not found");
    }

    var grantedAuthorities =
        AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_" + hiveUser.getRole());

    return new User(hiveUser.getUsername(), hiveUser.getPassword(), grantedAuthorities);
  }
}
