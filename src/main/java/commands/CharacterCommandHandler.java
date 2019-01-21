package commands;


import com.google.inject.Inject;
import database.DatabaseConnectionProvider;
import database.SwitchCharacterTransaction;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.sql.Connection;
import java.sql.SQLException;

public class CharacterCommandHandler implements CommandHandler {
    private final String helpText = "The character command sets your current character on this server by name.\n" +
            "If the character does not exist it is created with default stats and set as your current character.\n\n" +
            "The character command has the following structure:\n" +
            "!character [FLAGS] character_name\n\n" +
            "The character command accepts the following [FLAGS]:\n" +
            "-h, --help : prints the help text for the character command and then exits without attempting character creation\n\n" +
            "The remaining input is interpreted as the character name, spaces and special characters are allowed. Character names are case sensitive\n" +
            "Attempting to start a character name with  \"-\" will cause an error.";

    private final DatabaseConnectionProvider databaseConnectionProvider;

    @Inject
    CharacterCommandHandler(DatabaseConnectionProvider databaseConnectionProvider) {
        this.databaseConnectionProvider = databaseConnectionProvider;
    }

    public String handleCommand(MessageReceivedEvent event, String args) {
       System.out.println("Received !Create command with arguments : " + args);
       if (args.matches(FlagsRegex.HELP_LONG_FLAG_MATCHING_REGEX)|| args.matches(FlagsRegex.HELP_SHORT_FLAG_MATCHING_REGEX)) {
           return helpText;
       }
       if (args.startsWith("-")) {
           return  "\"" + args + "\" is not a valid set of flags or arguments for !character. Use !character -h for information regarding the correct use of this command";
       }
       if (switchCharacter(event.getGuild().getIdLong(), event.getAuthor().getIdLong(), args)) {
           return "Current character for  " + event.getAuthor().getName() + ": " + args;
       } else {
           return "Failed to switch character context for " + event.getAuthor().getName() + " to character: " + args;
       }
    }

    private boolean switchCharacter(long guildId, long userId, String character_name) {
        try {
            Connection connection = databaseConnectionProvider.get();
            if(connection != null) {
                SwitchCharacterTransaction.executeTransaction(guildId, userId, character_name, connection);
            }
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
