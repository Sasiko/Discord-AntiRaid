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
import org.json.JSONObject;
import utils.STATICS;

public class UserMutes {

  private static String path;

  static {

    try {

      path = STATICS.getJarContainingFolder(Main.class) + "//mutes.json";
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

  public static void setMuteDuration(String userId, long end_of_mute) {
    try {
      JSONObject obj = new JSONObject(Files.lines(Paths.get(path), StandardCharsets.UTF_8).collect(Collectors.joining("\n")));
      obj.put(userId, end_of_mute);
      FileUtils.fileWrite(path, obj.toString());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public static long getMuteDuration(String userId) {
    try {
      JSONObject obj = new JSONObject(Files.lines(Paths.get(path), StandardCharsets.UTF_8).collect(Collectors.joining("\n")));
      return obj.optLong(userId, 0);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return 0;
  }

  public static void removeMute(String userId){
    try {
      JSONObject obj = new JSONObject(Files.lines(Paths.get(path), StandardCharsets.UTF_8).collect(Collectors.joining("\n")));
      obj.remove(userId);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public static List<String> getExpiredMutes(){
    List<String> list = new ArrayList<>();
    try {
      JSONObject obj = new JSONObject(Files.lines(Paths.get(path), StandardCharsets.UTF_8).collect(Collectors.joining("\n")));
      Iterator<String> keys = obj.keys();
      long l = System.currentTimeMillis();
      while (keys.hasNext()){
        String userId = keys.next();
        long end_of_mute = obj.optLong(userId, 0);
        if (end_of_mute < l){
          list.add(userId);
          keys.remove();
        }
      }
      FileUtils.fileWrite(path, obj.toString());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return list;
  }

}
