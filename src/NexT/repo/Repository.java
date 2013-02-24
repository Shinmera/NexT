package NexT.repo;

import NexT.Commons;
import NexT.util.Toolkit;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Repository {
    private URL url;
    private HashMap<String,String> info = new HashMap<String,String>();
    private String[] packages;
    private boolean gotInfo=false;

    public static void main(String[] args) throws MalformedURLException{
        Repository repo = new Repository(new URL("http://repo.tymoon.eu/v2/test/"));
        System.out.println("PACKAGES: "+Toolkit.implode(repo.getPackageList(),";"));
        Package pack = repo.getPackage("testpack");
        System.out.println("Testpack URL: "+pack.getURL());
        if(repo.installPackage(pack,new File(".")))System.out.println("TEMP: "+pack.getTempFile().getAbsolutePath());
    }

    public Repository(){}
    public Repository(URL url){
        this.url=url;
    }

    public void getRepositoryInfo(){
        try {
            info = Toolkit.stringToMap(Toolkit.downloadFileToString(new URL(url, "index")),"\n",":");
            packages = Toolkit.downloadFileToString(new URL(url,"packages")).split("\n");
            gotInfo=true;
        } catch (MalformedURLException ex) {
            Commons.log.log(Level.WARNING, "[NexT][Repository] URL malformed.", ex);
        }
    }

    public boolean installPackage(String pkg,File location){
        return installPackage(new Package(this,pkg),location);
    }

    public boolean installPackage(Package pack,File location){
        if(installDependencies(pack,location)){
            if(pack.installPackage(location))
                return true;
        }
        return false;
    }

    public boolean installDependencies(Package pkg,File location){
        String[] deps = pkg.getDependencies();
        for(int i=0;i<deps.length;i++){
            deps[i]=deps[i].trim();
            if(deps[i].length()!=0){
                Commons.log.info("[NexT][Repository] Installing dependency: "+deps[i]);
                Package pack = new Package(this,deps[i]);
                if(installDependencies(pack,location)){
                    if(!pack.installPackage(location))return false;
                }else return false;
            }
        }
        return true;
    }

    public Package getPackage(String pkg){
        return new Package(this,pkg);
    }

    public URL getURL(){return url;}
    public String getName(){return getInfoField("name");}
    public String getMaintainer(){return getInfoField("maint");}
    public String getDescription(){return getInfoField("desc");}
    public String getInfoField(String field){
        if(!gotInfo)getRepositoryInfo();return info.get(field);}
    public String[] getPackageList(){
        if(!gotInfo)getRepositoryInfo();return packages;}

    public void setURL(URL url){this.url=url;}
}
