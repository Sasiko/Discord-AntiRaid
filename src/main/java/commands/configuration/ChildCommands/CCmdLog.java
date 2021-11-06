package commands.configuration.ChildCommands;

import commands.ChildCommand;
import commands.Command;
import java.awt.Color;
import main.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import org.apache.commons.lang3.math.NumberUtils;

public class CCmdLog extends ChildCommand {

  public CCmdLog(Command parentCommand, String invoke) {
    super(parentCommand, invoke);
  }

  @Override
  public void action(String[] args, String content, GuildMessageReceivedEvent event) throws InsufficientPermissionException {
    Member member = event.getMember();
    User user = event.getAuthor();
    Guild guild = event.getGuild();
    Message message = event.getMessage();
    TextChannel textChannel = message.getTextChannel();

    if (args.length != 1 || (message.getMentionedChannels().isEmpty() && !args[0].equalsIgnoreCase("stop"))) {
      sendHelp(textChannel);
      return;
    }

    String value = !message.getMentionedChannels().isEmpty() ? message.getMentionedChannels().get(0).getId() : "000000000000000000";

    String old_value = (String) Config.getConfig("LOG_CHANNEL_ID");

    if (Config.setConfig("LOG_CHANNEL_ID", value)) {
      EmbedBuilder embedBuilder = new EmbedBuilder();
      embedBuilder.setColor(Color.green);
      embedBuilder.setTitle("Config has been updated");
      embedBuilder.setDescription(String.format("Config **LOG_CHANNEL_ID** has been set from <#%s> to <#%s>", old_value, value).replace("<#000000000000000000>", "NONE"));
      textChannel.sendMessage(embedBuilder.build()).queue();
    } else {
      EmbedBuilder embedBuilder = new EmbedBuilder();
      embedBuilder.setColor(Color.red);
      embedBuilder.setTitle("Something went wrong");
      embedBuilder.setDescription("Please make sure you used the command properly");
      textChannel.sendMessage(embedBuilder.build()).queue();
    }

  }

  @Override
  public String help() {
    return "<#channel/stop>";
  }

  @Override
  public String description() {
    return
        String.format("Where should I log actions? Current value is <#%s>. If you want the bot to stop logging, set value to `stop`.",
            Config.getConfig("LOG_CHANNEL_ID"))
            .replace("<#000000000000000000>", "NONE");
  }

}
