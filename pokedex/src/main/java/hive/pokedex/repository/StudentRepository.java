package hive.pokedex.repository;

import hive.entity.user.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface StudentRepository extends JpaRepository<Student, Integer> {

  Student findById(int id);

}