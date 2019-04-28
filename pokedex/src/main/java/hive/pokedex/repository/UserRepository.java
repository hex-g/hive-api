package hive.pokedex.repository;

import hive.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserRepository extends JpaRepository<User, Integer> {

  User findByUsername(String username);

  boolean existsByUsername(String username);

}
