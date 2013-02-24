/**********************\
file: Storage.java
package: NexT.util
author: Shinmera
team: NexT
license: -
\**********************/
package NexT.util;

import NexT.Commons;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Storage {
    File f;
    HashMap<String,String> strings = new HashMap<String,String>();
    HashMap<String,Integer> integers = new HashMap<String,Integer>();
    HashMap<String,Double> doubles = new HashMap<String,Double>();
    HashMap<String,Boolean> booleans = new HashMap<String,Boolean>();
    
    public Storage(File f){
        this.f=f;
    }
    
    public void load() throws FileNotFoundException,IOException{
        if(!f.exists())throw new FileNotFoundException("Couldn't find the File "+f.getAbsolutePath());
        BufferedReader in = new BufferedReader(new FileReader(f));
        String read = "";
        
        int i=0;
        while ((read = in.readLine()) != null) {
            i++;
            read = read.trim();
            if(!read.isEmpty()||!read.contains(":")){
                char type = read.charAt(0);
                read = read.substring(2);
                String key = read.substring(0,read.indexOf(":")).trim();
                String val = read.substring(read.indexOf(":")+1).trim();
                switch(type){
                    case 's':strings.put(key, val);break;
                    case 'i':integers.put(key, Integer.parseInt(val));break;
                    case 'd':doubles.put(key, Double.parseDouble(val));break;
                    case 'b':booleans.put(key, Boolean.parseBoolean(val));break;
                    default:Commons.log.log(Level.WARNING,"[NexT][Storage] Couldn't identify "+i+": "+type+" "+read);break;
                }
            }
        }
        
        in.close();
    }
    
    public void save() throws IOException{
        f.createNewFile();
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(f),"UTF-8"));
        
        iteratePrint(strings.entrySet().iterator(),pw,'s');
        iteratePrint(integers.entrySet().iterator(),pw,'i');
        iteratePrint(doubles.entrySet().iterator(),pw,'d');
        iteratePrint(booleans.entrySet().iterator(),pw,'b');
        
        pw.flush();
        pw.close();
    }
    
    private void iteratePrint(Iterator it,PrintWriter pw,char type){
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            pw.println(type+" "+pair.getKey()+": "+pair.getValue());
        } 
    }
    
    public Object get(String s){
        if(strings.containsKey(s))return strings.get(s);
        if(integers.containsKey(s))return integers.get(s);
        if(doubles.containsKey(s))return doubles.get(s);
        if(booleans.containsKey(s))return booleans.get(s);
        return null;
    }
    
    public void set(String s,String v){
        if(v.equals("true")||v.equals("false"))
            booleans.put(s, Boolean.parseBoolean(v));
        else if(Toolkit.isNumeric(v)){
            if(v.contains("."))doubles.put(s, Double.parseDouble(v));
            else               integers.put(s,Integer.parseInt(v));
        }else{
            strings.put(s,v);
        }
    }
    
    public String _s(String s){return strings.get(s);}
    public int _i(String s){return integers.get(s);}
    public double _d(String s){return doubles.get(s);}
    public boolean _b(String s){return booleans.get(s);}
    public void s_(String s,String v){strings.put(s, v);};
    public void i_(String s,int v){integers.put(s, v);};
    public void d_(String s,double v){doubles.put(s, v);};
    public void b_(String s,boolean v){booleans.put(s, v);};
}
