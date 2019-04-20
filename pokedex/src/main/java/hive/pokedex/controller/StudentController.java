package hive.pokedex.controller;

import hive.entity.user.Person;
import hive.entity.user.Student;
import hive.entity.user.User;
import hive.pokedex.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

  @Autowired
  private StudentRepository studentRepository;

  @GetMapping
  public List<Student> find(
      @RequestParam(required = false) Integer id,
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String ra,
      @RequestParam(required = false) String username,
      @RequestParam(required = false) String password,
      @RequestParam(required = false) String role
  ) {
    var student = new Student(ra);
    student.setId(id);

    var person = new Person(name);
    person.setUser(new User(username, password, role));

    student.setPerson(person);

    List<Student> foundStudents = studentRepository.findAll(Example.of(student));
    return foundStudents;
  }

  @PostMapping
  public Student save(
      @RequestParam(required = false) Integer id,
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String ra,
      @RequestParam(required = false) String username,
      @RequestParam(required = false) String password,
      @RequestParam(required = false) String role
  ) {

    var student = new Student(ra);

    var person = new Person(name);

    var user = new User(username, password, role);

    if (id != null) {
      student.setId(id);
      person.setId(studentRepository.findById((int) id).getPerson().getId());
      user.setId(studentRepository.findById((int) id).getPerson().getUser().getId());
    }

    person.setUser(user);

    student.setPerson(person);

    studentRepository.save(student);

    return student;
  }

  @DeleteMapping
  public void deleteById(@RequestParam int id) {
    studentRepository.deleteById(id);
  }
}