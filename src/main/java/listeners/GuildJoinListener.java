package listeners;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildJoinListener extends ListenerAdapter {

  @Override
  public void onGuildJoin(GuildJoinEvent e) {

    String prefixMsg = "\nThe Bot joined:\n";

    Guild g = e.getGuild();

    System.out.println(prefixMsg + g.getName() + " {ServerID: " + g.getId() + ", Members: " + g.getMembers().size() + "}");

  }

}
