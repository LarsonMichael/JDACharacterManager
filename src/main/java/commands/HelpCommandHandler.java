package commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class HelpCommandHandler implements CommandHandler {
    private final String HELP_TEXT = "This bot is designed to help manage character stats and rolls for 5th edition.\n" +
            "Characters are tracked per user per server. Multiple characters can be owned by one user on a server,\n" +
            "but only one will be considered the current context at any given time.\n" +
            "For help with a particular command, use ![COMMAND] -h or ![COMMAND] --help for information on that command\n" +
            "The Character Manager bot accepts the following commands:\n" +
            "!help         : Prints the help text you are seeing. !help does not have in depth documentation for -h\n" +
            "!character    : Switches the current context to a provided character name. If that character doesn't exist it is created with default stats";
    @Override
    public String handleCommand(MessageReceivedEvent event, String args) {
        return HELP_TEXT;
    }
}
