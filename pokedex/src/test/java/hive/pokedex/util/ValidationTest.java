package hive.pokedex.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ValidationTest {

  @Test
  public void verifyText_whenOnlyWhiteSpaces_returnFalse() {
    assertEquals(Validation.isValid("    "), false);
  }

  @Test
  public void verifyText_whenNull_returnFalse() {
    assertEquals(Validation.isValid(null), false);
  }

  @Test
  public void verifyText_whenTextIsRetrieved_returnTrue() {
    assertEquals(Validation.isValid("text-test"), true);
  }

}
