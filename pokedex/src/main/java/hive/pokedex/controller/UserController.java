package hive.pokedex.controller;

import hive.entity.user.User;
import hive.pokedex.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired
  private UserRepository userRepository;

  @GetMapping
  public List<User> find(
      @RequestParam(required = false) Integer id,
      @RequestParam(required = false) String username,
      @RequestParam(required = false) String password,
      @RequestParam(required = false) String role
  ) {
    var user = new User(username, password, role);
    user.setId(id);

    List<User> foundUsers = userRepository.findAll(Example.of(user));
    return foundUsers;
  }

  @PostMapping
  public User save(
      @RequestParam(required = false) Integer id,
      @RequestParam(required = false) String username,
      @RequestParam(required = false) String password,
      @RequestParam(required = false) String role
  ) {

    var user = new User(username,password,role);

    if (id != null) {
      user.setId(id);
    }

    userRepository.save(user);

    return user;
  }

  @DeleteMapping
  public void deleteById(@RequestParam int id) {
    userRepository.deleteById(id);
  }

}