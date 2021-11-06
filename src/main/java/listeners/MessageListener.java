package listeners;

import commands.CommandHandler;
import java.awt.Color;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import main.Action;
import main.Config;
import main.MessagesLog;
import main.UserMutes;
import main.UserRoles;
import main.UserWarnings;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.JSONArray;
import org.json.JSONObject;


public class MessageListener extends ListenerAdapter {

  @Override
  public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
    CommandHandler.handleCommand(event);
    handleDuplicate(event);
  }

  public void handleDuplicate(GuildMessageReceivedEvent event){
    Message message = event.getMessage();
    String contentRaw = message.getContentDisplay();
    Member member = event.getMessage().getMember();
    Guild guild = event.getGuild();
    if (contentRaw.isEmpty()) return;
    if (member.getUser().isBot()) return;
    if (contentRaw.length() < (int) Config.getConfig("MIN_LENGTH")) return;
    List<Object> ignore_roles = ((JSONArray) Config.getConfig("IGNORE_ROLES")).toList();
    if (member.getRoles().stream().anyMatch(r -> ignore_roles.contains(r.getId()))){
      return;
    }
    List<JSONObject> similar = MessagesLog.findSimilar(contentRaw);
    if (!similar.isEmpty()) {
      UserWarnings.addUserCount(member.getId(), 1);
      int userCount = UserWarnings.getUserCount(member.getId());

      String logChannelId = (String) Config.getConfig("LOG_CHANNEL_ID");
      TextChannel logChannel = guild.getTextChannelById(logChannelId);

      EmbedBuilder embedBuilder = new EmbedBuilder();
      embedBuilder.setColor(Color.red);
      embedBuilder.setTitle("No Sanction - Duplicate Message");
      embedBuilder.setAuthor(member.getUser().getAsTag(), null, member.getUser().getEffectiveAvatarUrl());
      embedBuilder.setDescription(String.format("**Warn Count: %d**\n**Message: **%s", userCount, contentRaw));
      embedBuilder.setFooter("User ID: " + member.getId());
      embedBuilder.setTimestamp(Instant.now());

      if (userCount >= (int) Config.getConfig("COUNT")){
        UserWarnings.clearUserCount(member.getId());
        if ((boolean) Config.getConfig("DELETE")){
          message.delete().queue();
          for (JSONObject msgObj : similar) {
            if (msgObj.getString("authorId").equals(member.getId())){
              String channelId = msgObj.getString("channelId");
              TextChannel textChannelById = guild.getTextChannelById(channelId);
              if (textChannelById != null){
                textChannelById.deleteMessageById(msgObj.getString("messageId")).queue(s -> {}, e -> {});
              }
            }
          }
        }
        switch ((Action) Config.getConfig("ACTION")){
          case KICK:
            guild.kick(member, "Duplicate Messages").queue();
            embedBuilder.setTitle("User Sanctioned - KICK");
            break;
          case BAN_0:
            guild.ban(member, 0,"Duplicate Messages").queue();
            embedBuilder.setTitle("User Sanctioned - BAN (0-day message removal)");
            break;
          case BAN_1:
            guild.ban(member, 1,"Duplicate Messages").queue();
            embedBuilder.setTitle("User Sanctioned - BAN (1-day message removal)");
            break;
          case BAN_7:
            guild.ban(member, 7,"Duplicate Messages").queue();
            embedBuilder.setTitle("User Sanctioned - BAN (7-day message removal)");
            break;
          case MUTE:
            String muteRoleId = (String) Config.getConfig("MUTE_ROLE_ID");
            if (muteRoleId == null){
              break;
            }
            Role muteRole = guild.getRoleById(muteRoleId);
            int mute_time = (int) Config.getConfig("MUTE_TIME");
            if (muteRole != null){
              UserMutes.setMuteDuration(member.getId(), System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(mute_time));
              UserRoles.setUserRoles(member.getId(), member.getRoles().stream().filter(r -> !r.isManaged()).map(Role::getId).collect(Collectors.toList()));
              if ((boolean) Config.getConfig("RESET_ROLE")) {
                List<Role> newRoles = member.getRoles().stream().filter(Role::isManaged).collect(Collectors.toList());
                newRoles.add(muteRole);
                guild.modifyMemberRoles(member, newRoles).queue();
              } else {
                guild.addRoleToMember(member, muteRole).queue();
              }
            }
            embedBuilder.setTitle("User Sanctioned - MUTE (" + mute_time + " minutes)");
            break;
        }
      }
      logChannel.sendMessage(embedBuilder.build()).queue();
    }
    MessagesLog.addMessage(message);
  }

}
