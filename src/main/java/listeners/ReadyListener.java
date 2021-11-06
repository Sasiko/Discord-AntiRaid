package listeners;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import main.Config;
import main.UserMutes;
import main.UserRoles;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ReadyListener extends ListenerAdapter {

  private final ScheduledExecutorService service;

  public ReadyListener(ScheduledExecutorService service) {
    this.service = service;
  }

  @Override
  public void onReady(ReadyEvent e) {

    StringBuilder serverlist = new StringBuilder("\nThis Bot is running on following servers: \n");

    for (Guild g : e.getJDA().getGuilds()) {
      serverlist.append("    - ")
          .append(g.getName())
          .append(" {ServerID: ")
          .append(g.getId())
          .append(", Members: ")
          .append(g.getMembers().size())
          .append("} \n");
    }

    service.scheduleAtFixedRate(() -> {
      List<String> expiredMutes = UserMutes.getExpiredMutes();
      String muteRoleId = (String) Config.getConfig("MUTE_ROLE_ID");
      if (muteRoleId == null) {
        return;
      }
      Role muteRole = e.getJDA().getRoleById(muteRoleId);
      if (muteRole != null) {
        Guild guild = muteRole.getGuild();
        for (String userId : expiredMutes) {
          Member member = guild.getMemberById(userId);
          if (member != null) {
            guild.removeRoleFromMember(userId, muteRole).queue();
            if ((boolean) Config.getConfig("RESET_ROLE")){
              List<Object> list = UserRoles.getUserRoles(userId).toList();
              List<Role> roles = list.stream().map(id -> guild.getRoleById((String) id)).collect(Collectors.toList());
              roles.addAll(member.getRoles().stream().filter(Role::isManaged).collect(Collectors.toList()));
              guild.modifyMemberRoles(member, roles).queue();
              UserRoles.removeUserRoles(userId);
            }
          }
        }
      }
    }, 1, 1, TimeUnit.MINUTES);

    System.out.println(serverlist);
  }

}
