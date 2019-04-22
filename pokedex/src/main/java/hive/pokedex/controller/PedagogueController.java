package hive.pokedex.controller;

import hive.entity.user.Pedagogue;
import hive.entity.user.Person;
import hive.entity.user.User;
import hive.pokedex.exception.EntityAlreadyExistsException;
import hive.pokedex.exception.EntityNotFoundException;
import hive.pokedex.exception.NullValueException;
import hive.pokedex.exception.UsernameAlreadyExistsException;
import hive.pokedex.repository.PedagogueRepository;
import hive.pokedex.repository.UserRepository;
import hive.pokedex.util.CopyPropertiesNotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedagogue")
public class PedagogueController {

  private final String ROLE = "PEDAGOGUE";

  private PedagogueRepository pedagogueRepository;

  private UserRepository userRepository;

  @Autowired
  public PedagogueController(PedagogueRepository pedagogueRepository, UserRepository userRepository) {
    this.pedagogueRepository = pedagogueRepository;
    this.userRepository = userRepository;
  }

  @GetMapping
  public List<Pedagogue> find(
      @RequestParam(required = false) Integer id,
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String rm,
      @RequestParam(required = false) String username,
      @RequestParam(required = false) String password
  ) {
    var pedagogue = new Pedagogue(rm);
    pedagogue.setId(id);

    var person = new Person(name);
    person.setUser(new User(username, password, ROLE));

    pedagogue.setPerson(person);

    List<Pedagogue> foundPedagogues = pedagogueRepository.findAll(Example.of(pedagogue));

    if (foundPedagogues.isEmpty()) {
      throw new EntityNotFoundException();
    }

    return foundPedagogues;
  }

  @PostMapping
  public Pedagogue save(
      @RequestParam(required = false) Integer id,
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String rm,
      @RequestParam(required = false) String username,
      @RequestParam(required = false) String password
  ) {

    var pedagogue = new Pedagogue(rm);
    var person = new Person(name);
    var user = new User(username, password, ROLE);

    person.setUser(user);
    pedagogue.setPerson(person);

    if (id != null) {
      if (!pedagogueRepository.existsById(id)) {
        throw new EntityNotFoundException();
      }

      var pedagoguePersisted = pedagogueRepository.getOne(id);

      CopyPropertiesNotNull.copyProperties(pedagogue, pedagoguePersisted);

      CopyPropertiesNotNull.copyProperties(
          pedagogue.getPerson(),
          pedagoguePersisted.getPerson()
      );

      CopyPropertiesNotNull.copyProperties(
          pedagogue.getPerson().getUser(),
          pedagoguePersisted.getPerson().getUser()
      );
    } else if (name == null || rm == null || username == null || password == null ||
        name.isEmpty() || rm.isEmpty() || username.isEmpty() || password.isEmpty()) {
      throw new NullValueException();
    }

    if (pedagogueRepository.existsByRm(rm)) {
      throw new EntityAlreadyExistsException();
    }else if (userRepository.existsByUsername(username)) {
      throw new UsernameAlreadyExistsException();
    }

    pedagogueRepository.save(pedagogue);

    return pedagogue;
  }

  @DeleteMapping
  public void deleteById(@RequestParam int id) {
    if(!pedagogueRepository.existsById(id)){
      throw new EntityNotFoundException();
    }
    pedagogueRepository.deleteById(id);
  }

}