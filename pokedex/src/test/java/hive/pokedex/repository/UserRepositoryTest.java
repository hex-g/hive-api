package hive.pokedex.repository;

import hive.entity.user.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  private User user;

  @Before
  public void setup() {
    user = new User("teste","123","ADMIN");

    userRepository.save(user);
  }

  @Test
  public void saveUser() {
    User userSaved = userRepository.getOne(user.getId());

    assertEquals(user, userSaved);
  }

  @Test
  public void findByUsername() {
    User userFound = userRepository.findByUsername("teste");

    assertEquals(user, userFound);
  }

  @Test
  public void updateUser() {
    User userPersisted = userRepository.getOne(user.getId());

    userPersisted.setUsername("teste-atualizado");
    userPersisted.setPassword("321");

    userRepository.save(userPersisted);

    assertEquals(userPersisted, user);
  }

  @Test
  public void deleteById() {
    userRepository.deleteById(user.getId());

    assertFalse(userRepository.existsById(user.getId()));
  }

}
