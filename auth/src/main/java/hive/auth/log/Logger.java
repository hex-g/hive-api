package hive.auth.log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class Logger {
  private static final int LOG_SIZE = 10;

  public static final Logger instance = new Logger();

  private String[][] log = new String[LOG_SIZE][2];

  private int index = 0;
  private boolean filled = false;

  private Logger() {
  }

  public void addEntry(String username) {
    var now = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    log[index][0] = username;
    log[index++][1] = now;
    if (index == LOG_SIZE) {
      index = 0;
      filled = true;
    }
  }

  public String toHtmlTable() {
    if (index == 0 && !filled) {
      return "No data!";
    }

    var out = new StringBuilder("<table border=\"1\" style=\"border-collapse: collapse\">")
        .append("<tr><th>Username</th><th>Date and time</th></tr>");

    if (filled) {
      for (int i = index; i < LOG_SIZE; i++) {
        out.append("<tr><td>").append(log[i][0]);
        for (int j = 1; j < 2; j++) {
          out.append("</td><td>").append(log[i][j]);
        }
        out.append("</td></tr>");
      }
      fillFromZeroToCurrentPosition(out);
    } else {
      fillFromZeroToCurrentPosition(out);
    }

    return out.toString();
  }

  private void fillFromZeroToCurrentPosition(StringBuilder out) {
    for (int i = 0; i < index; i++) {
      out.append("<tr><td>").append(log[i][0]);
      for (int j = 1; j < 2; j++) {
        out.append("</td><td>").append(log[i][j]);
      }
      out.append("</td></tr>");
    }
  }
}
