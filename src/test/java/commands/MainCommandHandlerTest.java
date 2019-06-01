package commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MainCommandHandlerTest {

    //TODO rebuild this test and class with TDD instead of backfilling

    private static String MESSAGE_RESPONSE_STRING = "message";
    private static String COMMMAND_STRING = "command";

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private MessageReceivedEvent event;

    @Mock
    CharacterCommandHandler characterCommandHandler;

    @Mock
    HelpCommandHandler helpCommandHandler;

    @Mock
    StatsCommandHandler statsCommandHandler;

    private MainCommandHandler commandHandler;

    @Before
    public void setUp() {
        commandHandler = new MainCommandHandler(characterCommandHandler,
                helpCommandHandler,
                statsCommandHandler);
    }

    @Test
    public void testCommandHandler_DelegatesToCharacterCommandHandler_AndSendsMessage() {
        when(event.getMessage().getContentRaw()).thenReturn("!Character " + COMMMAND_STRING);
        when(characterCommandHandler.handleCommand(event, COMMMAND_STRING)).thenReturn(MESSAGE_RESPONSE_STRING);

        commandHandler.processCommand(event);

        verify(characterCommandHandler).handleCommand(event, COMMMAND_STRING);
        verify(event.getChannel().sendMessage(MESSAGE_RESPONSE_STRING)).queue();
    }

    @Test
    public void testCommandHandler_DelegatesToHelpCommandHandler_AndSendsMessage() {
        when(event.getMessage().getContentRaw()).thenReturn("!Help " + COMMMAND_STRING);
        when(helpCommandHandler.handleCommand(event, "!Help " +COMMMAND_STRING)).thenReturn(MESSAGE_RESPONSE_STRING);

        commandHandler.processCommand(event);

        verify(helpCommandHandler).handleCommand(event, "!Help " + COMMMAND_STRING);
        verify(event.getChannel().sendMessage(MESSAGE_RESPONSE_STRING)).queue();
    }

    @Test
    public void testCommandHandler_DelegatesToStatsCommandHandler_AndSendsMessage() {
        when(event.getMessage().getContentRaw()).thenReturn("!Stats " + COMMMAND_STRING);
        when(statsCommandHandler.handleCommand(event, COMMMAND_STRING)).thenReturn(MESSAGE_RESPONSE_STRING);

        commandHandler.processCommand(event);

        verify(statsCommandHandler).handleCommand(event, COMMMAND_STRING);
        verify(event.getChannel().sendMessage(MESSAGE_RESPONSE_STRING)).queue();
    }


    @Test
    public void testCommandHandler_CollapesCompoundSpaces_AndTrims() {
        when(event.getMessage().getContentRaw()).thenReturn("  !Character       " + COMMMAND_STRING + "     ");
        when(characterCommandHandler.handleCommand(event, COMMMAND_STRING)).thenReturn(MESSAGE_RESPONSE_STRING);

        commandHandler.processCommand(event);

        verify(characterCommandHandler).handleCommand(event, COMMMAND_STRING);
        verify(event.getChannel().sendMessage(MESSAGE_RESPONSE_STRING)).queue();
    }
}