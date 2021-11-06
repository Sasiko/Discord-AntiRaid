package main;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.apache.commons.collections4.EnumerationUtils;
import org.codehaus.plexus.util.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.STATICS;

public class Config {

  private static final HashMap<String, Object> configs = new HashMap<>();
  private static String path;

  static {

    try {

      path = STATICS.getJarContainingFolder(Main.class) + "//configuration.json";
      File file = new File(path);

      try {
        if (!file.exists()) {

          FileWriter fileWriter = new FileWriter(path);

          JSONObject obj = new JSONObject();

          obj.put("SIMILARITY", 0.9D);
          obj.put("DELETE", true);
          obj.put("ACTION", "NONE");
          obj.put("COUNT", 5);
          obj.put("MIN_LENGTH", 5);
          obj.put("MINUTE", 10);
          obj.put("LOG_CHANNEL_ID", "000000000000000000");
          obj.put("IGNORE_ROLES", new JSONArray());
          obj.put("MUTE_ROLE_ID", "000000000000000000");
          obj.put("MUTE_TIME", 10);
          obj.put("RESET_ROLE", true);

          fileWriter.write(obj.toString());

          fileWriter.flush();
          fileWriter.close();

          configs.put("SIMILARITY", 0.9D);
          configs.put("DELETE", true);
          configs.put("ACTION", Action.NONE);
          configs.put("COUNT", 5);
          configs.put("MIN_LENGTH", 5);
          configs.put("MINUTE", 10);
          configs.put("LOG_CHANNEL_ID", "000000000000000000");
          configs.put("IGNORE_ROLES", new JSONArray());
          configs.put("MUTE_ROLE_ID", "000000000000000000");
          configs.put("MUTE_TIME", 10);
          configs.put("RESET_ROLE", true);

        } else {
          configs.put("SIMILARITY", getConfigurationOrDefault("SIMILARITY", 0.9));
          configs.put("DELETE", getConfigurationOrDefault("DELETE", true));
          configs.put("ACTION", Action.valueOf((String) getConfigurationOrDefault("ACTION", "NONE")));
          configs.put("COUNT", getConfigurationOrDefault("COUNT", 5));
          configs.put("MIN_LENGTH", getConfigurationOrDefault("MIN_LENGTH", 5));
          configs.put("MINUTE", getConfigurationOrDefault("MINUTE", 10));
          configs.put("LOG_CHANNEL_ID", getConfigurationOrDefault("LOG_CHANNEL_ID", "000000000000000000"));
          configs.put("IGNORE_ROLES", getConfigurationOrDefault("IGNORE_ROLES", new JSONArray()));
          configs.put("MUTE_ROLE_ID", getConfigurationOrDefault("MUTE_ROLE_ID", "000000000000000000"));
          configs.put("MUTE_TIME", getConfigurationOrDefault("MUTE_TIME", 10));
          configs.put("RESET_ROLE", getConfigurationOrDefault("RESET_ROLE", true));
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  private static void setConfiguration(String option, Object value) {
    try {
      JSONObject obj = new JSONObject(Files.lines(Paths.get(path), StandardCharsets.UTF_8).collect(Collectors.joining("\n")));
      obj.put(option, value);
      FileUtils.fileWrite(path, obj.toString());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private static Object getConfiguration(String option) {
    try {
      JSONObject obj = new JSONObject(Files.lines(Paths.get(path), StandardCharsets.UTF_8).collect(Collectors.joining("\n")));
      if (!obj.has(option)) {
        return null;
      }
      return obj.get(option);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }

  private static Object getConfigurationOrDefault(String option, Object defaultValue) {
    try {
      JSONObject obj = new JSONObject(Files.lines(Paths.get(path), StandardCharsets.UTF_8).collect(Collectors.joining("\n")));
      if (!obj.has(option)) {
        return defaultValue;
      }
      return obj.get(option);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return defaultValue;
  }

  private static void removeConfiguration(String option) {
    try {
      JSONObject obj = new JSONObject(Files.lines(Paths.get(path), StandardCharsets.UTF_8).collect(Collectors.joining("\n")));
      obj.remove(option);
      FileUtils.fileWrite(path, obj.toString());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public static Object getConfig(String config) {
    return configs.get(config.toUpperCase());
  }

  public static boolean setConfig(String config, Object value) {
    config = config.toUpperCase();
    switch (config) {
      case "SIMILARITY":
        if (!(value instanceof Double)) {
          return false;
        }
        double similarity_value = (Double) value;
        similarity_value = Math.max(Math.min(similarity_value, 1D), 0.1);
        setConfiguration(config, similarity_value);
        configs.put(config, similarity_value);
        return true;
      case "DELETE":
      case "RESET_ROLE":
        if (!(value instanceof Boolean)) {
          return false;
        }
        setConfiguration(config, value);
        configs.put(config, value);
        return true;
      case "ACTION":
        try {
          Action action = (Action) value;
          setConfiguration(config, action.name());
          configs.put(config, action);
          return true;
        } catch (Exception e) {
          return false;
        }
      case "COUNT":
      case "MINUTE":
      case "MUTE_TIME":
      case "MIN_LENGTH":
        if (!(value instanceof Integer)) {
          return false;
        }
        int int_val = (int) value;
        int_val = Math.max(1, int_val);
        setConfiguration(config, int_val);
        configs.put(config, int_val);
        return true;
      case "LOG_CHANNEL_ID":
      case "MUTE_ROLE_ID":
        if (!(value instanceof String)) {
          return false;
        }
        setConfiguration(config, value);
        configs.put(config, value);
        return true;
      case "IGNORE_ROLES":
        if (!(value instanceof JSONArray)) {
          return false;
        }
        /*JSONArray roles_array = (JSONArray) configs.get(config);
        HashSet<Object> set = new HashSet<>(roles_array.toList());
        set.addAll((ArrayList) value);
        roles_array = new JSONArray(set);*/
        setConfiguration(config, value);
        configs.put(config, value);
        return true;
      default:
        return false;
    }
  }

}
