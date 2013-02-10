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
    
    public static String parse(DObject<HashMap<String,DObject>> obj, boolean beauty){
        if(beauty) return parse(obj, 0);
        else       return parse(obj);
    }
    
    public static String parse(DObject<HashMap<String,DObject>> obj, int level){
        StringBuilder s = new StringBuilder();
        HashMap<String,DObject> map = obj.get();
        String padding = (level<=0) ? "" : Toolkit.repeatString(" ", level*4);
        if(level!=-1)level++;
        for(String key : map.keySet()){
            DObject val = map.get(key);
            if(val.is(DObject.TYPE_OBJECT)){
                s.append("\n").append(padding).append(key).append(": {\n").
                    append(parse((DObject<HashMap<String,DObject>>)val, level)).
                append(padding).append("};\n");
            }else{
                s.append(padding).append(key).append(": ").append(val).append(";\n");
            }
        }
        return s.toString();
    }
    
    public static String parse(DObject<HashMap<String,DObject>> obj){
        StringBuilder s = new StringBuilder();
        HashMap<String,DObject> map = obj.get();
        for(String key : map.keySet()){
            DObject val = map.get(key);
            if(val.is(DObject.TYPE_OBJECT)){
                s.append(key).append(":{").append(parse((DObject<HashMap<String,DObject>>)val)).append("};");
            }else{
                s.append(key).append(":").append(val).append(";");
            }
        }
        return s.toString();
    }
}
