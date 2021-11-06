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

public class CCmdSimilarity extends ChildCommand {

  public CCmdSimilarity(Command parentCommand, String invoke) {
    super(parentCommand, invoke);
  }

  @Override
  public void action(String[] args, String content, GuildMessageReceivedEvent event) throws InsufficientPermissionException {
    Member member = event.getMember();
    User user = event.getAuthor();
    Guild guild = event.getGuild();
    Message message = event.getMessage();
    TextChannel textChannel = message.getTextChannel();

    if (args.length != 1 || !NumberUtils.isParsable(args[0])){
      sendHelp(textChannel);
      return;
    }

    double value = Double.parseDouble(args[0]);
    if (value < 0.1 || value > 1){
      sendHelp(textChannel);
      return;
    }

    double old_value = (double) Config.getConfig("SIMILARITY");

    if (Config.setConfig("SIMILARITY", value)) {
      EmbedBuilder embedBuilder = new EmbedBuilder();
      embedBuilder.setColor(Color.green);
      embedBuilder.setTitle("Config has been updated");
      embedBuilder.setDescription(String.format("Config **SIMILARITY** has been set from `%.1f` to `%.1f`", old_value, value));
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
        String.format("Provide a lower threshold of similarity (0.1 to 1.0, allows decimals) of five neighbouring messages for the action below to apply. Current value is `%.1f`.", (double) Config.getConfig("SIMILARITY"));
  }

}
