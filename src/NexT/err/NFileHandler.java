/**********************\
  file: NLogger.java
  package: transcend
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package NexT.err;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Logging handler class that writes and formats the log events to a file.
 * This uses an internal logging level and is intended for use with a logger
 * that uses a global logging level of ALL.
 * @author Shinmera
 */
public class NFileHandler extends NHandler{
    private PrintWriter pw;
    
    public NFileHandler(File f){this(f, Level.INFO);}
    public NFileHandler(File f, Level level){
        super(level);
        try{
            OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(f),"UTF-8");
            pw = new PrintWriter(fw);
        }catch(Exception e){
            Logger.getLogger("NexT").log(Level.WARNING,"[NexT][NLogger] Couldn't initialize error dump file "+f.getAbsolutePath());
        }
    }

    @Override
    public void publish(LogRecord record) {
        if(pw!=null && record.getLevel().intValue() >= level.intValue()){
            pw.println(sformat(record));
            pw.flush();
        }
    }

    public void flush() { pw.flush(); }
    public void close() throws SecurityException { pw.close(); }
}
