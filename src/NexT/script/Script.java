/**********************\
  file: Script.java
  package: NexT.script
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package NexT.script;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Script {
    private HashMap<String,Function> funcs = new HashMap<String,Function>();
    private HashMap<String,String> vars = new HashMap<String,String>();
    private File script;

    public Script(){

    }

    public boolean loadScript(File file){


        return true;
    }

    public Function getFunction(String name){return funcs.get(name);}
    public String getVariable(String name){return vars.get(name);}
    public void setFunction(String name,Function f){funcs.put(name,f);}
    public void setVariable(String name,String v){vars.put(name,v);}
    public boolean hasFunction(String name){return funcs.containsKey(name);}

    public String eval(String func,HashMap<String,String> args){return funcs.get(func).exec(args);}

    public class Function{
        Script script;
        public Function(Script script){
            this.script=script;
        }

        public boolean loadFunction(String block){
            
            return true;
        }

        public String exec(HashMap<String,String> args){
            return "";
        }
    }

    public class IfExpression{
        public final String EXPR_GREATER = ">";
        public final String EXPR_SMALLER = "<";
        public final String EXPR_EQUAL   = "==";
        public final String EXPR_NOT_EQUAL = "!=";
        public final String EXPR_GREATER_OR_EQUAL = ">=";
        public final String EXPR_SMALLER_OR_EQUAL = "<=";
        Script script;
        
        public IfExpression(Script script){
            this.script=script;
        }

        public boolean eval(String expression,HashMap<String,String> arg){
            ArrayList<String> args = NexT.script.Math.getArguments(expression);
            HashMap<String,String> var = vars;var.putAll(arg);//merge arguments and local variables for the maths parser.
            try{
            if(args.get(1).equals(EXPR_EQUAL)){if(NexT.script.Math.parseExpression(args.get(0), var, script) == NexT.script.Math.parseExpression(args.get(2), var, script))return true;else return false;}
            if(args.get(1).equals(EXPR_NOT_EQUAL)){if(NexT.script.Math.parseExpression(args.get(0), var, script) != NexT.script.Math.parseExpression(args.get(2), var, script))return true;else return false;}
            if(args.get(1).equals(EXPR_SMALLER)){if(NexT.script.Math.parseExpression(args.get(0), var, script) < NexT.script.Math.parseExpression(args.get(2), var, script))return true;else return false;}
            if(args.get(1).equals(EXPR_GREATER)){if(NexT.script.Math.parseExpression(args.get(0), var, script) > NexT.script.Math.parseExpression(args.get(2), var, script))return true;else return false;}
            if(args.get(1).equals(EXPR_SMALLER_OR_EQUAL)){if(NexT.script.Math.parseExpression(args.get(0), var, script) <= NexT.script.Math.parseExpression(args.get(2), var, script))return true;else return false;}
            if(args.get(1).equals(EXPR_GREATER_OR_EQUAL)){if(NexT.script.Math.parseExpression(args.get(0), var, script) >= NexT.script.Math.parseExpression(args.get(2), var, script))return true;else return false;}
            }catch(Exception e){Logger.getLogger("NexT").log(Level.WARNING, "[NexT][Script][IfExpression] Failed to evaluate!",e);return false;}
            return false;
        }
    }

}
