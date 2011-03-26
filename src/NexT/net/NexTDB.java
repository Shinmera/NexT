/**********************\
  file: NexTDB
  package: NexT.net
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package NexT.net;
import NexT.util.Meta;
import NexT.util.OptionSet;
import NexT.util.Return;
import NexT.util.Toolkit;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;

public class NexTDB {

    public static OptionSet fetchProgramList(String username,String pass){
        OptionSet<String,ArrayList<String>> set = new OptionSet();
        String content = getPage("http://tymoon.eu/NexT.php?u="+username+"&p="+pass+"&a=getprograms");
        int numItems = Toolkit.countSubstr(content, "<item>");
        
        System.out.println("Fetched "+numItems+" items.");
        String[] items = content.split("</item>");
        for(int i=0;i<numItems;i++){
            set.add("ID",     Toolkit.inBetween(items[i], "<ID=", ">"));
            set.add("title",  Toolkit.inBetween(items[i], "<title=", ">"));
            set.add("subject",Toolkit.inBetween(items[i], "<subject>", "</subject>"));
            set.add("status", Toolkit.inBetween(items[i], "<status=", ">"));
        }

        if(content.startsWith("E"))set.add("ERROR", content);

        return set;
    }

    public static OptionSet fetchUpdateList(String username,String pass,int pid,double version,String os){
        OptionSet<String,ArrayList<String>> set = new OptionSet();
        String content = getPage("http://tymoon.eu/NexT.php?u="+username+"&p="+pass+"&a=getupdates&e=pid:"+pid+";version:"+version+";os:"+os);
        int numItems = Toolkit.countSubstr(content, "<item>");

        System.out.println("Fetched "+numItems+" items.");
        String[] items = content.split("</item>");
        for(int i=0;i<numItems;i++){
            set.add("version",   Toolkit.inBetween(items[i], "<version=", ">"));
            set.add("filename",  Toolkit.inBetween(items[i], "<filename=", ">"));
            set.add("importance",Toolkit.inBetween(items[i], "<importance=", ">"));
            set.add("time",      Toolkit.inBetween(items[i], "<time=", ">"));
            set.add("changelog", Toolkit.inBetween(items[i], "<changelog>", "</changelog>"));
        }

        return set;
    }

    public static OptionSet excludeOld(OptionSet list,double version){
        for(int i=0;i<list.get("version").size();i++){
            if(Double.parseDouble(list.get("version").get(i).toString())<=version)
                list.removeAllAt(i);
        }
        return list;
    }

    public static Return createMeta(String name,String version,String basedir){
        try{
        File fi = new File(Toolkit.getSettingsDir("NexT"),name);
        if(fi.exists())fi.delete();
        if(!fi.createNewFile())return new Return(false,"Failed to create file");
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fi,false),"UTF-8"));
        pw.println(version);
        pw.println(basedir);
        pw.flush();pw.close();
        }catch(Exception e){return new Return(false,e);}
        return new Return();
    }



    public static String getPage(String urls){
        final URL url;
        String string="";
        try {
            url = new URL(urls);
            DataInputStream inputStream = new DataInputStream(url.openConnection().getInputStream());
            final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            while (reader.ready()) {
                string+=reader.readLine();
            }
            reader.close();
        } catch (Exception ex) {ex.printStackTrace();}
        return string;
    }

}
