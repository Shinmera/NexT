/**********************\
  file: ScriptManager.java
  package: NexT.script
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package NexT.script;

import NexT.Commons;
import NexT.util.SimpleSet;
import java.io.File;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

public class ScriptManager {
    private SimpleSet<String,Script> scripts = new SimpleSet<String,Script>();
    private Timer timer = new Timer();

    public ScriptManager(){}
    public boolean loadScript(File f){
        return loadScript(f,true);
    }

    public boolean loadScript(File f,boolean time){
        Script script = new Script();
        if(!script.loadScript(f))return false;

        scripts.put(f.getName().substring(0,f.getName().indexOf(".")), script);
        if(time){
            //add watch for automatic reload
            TimerTask task = new FileWatcher(f);
            timer.schedule(task , new Date(), 500);
        }
        return true;
    }

    public Script getScript(String name){return scripts.get(name);}
    public Script getScript(int n){return scripts.getAt(n);}
    public Script get(String name){return scripts.get(name);}
    public Script get(int n){return scripts.getAt(n);}
    public Script s(String name){return scripts.get(name);}
    public Script s(int n){return scripts.getAt(n);}
    public void delScript(String name){scripts.remove(name);}
    public void delScript(int n){scripts.removeAt(n);}
    public void clear(){scripts.clear();}

    class FileWatcher extends TimerTask {
        private long timeStamp;
        private File file;

        public FileWatcher( File file ) {
            this.file = file;
            this.timeStamp = file.lastModified();
        }

        public final void run() {
            long timeStamp = file.lastModified();

            if( this.timeStamp != timeStamp ) {
            this.timeStamp = timeStamp;
            onChange(file);
            }
        }

        public void onChange( File file ){
            Commons.log.info("[NexT][ScriptManager] Automatically reloading script '"+file.getName()+"'");
            loadScript(file,false);
        }
    }
}