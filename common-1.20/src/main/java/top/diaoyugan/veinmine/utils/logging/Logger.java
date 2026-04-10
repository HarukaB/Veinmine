package top.diaoyugan.veinmine.utils.logging;

import org.slf4j.LoggerFactory;

import static top.diaoyugan.veinmine.Constants.ID;

public class Logger {
    public static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ID);

    public static void throwLog(LoggerLevels type, String message, Object... params) {
        switch (type) {
            case WARN -> LOGGER.warn(message, params);
            case ERROR -> LOGGER.error(message, params);
            case DEBUG -> LOGGER.debug(message, params);
            default -> LOGGER.info(message, params);
        }
    }

    public static void throwLog(LoggerLevels type, String message) {
        switch (type) {
            case WARN -> LOGGER.warn(message);
            case ERROR -> LOGGER.error(message);
            case DEBUG -> LOGGER.debug(message);
            default -> LOGGER.info(message);
        }
    }
}
