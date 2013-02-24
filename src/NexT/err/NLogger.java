/**********************\
file: NLogger.java
package: NexT.err
author: Shinmera
team: NexT
license: -
\**********************/
package NexT.err;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class NLogger{
    /**
     * Returns a logger on INFO level with standard console and file handlers.
     * The logger name will be "err".
     * @return A Logger instance with the attached handlers.
     * @see NLogger#get(java.lang.String, java.util.logging.Level, java.util.logging.Level) 
     */
    public static Logger get(){return get("err");}
    
    /**
     * Returns a logger on INFO level with standard console and file handlers.
     * The log filename will be the same as the logger name + ".log".
     * @param name The name for the logger and logging file.
     * @return A Logger instance with the attached handlers.
     * @see NLogger#get(java.lang.String, java.util.logging.Level, java.util.logging.Level) 
     */
    public static Logger get(String name){return get(name, Level.INFO);}
    
    /**
     * Returns a logger on specified level with standard console and file
     * handlers. The log filename will be the same as the logger name + ".log".
     * @param name The name for the logger and logging file.
     * @param combined The logging level that is used in the log handlers.
     * @return A Logger instance with the attached handlers.
     * @see NLogger#get(java.lang.String, java.util.logging.Level, java.util.logging.Level) 
     */
    public static Logger get(String name, Level combined){return get(name, combined, combined);}
    
    /**
     * Returns a logger with standard console and file handlers attached. These
     * handlers will print all levels above their respective Levels.
     * @param name The name for the logger and logging file.
     * @param console The logging level that is used for the console. If null,
     * no console handler will be added.
     * @param file The logging level that is used for the file. If null, no file
     * handler will be added.
     * @return A Logger.get("NexTLib"); instance with the attached handlers.
     */
    public static Logger get(String name, Level console, Level file){
        Logger lg = Logger.getLogger(name);
        lg.setUseParentHandlers(false);
        if(console != null)lg.addHandler(new NHandler(console));
        if(file != null)   lg.addHandler(new NFileHandler(new File(name+".log"), file));
        lg.setLevel(Level.ALL);
        return lg;
    }
}
