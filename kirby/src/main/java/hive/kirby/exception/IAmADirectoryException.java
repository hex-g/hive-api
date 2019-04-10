package hive.kirby.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.I_AM_A_TEAPOT, reason = "I'm a directory!")
public class IAmADirectoryException extends RuntimeException {
}
