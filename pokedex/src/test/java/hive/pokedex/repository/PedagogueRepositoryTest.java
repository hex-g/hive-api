package hive.pokedex.repository;

import hive.entity.user.Pedagogue;
import hive.entity.user.Person;
import hive.entity.user.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.junit.Assert.*;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class PedagogueRepositoryTest {

  @Autowired
  private PedagogueRepository pedagogueRepository;

  private Pedagogue pedagogue;

  @Before
  public void setup(){
    pedagogue = new Pedagogue("rm-teste");

    var person = new Person("rodolfo");
    person.setUser(new User("rodolfo-teste","123","PEDAGOGUE"));

    pedagogue.setPerson(person);

    pedagogueRepository.save(pedagogue);
  }

  @Test
  public void savePedagogue(){
    Pedagogue pedagogueSaved = pedagogueRepository.getOne(pedagogue.getId());

    assertEquals(pedagogue, pedagogueSaved);
  }

  @Test
  public void findByRm(){
    Pedagogue pedagogueFound = pedagogueRepository.findByRm("rm-teste");

    assertEquals(pedagogue, pedagogueFound);
  }

  @Test
  public void updatePedagogue(){
    Pedagogue pedagoguePersisted = pedagogueRepository.getOne(pedagogue.getId());

    pedagoguePersisted.setRm("rm-teste-atualizado");
    pedagoguePersisted.getPerson().setName("rodolfo-atualizado");
    pedagoguePersisted.getPerson().getUser().setUsername("rodolfo-teste-atualizado");
    pedagoguePersisted.getPerson().getUser().setPassword("321");

    pedagogueRepository.save(pedagoguePersisted);

    assertEquals(pedagoguePersisted, pedagogue);
  }

  @Test
  public void deleteById(){
    pedagogueRepository.deleteById(pedagogue.getId());

    assertFalse(pedagogueRepository.existsById(pedagogue.getId()));
  }

}
