package com.ecommerce.Config.Documentation;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggingConfig {
    public static void configureLogging() {
        Logger rootLogger = Logger.getLogger("");

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);

        rootLogger.addHandler(consoleHandler);
        rootLogger.setLevel(Level.ALL);
    }
}
