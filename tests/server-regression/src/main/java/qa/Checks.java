package qa;

import java.lang.reflect.*;
import java.util.*;

public final class Checks {
  public interface Task {
    void run() throws Throwable;
  }

  public static final List<Map<String, Object>> rows = new ArrayList<>();

  public static void test(String name, Task task) {
    try {
      task.run();
      rows.add(Map.of("name", name, "passed", true));
    } catch (Throwable e) {
      while (e instanceof InvocationTargetException && e.getCause() != null) e = e.getCause();
      rows.add(Map.of("name", name, "passed", false, "error", e.toString()));
    }
  }

  public static void require(boolean b, String s) {
    if (!b) throw new AssertionError(s);
  }
}
