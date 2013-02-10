/**********************\
  file: DObject.java
  package: data
  author: Shinmera
  team: NexT
  license: -
\**********************/

package NexT.data;

import java.util.HashMap;
import java.util.Set;

public class DObject<Type extends Object> {
    public static final int TYPE_NULL = 0;
    public static final int TYPE_BOOLEAN = 1;
    public static final int TYPE_INTEGER = 2;
    public static final int TYPE_DOUBLE = 3;
    public static final int TYPE_STRING = 4;
    public static final int TYPE_OBJECT = 5;
    
    private int type = TYPE_NULL;
    private Type g;
    private HashMap<String,DObject> o;
    
    public DObject(){
        try{init((Type)new HashMap<String,DObject>());
        }catch(Exception ex){}
    }
    
    public DObject(Type t){init(t);}
    
    private void init(Type t){
        if(t==null){
            type = TYPE_NULL;
            g=null;
        }if(t instanceof Integer){
            type = TYPE_INTEGER;
            g=t;
        }else if(t instanceof Double){
            type = TYPE_DOUBLE;
            g=t;
        }else if(t instanceof Boolean){
            type = TYPE_BOOLEAN;
            g=t;
        }else if(t instanceof String){
            type = TYPE_STRING;
            g=t;
        }else if(t instanceof HashMap){
            type = TYPE_OBJECT;
            o=(HashMap)t;
        }else{
            throw new IllegalArgumentException("Bad class type: '"+t.getClass()+"' is not recognized.");
        }
    }
    
    public static DObject parse(Object o){
        DObject obj = null;
        
        if(o==null){
            obj = new DObject(null);
        }else if(o instanceof HashMap){
            obj = new DObject<HashMap<String,DObject>>((HashMap<String,DObject>)o);
        }else{
            try{
                obj = new DObject<Integer>((Integer)Integer.parseInt(o+""));
            }catch(Exception ex){
                try{
                    obj = new DObject<Double>((Double)Double.parseDouble(o+""));
                }catch(Exception ex2){
                    if(o instanceof Boolean){
                        obj = new DObject<Boolean>((Boolean)o);
                    }else
                    if(o instanceof String){
                        if(o.equals("false"))    obj = new DObject<Boolean>(Boolean.FALSE);
                        else if(o.equals("true"))obj = new DObject<Boolean>(Boolean.TRUE);
                        else                     obj = new DObject<String>(o+"");
                    }else{
                        throw new IllegalArgumentException("Bad class type: '"+o.getClass()+"' is not recognized.");
                    }
                }
            }
        }
        return obj;
    }
    
    public int getType(){return type;}
    public boolean is(int type){return (this.type==type);}
    public Type    get()        {return (g==null) ?  (o==null) ? null : (Type)o : g;}
    public DObject get(String s){return (o==null) ? null : o.get(s);}
    public Set<String> getKeySet(){return (o==null) ? null : o.keySet();}
    public boolean contains(String key){return (o==null) ? null : o.containsKey(key);}
    public void set(Type t)             {if(g!=null)g=t;else if(o!=null)o=(HashMap)t;}
    public void set(String s,DObject d) {if(o!=null)o.put(s,d);}
    public void set(String s,Object o) throws Exception{set(s,parse(o));}
    public int size(){return (o==null)? 1 : o.size();}
    public String toString(){return get()+"";}
}
