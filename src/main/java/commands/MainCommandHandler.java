package commands;

import com.google.inject.Inject;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class MainCommandHandler {
    private final CharacterCommandHandler characterCommandHandler;
    private final HelpCommandHandler helpCommandHandler;
    private final StatsCommandHandler statsCommandHandler;

    @Inject
    MainCommandHandler(CharacterCommandHandler characterCommandHandler,
                       HelpCommandHandler helpCommandHandler,
                       StatsCommandHandler statsCommandHandler) {
        this.characterCommandHandler = characterCommandHandler;
        this.helpCommandHandler = helpCommandHandler;
        this.statsCommandHandler =  statsCommandHandler;
    }

    public void processCommand(MessageReceivedEvent event) {
        //Filter the command to eliminate compound spaces and trailing/leading whitespace
        String command = event.getMessage().getContentRaw().replaceAll("( )+", " ").trim();
        String output = null;

        if (command.matches(CommandRegex.CHARACTER_COMMAND_MATCHING_REGEX)) {
             output = characterCommandHandler.handleCommand(event, getArgs(command, CommandRegex.CHARACTER_COMMAND_STRIP_REGEX));
        }

        if (command.matches(CommandRegex.HELP_COMMAND_MATCHING_REGEX)) {
            output = helpCommandHandler.handleCommand(event, command);
        }

        if (command.matches(CommandRegex.STATS_COMMAND_MATCHING_REGEX)) {
            output = statsCommandHandler.handleCommand(event, getArgs(command, CommandRegex.STATS_COMMAND_STRIPPING_REGEX));
        }

        if (output != null){
            event.getChannel().sendMessage(output).queue();
        }
    }

    private String getArgs(String command, String commandStripRegex) {
        return command.replaceFirst(commandStripRegex, "").trim();
    }
}
