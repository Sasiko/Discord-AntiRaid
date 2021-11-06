package commands.configuration.ChildCommands;

import commands.ChildCommand;
import commands.Command;
import java.awt.Color;
import main.Action;
import main.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

public class CCmdAction extends ChildCommand {

  public CCmdAction(Command parentCommand, String invoke) {
    super(parentCommand, invoke);
  }

  @Override
  public void action(String[] args, String content, GuildMessageReceivedEvent event) throws InsufficientPermissionException {
    Member member = event.getMember();
    User user = event.getAuthor();
    Guild guild = event.getGuild();
    Message message = event.getMessage();
    TextChannel textChannel = message.getTextChannel();

    if (args.length != 1) {
      sendHelp(textChannel);
      return;
    }

    Action new_action;
    try {
      new_action = Action.valueOf(args[0].toUpperCase());
    } catch (IllegalArgumentException e) {
      sendHelp(textChannel);
      return;
    }

    Action old_action = (Action) Config.getConfig("ACTION");

    if (Config.setConfig("ACTION", new_action)) {
      EmbedBuilder embedBuilder = new EmbedBuilder();
      embedBuilder.setColor(Color.green);
      embedBuilder.setTitle("Config has been updated");
      embedBuilder.setDescription(String.format("Config **ACTION** has been set from `%s` to `%s`", old_action, new_action));
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
    return "<value>";
  }

  @Override
  public String description() {
    return
        String.format(
            "Provide the action (aside from deleting) to apply if messages are proven similar. Can be `NULL` (Do nothing), `KICK`, `BAN_0`, `BAN_1` (1-day message removal), `BAN_7` (7-day message removal), or `MUTE`. Current value is `%s`.",
            Config.getConfig("ACTION"));
  }

}
