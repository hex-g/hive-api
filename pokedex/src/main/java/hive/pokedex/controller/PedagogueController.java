package hive.pokedex.controller;

import hive.entity.user.Pedagogue;
import hive.entity.user.Person;
import hive.entity.user.User;
import hive.pokedex.repository.PedagogueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedagogue")
public class PedagogueController {

  @Autowired
  private PedagogueRepository pedagogueRepository;

  @GetMapping
  public List<Pedagogue> find(
      @RequestParam(required = false) Integer id,
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String rm,
      @RequestParam(required = false) String username,
      @RequestParam(required = false) String password,
      @RequestParam(required = false) String role
  ) {
    var pedagogue = new Pedagogue(rm);
    pedagogue.setId(id);

    var person = new Person(name);
    person.setUser(new User(username, password, role));

    pedagogue.setPerson(person);

    List<Pedagogue> foundPedagogues = pedagogueRepository.findAll(Example.of(pedagogue));
    return foundPedagogues;
  }

  @PostMapping
  public Pedagogue save(
      @RequestParam(required = false) Integer id,
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String rm,
      @RequestParam(required = false) String username,
      @RequestParam(required = false) String password,
      @RequestParam(required = false) String role
  ) {

    var pedagogue = new Pedagogue(rm);

    var person = new Person(name);

    var user = new User(username, password, role);

    if (id != null) {
      pedagogue.setId(id);
      person.setId(pedagogueRepository.findById((int) id).getPerson().getId());
      user.setId(pedagogueRepository.findById((int) id).getPerson().getUser().getId());
    }

    person.setUser(user);

    pedagogue.setPerson(person);

    pedagogueRepository.save(pedagogue);

    return pedagogue;
  }

  @DeleteMapping
  public void deleteById(@RequestParam int id) {
    pedagogueRepository.deleteById(id);
  }

}