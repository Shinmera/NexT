/**********************\
  file: PackageDownloader
  package: NexT.pkg
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package NexT.pkg;

import NexT.util.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class PackageDownloader {
    private HashMap<String,HashMap<String,String>> dpkgs = new HashMap();
    private ArrayList<String> list = new ArrayList();
    private ArrayList<String> repos = new ArrayList();

    public static void main(String[] args) throws IOException {
        PackageDownloader pd = new PackageDownloader();
        System.out.println("Getting public repo snapshot...");
        pd.update();
        System.out.println("\nAvailable packages:");
        String[] pkgs = pd.getPackageList();
        for(int i=0;i<pkgs.length;i++){System.out.println(">"+pkgs[i]);}
    }

    public PackageDownloader(){
    }

    public PackageDownloader(String[] repos){
        this.repos.addAll(Arrays.asList(repos));
    }

    public void addRepo(String repo){
        if(!repos.contains(repo))repos.add(repo);
    }

    public void delRepo(String repo){
        if(repos.contains(repo))repos.remove(repo);
    }

    /**
     * Private instance callback.
     * @return The list of available packages
     */
    public String[] getPackageList(){
        return list.toArray(new String[list.size()]);
    }

    /**
     * Private instance callback.
     * @return The list of locally cached repos
     */
    public String[] getRepoList(){
        return repos.toArray(new String[list.size()]);
    }

    /**
     * Downloads a full repository snapshot.
     * @return Success
     * @throws IOException in case the temp file fails to be created
     */
    public boolean update() throws IOException{
        dpkgs.clear();
        list.clear();
        if(repos.isEmpty()){ //get public repo list
            File repo = File.createTempFile("list","xrp");
            repo.deleteOnExit();
            if(!Toolkit.downloadFile("http://repo.tymoon.eu/index.xrp", repo)){
                System.out.println("[FAIL]");
                return false;
            }

            //load repos
            BufferedReader in = new BufferedReader(new FileReader(repo));
            String read = "";
            while((read = in.readLine())!= null){
                read = read.trim();
                if(!read.startsWith("#")){//block commentary
                    getRepository(read);
                    repos.add(read);
                }
            }
        }else{
            for(int i=0;i<repos.size();i++){
                getRepository(repos.get(i));
            }
        }
        return true;
    }

    /**
     * Downloads a NexT repository index file and converts it into a HashMap.
     * @param name The repository name
     * @return Success
     * @throws IOException in case the temp file fails to be created
     */
    public boolean getRepository(String name) throws IOException{
        if(!repos.contains(name))repos.add(name);
        System.out.println("GET http://repo.tymoon.eu/"+name);
        //create temporary file for repo download
        File repo = File.createTempFile("repo","xrp");
        repo.deleteOnExit();
        if(!Toolkit.downloadFile("http://repo.tymoon.eu/"+name+"/index.xrp", repo)){
            System.out.println("[FAIL]");
            return false;
        }

        //read file in
        BufferedReader in = new BufferedReader(new FileReader(repo));
        String read = "";String cur = "";
        while((read = in.readLine())!= null){
            read = read.trim();
            if(!read.startsWith("#")&&!read.equals("")){ //block commentary
                if(read.startsWith("package: ")){ //new package
                    cur = read.substring(9);
                    list.add(cur);
                    dpkgs.put(cur,new HashMap<String,String>());
                    dpkgs.get(cur).put("id", cur);
                    dpkgs.get(cur).put("name", cur);
                    dpkgs.get(cur).put("version", "0.0");
                    dpkgs.get(cur).put("repo", name);
                    dpkgs.get(cur).put("basedir",new File(System.getProperty("user.home"),cur).getAbsolutePath());
                }
                if(!cur.equals("")){
                    dpkgs.get(cur).put(read.substring(0,read.indexOf(":")), read.substring(read.indexOf(":")+2));
                }
            }
        }
        return true;
    }

    /**
     * Searches for the package in the local repo snapshot. If it is found,
     * it will return the package information. Otherwise null.
     * @param name
     * @return The package information
     */
    public HashMap<String,String> findPackage(String name){
        name.toLowerCase();
        if(dpkgs.containsKey(name))
            return dpkgs.get(name);
        for(int i=0;i<list.size();i++){
            if(dpkgs.get(list.get(i)).containsValue(name))return dpkgs.get(list.get(i));
        }
        return null;
    }

    /**
     * Looks for an update. If there is one, it'll download it and replace the
     * old package.
     * @param name The package name
     * @param version The current local version
     * @param folder The folder to save the package to
     * @return Success
     * @throws IOException in case the temp file fails to be created
     */
    public boolean updatePackage(String name,double version,File folder) throws IOException{
        if(version<Double.parseDouble(dpkgs.get(name).get("version"))){
            folder.delete();
            folder.mkdir();
            if(!downloadPackage(name,folder))return false;
        }
        return true;
    }

    /**
     * Downloads the specified package to the specified folder.
     * @param name The package name
     * @param folder The folder to download the package to
     * @return Success
     * @throws IOException in case the temp file fails to be created
     */
    public boolean downloadPackage(String name,File folder) throws IOException{
        folder.mkdirs();
        if(dpkgs.isEmpty())return false;
        if(!dpkgs.containsKey(name))return false;
        System.out.println("GET http://repo.tymoon.eu/"+dpkgs.get(name).get("repo")+"/"+name);
        //create temporary file for pkg file list download
        File pkg = File.createTempFile(name,"xpkg");
        pkg.deleteOnExit();
        if(!Toolkit.downloadFile("http://repo.tymoon.eu/"+dpkgs.get(name).get("repo")+"/"+name+"/index.xpkg",pkg)){
            System.out.println("[FAIL]");
            return false;
        }

        //read file list and evaluate
        BufferedReader in = new BufferedReader(new FileReader(pkg));
        String read = "";
        while((read = in.readLine())!= null){
            read = read.trim();
            if(!read.startsWith("#")&&!read.equals("")){ //block commentary
                File cur = null;
                if(File.separator.equals("\\"))
                    cur = new File(folder,read.substring(read.indexOf(" ")+1).replaceAll("/","\\\\\\\\"));
                else
                    cur = new File(folder,read.substring(read.indexOf(" ")+1).replaceAll("/",File.separator));
                if(read.trim().endsWith("/")){ //it's a folder
                    cur.mkdirs();
                }else{ //download the file
                    System.out.println("GET --> "+read);
                    if(!Toolkit.downloadFile("http://repo.tymoon.eu/"+dpkgs.get(name).get("repo")+"/"+name+"/"+read.substring(0,read.indexOf(" ")),cur)){
                        System.out.println("GET --> [FAIL]");
                        folder.delete(); //clean up
                        folder.mkdir();
                        System.out.println("[FAIL]");
                        return false;
                    }
                }
            }
        }
        return true;
    }
}