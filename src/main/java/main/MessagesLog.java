package main;
import java.io.File;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.api.entities.Message;
import net.ricecode.similarity.DiceCoefficientStrategy;
import net.ricecode.similarity.SimilarityStrategy;
import net.ricecode.similarity.StringSimilarityService;
import net.ricecode.similarity.StringSimilarityServiceImpl;
import org.apache.commons.lang3.math.NumberUtils;
import org.codehaus.plexus.util.FileUtils;
import org.json.JSONObject;
import utils.STATICS;

public class MessagesLog {

  private static String path;

  static {

    try {
      path = STATICS.getJarContainingFolder(Main.class) + "//messages.json";
      File file = new File(path);
      if (!file.exists()) {
        FileUtils.fileWrite(path, "{}");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public static void addMessage(Message message) {
    try {
      JSONObject json = new JSONObject(FileUtils.fileRead(path));
      JSONObject msgObj = new JSONObject();
      msgObj.put("authorId", message.getAuthor().getId());
      msgObj.put("messageId", message.getId());
      msgObj.put("channelId", message.getChannel().getId());
      msgObj.put("content", message.getContentRaw());
      json.put(String.valueOf(System.currentTimeMillis()), msgObj);
      long deadline = System.currentTimeMillis() - ((int) Config.getConfig("MINUTE") * 60000);
      Map<String, Object> map = json.toMap();
      map.keySet().removeIf(s -> Long.parseLong(s) < deadline);
      FileUtils.fileWrite(path, new JSONObject(map).toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static List<JSONObject> findSimilar(String original) {
    List<JSONObject> similar = new ArrayList<>();
    try {
      JSONObject json = new JSONObject(FileUtils.fileRead(path));
      SimilarityStrategy strategy = new DiceCoefficientStrategy();
      StringSimilarityService service = new StringSimilarityServiceImpl(strategy);
      double similarity_config = (double) Config.getConfig("SIMILARITY");
      Iterator<String> keys = json.keys();
      while (keys.hasNext()) {
        JSONObject obj = json.optJSONObject(keys.next());
        if (obj == null) continue;
        double score = service.score(original, obj.getString("content"));
        if (score >= similarity_config){
          similar.add(obj);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return similar;
  }


}
