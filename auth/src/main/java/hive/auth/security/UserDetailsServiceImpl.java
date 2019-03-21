package hive.auth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  private RestTemplate restTemplate;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    var hiveUser =
        restTemplate.getForObject("http://repository-service/user/" + username, hive.repository.model.User.class);

    if (hiveUser == null) {
      throw new UsernameNotFoundException("Username: " + username + " not found");
    }

    var grantedAuthorities =
        AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_" + hiveUser.getRole());

    return new User(hiveUser.getUsername(), hiveUser.getPassword(), grantedAuthorities);
  }
}
