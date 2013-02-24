package NexT;

import NexT.err.NLogger;
import java.util.logging.Level;
import java.util.logging.Logger;


public final class Commons {
    public static Logger log = NLogger.get("NexTLib", Level.INFO, null);
}
