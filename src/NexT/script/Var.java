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

public class Var {
    public static final int TYPE_BOOLEAN= 0x00;
    public static final int TYPE_STRING = 0x01;
    public static final int TYPE_INTEGER= 0x02;
    public static final int TYPE_DOUBLE = 0x03;
    public static final int TYPE_BOOLEAN_ARRAY= 0x10;
    public static final int TYPE_STRING_ARRAY = 0x11;
    public static final int TYPE_INTEGER_ARRAY= 0x12;
    public static final int TYPE_DOUBLE_ARRAY = 0x13;

    private int type = TYPE_STRING;
    private boolean bool = false;
    private String str = "";
    private int integ = 0;
    private double doub = 0.0;
    private boolean[] a_bool;
    private String[] a_str;
    private int[] a_integ;
    private double[] a_doub;

    public Var(int type){this.type=type;}
    public Var(String value){type=detectType(value);set(value);}
    public Var(int type,String value){this(type);set(value);}

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
                else first = value.substring(1,value.length()-1);

                if(Toolkit.isNumeric(first)){
                    if(first.contains("."))return TYPE_DOUBLE_ARRAY;
                    else return TYPE_INTEGER_ARRAY;
                }else{
                    if(first.equals("true")||first.equals("false"))return TYPE_BOOLEAN_ARRAY;
                    else return TYPE_STRING_ARRAY;
                }
            }
        }
        return TYPE_STRING;
    }

    public void set(String value){
        value=value.trim();
        switch(type){
            case TYPE_BOOLEAN: bool=Boolean.parseBoolean(value);break;
            case TYPE_INTEGER: integ=Integer.parseInt(value);break;
            case TYPE_DOUBLE: doub=Double.parseDouble(value);break;
            case TYPE_STRING_ARRAY: a_str=value.substring(1,value.length()-1).split(",");break;
            case TYPE_BOOLEAN_ARRAY: a_bool=Toolkit.stringToBoolArray(value.substring(1,value.length()-1).split(","));break;
            case TYPE_INTEGER_ARRAY: a_integ=Toolkit.stringToIntArray(value.substring(1,value.length()-1).split(","));break;
            case TYPE_DOUBLE_ARRAY: a_doub=Toolkit.stringToDoubleArray(value.substring(1,value.length()-1).split(","));break;
            default: str=value;break;
        }
    }
    
    public Object get(){
        switch(type){
            case TYPE_BOOLEAN: return bool;
            case TYPE_INTEGER: return integ;
            case TYPE_DOUBLE: return doub;
            case TYPE_STRING_ARRAY: return a_str;
            case TYPE_BOOLEAN_ARRAY: return a_bool;
            case TYPE_INTEGER_ARRAY: return a_integ;
            case TYPE_DOUBLE_ARRAY: return a_doub;
            default: return str;
        }
    }

    public int getType(){return type;}

    public String toString(){
        return get()+"";
    }
}
