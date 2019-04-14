package hive.album.repository;

import hive.entity.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface UserRepository extends CrudRepository<User, Integer> {
  User findByUsername(String username);
}
