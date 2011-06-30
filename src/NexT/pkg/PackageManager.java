/**********************\
  file: PackageManager
  package: NexT.pkg
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package NexT.pkg;
import NexT.util.ConfigManager;
import NexT.util.OptionSet;
import NexT.util.Toolkit;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class PackageManager {
    private OptionSet<String,ArrayList<String>> config = new OptionSet();
    private ConfigManager confman;
    private PackageDownloader pckgd = new PackageDownloader();

    public static void main(String[] args){
        PackageManager man = new PackageManager();
        man.update();
        man.install("tinychum",null);
        man.saveConfig();
    }

    /**
     * Constructor with saved repositories.
     */
    public PackageManager(){
        File fi = new File(Toolkit.getSettingsDir("NexTPackageManager"),"list");
        confman = new ConfigManager("PackageManager","1.0",fi.getAbsolutePath());
        loadConfig();
        for(int i=0;i<config.get("repo").size();i++){
            pckgd.addRepo(config.get("repo").get(i).toString());
        }
    }

    /**
     * Constructor with preselected repositories.
     * @param repos The repository array
     */
    public PackageManager(String[] repos){
        File fi = new File(Toolkit.getSettingsDir("NexTPackageManager"),"list");
        confman = new ConfigManager("PackageManager","1.0",fi.getAbsolutePath());
        loadConfig();
        for(int i=0;i<repos.length;i++){
            pckgd.addRepo(repos[i]);
        }
    }

    /**
     * Load the configuration file.
     * @return Success
     */
    public boolean loadConfig(){
        boolean success = true;
        if(!confman.loadConfig())success=false;
        config = confman.output();
        if(config==null)config = new OptionSet<String, ArrayList<String>>();
        if(!config.containsKey("repo"))config.put("repo",new ArrayList());
        if(!config.containsKey("package"))config.put("package",new ArrayList());
        if(!config.containsKey("version"))config.put("version",new ArrayList());
        if(!config.containsKey("basedir"))config.put("basedir",new ArrayList());
        return success;
    }

    /**
     * Save the configuration file.
     * @return Success
     */
    public boolean saveConfig(){
        confman.input(config);
        if(!confman.saveConfig())return false;
        return true;
    }

    /**
     * Returns the names of all available packages.
     * @return The name array
     */
    public String[] getAvailablePackages(){
        return pckgd.getPackageList();
    }

    /**
     * Returns the names of all installed packages.
     * @return The name array
     */
    public String[] getInstalledPackages(){
        Object[] temp = config.get("package").toArray();
        return Arrays.copyOf(temp, temp.length,String[].class);
    }

    /**
     * Returns if the package is installed or not.
     * @param pkg The package name
     * @return 
     */
    public boolean isInstalled(String pkg){
        return config.get("package").contains(pkg);
    }

    /**
     * Sets a package as installed by external source.
     * @param pkg The package name
     * @param version The version currently installed
     * @param folder The base folder of the package
     */
    public void setInstalled(String pkg,String version,File folder){
        pkg = pckgd.findPackage(pkg).get("id");
        if(!config.get("package").contains(pkg)){
            config.get("package").add(pkg);
            config.get("basedir").add(folder.getAbsolutePath());
            config.get("version").add(version);
        }
    }

    /**
     * Install the selected package.
     * @param pkg The package name
     * @param folder The folder to install the data to. If null, the home dir or package specific directory will be used.
     * @return Success
     */
    public boolean install(String pkg,File folder){
        System.out.println("INSTALL: "+pkg);
        HashMap<String,String> info = pckgd.findPackage(pkg);
        if(info==null)return false;
        if(!config.get("package").contains(pkg)){
            if(folder==null){
                String path = info.get("basedir");
                path = path.trim().replaceAll("/", File.separator);
                String temp = Toolkit.inBetween(path, "<S", ">");
                path = path.replace("<S"+temp+">", Toolkit.getSettingsDir(temp).getAbsolutePath());
                path = path.replace("<U>",System.getProperty("user.home"));
                folder = new File(path);
            }
            try{
                if(info.containsKey("depends")){
                    String[] pkgs = info.get("depends").split(";");
                    for(int i=0;i<pkgs.length;i++){
                        if(!config.get("package").contains(pkgs[i]))install(pkgs[i],null);
                        else upgrade(pkgs[i]);
                    }
                }
                pckgd.downloadPackage(info.get("id"), folder);
            }catch(Exception e){e.printStackTrace();return false;}
            config.get("package").add(info.get("id"));
            config.get("version").add(info.get("version"));
            config.get("basedir").add(folder.getAbsolutePath());
        }
        return true;
    }
    
    /**
     * Force install the selected package.
     * @param pkg The package name
     * @param folder The folder to install the data to. If null, the home dir or package specific directory will be used.
     * @return Success
     */
    public boolean forceInstall(String pkg,File folder){
        System.out.println("INSTALL: "+pkg);
        HashMap<String,String> info = pckgd.findPackage(pkg);
        if(info==null)return false;
            if(folder==null){
                String path = info.get("basedir");
                path = path.trim().replaceAll("/", File.separator);
                String temp = Toolkit.inBetween(path, "<S", ">");
                path = path.replace("<S"+temp+">", Toolkit.getSettingsDir(temp).getAbsolutePath());
                path = path.replace("<U>",System.getProperty("user.home"));
                folder = new File(path);
            }
            try{
                if(info.containsKey("depends")){
                    String[] pkgs = info.get("depends").split(";");
                    for(int i=0;i<pkgs.length;i++){
                        if(!config.get("package").contains(pkgs[i]))install(pkgs[i],null);
                        else upgrade(pkgs[i]);
                    }
                }
                pckgd.downloadPackage(info.get("id"), folder);
            }catch(Exception e){e.printStackTrace();return false;}
            config.get("package").add(info.get("id"));
            config.get("version").add(info.get("version"));
            config.get("basedir").add(folder.getAbsolutePath());
        return true;
    }

    /**
     * Remove the selected package.
     * @param pkg The package to remove
     * @return Success
     */
    public boolean remove(String pkg){
        System.out.println("REMOVE: "+pkg);
        HashMap<String,String> info = pckgd.findPackage(pkg);
        if(info==null)return false;
        if(!config.get("package").contains(info.get("id")))return true;
        int pos = config.get("package").indexOf(info.get("id"));
        File f = new File(config.get("basedir").get(pos).toString());
        if(!f.delete())return false;
        config.get("package").remove(pos);
        config.get("version").remove(pos);
        config.get("basedir").remove(pos);
        return true;
    }

    /**
     * Update repository indexes.
     * @return Success
     */
    public boolean update(){
        System.out.println("UPDATE: REPOSITORIES");
        try{pckgd.update();}catch(Exception e){e.printStackTrace();return false;}
        return true;
    }

    /**
     * Upgrade all packages.
     * @return Success
     */
    public boolean upgrade(){
        boolean ret = true;
        System.out.println("UPGRADE: ALL");
        for(int i=0;i<config.get("package").size();i++){
            if(!upgrade(config.get("package").get(i).toString()))ret = false;
        }
        return ret;
    }

    /**
     * Upgrade the selected package.
     * @param pkg The package name
     * @return Success
     */
    public boolean upgrade(String pkg){
        HashMap<String,String> info = pckgd.findPackage(pkg);
        pkg = info.get("id");
        int i = config.get("package").indexOf(pkg);
        System.out.println("UPGRADE: "+pkg+" to "+config.get("basedir").get(i));

        if(info.containsKey("depends")){
            String[] pkgs = info.get("depends").split(";");
            for(int j=0;j<pkgs.length;j++){
                if(!config.get("package").contains(pkgs[j]))install(pkgs[j],null);
                else upgrade(pkgs[j]);
            }
        }

        try{pckgd.updatePackage(pkg,
                                    Double.parseDouble(config.get("version").get(i).toString()),
                                    new File(config.get("basedir").get(i).toString()));
        }catch(Exception e){e.printStackTrace();}
        return true;
    }


}
