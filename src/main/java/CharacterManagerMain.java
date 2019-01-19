import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CharacterManagerMain extends ListenerAdapter {
    private static Connection databaseConnection;

    public static void main(String[] args) throws LoginException {
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        builder.setToken(System.getenv("BOT_TOKEN"));
        builder.addEventListener(new CharacterManagerMain());
        builder.build();

       databaseConnection = null;
       try {
           databaseConnection = DriverManager.getConnection("jdbc:postgresql://localhost/postgres", System.getenv("DATABASE_USER"), System.getenv("DATABASE_PASSWORD"));
           System.out.println("CONNECTED");
       } catch (SQLException e) {
           System.out.println(e.getMessage());
       }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.getAuthor().isFake()) {
            return;
        }

        System.out.println("We received a message from " +
                event.getAuthor().getName() + ": " +
                event.getMessage().getContentDisplay()
        );

        if (event.getMessage().getContentRaw().equalsIgnoreCase("!ping")) {
            event.getChannel().sendMessage("Pong!").queue();
        }

        String createCommandCreationRegex = "^(?i)!CHARACTER(?-i) .*";
        String createCommandStripRegex = "^(?i)!CHARACTER(?-i) ";
        if (event.getMessage().getContentRaw().matches(createCommandCreationRegex)) {
            String commandString = event.getMessage().getContentRaw().replaceFirst(createCommandStripRegex, "");
            System.out.println("Received !Create command with arguments : " + commandString);
            if (createCharacter(event.getGuild().getIdLong(), event.getAuthor().getIdLong(), commandString)) {
                event.getChannel().sendMessage("Current Character for  " + event.getAuthor().getName() + ": " + commandString).queue();
            } else {
                event.getChannel().sendMessage("Failed to switch character context for " + event.getAuthor().getName() + " to character: " + commandString).queue();
            }
        }

        event.getGuild().getIdLong();
    }

    private boolean createCharacter(long guildId, long userId, String args) {
        String createCharacterTransaction = "BEGIN; " +
                "INSERT INTO \"CHARACTER_MANAGER\".\"CHARACTERS_TO_ID\" (\"DISCORD_GUILD_ID\", \"DISCORD_USER_ID\", \"CHARACTER_NAME\") " +
                "VALUES (?, ?, ?) " +
                "ON CONFLICT ON CONSTRAINT \"CHARACTERS_TO_ID_pkey\" DO NOTHING; " +

                "INSERT INTO \"CHARACTER_MANAGER\".\"CURRENT_CHARACTER_CONTEXT\" (\"DISCORD_GUILD_ID\", \"DISCORD_USER_ID\", \"CHARACTER_NAME\") " +
                "VALUES (?, ? , ?) " +
                "ON CONFLICT ON CONSTRAINT \"CURRENT_CHARACTER_CONTEXT_pkey\" DO UPDATE SET \"CHARACTER_NAME\" = ? " +
                "WHERE \"CURRENT_CHARACTER_CONTEXT\".\"DISCORD_GUILD_ID\" = ? AND \"CURRENT_CHARACTER_CONTEXT\".\"DISCORD_USER_ID\" = ?; " +

                "INSERT INTO \"CHARACTER_MANAGER\".\"CHARACTERS\"(\"CHARACTER_ID\") " +
                        "SELECT c.\"CHARACTER_ID\" " +
                        "FROM \"CHARACTER_MANAGER\".\"CHARACTERS_TO_ID\" c " +
                        "WHERE c.\"DISCORD_GUILD_ID\" = ? AND c.\"DISCORD_USER_ID\" = ? AND c.\"CHARACTER_NAME\" = ?" +
                "ON CONFLICT ON CONSTRAINT \"CHARACTERS_pkey\" DO NOTHING; " +

                "COMMIT;";

        try {
            PreparedStatement statement = databaseConnection.prepareStatement(createCharacterTransaction);
            statement.setLong(1, guildId);
            statement.setLong(2, userId);
            statement.setString(3, args);
            statement.setLong(4, guildId);
            statement.setLong(5, userId);
            statement.setString(6, args);
            statement.setString(7, args);
            statement.setLong(8, guildId);
            statement.setLong(9, userId);
            statement.setLong(10, guildId);
            statement.setLong(11, userId);
            statement.setString(12, args);
            statement.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
