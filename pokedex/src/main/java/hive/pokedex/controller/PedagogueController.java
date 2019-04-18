package hive.pokedex.controller;

import hive.entity.user.Pedagogue;
import hive.entity.user.Person;
import hive.entity.user.User;
import hive.pokedex.repository.PedagogueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityTransaction;

@RestController
@RequestMapping("/pedagogue")
public class PedagogueController {

  @Autowired
  private PedagogueRepository pedagogues;

  @GetMapping
  public Iterable<Pedagogue> findAll() {
    return pedagogues.findAll();
  }

  @GetMapping("/get{rm}")
  public Pedagogue findByName(@PathVariable String rm){
    return pedagogues.findByRm(rm);
  }

  @PostMapping
  public Pedagogue save(
      @RequestParam String name,
      @RequestParam String rm,
      @RequestParam String username,
      @RequestParam String password,
      @RequestParam String role
  ) {
    Pedagogue pedagogue = new Pedagogue(rm);

    Person person = new Person(name);
    person.setUser(new User(username,password,role));

    pedagogue.setPerson(person);

    pedagogues.save(pedagogue);
    return pedagogue;
  }

}