package hive.repository.controller;

import hive.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hive.common.entity.User;

@RestController
@RequestMapping("/user")
public class UserController {
  @GetMapping("/{username}")
  public User getUserByUsername(@PathVariable final String username) {
    return UserRepository.instance.findUserByUsername(username);
  }
}
