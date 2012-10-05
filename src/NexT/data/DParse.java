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
    public static DObject<HashMap<String,DObject>> parse(File f) throws Exception{return parse(Toolkit.loadFileToString(f));}
    public static DObject<HashMap<String,DObject>> parse(String s) throws Exception{
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
                }else{
                    obj.set(key,DObject.parse(rest));
                }
            }
            if(!"".equals(okey)){
                inside+=line+';';
                height+=Toolkit.countSubstr(line, "\\{");
                height+=Toolkit.countSubstr(line, "\\}");
                if(height==0){
                    obj.set(okey,parse(inside.substring(inside.indexOf("{")+1,inside.lastIndexOf("}"))));
                    okey="";
                    inside="";
                }
            }
        }
        return obj;
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
