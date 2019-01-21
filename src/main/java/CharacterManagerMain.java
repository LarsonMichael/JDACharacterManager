import com.google.inject.Inject;
import commands.MainCommandHandler;
import database.DatabaseConnectionProvider;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;


import javax.security.auth.login.LoginException;

public class CharacterManagerMain extends ListenerAdapter {
    DatabaseConnectionProvider databaseConnectionProvider;
    MainCommandHandler mainCommandHandler;

    @Inject
    CharacterManagerMain(DatabaseConnectionProvider databaseConnectionProvider, MainCommandHandler mainCommandHandler) throws LoginException {
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        builder.setToken(System.getenv("BOT_TOKEN"));
        builder.addEventListener(this);
        builder.build();
        this.databaseConnectionProvider = databaseConnectionProvider;
        this.mainCommandHandler = mainCommandHandler;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.getAuthor().isFake()) {
            return;
        }

        if (event.getMessage().getContentRaw().startsWith("!")) {
            System.out.println("We received a command from " +
                    event.getAuthor().getName() + ": " +
                    event.getMessage().getContentDisplay()
            );
        }
        try {
            mainCommandHandler.processCommand(event);
        } catch (Exception e) {
            //no need for a single exception to crash out our program
            System.out.println(e.getMessage());
        }
    }
}
