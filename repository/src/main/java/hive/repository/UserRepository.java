package hive.repository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;

import hive.repository.model.User;

public class UserRepository {
  public static UserRepository instance = new UserRepository();

  private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

  private List<User> users = Arrays.asList(
      new User(1, "victor", encoder.encode("123"), "Victor", "ADMIN"),
      new User(2, "samuel", encoder.encode("123"), "Samuel", "PEDAGOGUE"),
      new User(3, "german", encoder.encode("123"), "Germano", "STUDENT")
  );

  private UserRepository() {
  }

  public User findUserByUsername(String username) {
    for (var u :
        users) {
      if (u.getUsername().equals(username)) {
        return new User(
            u.getId(),
            u.getUsername(),
            u.getPassword(),
            u.getName(),
            u.getRole()
        );
      }
    }

    return null;
  }
}
