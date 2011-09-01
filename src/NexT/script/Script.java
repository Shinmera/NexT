/**********************\
  file: Script.java
  package: NexT.script
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package NexT.script;

import NexT.err.InvalidArgumentCountException;
import NexT.err.MissingOperandException;
import NexT.util.Toolkit;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

public class Script {
    private HashMap<String,Function> funcs = new HashMap<String,Function>();
    private HashMap<String,String> vars = new HashMap<String,String>();
    private File script;

    public Script(){

    }

    public static void main(String[] args){
        Script script = new Script();
        JFileChooser jfc = new JFileChooser();
        jfc.showOpenDialog(null);
        script.loadScript(jfc.getSelectedFile());
        Object[] funcs = script.getFunctions().toArray();
        System.out.println("Functions: "+Toolkit.implode(funcs, ", "));
        System.out.println(">>"+script.eval("testFunction", null));
    }

    public boolean loadScript(File file){
        String[] contents = Toolkit.loadFileToString(file).split("\n");
        if(!contents[0].equals("#NexT/script"))return false;
        boolean inBlock=false,skip=false;int bracketCounter=0;
        String name = "";StringBuilder cont = new StringBuilder();
        for(int i=1;i<contents.length;i++){
                if(contents[i].contains("#"))contents[i] = contents[i].substring(0,contents[i].indexOf("#"));
                if(contents[i].contains("//"))contents[i] = contents[i].substring(0,contents[i].indexOf("//"));
                if(contents[i].contains("/*"))skip = true;
                if(contents[i].contains("*/"))skip = false;
                contents[i] = contents[i].trim();

                if(!skip&&contents[i].length() != 0){
                    if(!inBlock&&contents[i].contains("{")&&contents[i].startsWith("defun")){
                        inBlock = true;
                        name = contents[i].substring(contents[i].indexOf(" "),contents[i].indexOf("{")).trim();
                        funcs.put(name, new Function(this));
                        contents[i]="";
                    }
                    if(inBlock){
                        //Function
                        if(contents[i].contains("{"))bracketCounter++;
                        if(contents[i].contains("}")){
                            if(bracketCounter>0)bracketCounter--;
                            else{
                                inBlock = false;
                                contents[i] = contents[i].substring(0,contents[i].indexOf("}"));
                            }
                        }

                        contents[i] = contents[i].trim();
                        if(contents[i].length() != 0){ //value.
                            cont.append(contents[i]).append("\n");
                        }

                        if(!inBlock){ //End of block reached.
                            funcs.get(name).loadFunction(cont.toString());
                            cont = new StringBuilder();
                        }
                    }else if(contents[i].length() != 0){
                        //Variable
                        String key = contents[i].split("=")[0];
                        String val = contents[i].split("=")[1];
                        vars.put(key.trim(), val.trim());
                    }
                }
            }

        return true;
    }

    public Set<String> getFunctions(){return funcs.keySet();}
    public Function getFunction(String name){return funcs.get(name);}
    public String getVariable(String name){return vars.get(name);}
    public void setFunction(String name,Function f){funcs.put(name,f);}
    public void setVariable(String name,String v){vars.put(name,v);}
    public boolean hasFunction(String name){return funcs.containsKey(name);}

    public String eval(String func,HashMap<String,String> args){
        if(args==null)args = new HashMap<String,String>();
        try{return funcs.get(func).exec(args);
        }catch(Exception ex){Logger.getLogger("NexT").log(Level.SEVERE,"[NexT][Script][Function] Error while running function '"+func+"' on line "+funcs.get(func).getCurrentLine(),ex);}
        return "";
    }

    public class Function{
        Script script;
        String[] contents;
        int n=0;
        public Function(Script script){
            this.script=script;
        }

        public int getCurrentLine(){return n;}

        public boolean loadFunction(String block){
            //purify script
            ArrayList<String> pure = new ArrayList<String>();
            String[] contents = block.split("\n");
            for(int i=0;i<contents.length;i++){
                contents[i]=contents[i].trim();
                if(contents[i].length() != 0)pure.add(contents[i]);
            }
            this.contents=pure.toArray(new String[pure.size()]);
            return true;
        }

        public HashMap<String,String> allVars(HashMap<String,String> locals){
            HashMap<String,String> temp = locals;temp.putAll(vars);
            return temp;
        }

        public String exec(HashMap<String,String> args) throws MissingOperandException, InvalidArgumentCountException{
            int inBlock=0;String expression="";
            IfExpression ifexpr = new IfExpression(script);
            Function func = new Function(script);
            StringBuilder trueBlock = new StringBuilder();
            StringBuilder elseBlock = new StringBuilder();
            HashMap<String,String> locals = args;
            for(int i=0;i<contents.length;i++){
                if(inBlock==0){
                    if(contents[i].startsWith("return")){
                        if(contents[i].contains("(")){
                            return NexT.script.Math.parseExpression(contents[i].substring(contents[i].indexOf("(")+1,contents[i].length()-2).trim(), allVars(locals), script)+"";
                        }else{
                            String ret = contents[i].substring(7);
                            if(locals.containsKey(ret))return locals.get(ret);
                            if(vars.containsKey(ret))return locals.get(ret);
                            throw new MissingOperandException();
                        }
                    }else
                    if(contents[i].startsWith("if")){
                        expression = contents[i].substring(3,contents[i].indexOf("{")).trim();
                        inBlock=1;
                    }else
                    if(contents[i].contains("=")){
                        String key = contents[i].substring(0,contents[i].indexOf("=")).trim();
                        String value="";
                        if(contents[i].contains("\""))value=contents[i].substring(contents[i].indexOf("\"")+1,contents[i].length()-2);
                        else value=""+NexT.script.Math.parseExpression(contents[i].substring(contents[i].indexOf("=")+1,contents[i].length()).trim(), allVars(locals), script);
                        if(locals.containsKey(key)||!vars.containsKey(key))vars.put(key,value);
                        else vars.put(key, value);
                    }else{
                        NexT.script.Math.parseExpression(contents[i],allVars(locals),script);
                    }
                }else if(inBlock==1){
                    if(contents[i].contains("else")){inBlock=2;contents[i]="";}
                    else if(contents[i].contains("}"))inBlock = -1;
                    else trueBlock.append(contents[i]).append("\n");
                }
                if(inBlock==2){
                    if(contents[i].contains("}"))inBlock = -1;
                    else elseBlock.append(contents[i]).append("\n");
                }
                if(inBlock==-1){
                    String ret = "";
                    if(ifexpr.eval(expression, allVars(locals))){
                        func.loadFunction(trueBlock.toString());
                        ret = func.exec(allVars(locals));
                    }else if(elseBlock.toString().length() != 0){
                        func.loadFunction(elseBlock.toString());
                        ret = func.exec(allVars(locals));
                    }
                    if(ret.length() !=0)return ret;
                    trueBlock = new StringBuilder();
                    elseBlock = new StringBuilder();
                    inBlock=0;
                }
            }
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
            ArrayList<String> args = NexT.script.Math.getArguments("- "+expression.trim());//bogus operator to abuse the arguments function
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
