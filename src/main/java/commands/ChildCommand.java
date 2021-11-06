package commands;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import utils.STATICS;

public abstract class ChildCommand implements Command {

  Command parentCommand;
  String invoke;

  public ChildCommand(Command parentCommand, String invoke) {
    this.parentCommand = parentCommand;
    this.invoke = invoke;
  }

  @Override
  public String getCmdName() {
    return parentCommand.getCmdName() + " " + invoke;
  }

}
