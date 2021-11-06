package commands;

import java.util.ArrayList;
import java.util.Arrays;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;
import utils.STATICS;

public class CommandParser {

  public static CommandContainer parser(String rw, GuildMessageReceivedEvent e) {

    String beheaded = StringUtils.replace(rw, STATICS.PREFIX, "", 1);
    String[] splitBeheaded = beheaded.split(" ");

    ArrayList<String> split = new ArrayList<>(Arrays.asList(splitBeheaded));

    String invoke = split.get(0).toLowerCase();
    String[] args = new String[split.size() - 1];
    split.subList(1, split.size()).toArray(args);

    StringBuilder content = new StringBuilder();
    for (String arg : args) {
      content.append(arg).append(" ");
    }

    return new CommandContainer(invoke, args, content.toString().trim(), e);
  }

  public static class CommandContainer {

    public final String invoke;
    public final String[] args;
    public final String content;
    public final GuildMessageReceivedEvent event;

    public CommandContainer(String invoke, String[] args, String content, GuildMessageReceivedEvent e) {
      this.invoke = invoke;
      this.args = args;
      this.content = content;
      this.event = e;
    }
  }

}
