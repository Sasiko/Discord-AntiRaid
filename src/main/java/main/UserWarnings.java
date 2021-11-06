package main;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.codehaus.plexus.util.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.STATICS;

public class UserWarnings {

  private static String path;

  static {

    try {

      path = STATICS.getJarContainingFolder(Main.class) + "//warnings.json";
      File file = new File(path);

      try {
        if (!file.exists()) {

          FileWriter fileWriter = new FileWriter(path);

          JSONObject obj = new JSONObject();

          fileWriter.write(obj.toString());

          fileWriter.flush();
          fileWriter.close();

        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public static void addUserCount(String userId, int amount) {
    try {
      JSONObject obj = new JSONObject(Files.lines(Paths.get(path), StandardCharsets.UTF_8).collect(Collectors.joining("\n")));
      JSONArray warnings = obj.optJSONArray(userId);
      if (warnings == null) warnings = new JSONArray();
      long warnTime = System.currentTimeMillis() - TimeUnit.MINUTES.toMillis((int) Config.getConfig("MINUTE"));
      List<Object> warningsList = warnings.toList();
      warningsList.removeIf(l -> (long) l < warnTime);
      for (int i = 0; i < amount; i++) {
        warningsList.add(System.currentTimeMillis());
      }
      obj.put(userId, warningsList);
      FileUtils.fileWrite(path, obj.toString());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public static void clearUserCount(String userId) {
    try {
      JSONObject obj = new JSONObject(Files.lines(Paths.get(path), StandardCharsets.UTF_8).collect(Collectors.joining("\n")));
      obj.remove(userId);
      FileUtils.fileWrite(path, obj.toString());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public static int getUserCount(String userId) {
    try {
      JSONObject obj = new JSONObject(Files.lines(Paths.get(path), StandardCharsets.UTF_8).collect(Collectors.joining("\n")));
      JSONArray warnings = obj.optJSONArray(userId);
      if (warnings == null) return 0;
      long warnTime = System.currentTimeMillis() - TimeUnit.MINUTES.toMillis((int) Config.getConfig("MINUTE"));
      List<Object> warningsList = warnings.toList();
      warningsList.removeIf(l -> (long) l < warnTime);
      return warningsList.size();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return 0;
  }

}
