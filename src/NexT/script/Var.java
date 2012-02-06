/**********************\
  file: Var.java
  package: NexT.script
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package NexT.script;

import NexT.util.Toolkit;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Var {
    public static final int TYPE_NULL   = -1;
    public static final int TYPE_BOOLEAN= 0x00;
    public static final int TYPE_STRING = 0x01;
    public static final int TYPE_INTEGER= 0x02;
    public static final int TYPE_DOUBLE = 0x03;
    public static final int TYPE_BOOLEAN_ARRAY= 0x10;
    public static final int TYPE_STRING_ARRAY = 0x11;
    public static final int TYPE_INTEGER_ARRAY= 0x12;
    public static final int TYPE_DOUBLE_ARRAY = 0x13;
    public static final int TYPE_MIXED_ARRAY  = 0x14;

    private int type = TYPE_STRING;
    private boolean bool = false;
    private String str = "";
    private int integ = 0;
    private double doub = 0.0;
    private boolean[] a_bool;
    private String[] a_str;
    private int[] a_integ;
    private double[] a_doub;
    private Var[] a_var;

    public Var(){this.type=TYPE_NULL;}
    public Var(int type){this.type=type;}
    public Var(String value){type=detectType(value);set(value);}
    public Var(int type,String value){this(type);set(value);}
    public Var(double value){type=TYPE_DOUBLE;doub=value;}
    public Var(boolean bool){type=TYPE_BOOLEAN;this.bool=bool;}
    public Var(boolean[] bools){type=TYPE_BOOLEAN_ARRAY;a_bool=bools;}
    public Var(double[] doubs){type=TYPE_DOUBLE_ARRAY;a_doub=doubs;}
    public Var(int[] ints){type=TYPE_INTEGER_ARRAY;a_integ=ints;}
    public Var(String[] strings){type=TYPE_STRING_ARRAY;a_str=strings;}
    public Var(Var[] vars){type=TYPE_MIXED_ARRAY;a_var=vars;}

    /**
     * Tries to automatically detect the type of variable from a String. Defaults to String.
     * @param value The String to search.
     * @return The int flag of the found type.
     */
    public static int detectType(String value){
        value=value.trim();
        if(Toolkit.isNumeric(value)){
            if(value.contains("."))return TYPE_DOUBLE;
            else return TYPE_INTEGER;
        }else{
            if(value.equals("true")||value.equals("false"))return TYPE_BOOLEAN;
            else if(value.startsWith("{")&&value.endsWith("}")){
                String first = "";
                if(value.contains(","))first = value.substring(1,value.indexOf(",")).trim();
                else first = value.substring(1,value.length()-1).trim();
                if(Toolkit.isNumeric(first)){
                    if(first.contains("."))return TYPE_DOUBLE_ARRAY;
                    else return TYPE_INTEGER_ARRAY;
                }else{
                    if(first.equals("true")||first.equals("false"))return TYPE_BOOLEAN_ARRAY;
                    else if(first.contains("{"))return TYPE_MIXED_ARRAY;
                    else return TYPE_STRING_ARRAY;
                }
            }
        }
        return TYPE_STRING;
    }

    /**
     * Attempts to set the value of the variable. Defaults to String.
     * @param value The value to set.
     */
    public void set(String value){
        value=value.trim();
        switch(type){
            case TYPE_NULL:break;
            case TYPE_BOOLEAN: bool=Boolean.parseBoolean(value);break;
            case TYPE_INTEGER: integ=Integer.parseInt(value);break;
            case TYPE_DOUBLE: doub=Double.parseDouble(value);break;
            case TYPE_STRING_ARRAY: a_str=value.substring(1,value.length()-1).split(",");break;
            case TYPE_BOOLEAN_ARRAY: a_bool=Toolkit.stringToBoolArray(value.substring(1,value.length()-1).split(","));break;
            case TYPE_INTEGER_ARRAY: a_integ=Toolkit.stringToIntArray(value.substring(1,value.length()-1).split(","));break;
            case TYPE_DOUBLE_ARRAY: a_doub=Toolkit.stringToDoubleArray(value.substring(1,value.length()-1).split(","));break;
            case TYPE_MIXED_ARRAY:
                value=value.substring(1,value.length()-1).replaceAll(", ",",").replaceAll(" ,",",");
                ArrayList<Var> vars = new ArrayList<Var>();
                int brackets=0;int begin=0;
                for(int i=0;i<value.length();i++){
                    switch(value.charAt(i)){
                        case '{':brackets++;begin=i;break;
                        case '}':brackets--;
                            if(brackets==0){
                                vars.add(new Var(value.substring(begin,i+1)));
                            }
                            break;
                        default:
                            if(brackets==0&&value.charAt(i)!=','){
                                if(value.indexOf(',', i)!=-1){
                                    int pos = value.indexOf(",",i);
                                    vars.add(new Var(value.substring(i,pos)));
                                    i=pos;
                                }else{
                                    vars.add(new Var(value.substring(i)));
                                    i=value.length();
                                }
                            }
                            break;
                    }
                }
                a_var = vars.toArray(new Var[vars.size()]);
                break;
            default: str=value;type=TYPE_STRING;break;
        }
    }

    /**
     * Returns the internal representation of the variable. Casting is necessary, but safe.
     * @return The value of the variable.
     */
    public Object get(){
        switch(type){
            case TYPE_BOOLEAN: return bool;
            case TYPE_STRING: return str;
            case TYPE_INTEGER: return integ;
            case TYPE_DOUBLE: return doub;
            case TYPE_STRING_ARRAY: return a_str;
            case TYPE_BOOLEAN_ARRAY: return a_bool;
            case TYPE_INTEGER_ARRAY: return a_integ;
            case TYPE_DOUBLE_ARRAY: return a_doub;
            case TYPE_MIXED_ARRAY: return a_var;
            case TYPE_NULL: return null;
            default: return str;
        }
    }

    /**
     * Attempts to "fix" the value to a double. A warning is logged if the operation is unsure and might return a false 0.
     * @return The fixed double value of this variable.
     */
    public double fix(){
        return fix(0);
    }
    
    /**
     * Attempts to "fix" the value to a double. A warning is logged if the operation is unsure and might return a false 0. In case of an array, n may be specified.
     * @return The fixed double value of this variable.
     */
    public double fix(int n){
        switch(type){
            case TYPE_BOOLEAN:
                return (bool) ? 1 : 0;
            case TYPE_STRING:       Logger.getLogger("NexT").log(Level.WARNING,"[Var] Attempting to fix String to double.");
                return (Toolkit.isNumeric(str)) ? Double.parseDouble(str) : 0;
            case TYPE_INTEGER:
                return integ+0.0;
            case TYPE_DOUBLE:
                return doub;
            case TYPE_STRING_ARRAY: Logger.getLogger("NexT").log(Level.WARNING,"[Var] Attempting to fix String array to first element.");
                return (Toolkit.isNumeric(a_str[n])) ? Double.parseDouble(str) : 0;
            case TYPE_BOOLEAN_ARRAY:
                return (a_bool[n]) ? 1 : 0;
            case TYPE_INTEGER_ARRAY:
                return a_integ[n]+0.0;
            case TYPE_DOUBLE_ARRAY:
                return a_doub[n];
            case TYPE_MIXED_ARRAY:
                return a_var[n].fix();
            case TYPE_NULL:         Logger.getLogger("NexT").log(Level.WARNING,"[Var] Fixing NULL to 0.");
                return 0.0;
            default: return (Toolkit.isNumeric(str)) ? Double.parseDouble(str) : doub;
        }
    }

    public int getType(){return type;}
    public void setType(int type){this.type=type;}

    public String toString(){
        return get()+"";
    }
}
