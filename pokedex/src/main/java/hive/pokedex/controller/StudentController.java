package hive.pokedex.controller;

import hive.entity.user.Person;
import hive.entity.user.Student;
import hive.entity.user.User;
import hive.pokedex.exception.EntityAlreadyExistsException;
import hive.pokedex.exception.EntityNotFoundException;
import hive.pokedex.exception.NullValueException;
import hive.pokedex.exception.UsernameAlreadyExistsException;
import hive.pokedex.repository.StudentRepository;
import hive.pokedex.repository.UserRepository;
import hive.pokedex.util.FillNullValues;
import hive.pokedex.util.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/student")
public class StudentController {

  private final String ROLE = "STUDENT";

  private StudentRepository studentRepository;

  private UserRepository userRepository;

  @Autowired
  public StudentController(StudentRepository studentRepository, UserRepository userRepository) {
    this.studentRepository = studentRepository;
    this.userRepository = userRepository;
  }

  @GetMapping
  public List<Student> find(
      @RequestParam(required = false) Integer id,
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String ra,
      @RequestParam(required = false) String username,
      @RequestParam(required = false) String password
  ) {
    var student = new Student(ra);
    student.setId(id);

    var person = new Person(name);
    person.setUser(new User(username, password, ROLE));

    student.setPerson(person);

    List<Student> foundStudents = studentRepository.findAll(Example.of(student));

    if (foundStudents.size() == 0) {
      throw new EntityNotFoundException();
    }

    return foundStudents;
  }

  @PostMapping
  public Student save(
      @RequestParam(required = false) Integer id,
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String ra,
      @RequestParam(required = false) String username,
      @RequestParam(required = false) String password
  ) {

    var student = new Student(ra);
    var person = new Person(name);
    var user = new User(username, password, ROLE);

    person.setUser(user);
    student.setPerson(person);

    if (id != null) {
      if (!studentRepository.existsById(id)) {
        throw new EntityNotFoundException();
      }

      var studentPersisted = studentRepository.getOne(id);

      FillNullValues.copyProperties(student, studentPersisted);

      FillNullValues.copyProperties(
          student.getPerson(),
          studentPersisted.getPerson()
      );

      FillNullValues.copyProperties(
          student.getPerson().getUser(),
          studentPersisted.getPerson().getUser()
      );
    }

    if (!Validation.isValid(student.getRa()) ||
        !Validation.isValid(student.getPerson().getName()) ||
        !Validation.isValid(student.getPerson().getUser().getUsername()) ||
        !Validation.isValid(student.getPerson().getUser().getPassword())) {
      throw new NullValueException();
    }else if (studentRepository.existsByRa(ra)) {
      throw new EntityAlreadyExistsException();
    }else if (userRepository.existsByUsername(username)) {
      throw new UsernameAlreadyExistsException();
    }

    studentRepository.save(student);

    return student;
  }

  @DeleteMapping
  public void deleteById(@RequestParam int id) {
    if (!studentRepository.existsById(id)) {
      throw new EntityNotFoundException();
    }
    studentRepository.deleteById(id);
  }
}