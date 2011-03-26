/**********************\
  file: ConfigManager
  package: util
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package NexT.util;
import java.io.*;
import java.util.*;

/*
 * Configuration Manager for easy configuration loading and saving of OptionSets.
 */
public class ConfigManager {
    public String fileName;
    public String programName;
    public String programVersion;
    private OptionSet<String,ArrayList> options;
    public boolean verbose;
    public boolean overwrite;
    
    public ConfigManager(String programName){
        this.programName = programName;
        this.fileName = programName+".cfg";
        this.programVersion = "";
        this.options = new OptionSet();
        this.verbose = false;
        this.overwrite = false;
    }

    public ConfigManager(String programName, String programVersion){
        this.programName = programName;
        this.fileName = programName+".cfg";
        this.programVersion = programVersion;
        this.options = new OptionSet();
        this.verbose = false;
        this.overwrite = false;
    }

    public ConfigManager(String programName, String programVersion, String fileName){
        this.programName = programName;
        this.fileName = fileName;
        this.programVersion = programVersion;
        this.options = new OptionSet();
        this.verbose = false;
        this.overwrite = false;
    }

    public boolean saveConfig(String fileName){
        String temp = this.fileName;
        this.fileName = fileName;
        boolean ret = saveConfig();
        this.fileName = temp;
        return ret;
    }

    public boolean saveConfig(){
        File f = new File(fileName);
        if(fileName.isEmpty()){if(verbose)System.out.println("CONFMAN: Filename is empty!");return false;}
        if(f.exists())f.delete();
        //if(!f.canWrite()){if(verbose)System.out.println("CONFMAN: Insufficient writing rights.");return false;}
        if(f.isDirectory()){if(verbose)System.out.println("CONFMAN: File is a directory!");return false;}

        try{
            if(options.size()>0){
            f.createNewFile();
            OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(f),"UTF-8");
            PrintWriter pw = new PrintWriter(fw);

            //print header
            pw.println("#"+programName.toUpperCase()+programVersion+" CONFIG FILE");

            for(int i=0;i<options.size();i++){
                ArrayList<String> temp = options.get(i);
                for(int j=0;j<temp.size();j++){
                    pw.println(options.getKey(i)+": "+temp.get(j));
                }
            }

            //print footer
            pw.println("#EOF");
            pw.flush();
            pw.close();
            fw.close();
            }
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean loadConfig(String fileName){
        String temp = this.fileName;
        this.fileName = fileName;
        boolean ret = loadConfig();
        this.fileName = temp;
        return ret;
    }

    public boolean loadConfig(){
        File f = new File(fileName);
        if(fileName.isEmpty()){if(verbose)System.out.println("CONFMAN: Filename is empty!");return false;}
        if(!f.exists()){if(verbose)System.out.println("CONFMAN: File not found!");return false;}
        if(!f.canRead()){if(verbose)System.out.println("CONFMAN: Can't read file!");return false;}
        if(f.isDirectory()){if(verbose)System.out.println("CONFMAN: File is a directory!");return false;}

        try{
            BufferedReader in = new BufferedReader(new FileReader(f));
            String read = in.readLine();
            if ((read == null) || (read.isEmpty())){if(verbose)System.out.println("CONFMAN: Empty file!");return false;}
            if(!read.startsWith("#"+programName.toUpperCase()+programVersion)){if(verbose)System.out.println("CONFMAN: Wrong or missing header!");return false;}
            while ((read = in.readLine()) != null) {
                if(!read.startsWith("#")&&!read.isEmpty()){
                    String key = read.substring(0,read.indexOf(":"));
                    String value = read.substring(read.indexOf(":")+2);
                    if(!options.containsKey(key)){
                        ArrayList<String> temp = new ArrayList();
                        temp.add(value);
                        options.put(key,temp);
                    } else {
                        if(!overwrite)
                            options.get(key).add(value);
                        else{
                            options.get(key).clear();
                            options.get(key).add(value);
                        }
                    }
                }
                if(read.equals("#EOF"))break;
            }
            
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }

        return true;
    }


    public void input(OptionSet options){this.options=options;}
    public OptionSet output(){return options;}
}
