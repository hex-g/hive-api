package hive.pokedex.repository;

import hive.entity.user.Pedagogue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface PedagogueRepository extends JpaRepository<Pedagogue, Integer> {

  Pedagogue findById(int id);

}