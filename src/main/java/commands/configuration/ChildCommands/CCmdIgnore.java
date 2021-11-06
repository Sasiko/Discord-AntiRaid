package commands.configuration.ChildCommands;

import commands.ChildCommand;
import commands.Command;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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

public class CCmdIgnore extends ChildCommand {

  public CCmdIgnore(Command parentCommand, String invoke) {
    super(parentCommand, invoke);
  }

  @Override
  public void action(String[] args, String content, GuildMessageReceivedEvent event) throws InsufficientPermissionException {
    Member member = event.getMember();
    User user = event.getAuthor();
    Guild guild = event.getGuild();
    Message message = event.getMessage();
    TextChannel textChannel = message.getTextChannel();

    if (args.length != 1 || (message.getMentionedRoles().isEmpty() && !NumberUtils.isDigits(args[0]))) {
      sendHelp(textChannel);
      return;
    }

    String role_id = !message.getMentionedRoles().isEmpty() ? message.getMentionedRoles().get(0).getId() : args[0];

    JSONArray old_value = (JSONArray) Config.getConfig("IGNORE_ROLES");

    List<Object> list = old_value.toList();

    list.add(role_id);

    JSONArray new_value = new JSONArray(list);

    if (Config.setConfig("IGNORE_ROLES", new_value)) {
      EmbedBuilder embedBuilder = new EmbedBuilder();
      embedBuilder.setColor(Color.green);
      embedBuilder.setTitle("Config has been updated");
      embedBuilder.setDescription(String.format("Config **IGNORE_ROLES** has been set to %s",
          new_value.toList().stream().map(o -> "<@&" + o + ">").collect(Collectors.joining(", "))));
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
    return "<@role/role id>";
  }

  @Override
  public String description() {
    return
        String.format("Are there certain roles that the bot should ignore? Current list: %s",
            ((JSONArray) Config.getConfig("IGNORE_ROLES")).toList().stream().map(o -> "<@&" + o + ">").collect(Collectors.joining(", ")));
  }

}
