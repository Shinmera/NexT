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

public class NLogger{
    public static Logger get(){
        Logger lg = Logger.getLogger("+");
        lg.setUseParentHandlers(false);
        lg.addHandler(new NHandler(new File("err.log")));
        return lg;
    }
}
