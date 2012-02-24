/**********************\
  file: NSCREPL.java
  package: NexT.script
  author: Shinmera
  team: NexT
  license: -
\**********************/

package NexT.script;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class NSCREPL extends Thread{
    private HashMap<String,NSCObject> funcs = new HashMap<String,NSCObject>();
    private Queue<String> execQueue = new LinkedList<String>();
    
    public static void main(String[] args){
        NSCREPL repl = new NSCREPL();
        repl.start();
    }
    
    public void run(){
        while(!isInterrupted()){
            String exec = execQueue.poll();
            if(exec!=null){
                NSCObject obj = new NSCObject(this,exec);
                obj.eval(null);
            }
            if(execQueue.isEmpty()){
                try{Thread.sleep(500);}catch(Exception e){}
            }
        }
    }
    
    public void run(String command){
        execQueue.add(command);
    }
    
    public boolean functionExists(String fun){
        return funcs.containsKey(fun);
    }
    
    public NSCObject eval(String fun,NSCObject args){
        return funcs.get(fun).eval(args);
    }
    
    public NSCObject eval(String fun,List<NSCObject> args){
        NSCObject arg = new NSCObject(this,args);
        return funcs.get(fun).eval(arg);
    }
}
