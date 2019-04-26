package hive.pokedex.controller;

import hive.entity.user.User;
import hive.pokedex.exception.EntityNotFoundException;
import hive.pokedex.exception.NullValueException;
import hive.pokedex.exception.UsernameAlreadyExistsException;
import hive.pokedex.repository.UserRepository;
import hive.pokedex.util.FillNullValues;
import hive.pokedex.util.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/user")
public class UserController {

  private final String ROLE = "ADMIN";

  private UserRepository userRepository;

  @Autowired
  public UserController(UserRepository userRepository){
    this.userRepository=userRepository;
  }

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

    if (foundUsers.size() == 0) {
      throw new EntityNotFoundException();
    }

    return foundUsers;
  }

  @PostMapping
  public User save(
      @RequestParam(required = false) Integer id,
      @RequestParam(required = false) String username,
      @RequestParam(required = false) String password
  ) {

    var user = new User(username, password, ROLE);

    if (id != null) {
      if (!userRepository.existsById(id)) {
        throw new EntityNotFoundException();
      }

      var userPersisted = userRepository.getOne(id);

      FillNullValues.copyProperties(user, userPersisted);
    }

    if (!Validation.isValid(user.getUsername()) ||
        !Validation.isValid(user.getPassword())) {
      throw new NullValueException();
    }else if (userRepository.existsByUsername(username)) {
      throw new UsernameAlreadyExistsException();
    }

    userRepository.save(user);

    return user;
  }

  @DeleteMapping
  public void deleteById(@RequestParam int id) {
    if (!userRepository.existsById(id)) {
      throw new EntityNotFoundException();
    }

    userRepository.deleteById(id);
  }

}
