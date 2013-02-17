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
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class NHandler extends Handler{
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    PrintWriter pw;
    
    public NHandler(File f){
        try{
            if(f!=null){
                OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(f),"UTF-8");
                pw = new PrintWriter(fw);
            }
        }catch(Exception e){
            Logger.getLogger("NexT").log(Level.WARNING,"[NexT][NLogger] Couldn't initialize error dump file "+f.getAbsolutePath());
        }
    }

    public String sformat(LogRecord record) {
        if(record.getThrown()==null)
            return sdf.format(record.getMillis())+" ["+record.getLevel().getName()+"]"+record.getMessage();
        else{
            return sdf.format(record.getMillis())+" ["+record.getLevel().getName()+"]"+record.getMessage()+"\n"+
                   record.getResourceBundleName()+"."+record.getSourceClassName()+"."+record.getSourceMethodName()+": "+
                   Toolkit.stackTrace(record.getThrown());
        }
    }

    public String format(LogRecord record) {
        if(record.getLevel()==null)record.setLevel(Level.INFO);
        if(record.getThrown()==null)
            return sdf.format(record.getMillis())+" ["+record.getLevel().getName()+"]"+record.getMessage()
                +"\n"+record.getResourceBundleName()+"."+record.getSourceClassName()+"."+record.getSourceMethodName();
        else
            return sdf.format(record.getMillis())+" ["+record.getLevel().getName()+"]"+record.getMessage()
                +"\n"+record.getResourceBundleName()+"."+record.getSourceClassName()+"."+record.getSourceMethodName()+": "
                +record.getThrown().getMessage();
    }

    @Override
    public void publish(LogRecord record) {
        System.out.println(sformat(record));
        if(pw!=null){
            pw.println(format(record));
            pw.flush();
        }
    }

    public void flush() { pw.flush(); }
    public void close() throws SecurityException { pw.close(); }

}
