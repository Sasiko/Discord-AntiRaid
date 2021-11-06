package main;


import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import commands.configuration.CmdConfig;
import commands.CommandHandler;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import javax.security.auth.login.LoginException;
import listeners.MessageListener;
import listeners.GuildJoinListener;
import listeners.ReadyListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;

public class Main {

  private static final ScheduledExecutorService service = Executors.newScheduledThreadPool(2);

  public static void main(String[] args) throws Exception {

    new Config();
    new TokenConfig();

    JDABuilder builder = new JDABuilder()
        .setToken(TokenConfig.getToken())
        .setAutoReconnect(true)
        .setStatus(OnlineStatus.ONLINE);

    EventWaiter eventWaiter = new EventWaiter();

    addCommand(eventWaiter);
    registerListener(builder, eventWaiter);

    try {
      JDA jda = builder.build();
    } catch (LoginException e) {
      e.printStackTrace();
    }


  }

  public static void registerListener(JDABuilder builder, EventWaiter eventWaiter) {

    builder.addEventListeners(new ReadyListener(service));
    builder.addEventListeners(new MessageListener());
    builder.addEventListeners(new GuildJoinListener());
    builder.addEventListeners(eventWaiter);

  }

  public static void addCommand(EventWaiter eventWaiter) {

    CommandHandler.addCommand("config", new CmdConfig());

  }


}