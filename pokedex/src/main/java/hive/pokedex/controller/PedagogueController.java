package hive.pokedex.controller;

import hive.entity.user.Pedagogue;
import hive.entity.user.Person;
import hive.entity.user.User;
import hive.pokedex.repository.PedagogueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedagogue")
public class PedagogueController {

  @Autowired
  private PedagogueRepository pedagogues;

  @GetMapping("/get/{rm}")
  public Pedagogue buscar(@PathVariable final String rm) {
    return pedagogues.findByRm(rm);
  }

  @GetMapping("/insert:{name},{linkGit},{rm},{username},{password},{role},{status}")
  public void save(
      @PathVariable final String name,
      @PathVariable final String linkGit,
      @PathVariable final String rm,
      @PathVariable final String username,
      @PathVariable final String password,
      @PathVariable final int role,
      @PathVariable final int status
  ) {

    Pedagogue pedagogue = new Pedagogue(rm);
    Person person = new Person(name, linkGit);
    person.setUser(new User(username,password,role,status));

    pedagogue.setPerson(person);

    pedagogues.save(pedagogue);
  }
}