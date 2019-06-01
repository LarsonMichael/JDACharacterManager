import com.google.inject.Inject;
import jda.MainListenerAdapter;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;


import javax.security.auth.login.LoginException;

class CharacterManagerMain {
    @Inject
    CharacterManagerMain(MainListenerAdapter mainListenerAdapter) throws LoginException {
        this(mainListenerAdapter, new JDABuilder(AccountType.BOT));
    }

    CharacterManagerMain(MainListenerAdapter mainListenerAdapter, JDABuilder builder) throws LoginException {
        builder.setToken(System.getenv("BOT_TOKEN"));
        builder.addEventListener(mainListenerAdapter);
        builder.build();
    }
}
