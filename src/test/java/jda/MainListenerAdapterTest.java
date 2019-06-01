package jda;

import commands.MainCommandHandler;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class MainListenerAdapterTest {

    @Mock
    private MainCommandHandler mainCommandHandler;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private MessageReceivedEvent event;

    private  MainListenerAdapter mainListenerAdapter;

    @Before
    public void setUp() {
        when(event.getAuthor().isBot()).thenReturn(false);
        when(event.getAuthor().isFake()).thenReturn(false);
        when(event.getMessage().getContentRaw()).thenReturn("!Help");

        mainListenerAdapter = new MainListenerAdapter(mainCommandHandler);
    }

    @Test
    public void testMessageReceivedDoesNothingForBots() {
        when(event.getAuthor().isBot()).thenReturn(true);

        mainListenerAdapter.onMessageReceived(event);

        verifyNoMoreInteractions(mainCommandHandler);
    }

    @Test
    public void testMessageReceived_DoesNothing_WhenMessageAuthorIsFake() {
        when(event.getAuthor().isFake()).thenReturn(true);

        mainListenerAdapter.onMessageReceived(event);

        verifyNoMoreInteractions(mainCommandHandler);
    }

    @Test
    public void testMessageReceived_DoesNothing_WhenMessageDoesNotStartWithBangOperator() {
        when(event.getMessage().getContentRaw()).thenReturn("I'm not talking to the bot");

        mainListenerAdapter.onMessageReceived(event);

        verifyNoMoreInteractions(mainCommandHandler);
    }

    @Test
    public void testMessageReceived_DelegatesToCommandHandler_WhenReceivingCommand() {
        mainListenerAdapter.onMessageReceived(event);

        verify(mainCommandHandler).processCommand(event);
    }

    @Test
    public void testExceptionFromCommandHandler_DoesNotThrowExceptionFromListener() {
        doThrow(new RuntimeException("Blowing  up for reasons")).when(mainCommandHandler).processCommand(event);

        mainListenerAdapter.onMessageReceived(event);
    }
}