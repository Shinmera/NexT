/**********************\
  file: NSCObject.java
  package: NexT.script
  author: Shinmera
  team: NexT
  license: -
\**********************/

package NexT.script;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NSCObject<A extends NSCObject> extends ArrayList<NSCObject>{
    NSCREPL repl = null;
    
    public NSCObject(NSCREPL repl){
        this.repl=repl;
    }
    
    public NSCObject(NSCREPL repl,String obj){
        this.repl=repl;
    }
    
    public NSCObject(NSCREPL repl,List<NSCObject> list){
        this(repl);
        addAll(list);
    }
    
    public NSCObject eval(NSCObject args){
        if(size()==0)return null;
        if(repl.functionExists(get(0).toString())){
            return repl.eval(get(0).toString(),subList(1, size()));
        }else{
            return this;
        }
    }
}
