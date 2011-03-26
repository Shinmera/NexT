/**********************\
  file: OptionManager
  package: util
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package NexT.util;
import java.io.*;
import java.util.*;

/*
 * Dummy class
 *
 */
public class OptionManager extends OptionSet{
    private ArrayList<String> permissions;
    private ConfigManager conf = new ConfigManager("default.conf");

    public boolean save(){
        conf.input(this);
        return conf.saveConfig();
    }

    public boolean load(){
        boolean ret = conf.loadConfig();
        //uuh
        return ret;
    }
}
