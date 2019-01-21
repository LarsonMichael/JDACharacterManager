package commands;

import com.google.inject.Inject;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class MainCommandHandler {
    private final CharacterCommandHandler characterCommandHandler;
    private final HelpCommandHandler helpCommandHandler;

    @Inject
    MainCommandHandler(CharacterCommandHandler characterCommandHandler,
                       HelpCommandHandler helpCommandHandler) {
        this.characterCommandHandler = characterCommandHandler;
        this.helpCommandHandler = helpCommandHandler;
    }

    public void processCommand(MessageReceivedEvent event) {
        String command = event.getMessage().getContentRaw();
        String output = null;

        if (command.matches(CommandRegex.CHARACTER_COMMAND_MATCHING_REGEX)) {
             output = characterCommandHandler.handleCommand(event, getArgs(command, CommandRegex.CHARACTER_COMMAND_STRIP_REGEX));
        }

        if (command.matches(CommandRegex.HELP_COMMAND_MATCHING_REGEX)) {
            output = helpCommandHandler.handleCommand(event, command);
        }

        if (output != null){
            event.getChannel().sendMessage(output).queue();
        }
    }

    private String getArgs(String command, String commandStripRegex) {
        return command.replaceFirst(commandStripRegex, "");
    }
}
