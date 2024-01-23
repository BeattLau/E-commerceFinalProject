package eCommerce;
import java.io.IOException;
import java.util.logging.*;
public class LoggingConfig {
    private static final Logger logger = Logger.getLogger(LoggingConfig.class.getName());
    public static void configureLogging() {
        Logger rootLogger = Logger.getLogger("");
        for (Handler handler : rootLogger.getHandlers()) {
            handler.setLevel(Level.ALL);
        }
        rootLogger.setLevel(Level.ALL);

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        rootLogger.addHandler(consoleHandler);

        try {
            FileHandler fileHandler = new FileHandler("application.log");
            fileHandler.setLevel(Level.ALL);
            rootLogger.addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
