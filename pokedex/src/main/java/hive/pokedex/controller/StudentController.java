package hive.pokedex.controller;

import hive.entity.user.Person;
import hive.entity.user.Student;
import hive.entity.user.User;
import hive.pokedex.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student")
public class StudentController {

  @Autowired
  private StudentRepository students;

  @GetMapping("/get/{ra}")
  public Student buscar(@PathVariable final String ra) {
    return students.findByRa(ra);
  }

  @GetMapping("/insert:{name},{linkGit},{ra},{username},{password},{role},{status}")
  public void save(
      @PathVariable final String name,
      @PathVariable final String linkGit,
      @PathVariable final String ra,
      @PathVariable final String username,
      @PathVariable final String password,
      @PathVariable final int role,
      @PathVariable final int status
  ) {

    Student student = new Student(ra);
    Person person = new Person(name,linkGit);
    person.setUser(new User(username,password,role,status));

    student.setPerson(person);

    students.save(student);
  }

}