package commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public interface CommandHandler {
    String handleCommand(MessageReceivedEvent event, String args);
}
