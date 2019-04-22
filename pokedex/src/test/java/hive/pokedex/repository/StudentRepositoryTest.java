package hive.pokedex.repository;

import hive.entity.user.Pedagogue;
import hive.entity.user.Person;
import hive.entity.user.Student;
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
public class StudentRepositoryTest {

  @Autowired
  private StudentRepository studentRepository;

  private Student student;

  @Before
  public void setup() {
    student = new Student("ra-teste");

    Person person = new Person("rodolfo");
    person.setUser(new User("rodolfo-teste", "123", "STUDENT"));

    student.setPerson(person);

    studentRepository.save(student);
  }

  @Test
  public void saveStudent() {
    Student studentSaved = studentRepository.getOne(student.getId());

    assertEquals(student, studentSaved);
  }

  @Test
  public void findByRa() {
    Student studentFound = studentRepository.findByRa("ra-teste");

    assertEquals(student, studentFound);
  }

  @Test
  public void updateStudent() {
    Student studentPersisted = studentRepository.getOne(student.getId());

    studentPersisted.setRa("ra-teste-atualizado");
    studentPersisted.getPerson().setName("rodolfo-atualizado");
    studentPersisted.getPerson().getUser().setUsername("rodolfo-teste-atualizado");
    studentPersisted.getPerson().getUser().setPassword("321");

    studentRepository.save(studentPersisted);

    assertEquals(studentPersisted, student);
  }

  @Test
  public void deleteById() {
    studentRepository.deleteById(student.getId());

    assertFalse(studentRepository.existsById(student.getId()));
  }
}
