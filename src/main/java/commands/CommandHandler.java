package commands;

import java.awt.Color;
import java.util.HashMap;
import java.util.Optional;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import utils.STATICS;

public class CommandHandler {

  public static final HashMap<String, Command> commands = new HashMap<>();

  public static void addCommand(String invoke, Command cmd) {
    commands.put(invoke.toLowerCase(), cmd);
    System.out.println("[INFO] Command " + STATICS.PREFIX + invoke.toLowerCase() + " has been registered.");
  }

  public static void handleCommand(GuildMessageReceivedEvent event) {
    Message message = event.getMessage();
    User author = message.getAuthor();
    if (message.getContentRaw().toLowerCase().startsWith(STATICS.PREFIX.toLowerCase()) && !author.getId()
        .equals(event.getJDA().getSelfUser().getId())) {
      CommandParser.CommandContainer cmd = CommandParser.parser(message.getContentRaw(), event);
      if (commands.containsKey(cmd.invoke)) {
        Command command = commands.get(cmd.invoke);
        try {
          Member member = event.getMember();
          Optional<Permission> permission = command.requiredPermission();
          if (permission.isPresent() && !member.hasPermission(permission.get())) {
            return;
          }
          command.action(cmd.args, cmd.content, cmd.event);
        } catch (InsufficientPermissionException e) {
          if (e.getChannelId() == 0) {
            cmd.event.getChannel().sendMessage(
                new EmbedBuilder().setTitle("MISSING PERMISSION").setDescription("The bot is missing following permission: " + e.getPermission())
                    .setColor(Color.red).build()).queue();
          }
        }
      }
    }
  }
}
