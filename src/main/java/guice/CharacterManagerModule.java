package guice;

import com.google.inject.AbstractModule;
import database.DatabaseConnectionProvider;



public class CharacterManagerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(DatabaseConnectionProvider.class);
    }
}
