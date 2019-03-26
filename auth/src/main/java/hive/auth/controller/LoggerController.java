package hive.auth.controller;

import hive.auth.log.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class LoggerController {
  @GetMapping("/log")
  public String getLog() {
    return Logger.instance.toHtmlTable();
  }
}
