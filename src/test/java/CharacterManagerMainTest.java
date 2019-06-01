import jda.MainListenerAdapter;
import net.dv8tion.jda.core.JDABuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.security.auth.login.LoginException;

import static org.mockito.Mockito.inOrder;

@RunWith(MockitoJUnitRunner.class)
public class CharacterManagerMainTest {

    @Mock
    private MainListenerAdapter mainListenerAdapter;

    @Mock
    private JDABuilder builder;

    @Test
    public void testMainAddsMainListenerAdapterToBuilderBeforeBuild() throws LoginException {
        new CharacterManagerMain(mainListenerAdapter, builder);

        InOrder inOrder = inOrder(mainListenerAdapter, builder);
        inOrder.verify(builder).addEventListener(mainListenerAdapter);
        inOrder.verify(builder).build();
    }
}