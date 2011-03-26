/**********************\
  file: Toolkit
  package: NexT.util
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package NexT.util;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;

public final class Toolkit {

    /**
     * Function to reverse the order of all elements in the array.
     *
     * @param b The array to reverse.
     * @return The reversed array
     */
    public static Object[] reverse(Object[] b) {
        int left  = 0;          // index of leftmost element
        int right = b.length-1; // index of rightmost element

        while (left < right) {
            // exchange the left and right elements
            Object temp = b[left];
            b[left]  = b[right];
            b[right] = temp;

            // move the bounds toward the center
            left++;
            right--;
        }
        return b;
    }

    /**
     * Returns the amount of times the {@link sub} String has been found
     * in the {@link stack} String.
     *
     * @param stack The String which is to be searched through.
     * @param sub The String which is to be searched for.
     * @return The amount of times it has been found.
     */
    public static int countSubstr(String stack,String sub){
        return stack.split(sub).length-1;
    }

    /**
     * Returns the Color object that's parsed by the String.
     * This function supports the integer hash, hex formatting and
     * even literal colors. If the color can't be parsed, it returns black.
     *
     * @param icolors The String which is to be parsed into a color Object.
     * @return The parsed Color object.
     * @see java.awt.Color
     */
    public static Color toColor(String icolors){
        if(icolors == null || icolors.equals(""))return Color.BLACK;
        icolors = icolors.trim();
        icolors = icolors.replaceAll("_", " ");
        if(Toolkit.countSubstr(icolors, ",")==2){
            String[] colors = icolors.trim().split(",");
            return new Color(Integer.parseInt(colors[0].trim()),
                             Integer.parseInt(colors[1].trim()),
                             Integer.parseInt(colors[2].trim()));
        }
        if(isNumeric(icolors))return new Color(Integer.parseInt(icolors));
        if(icolors.startsWith("#"))return Color.decode(icolors);
        if(icolors.equalsIgnoreCase("black"))return Color.BLACK;
        if(icolors.equalsIgnoreCase("blue"))return Color.BLUE;
        if(icolors.equalsIgnoreCase("cyan"))return Color.CYAN;
        if(icolors.equalsIgnoreCase("dark gray"))return Color.DARK_GRAY;
        if(icolors.equalsIgnoreCase("gray"))return Color.GRAY;
        if(icolors.equalsIgnoreCase("green"))return Color.GREEN;
        if(icolors.equalsIgnoreCase("light gray"))return Color.LIGHT_GRAY;
        if(icolors.equalsIgnoreCase("magenta"))return Color.MAGENTA;
        if(icolors.equalsIgnoreCase("orange"))return Color.ORANGE;
        if(icolors.equalsIgnoreCase("pink"))return Color.PINK;
        if(icolors.equalsIgnoreCase("red"))return Color.RED;
        if(icolors.equalsIgnoreCase("white"))return Color.WHITE;
        if(icolors.equalsIgnoreCase("yellow"))return Color.YELLOW;
        if(icolors.equals("TC:COLOR")){
            Color col = JColorChooser.showDialog(null,"Text Color",Color.BLACK);
            if(col!=null)return col;
        }

        return Color.BLACK;
    }

    /**
     * Trims the string with begin and end limiters.
     * @param stack The source String.
     * @param begin The left side limiter.
     * @param end The right side limiter.
     * @return The trimmed string.
     */
    public static String inBetween(String stack,String begin,String end){
        int beginPos = stack.indexOf(begin)+begin.length();
        int endPos = stack.indexOf(end,beginPos);
        if(beginPos==-1||endPos==-1)return null;
        return stack.substring(beginPos,endPos);
    }


    /**
     * Trims the string with begin and end limiters.
     * @param stack The source String.
     * @param begin The left side limiter.
     * @param end The right side limiter.
     * @param pos The position to start searching for.
     * @return The trimmed string.
     */
    public static String inBetween(String stack,String begin,String end,int pos){
        int beginPos = stack.indexOf(begin,pos)+begin.length();
        int endPos = stack.indexOf(end,beginPos);
        if(beginPos==-1||endPos==-1)return null;
        return stack.substring(beginPos,endPos);
    }

    /**
     * Tries to download the specified File and save it to disc.
     * This produces a ProgressThread which can be used to watch the download progress.
     * 
     * @param url_str The URL to download the file from.
     * @param saveFile The File to save it to.
     * @return ProgressThread instance to watch the download Progress.
     * @see ProgressThread
     */
    public static boolean downloadFile(String url_str, File saveFile){
        try{
        BufferedInputStream in = new java.io.BufferedInputStream(new

        URL(url_str).openStream());
        FileOutputStream fos = new FileOutputStream(saveFile);
        BufferedOutputStream bout = new BufferedOutputStream(fos,1024);
        byte[] data = new byte[1024];
        int x=0;
        while((x=in.read(data,0,1024))>=0){
            bout.write(data,0,x);
        }
        bout.close();
        in.close();
        }catch(Exception e){e.printStackTrace();return false;}
	return true;
    }

    /**
     * Tries to unzip the specified archive.
     * This produces a ProgressThread which can be used to watch the extracting progress.
     *
     * @param filename The path to the archive that is to be unzipped.
     * @param where Where to extract the contents to.
     * @return ProgressThread instance to watch the unzipping Progress.
     * @see ProgressThread
     */
    public static ProgressThread unzipFile(final String filename,final String where){
        if(!new File(filename).canRead()){JOptionPane.showMessageDialog(null, "Can't read source zip!\n"+filename);return null;}
        if(!new File(filename).getParentFile().setWritable(true)){JOptionPane.showMessageDialog(null, "Can't write to parent!\n"+new File(filename).getParent());return null;}
        ProgressThread t = new ProgressThread("Exracting..."){
            public void run(){
        try{
        ZipFile archive = new ZipFile(filename);
        Enumeration<? extends ZipEntry> fileList = archive.entries();
        setMaximum(archive.size());int i=0;
        // Go through each file in the ZIP archive.
        for (ZipEntry e = fileList.nextElement();fileList.hasMoreElements();e = fileList.nextElement()) {
            System.out.println("Expanding " + e.getName());
            setCurrent(i);setStatus("Expanding: "+e.getName());
            // If the zip entry is a directory, make the directory.
            if (e.isDirectory()) new File(where+e.getName()).mkdir();
            else {
                InputStream in = archive.getInputStream(e);
                OutputStream out = new BufferedOutputStream(new FileOutputStream(where+e.getName()));
                byte[] buffer = new byte[1024];
                int len;
                while((len = in.read(buffer)) >= 0)
                    out.write(buffer, 0, len);

                in.close();
                out.close();

            }
            i++;
        }
        setDone();}catch (IOException e) {e.printStackTrace();}}};
        t.start();return t;
    }

    /**
     * Glues together all the array elements with {@link delim} in between elements.
     * 
     * @param ary The array to be glued together.
     * @param delim The delimiter to be put between elements.
     * @return The glued array.
     */
    public static String implode(Object[] ary, String delim) {
    String out = "";
    for(int i=0; i<ary.length; i++) {
        if(i!=0) { out += delim; }
        out += ary[i]+"";
    }
    return out;
    }

    /**
     * Checks if a String is numeric.
     *
     * @param str The String to be checked.
     * @return True if it's numeric.
     */
    public static boolean isNumeric(String str) {
        try {Double.parseDouble(str);
        }catch(NumberFormatException ex){return false;}
        return true;
    }

    /**
     * Checks for an existing settings directory for the specified {@link program}
     * and returns the path to it. If it doesn't exist, it will try create one for you.
     * @param program The program name.
     * @return File pointing to the location.
     */
    public static File getSettingsDir(String program){
        String userHome = System.getProperty("user.home", ".");
        File work = new File(userHome);
        switch(Meta.getOS()){
            case Meta.OS_LINUX:
                work = new File(userHome, "."+program+"/");break;
            case Meta.OS_WINDOWS:
                String applicationData = System.getenv("APPDATA");
                if (applicationData != null) work = new File(applicationData,"."+program+'/');
                else work = new File(userHome, '.' + program + '/');
                break;
            case Meta.OS_APPLE:
                work = new File(userHome, "Library/Application Support/"+program);break;
            default:
                work = new File(userHome, program + '/');
        }
        System.out.println("Creating settings dir: "+work);
        if((!work.exists())&&(!work.mkdirs()))throw new RuntimeException("Failed to create the setttings dir: "+work);
        return work;
    }

}
