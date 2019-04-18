package hive.pokedex.controller;

import hive.entity.user.Student;
import hive.pokedex.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentRepository students;

    @GetMapping
    public Iterable<Student> buscar() {
        return students.findAll();
    }
}