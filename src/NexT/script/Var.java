/**********************\
  file: Var.java
  package: NexT.script
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package NexT.script;

public class Var {
    public static final int TYPE_BOOLEAN= 0x00;
    public static final int TYPE_STRING = 0x01;
    public static final int TYPE_INTEGER= 0x02;
    public static final int TYPE_DOUBLE = 0x03;

    private int type = TYPE_STRING;
    private boolean bool = false;
    private String str = "";
    private int integ = 0;
    private double doub = 0.0;

    public Var(int type){this.type=type;}
    public Var(int type,String value){this(type);set(value);}

    public void set(String value){
        switch(type){
            case TYPE_BOOLEAN: bool=Boolean.parseBoolean(value);break;
            case TYPE_INTEGER: integ=Integer.parseInt(value);break;
            case TYPE_DOUBLE: doub=Double.parseDouble(value);break;
            default: str=value;break;
        }
    }
    
    public String get(){
        switch(type){
            case TYPE_BOOLEAN: return bool+"";
            case TYPE_INTEGER: return integ+"";
            case TYPE_DOUBLE: return doub+"";
            default: return str;
        }
    }

    public void add(Var var){
        switch(type){
            case TYPE_BOOLEAN: break;
            case TYPE_INTEGER: integ+=Double.parseDouble(var.get());break;
            case TYPE_DOUBLE: doub+=Double.parseDouble(var.get());break;
            default: str+=var.get();break;
        }
    }

    public void sub(Var var){
        switch(type){
            case TYPE_BOOLEAN: break;
            case TYPE_INTEGER: integ-=Double.parseDouble(var.get());break;
            case TYPE_DOUBLE: doub-=Double.parseDouble(var.get());break;
            default: break;
        }
    }
}
