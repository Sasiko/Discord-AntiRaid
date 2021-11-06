package commands;

import java.awt.Color;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import utils.STATICS;

public interface Command {

  void action(String[] args, String content, GuildMessageReceivedEvent event) throws InsufficientPermissionException;

  default String help() {
    return "";
  }

  default String description() {
    return "";
  }

  default Optional<Permission> requiredPermission() {
    return Optional.empty();
  }

  default void sendHelp(TextChannel textChannel) {
    EmbedBuilder embedBuilder = new EmbedBuilder().setColor(Color.red);
    embedBuilder.setTitle(String.format("Please use: **%s%s %s**", STATICS.PREFIX, getCmdName(), help()).trim());
    embedBuilder.appendDescription(String.format("\n**Description:\n**%s", description()));
    textChannel.sendMessage(embedBuilder.build()).queue();
  }

  default String getCmdName() {
    return this.getClass().getSimpleName().replace("Cmd", "").toLowerCase();
  }

}
