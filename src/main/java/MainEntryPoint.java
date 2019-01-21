import com.google.inject.Guice;
import com.google.inject.Injector;
import guice.CharacterManagerModule;

public class MainEntryPoint {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new CharacterManagerModule());
        injector.getInstance(CharacterManagerMain.class);
    }
}
