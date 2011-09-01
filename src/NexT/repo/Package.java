package NexT.repo;

import NexT.util.Toolkit;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Package {
    private File tmpfile;
    private URL url;
    private HashMap<String,String> info = new HashMap<String,String>();
    private String[] dependencies;
    private boolean gotInfo=false;
    private boolean gotPackage=false;

    public Package(Repository repo,String name){
        try {
            url = new URL(repo.getURL(),name);
        } catch (MalformedURLException ex) {
            Logger.getLogger("NexT").log(Level.WARNING, "[NexT][Package] URL malformed.", ex);
        }
    }

    public Package(URL url){
        this.url=url;
    }

    public void getPackageInfo(){
        try {
            info = Toolkit.stringToMap(Toolkit.downloadFileToString(new URL(url, "index")),"\n",":");
            if(info.containsKey("dep"))dependencies = info.get("dep").split(";");
            else dependencies = new String[0];
            gotInfo=true;
        } catch (MalformedURLException ex) {
            Logger.getLogger("NexT").log(Level.WARNING, "[NexT][Package] URL malformed.", ex);
        }
    }

    public boolean downloadPackage(){
        if(!gotInfo)getPackageInfo();
        try {
            tmpfile = new File(new File(System.getProperty("java.io.tmpdir")),info.get("name"));
            gotPackage = Toolkit.downloadFile(new URL(url, "pack.zip"), tmpfile);
        } catch (MalformedURLException ex) {
            Logger.getLogger("NexT").log(Level.SEVERE, "[NexT][Package] URL malformed.", ex);
            return false;
        }
        return true;
    }

    public boolean installPackage(File location){
        if(!gotPackage)downloadPackage();
        try {
            return Toolkit.unzipFile(tmpfile, location);
        } catch (IOException ex) {
            Logger.getLogger("NexT").log(Level.SEVERE, "[NexT][Package] IO Exception while unzipping.", ex);
            return false;
        }
    }


    public URL getURL(){return url;}
    public File getTempFile(){return tmpfile;}
    public String getName(){return getInfoField("name");}
    public String getRepo(){return getInfoField("repo");}
    public String getVersion(){return getInfoField("vers");}
    public String getAuthor(){return getInfoField("author");}
    public String getDescription(){return getInfoField("desc");}
    public String getInfoField(String field){
        if(!gotInfo)getPackageInfo();return info.get(field);}
    public String[] getDependencies(){
        if(!gotInfo)getPackageInfo();return dependencies;}

    public void setURL(){this.url=url;}
}
