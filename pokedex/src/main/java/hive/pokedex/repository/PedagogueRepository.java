package hive.pokedex.repository;

import hive.entity.user.Pedagogue;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface PedagogueRepository extends CrudRepository<Pedagogue, Integer> {
  Pedagogue findByRm(String rm);
}