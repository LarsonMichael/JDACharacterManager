package jda;

import com.google.inject.Inject;
import commands.MainCommandHandler;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MainListenerAdapter extends ListenerAdapter {
    private final MainCommandHandler mainCommandHandler;

    @Inject
    MainListenerAdapter(MainCommandHandler mainCommandHandler) {
        this.mainCommandHandler = mainCommandHandler;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.getAuthor().isFake()) {
            return;
        }

        if (event.getMessage().getContentRaw().startsWith("!")) {
            System.out.println("We received a command from " +
                    event.getAuthor().getName() + ": " +
                    event.getMessage().getContentDisplay()
            );
            try {
                mainCommandHandler.processCommand(event);
            } catch (Exception e) {
                //no need for a single exception to crash out our program
                System.out.println(e.getMessage());
            }
        }
    }
}
