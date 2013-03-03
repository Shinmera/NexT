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
    
    /**
     * Parse a file into a DObject tree.
     * 
     * @param f
     * @return 
     * @see DParse#parse(java.lang.String) 
     */
    public static DObject<HashMap<String,DObject>> parse(File f){return parse(Toolkit.loadFileToString(f));}
    
    /**
     * Parse a string into a DObject tree. Sample tree:
     * field: value;
     * object: {
     *   field: value;
     * };
     * 
     * @param s The string to parse
     * @return 
     */
    public static DObject<HashMap<String,DObject>> parse(String s){
        DObject<HashMap<String,DObject>> obj = new DObject<HashMap<String,DObject>>(new HashMap<String,DObject>());
        s = s.replaceAll("//(.+)","").replaceAll("(?s)/\\*(.+?)\\*/", "").trim();

        String[] statements = s.split(";");
        int height = 0;
        String inside="",okey="",key="",rest="";
        for(String statement : statements){
            statement = statement.trim();
            if(statement.contains(":"))key = statement.substring(0,statement.indexOf(":")).trim();
            rest= statement.substring(statement.indexOf(":")+1).trim();

            if(okey.equals("")){
                if(rest.startsWith("{")){
                    okey=key;
                }else{
                    obj.set(key,DObject.parse(rest));
                }
            }
            if(!okey.equals("")){
                inside+=statement+';';
                height+=Toolkit.countSubstr(statement, "\\{");
                height+=Toolkit.countSubstr(statement, "\\}");
                if(height==0){
                    obj.set(okey,parse(inside.substring(inside.indexOf("{")+1,inside.lastIndexOf("}"))));
                    okey="";
                    inside="";
                }
            }
        }
        return obj;
    }
    
    /**
     * Serialise a DObject tree into a string representation.
     * @param obj
     * @param beauty Whether to beautify or minify the resulting string.
     * @return 
     */
    public static String parse(DObject<HashMap<String,DObject>> obj, boolean beauty){
        if(beauty) return parse(obj, 0);
        else       return parse(obj);
    }
    
    private static String parse(DObject<HashMap<String,DObject>> obj, int level){
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
    
    private static String parse(DObject<HashMap<String,DObject>> obj){
        StringBuilder s = new StringBuilder();
        HashMap<String,DObject> map = obj.get();
        for(String key : map.keySet()){
            DObject val = map.get(key);
            if(val.is(DObject.TYPE_OBJECT)){
                if(val.size() > 0) //Quick and dirty fix. Remove once proper parser is implemented.
                    s.append(key).append(":{").append(parse((DObject<HashMap<String,DObject>>)val)).append("};");
            }else{
                s.append(key).append(":").append(val).append(";");
            }
        }
        return s.toString();
    }
    
    /**
     * Get a DObject tree leaf through branch notation. IllegalArgumentExceptions
     * are thrown if a leaf cannot be found or if the base DObject isn't of
     * TYPE_OBJECT.
     * 
     * @param base The root of the DObject tree to search through.
     * @param branch The branch notation of the leaf we want to access.
     * @param sep The leaf separator used in the branch notation.
     * @return 
     */
    public static DObject get(DObject base, String branch, String sep){
        if(base.getType() != DObject.TYPE_OBJECT)throw new IllegalArgumentException("DObject has to be of TYPE_OBJECT.");
        String[] leaves = branch.split(sep);
        leaves[0] = leaves[0].trim();
        if(!base.contains(leaves[0]))throw new IllegalArgumentException("Dobject does not contain leaf '"+leaves[0]+"'.");
        if(leaves.length == 1)return base.get(leaves[0]);
        else                  return DParse.get(base.get(leaves[0]), branch.substring(branch.indexOf(sep)+1), sep);
    }
}
