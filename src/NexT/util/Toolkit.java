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
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.swing.JColorChooser;

/**
 * Supplies various commonly usable functions.
 * @author Shinmera
 */
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
        icolors = icolors.replaceAll(",", " ");
        if(countSubstr(icolors, " ")==2){
            String[] colors = icolors.trim().split(" ");
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
     * 
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
     *
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
     * 
     * @param url_str The URL to download the file from.
     * @param saveFile The File to save it to.
     * @return true for success
     */
    public static boolean downloadFile(URL url_str, File saveFile){
        try{
            BufferedInputStream in = new java.io.BufferedInputStream(url_str.openStream());
            FileOutputStream fos = new FileOutputStream(saveFile);
            BufferedOutputStream bout = new BufferedOutputStream(fos,1024);
            byte[] data = new byte[1024];
            int x=0;
            while((x=in.read(data,0,1024))>=0){
                bout.write(data,0,x);
            }
            bout.close();
            in.close();
        }catch(Exception e){Logger.getLogger("NexT").log(Level.WARNING,"[NexT][Toolkit] File download failed.",e);return false;}
	return true;
    }


    public static String downloadFileToString(URL url_str){
        try{
            StringBuilder s = new StringBuilder();
            String temp;
            BufferedReader br = new BufferedReader(new InputStreamReader(url_str.openStream()));
            while ((temp = br.readLine()) != null) {
                s.append(temp+"\n");
            }
            return s.toString();
        }catch(Exception e){Logger.getLogger("NexT").log(Level.WARNING,"[NexT][Toolkit] File download failed.",e);}
        return "";
    }

    /**
     * Tries to unzip the specified archive.
     *
     * @param filename The path to the archive that is to be unzipped.
     * @param where Where to extract the contents to.
     * @return success boolean.
     */
    public static boolean unzipFile(File filename,File where) throws IOException{
        ZipFile zipFile = new ZipFile(filename);
        Enumeration files = zipFile.entries();
        FileOutputStream fos = null;

        while (files.hasMoreElements()) {
            try {
                ZipEntry entry = (ZipEntry) files.nextElement();
                InputStream eis = zipFile.getInputStream(entry);
                byte[] buffer = new byte[1024];
                int bytesRead = 0;

                File f = new File(where,entry.getName());

                if (entry.isDirectory()) {
                    f.mkdirs();
                    continue;
                } else {
                    f.getParentFile().mkdirs();
                    f.createNewFile();
                }

                fos = new FileOutputStream(f);

                while ((bytesRead = eis.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                Logger.getLogger("NexT").log(Level.WARNING,"[NexT][Toolkit] Failed to unzip data.",e);continue;
            } finally {
                if (fos != null) 
                    try {fos.close();} catch (IOException e) {Logger.getLogger("NexT").log(Level.WARNING,"[NexT][Toolkit] Failed to read archive.",e);}
            }
        }
        return true;
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
     * 
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

    /**
     * Used to simulate rows in console output.
     * 
     * @param in The string to append the spaces to.
     * @param maxlen The maximum size of the row.
     * @return the new String with appended spaces.
     */
    public static String insertSpaces(String in,int maxlen){
        for(int i=in.length();i<maxlen;i++)
            in+=" ";
        return in;
    }

    /**
     * Turns a string into a hash map, using newline and equals as delimiters.
     *
     * @param s the string to map-ify
     * @return The resulting key->value map
     */
    public static HashMap<String,String> stringToMap(String s){
        return stringToMap(s,"\n","=");
    }

    /**
     * Turns a string into a hash map, using newline and equals as delimiters.
     *
     * @param s the string to map-ify
     * @param delim1 the delimiter between two key->value pairs
     * @param delim2 the delimiter between the key and the value
     * @return The resulting key->value map
     */
    public static HashMap<String,String> stringToMap(String s,String delim1,String delim2){
        HashMap<String,String> map = new HashMap();
        String[] lines = s.split(delim1);
        for(int i=0;i<lines.length;i++){
            if(lines[i].contains(delim2)){
                String key = lines[i].substring(0,lines[i].indexOf(delim2)).trim();
                String val = lines[i].substring(lines[i].indexOf(delim2)+1).trim();
                map.put(key,val);
            }
        }
        return map;
    }

    /**
     * Joins two arrays together.
     * @param A The first array.
     * @param B The second array.
     * @return The resulting array.
     */
    public static Object[] joinArray(Object[] A,Object[] B){
        Object[] C= new Object[A.length+B.length];
        System.arraycopy(A, 0, C, 0, A.length);
        System.arraycopy(B, 0, C, A.length, B.length);
        return C;
    }

    public static int[] stringToIntArray(String[] a){
        int[] b = new int[a.length];
        for(int i=0;i<a.length;i++)b[i]=Integer.parseInt(a[i]);
        return b;
    }

    public static double[] stringToDoubleArray(String[] a){
        double[] b = new double[a.length];
        for(int i=0;i<a.length;i++)b[i]=Double.parseDouble(a[i]);
        return b;
    }

    public static boolean[] stringToBoolArray(String[] a){
        boolean[] b = new boolean[a.length];
        for(int i=0;i<a.length;i++)b[i]=Boolean.parseBoolean(a[i]);
        return b;
    }

    /**
     * Makes a number positive.
     * @param d a number
     * @return the positive number
     */
    public static double p(double d){
        if(d<0)return d*-1;else return d;
    }

    /**
     * Conveniently print a command line menu header
     * @param text The text that should be enboxed.
     * @param c The border character.
     * @param innerBorder The distance between border and text.
     * @param log If set, the menu will be printed to the logger, instead of the default system stream.
     */
    public static void printMenu(String text,String c,int innerBorder,Logger log){
        String[] lines = text.split("\n");
        //determine longest line
        int maxLines = 0;
        for(int i=0;i<lines.length;i++){
            if(lines[i].length()>maxLines)maxLines = lines[i].length();
        }

        printToLoggerOrSys(getN(c,maxLines+2*innerBorder+2*c.length()),log);
        for(int i=0;i<lines.length;i++){
            if(lines[i].startsWith("><"))
                    printToLoggerOrSys(c+getN(" ",(maxLines-lines[i].length())/2+1+innerBorder)+lines[i].substring(2)+getN(" ",(maxLines-lines[i].length())/2+1+innerBorder)+c,log);
            else
                    printToLoggerOrSys(c+getN(" ",innerBorder)+lines[i]+getN(" ",maxLines-lines[i].length()+innerBorder)+c,log);
        }
        printToLoggerOrSys(getN(c,maxLines+2*innerBorder+2*c.length()),log);
    }

    private static void printToLoggerOrSys(String s,Logger log){
        if(log==null)System.out.println(s);
        else log.info(s);
    }

    private static String getN(String c,int n){
        String ret = "";
        for(int i=0;i<n;i++){ret+=c;}
        return ret;
    }

    public static String unifyNumberString(int n,int m){
        String ret = n+"";
        for(int i=m;i>0;i--){
            if(n<java.lang.Math.pow(10,i))ret="0"+ret;
        }
        return ret;
    }

    /**
     * Saves a string to a text file in UTF-8 encoding.
     * @param s The string to save.
     * @param f The file to save the string to.
     * @return true on success, false on failure.
     */
    public static boolean saveStringToFile(String s,File f){
        try{
            f.createNewFile();
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(f),"UTF-8"));
            pw.print(s);
            pw.flush();
            pw.close();
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Reads in a file to a String.
     * @param f The file to read in.
     * @return The read String. Empty on failure.
     */
    public static String loadFileToString(File f){
        StringBuilder s = new StringBuilder();
        try{
            BufferedReader in = new BufferedReader(new FileReader(f));
            String read = "";
            while ((read = in.readLine()) != null)s.append(read+"\n");
            in.close();
        }catch(IOException e){
            e.printStackTrace();
            return "";
        }
        return s.toString();
    }

}
