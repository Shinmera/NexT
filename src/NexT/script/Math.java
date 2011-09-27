/**********************\
  file: Math.java
  package: NexT.util
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package NexT.script;

import java.util.logging.Logger;
import NexT.err.InvalidArgumentCountException;
import NexT.err.MissingOperandException;
import NexT.util.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import static java.lang.Math.*;

public class Math {
    public static final String OP_ADD = "+";
    public static final String OP_SUB = "-";
    public static final String OP_MUL = "*";
    public static final String OP_DIV = "/";
    public static final String OP_POW = "^";
    public static final String OP_SQR = "_";
    public static final String OP_MOD = "%";
    public static final String OP_SIN = "sin";
    public static final String OP_COS = "cos";
    public static final String OP_TAN = "tan";
    public static final String OP_RAN = "rand";
    public static final String OP_ARR = "<";
    public static final String OP_ARS = ">";

    public static final HashMap<String,Double> CONST = new HashMap<String,Double>();

    static{
        CONST.put("PI", PI);
        CONST.put("E", E);
    }

    public static void main(String[] args) throws Exception{
        parseExpression("(* 2 3 )",null,null);
    }

    public static Var parseExpression(String expression,HashMap<String,Var> vars,Script script) throws MissingOperandException, InvalidArgumentCountException{
        if(expression.startsWith("("))expression=expression.substring(1,expression.length()-1);
        if(vars==null)vars = new HashMap<String,Var>();
        if(script==null)script = new Script();
        
        ArrayList<String> args = getArguments(expression);
        String op = "";
        Var result = new Var("0.0");

        if(expression.contains(" "))op = expression.substring(0,expression.indexOf(" "));
        else op = expression;

        if(op.equals(OP_ADD)){
                if(args.size()<2)throw new InvalidArgumentCountException();
                for(int i=0;i<args.size();i++)
                    result=new Var(result.fix()+passToParser(i,args,vars,script).fix());
        } else if (op.equals(OP_SUB)) {
                if(args.size()<2)throw new InvalidArgumentCountException();
                result=passToParser(0,args,vars,script);
                for(int i=1;i<args.size();i++)
                    result=new Var(result.fix()-passToParser(i,args,vars,script).fix());
        } else if (op.equals(OP_MUL)) {
                if(args.size()<2)throw new InvalidArgumentCountException();
                result=passToParser(0,args,vars,script);
                for(int i=1;i<args.size();i++)
                    result=new Var(result.fix()*passToParser(i,args,vars,script).fix());
        } else if (op.equals(OP_DIV)) {
                if(args.size()<2)throw new InvalidArgumentCountException();
                result=passToParser(0,args,vars,script);
                for(int i=1;i<args.size();i++)
                    result=new Var(result.fix()/passToParser(i,args,vars,script).fix());
        } else if (op.equals(OP_POW)) {
                if(args.size()>2)throw new InvalidArgumentCountException();
                result=new Var(pow(passToParser(0,args,vars,script).fix(), passToParser(1,args,vars,script).fix()));
        } else if (op.equals(OP_SQR)) {
                if(args.size()>1)throw new InvalidArgumentCountException();
                result=new Var(sqrt(passToParser(0,args,vars,script).fix()));
        } else if (op.equals(OP_MOD)) {
                if(args.size()>2)throw new InvalidArgumentCountException();
                result=new Var(passToParser(0,args,vars,script).fix() % passToParser(1,args,vars,script).fix());
        } else if (op.equals(OP_SIN)) {
                if(args.size()>1)throw new InvalidArgumentCountException();
                result=new Var(sin(passToParser(0,args,vars,script).fix()));
        } else if (op.equals(OP_COS)) {
                if(args.size()>1)throw new InvalidArgumentCountException();
                result=new Var(cos(passToParser(0,args,vars,script).fix()));
        } else if (op.equals(OP_TAN)) {
                if(args.size()>1)throw new InvalidArgumentCountException();
                result=new Var(tan(passToParser(0,args,vars,script).fix()));
        } else if (op.equals(OP_RAN)) {
                if(args.size()>0)throw new InvalidArgumentCountException();
                result=new Var(random());
        } else if (op.equals(OP_ARR)) {
                if(args.size()!=2)throw new InvalidArgumentCountException();
                int pos = (int)passToParser(1,args,vars,script).fix();
                if(vars.containsKey(args.get(0))){
                    Var array = vars.get(args.get(0));
                    switch(array.getType()){
                        case Var.TYPE_BOOLEAN_ARRAY:
                            result=((boolean[])array.get())[pos] ? new Var(true) : new Var(false);
                            break;
                        case Var.TYPE_DOUBLE_ARRAY:
                            result=new Var(((double[])array.get())[pos]);
                            break;
                        case Var.TYPE_INTEGER_ARRAY:
                            result=new Var(Var.TYPE_INTEGER,((int[])array.get())[pos]+"");
                            break;
                        case Var.TYPE_MIXED_ARRAY:
                            result=((Var[])array.get())[pos];
                            break;
                        default:
                            result=new Var(((Object[])array.get())[pos]+"");
                    }
                }else throw new InvalidArgumentCountException("Variable '"+args.get(0)+"' not found!");
        }else{
                if(vars.containsKey(op))result=vars.get(op);
                else if(CONST.containsKey(op))result = new Var(CONST.get(op));
                else if(script.hasFunction(op)){
                    HashMap<String,Var> tmp = vars;
                    for(int i=0;i<args.size();i++){
                        if(args.get(i).startsWith("$"))args.set(i,vars.get(args.get(i)).get().toString());
                        tmp.put("$"+i, new Var(args.get(i)));
                    }
                    return script.eval(op,tmp);
                }
                else if(Toolkit.isNumeric(expression))result = new Var(Double.parseDouble(expression));
                else return new Var(expression);
        }
        return result;
    }

    private static Var passToParser(int n,ArrayList<String> args,HashMap<String,Var> vars,Script script) throws MissingOperandException, InvalidArgumentCountException{
        if(!Toolkit.isNumeric(args.get(n)))return parseExpression(args.get(n),vars,script);
        else return new Var(args.get(n));
    }

    public static ArrayList<String> getArguments(String expression){
        ArrayList<String> args = new ArrayList<String>();
        if(!expression.contains(" "))return args;
        boolean inStandard=false;
        int bracketCounter=0;
        int beginning=0;
        char[] expr = (expression+" ").toCharArray();
        for(int i=1;i<expr.length-1;i++){
            if(expr[i]=='('){
                if(bracketCounter==0)beginning=i+1;
                bracketCounter++;
            }
            else if(expr[i]==')'){
                bracketCounter--;
                if(bracketCounter==0){
                    args.add(expression.substring(beginning,i).trim());
                    inStandard=false;
                }
            }
            else if(bracketCounter==0&&expr[i]==' '&&!inStandard){
                inStandard=true;
                beginning=i+1;
            }
            else if(bracketCounter==0&&expr[i+1]==' '&&inStandard){
                inStandard=false;
                args.add(expression.substring(beginning,i+1).trim());
            }
        }
        return args;
    }
}
