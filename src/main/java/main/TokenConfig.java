package main;

import java.io.File;
import java.io.FileInputStream;
import org.apache.commons.configuration.PropertiesConfiguration;
import utils.STATICS;

public class TokenConfig {

  private static String path;

  static {

    try {
      path = STATICS.getJarContainingFolder(Main.class) + "//token.properties";
      File file = new File(path);

      if (!file.exists()) {

        file.createNewFile();
        FileInputStream fs = new FileInputStream(path);

        PropertiesConfiguration conf = new PropertiesConfiguration(path);
        conf.load(fs);

        conf.addProperty("BOT_TOKEN", "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");

        conf.save();

      }
    } catch (Exception e) {
      e.printStackTrace();
    }


  }

  public static String getToken() {
    try {

      PropertiesConfiguration conf = new PropertiesConfiguration(path);

      return conf.getString("BOT_TOKEN", "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");

    } catch (Exception e) {
    }
    return "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
  }

}
