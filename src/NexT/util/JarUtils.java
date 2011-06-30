/**********************\
  file: JarUtils.java
  package: NexT.util
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package NexT.util;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class JarUtils {
    public ArrayList<String> readTextFromJar(String s) {
        ArrayList<String> list = new ArrayList<String>();String line;
        try {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(s);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while (null != (line = br.readLine())) {
                list.add(line);
            }
            br.close();
            is.close();
        }catch (Exception e) { e.printStackTrace();}
        return list;
    }

    public Image readImageFromJar(String s){
        URL imgURL = this.getClass().getClassLoader().getResource(s);
        Toolkit tk = java.awt.Toolkit.getDefaultToolkit();
        return tk.getImage(imgURL);
    }

    public boolean includeJar(File path){
        if(path.isDirectory())return false;
        if(!path.exists())return false;
        try{ClassPathHacker.addFile(path);}
        catch(Exception e){e.printStackTrace();return false;}
        return true;
    }
}
