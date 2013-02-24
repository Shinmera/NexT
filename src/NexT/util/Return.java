/**********************\
  file: Return
  package: NexT.util
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package NexT.util;

import NexT.Commons;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Return {
    private boolean success;
    private String err;
    private Exception e;

    public Return(){
        this.success=true;
        this.err = "";
    }

    public Return(boolean success){
        this.success=success;
        this.err = "";
    }

    public Return(boolean success,String err){
        this.success=success;
        this.err = err;
    }

    public Return(boolean success,Exception e){
        this.success=success;
        this.err = "";
        this.e = e;
    }

    public Return(boolean success,String err,Exception e){
        this.success=success;
        this.err = err;
        this.e = e;
    }

    public boolean isSuccessful(){return success;}
    public String getError(){return err;}
    public Exception getException(){return e;}
    public boolean justify(){
        if(!success)Commons.log.log(Level.SEVERE, err, e);
        return success;
    }
}
