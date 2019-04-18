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

    @GetMapping
    public Iterable<User> buscar() {
        return users.findAll();
    }

}