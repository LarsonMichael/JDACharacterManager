package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SwitchCharacterTransaction {
    private static String SWITCH_CHARACTER_TRANSACTION = "BEGIN; " +
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

    public static void executeTransaction(long discordGuildId, long discordUserId, String characterName, Connection connection) throws SQLException {

        PreparedStatement statement = connection.prepareStatement(SWITCH_CHARACTER_TRANSACTION);
        statement.setLong(1, discordGuildId);
        statement.setLong(2, discordUserId);
        statement.setString(3, characterName);
        statement.setLong(4, discordGuildId);
        statement.setLong(5, discordUserId);
        statement.setString(6, characterName);
        statement.setString(7, characterName);
        statement.setLong(8, discordGuildId);
        statement.setLong(9, discordUserId);
        statement.setLong(10, discordGuildId);
        statement.setLong(11, discordUserId);
        statement.setString(12, characterName);
        statement.execute();
        connection.close();
    }
}
