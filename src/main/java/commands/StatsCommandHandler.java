package commands;

import com.google.inject.Inject;
import database.CharacterContext;
import database.CharacterRetrievalException;
import database.CurrentContextRetrievalTransaction;
import database.DatabaseConnectionProvider;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StatsCommandHandler implements CommandHandler {
    private final DatabaseConnectionProvider databaseConnectionProvider;
    private final String helpText = "!Stats retrieves the ability scores for the users current character.\n" +
            "Ability scores should be set with the !Set command (use !Set -h for details).\n\n" +
            "The !Stats command has the following structure:\n" +
            "!Stats [FlAGS]\n\n" +
            "The !Stats command accepts the following {FLAGS]:\n" +
            "-h, --help: Prints the manual for using the !Stats command and returns no further info.\n\n" +
            "!Stats has no required non-flag arguments";

    @Inject
    StatsCommandHandler(DatabaseConnectionProvider databaseConnectionProvider) {
        this.databaseConnectionProvider = databaseConnectionProvider;
    }

    @Override
    public String handleCommand(MessageReceivedEvent event, String args) {
        System.out.println("Received !STATS command with arguments : " + args);
        if (args.isEmpty()) {
            try{
                CharacterContext characterContext = getCharacterInfo(event.getGuild().getIdLong(), event.getAuthor().getIdLong());
                return getResults(characterContext);
            } catch (CharacterRetrievalException e) {
                return "An error occurred while retrieving the current character context.";
            }
        }

        if (args.matches(FlagsRegex.HELP_SHORT_FLAG_MATCHING_REGEX) || args.matches(FlagsRegex.HELP_LONG_FLAG_MATCHING_REGEX)) {
            return helpText;
        } else {
            return  "\"" + args + "\" is not a valid set of flags or arguments for !stats. Use !stats -h for information regarding the correct use of this command";
        }
    }

    private CharacterContext getCharacterInfo(long guildId, long userId) throws CharacterRetrievalException {
        CharacterContext characterContext = new CharacterContext();
        try {
            ResultSet resultSet = CurrentContextRetrievalTransaction.executeTransaction(guildId, userId, databaseConnectionProvider.get());
            if (resultSet.next()) {
               characterContext.setGuild(resultSet.getLong("DISCORD_GUILD_ID"));
               characterContext.setOwner(resultSet.getLong("DISCORD_USER_ID"));
               characterContext.setName(resultSet.getString("CHARACTER_NAME"));
               characterContext.setSTR(resultSet.getInt("STR"));
               characterContext.setDEX(resultSet.getInt("DEX"));
               characterContext.setCON(resultSet.getInt("CON"));
               characterContext.setINT(resultSet.getInt("INT"));
               characterContext.setWIS(resultSet.getInt("WIS"));
               characterContext.setCHA(resultSet.getInt("CHA"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new CharacterRetrievalException();
        }
        return characterContext;
    }

    private String getResults(CharacterContext characterContext) {
        return "Stats for " + characterContext.getName() + ":\n" +
                "STR: " + characterContext.getSTR()+ "\n" +
                "DEX: " + characterContext.getDEX()+ "\n" +
                "CON: " + characterContext.getCON()+ "\n" +
                "INT: " + characterContext.getINT()+ "\n" +
                "WIS: " + characterContext.getWIS()+ "\n" +
                "CHA: " + characterContext.getCON();
    }
}
