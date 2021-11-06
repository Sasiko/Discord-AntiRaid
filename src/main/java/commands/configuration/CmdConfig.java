package commands.configuration;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import commands.ChildCommand;
import commands.Command;
import commands.configuration.ChildCommands.CCmdAction;
import commands.configuration.ChildCommands.CCmdCount;
import commands.configuration.ChildCommands.CCmdDelete;
import commands.configuration.ChildCommands.CCmdIgnore;
import commands.configuration.ChildCommands.CCmdLog;
import commands.configuration.ChildCommands.CCmdMinLength;
import commands.configuration.ChildCommands.CCmdMinute;
import commands.configuration.ChildCommands.CCmdMute;
import commands.configuration.ChildCommands.CCmdMuteTime;
import commands.configuration.ChildCommands.CCmdResetRole;
import commands.configuration.ChildCommands.CCmdSimilarity;
import commands.configuration.ChildCommands.CCmdUnignore;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import utils.STATICS;

public class CmdConfig implements Command {

  LinkedHashMap<String, ChildCommand> childCommands = new LinkedHashMap<>();

  public CmdConfig() {
    childCommands.put("similarity", new CCmdSimilarity(this, "similarity"));
    childCommands.put("delete", new CCmdDelete(this, "delete"));
    childCommands.put("action", new CCmdAction(this, "action"));
    childCommands.put("count", new CCmdCount(this, "count"));
    childCommands.put("minute", new CCmdMinute(this, "minute"));
    childCommands.put("log", new CCmdLog(this, "log"));
    childCommands.put("ignore", new CCmdIgnore(this, "ignore"));
    childCommands.put("unignore", new CCmdUnignore(this, "unignore"));
    childCommands.put("mute", new CCmdMute(this, "mute"));
    childCommands.put("mutetime", new CCmdMuteTime(this, "mutetime"));
    childCommands.put("resetrole", new CCmdResetRole(this, "resetrole"));
    childCommands.put("minlength", new CCmdMinLength(this, "minlength"));
  }

  @Override
  public Optional<Permission> requiredPermission() {
    return Optional.of(Permission.ADMINISTRATOR);
  }

  @Override
  public void action(String[] args, String content, GuildMessageReceivedEvent event) {

    Member member = event.getMember();
    User user = event.getAuthor();
    Guild guild = event.getGuild();
    Message message = event.getMessage();
    TextChannel textChannel = message.getTextChannel();

    if (args.length < 1){
      sendHelp(textChannel);
      return;
    }

    String invoke = args[0].toLowerCase();

    if (childCommands.containsKey(invoke)){
      ChildCommand childCommand = childCommands.get(invoke);
      childCommand.action(Arrays.copyOfRange(args, 1, args.length), content.replaceFirst(args[0], "").trim(), event);
    } else {
      sendHelp(textChannel);
    }

  }

  @Override
  public void sendHelp(TextChannel textChannel) {
    EmbedBuilder embedBuilder = new EmbedBuilder();
    embedBuilder.setTitle("Config");

    for (ChildCommand childCommand : childCommands.values()) {
      embedBuilder.appendDescription(String.format("**%s%s %s**", STATICS.PREFIX, childCommand.getCmdName(), childCommand.help()));
      embedBuilder.appendDescription(String.format("\n%s", childCommand.description()));
      embedBuilder.appendDescription("\n\n");
    }

    textChannel.sendMessage(embedBuilder.build()).queue();
  }
}
