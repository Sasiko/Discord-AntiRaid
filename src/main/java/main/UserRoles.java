package main;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import org.codehaus.plexus.util.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.STATICS;

public class UserRoles {

  private static String path;

  static {

    try {

      path = STATICS.getJarContainingFolder(Main.class) + "//userRoles.json";
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

  public static void setUserRoles(String userId, List<String> roles) {
    try {
      JSONObject obj = new JSONObject(Files.lines(Paths.get(path), StandardCharsets.UTF_8).collect(Collectors.joining("\n")));
      obj.put(userId, roles);
      FileUtils.fileWrite(path, obj.toString());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public static JSONArray getUserRoles(String userId) {
    try {
      JSONObject obj = new JSONObject(Files.lines(Paths.get(path), StandardCharsets.UTF_8).collect(Collectors.joining("\n")));
      return obj.optJSONArray(userId);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }

  public static void removeUserRoles(String userId){
    try {
      JSONObject obj = new JSONObject(Files.lines(Paths.get(path), StandardCharsets.UTF_8).collect(Collectors.joining("\n")));
      obj.remove(userId);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

}
