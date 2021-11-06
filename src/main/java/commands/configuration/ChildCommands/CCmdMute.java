package commands.configuration.ChildCommands;

import commands.ChildCommand;
import commands.Command;
import java.awt.Color;
import java.util.List;
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
import org.json.JSONArray;

public class CCmdMute extends ChildCommand {

  public CCmdMute(Command parentCommand, String invoke) {
    super(parentCommand, invoke);
  }

  @Override
  public void action(String[] args, String content, GuildMessageReceivedEvent event) throws InsufficientPermissionException {
    Member member = event.getMember();
    User user = event.getAuthor();
    Guild guild = event.getGuild();
    Message message = event.getMessage();
    TextChannel textChannel = message.getTextChannel();

    if (args.length != 1 || message.getMentionedRoles().isEmpty()){
      sendHelp(textChannel);
      return;
    }

    String old_value = (String) Config.getConfig("MUTE_ROLE_ID");
    String new_value = message.getMentionedRoles().get(0).getId();

    if (Config.setConfig("MUTE_ROLE_ID", new_value)) {
      EmbedBuilder embedBuilder = new EmbedBuilder();
      embedBuilder.setColor(Color.green);
      embedBuilder.setTitle("Config has been updated");
      embedBuilder.setDescription(String.format("Config **MUTE_ROLE_ID** has been set from <@&%s> to <@&%s>", old_value, new_value));
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
    return "<@role>";
  }

  @Override
  public String description() {
    return
        String.format("Should the bot be muting violators, what role should it use? Current value is <@&%s>. Config only active if `action` is `MUTE`.", Config.getConfig("MUTE_ROLE_ID"));
  }

}
