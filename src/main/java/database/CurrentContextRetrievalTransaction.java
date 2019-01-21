package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CurrentContextRetrievalTransaction {
    private static String CURRENT_CONTEXT_RETRIEVAL_TRANSACTION = "SELECT " +
            "\"CURRENT_CHARACTER_CONTEXT\".\"DISCORD_GUILD_ID\", " +
            "\"CURRENT_CHARACTER_CONTEXT\".\"DISCORD_USER_ID\", " +
            "\"CURRENT_CHARACTER_CONTEXT\".\"CHARACTER_NAME\", " +
            "\"CHARACTERS_TO_ID\".\"CHARACTER_ID\", " +
            "\"CHARACTERS\".\"STR\", " +
            "\"CHARACTERS\".\"DEX\", " +
            "\"CHARACTERS\".\"CON\", " +
            "\"CHARACTERS\".\"INT\", " +
            "\"CHARACTERS\".\"WIS\", " +
            "\"CHARACTERS\".\"CHA\" " +
            "FROM \"CHARACTER_MANAGER\".\"CURRENT_CHARACTER_CONTEXT\" " +
            "INNER JOIN \"CHARACTER_MANAGER\".\"CHARACTERS_TO_ID\" " +
            "ON \"CURRENT_CHARACTER_CONTEXT\".\"CHARACTER_NAME\" = \"CHARACTERS_TO_ID\".\"CHARACTER_NAME\" " +
            "AND \"CURRENT_CHARACTER_CONTEXT\".\"DISCORD_GUILD_ID\" = \"CHARACTERS_TO_ID\".\"DISCORD_GUILD_ID\" " +
            "AND \"CURRENT_CHARACTER_CONTEXT\".\"DISCORD_USER_ID\" = \"CHARACTERS_TO_ID\".\"DISCORD_USER_ID\" " +
            "INNER JOIN \"CHARACTER_MANAGER\".\"CHARACTERS\" " +
            "ON \"CHARACTERS_TO_ID\".\"CHARACTER_ID\" = \"CHARACTERS\".\"CHARACTER_ID\" " +
            "WHERE \"CURRENT_CHARACTER_CONTEXT\".\"DISCORD_GUILD_ID\" = ? " +
            "AND \"CURRENT_CHARACTER_CONTEXT\".\"DISCORD_USER_ID\" = ? ";

    public static ResultSet executeTransaction(long guildId, long userId, Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(CURRENT_CONTEXT_RETRIEVAL_TRANSACTION);
        statement.setLong(1, guildId);
        statement.setLong(2, userId);
        ResultSet rs = statement.executeQuery();
        connection.close();
        return rs;
    }

}
