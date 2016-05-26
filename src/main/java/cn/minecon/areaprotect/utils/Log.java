package cn.minecon.areaprotect.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Log {
	private static Logger logger;
	
    public static void initLogger(Logger logger) {
    	Log.logger = logger;
    }
    
    public static void info(final String msg) {
        logger.log(Level.INFO, msg);
    }
    
    public static void warning(final String msg) {
        logger.log(Level.WARNING, msg);
    }

    public static void info(final String msg, final Throwable e) {
        logger.log(Level.INFO, msg, e);
    }

    public static void warning(final String msg, final Throwable e) {
        logger.log(Level.WARNING, msg, e);
    }
    
    public static void severe(final String msg) {
        logger.log(Level.SEVERE, msg);
    }

    public static void severe(final String msg, final Throwable e) {
        logger.log(Level.SEVERE, msg, e);
    }
}
