package hive.pokedex.controller;

import hive.entity.user.User;
import hive.pokedex.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository users;

    @GetMapping("/get/{username}")
    public User buscar(@PathVariable final String username) {
        return users.findByUsername(username);
    }

}