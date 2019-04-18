package hive.pokedex.repository;

import hive.entity.user.Student;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface StudentRepository extends CrudRepository<Student, Integer> {
  Student findByRa(String ra);
}