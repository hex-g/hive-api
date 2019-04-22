package hive.pokedex.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Entity already registered, try again")
public class EntityAlreadyExistsException extends RuntimeException{
}
