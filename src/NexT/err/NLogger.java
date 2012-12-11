/**********************\
file: NLogger.java
package: NexT.err
author: Shinmera
team: NexT
license: -
\**********************/
package NexT.err;

import java.io.File;
import java.util.logging.Logger;

public abstract class NLogger{
    public static Logger get(String name){
        Logger lg = Logger.getLogger(name);
        lg.setUseParentHandlers(false);
        lg.addHandler(new NHandler(new File(name+".log")));
        return lg;
    }
    public static Logger get(){
        return get("err");
    }
}
