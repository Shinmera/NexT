/**********************\
  file: NLogger.java
  package: transcend
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package NexT.err;

import NexT.util.Toolkit;
import java.text.SimpleDateFormat;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Logging handler class that writes and formats the log events to the console.
 * This uses an internal logging level and is intended for use with a logger
 * that uses a global logging level of ALL.
 * @author Shinmera
 */
public class NHandler extends Handler{
    public static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    protected Level level;
    
    public NHandler(){this(Level.INFO);}
    public NHandler(Level level){this.level=level;}

    public String sformat(LogRecord record) {
        if(record.getThrown()==null)
            return sdf.format(record.getMillis())+" ["+record.getLevel().getName()+"]"+record.getMessage();
        else{
            return sdf.format(record.getMillis())+" ["+record.getLevel().getName()+"]"+record.getMessage()+"\n"+
                   record.getResourceBundleName()+"."+record.getSourceClassName()+"."+record.getSourceMethodName()+": "+
                   Toolkit.stackTrace(record.getThrown());
        }
    }

    @Override
    public void publish(LogRecord record) {
        if(record.getLevel().intValue() >= level.intValue())
            System.out.println(sformat(record));
    }

    public void flush(){}
    public void close() throws SecurityException{}
    
    public void setLevel(Level level){this.level=level;}
    public Level getLevel(){return level;}
}
