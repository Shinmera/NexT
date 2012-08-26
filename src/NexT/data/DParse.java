/**********************\
  file: DParse.java
  package: data
  author: Shinmera
  team: NexT
  license: -
\**********************/

package NexT.data;

import NexT.util.Toolkit;
import java.io.File;
import java.util.HashMap;

public class DParse {
    public static DObject<HashMap<String,DObject>> parse(File f){return parse(Toolkit.loadFileToString(f));}
    public static DObject<HashMap<String,DObject>> parse(String s){
        try{
            DObject<HashMap<String,DObject>> obj = new DObject<HashMap<String,DObject>>(new HashMap<String,DObject>());
            s = s.replaceAll("//(.+)","").replaceAll("(?s)/\\*(.+?)\\*/", "").trim();
            
            String[] vals = s.split(";");
            int height = 0;
            String inside="",okey="",key="",rest="";
            for(String line : vals){
                line = line.trim();
                if(line.contains(":"))key = line.substring(0,line.indexOf(":")).trim();
                rest= line.substring(line.indexOf(":")+1).trim();

                if("".equals(okey)){
                    if(rest.startsWith("{")){
                        okey=key;
                        System.out.println("BEGIN "+key);
                    }else{
                        obj.set(key,DObject.parse(rest));
                        System.out.println(">"+key+" = "+obj.get(key).get()+" ("+obj.get(key).getType()+")");
                    }
                }
                if(!"".equals(okey)){
                    inside+=line+';';
                    height+=Toolkit.countSubstr(line, "\\{");
                    height+=Toolkit.countSubstr(line, "\\}");
                    if(height==0){
                        obj.set(okey,parse(inside.substring(inside.indexOf("{")+1,inside.lastIndexOf("}"))));
                        System.out.println("END "+okey);
                        okey="";
                        inside="";
                    }
                }
            }
            return obj;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
    
    public static String parse(DObject<HashMap<String,DObject>> obj){
        String s = "";
        HashMap<String,DObject> map = obj.get();
        for(String key : map.keySet()){
            DObject val = map.get(key);
            if(val.is(DObject.TYPE_OBJECT))s+=key+":{"+parse((DObject<HashMap<String,DObject>>)val)+"};";
            else                           s+=key+":"+val+";";
        }
        return s;
    }
}
