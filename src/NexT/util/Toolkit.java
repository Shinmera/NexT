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
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
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
     * @param fallback Fallback color in case it fails to parse the string.
     * @return The parsed Color object.
     * @see java.awt.Color
     */
    public static Color toColor(String icolors,Color fallback){
        if(icolors == null || icolors.length() == 0)return fallback;
        icolors = icolors.replaceAll("_", " ");
        icolors = icolors.replaceAll(",", " ");
        icolors = icolors.replaceAll(";", " ");
        icolors = icolors.trim();
        if(countSubstr(icolors, " ")>1){
            String[] colors = icolors.split(" ");
            if(colors.length==3)
                return new Color(Integer.parseInt(colors[0].trim()),
                                 Integer.parseInt(colors[1].trim()),
                                 Integer.parseInt(colors[2].trim()));
            if(colors.length==4)
                return new Color(Integer.parseInt(colors[0].trim()),
                                 Integer.parseInt(colors[1].trim()),
                                 Integer.parseInt(colors[2].trim()),
                                 Integer.parseInt(colors[3].trim()));
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
        Logger.getLogger("NexT").log(Level.WARNING,"[NexT][Toolkit] Couldn't parse color string ("+icolors+"). Falling back to black!");
        return fallback;
    }
    public static Color toColor(String icolors){return toColor(icolors,Color.BLACK);}

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
        StringBuilder out = new StringBuilder();
        for(int i=0; i<ary.length; i++) {
            if(i!=0) { out.append(delim); }
            out.append(ary[i].toString());
        }
        return out.toString();
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
    
    public static int[] toIntArray(Object[] a){         int[] b=new int[a.length];          for(int i=0;i<a.length;i++){b[i]=(Integer)a[i];}    return b;}
    public static double[] toDoubleArray(Object[] a){   double[] b=new double[a.length];    for(int i=0;i<a.length;i++){b[i]=(Double)a[i];}     return b;}
    public static float[] toFloatArray(Object[] a){     float[] b=new float[a.length];      for(int i=0;i<a.length;i++){b[i]=(Float)a[i];}      return b;}
    public static boolean[] toBoolArray(Object[] a){    boolean[] b=new boolean[a.length];  for(int i=0;i<a.length;i++){b[i]=(Boolean)a[i];}    return b;}

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
            Logger.getLogger("NexT").log(Level.WARNING,"[NexT][Toolkit] Failed to save String to file.",e);
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
            Logger.getLogger("NexT").log(Level.WARNING,"[NexT][Toolkit] Failed to load file to String.",e);
            return "";
        }
        return s.toString();
    }

    /**
     * Converts a byte array into a String.
     * @param data The byte array to convert.
     * @return The converted String.
     */
    private static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9))
                    buf.append((char) ('0' + halfbyte));
                else
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while(two_halfs++ < 1);
        }
        return buf.toString();
    }

    /**
     * Converts a text into it's SHA-1 hash. Returns the text on failure.
     * @param text The text to hash.
     * @return The SHA-1 hash.
     */
    public static String SHA1(String text) {
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] sha1hash = new byte[40];
            md.update(text.getBytes("iso-8859-1"), 0, text.length());
            sha1hash = md.digest();
            return convertToHex(sha1hash);
        }catch(Exception e){Logger.getLogger("NexT").log(Level.WARNING,"[NexT][Toolkit] Failed to create SHA-1 hash.",e);}
        return text;
    }

    /**
     * Inserts an element into an array.
     * @param array The array to insert into
     * @param pos The position to insert at.
     * @param insert The value to insert.
     * @return The new array.
     */
    public static int[] insertIntoArray(int[] array,int pos,int insert){
        int[] a = new int[array.length+1];boolean passed=false;
        for(int i=0;i<array.length;i++){
            if(i==pos){
                a[i]=insert;i++;
                passed=true;
            }
            if(passed)a[i]=array[i];
            else      a[i]=array[i-1];
        }
        return a;
    }

    /**
     * Inserts an element into an array.
     * @param array The array to insert into
     * @param pos The position to insert at.
     * @param insert The value to insert.
     * @return The new array.
     */
    public static double[] insertIntoArray(double[] array,int pos,double insert){
        double[] a = new double[array.length+1];boolean passed=false;
        for(int i=0;i<array.length;i++){
            if(i==pos){
                a[i]=insert;i++;
                passed=true;
            }
            if(passed)a[i]=array[i];
            else      a[i]=array[i-1];
        }
        return a;
    }

    /**
     * Inserts an element into an array.
     * @param array The array to insert into
     * @param pos The position to insert at.
     * @param insert The value to insert.
     * @return The new array.
     */
    public static boolean[] insertIntoArray(boolean[] array,int pos,boolean insert){
        boolean[] a = new boolean[array.length+1];boolean passed=false;
        for(int i=0;i<array.length;i++){
            if(i==pos){
                a[i]=insert;i++;
                passed=true;
            }
            if(passed)a[i]=array[i];
            else      a[i]=array[i-1];
        }
        return a;
    }

    /**
     * Inserts an element into an array.
     * @param array The array to insert into
     * @param pos The position to insert at.
     * @param insert The value to insert.
     * @return The new array.
     */
    public static String[] insertIntoArray(String[] array,int pos,String insert){
        String[] a = new String[array.length+1];boolean passed=false;
        for(int i=0;i<array.length;i++){
            if(i==pos){
                a[i]=insert;i++;
                passed=true;
            }
            if(passed)a[i]=array[i];
            else      a[i]=array[i-1];
        }
        return a;
    }

    /**
     * Inserts an element into an array.
     * @param array The array to insert into
     * @param pos The position to insert at.
     * @param insert The value to insert.
     * @return The new array.
     */
    public static Object[] insertIntoArray(Object[] array,int pos,Object insert){
        Object[] a = new Object[array.length+1];boolean passed=false;
        for(int i=0;i<array.length;i++){
            if(i==pos){
                a[i]=insert;i++;
                passed=true;
            }
            if(passed)a[i]=array[i];
            else      a[i]=array[i-1];
        }
        return a;
    }

    /**
     * Converts a primitive type array to an object array.
     * @param arr The primitive array.
     * @return The new Object array.
     */
    public static Object[] toObjectArray(int[] arr){
        Object[] a = new Object[arr.length];
        for(int i=0;i<arr.length;i++){a[i]=(Integer)arr[i];}
        return a;
    }

    /**
     * Converts a primitive type array to an object array.
     * @param arr The primitive array.
     * @return The new Object array.
     */
    public static Object[] toObjectArray(double[] arr){
        Object[] a = new Object[arr.length];
        for(int i=0;i<arr.length;i++){a[i]=(Double)arr[i];}
        return a;
    }

    /**
     * Converts a primitive type array to an object array.
     * @param arr The primitive array.
     * @return The new Object array.
     */
    public static Object[] toObjectArray(boolean[] arr){
        Object[] a = new Object[arr.length];
        for(int i=0;i<arr.length;i++){a[i]=(Boolean)arr[i];}
        return a;
    }

    /**
     * Converts a primitive type array to an object array.
     * @param arr The primitive array.
     * @return The new Object array.
     */
    public static Object[] toObjectArray(float[] arr){
        Object[] a = new Object[arr.length];
        for(int i=0;i<arr.length;i++){a[i]=(Float)arr[i];}
        return a;
    }

    /**
     * Converts a primitive type array to an object array.
     * @param arr The primitive array.
     * @return The new Object array.
     */
    public static Object[] toObjectArray(char[] arr){
        Object[] a = new Object[arr.length];
        for(int i=0;i<arr.length;i++){a[i]=(Character)arr[i];}
        return a;
    }

    /**
     * Converts a primitive type array to an object array.
     * @param arr The primitive array.
     * @return The new Object array.
     */
    public static Object[] toObjectArray(short[] arr){
        Object[] a = new Object[arr.length];
        for(int i=0;i<arr.length;i++){a[i]=(Short)arr[i];}
        return a;
    }

    /**
     * Quick access to a formatted date output.
     * @param format The formatting String. Has to be SimpleDateFormat compatible. null will default to MM.dd HH:mm:ss
     * @return The current time in the given format.
     */
    public static String getCurrentFormattedTime(String format){
        SimpleDateFormat sdf;
        if(format!=null)sdf = new SimpleDateFormat(format);
        else            sdf = new SimpleDateFormat("dd.MM HH:mm:ss");
        return sdf.format(new Date());
    }

    /**
     * Returns a given string n amount of times.
     * @param s The string to be duplicated.
     * @param n The amount of times to append.
     * @return The final string.
     * @deprecated Alias for repeatString
     */
    @Deprecated
    public static String getStringNTimes(String s,int n){return repeatString(s,n);}
    
    /**
     * Returns a given string n amount of times.
     * @param s The string to be duplicated.
     * @param n The amount of times to append.
     * @return The final string.
     */
    public static String repeatString(String s,int n){
        return new String(new char[n]).replace("\0",s);
    }
    
    /**
     * Splits a string into l sized chunks.
     * @param s The string to split.
     * @param l The length of each string chunk.
     * @return  Array of the string chunks.
     */
    public String[] splitStringByLength(String s,int l){
        return s.split("(?<=\\G.{"+l+"})");
    }
    
    /**
     * Sorts a HashMap by the values and returns it as a TreeMap.
     * @param map The HashMap to sort.
     * @return The sorted HashMap as a TreeMap.
     * URL: http://stackoverflow.com/questions/109383/how-to-sort-a-mapkey-value-on-the-values-in-java
     */
    public static TreeMap<String,Double> sortHashMapByDoubleValue(HashMap<String,Double> map){
        DoubleValueComparator bvc =  new DoubleValueComparator(map);
        TreeMap<String,Double> sorted_map = new TreeMap(bvc);
        sorted_map.putAll(map);
        return sorted_map;
    }
    
    static class DoubleValueComparator implements Comparator {
        Map base;
        public DoubleValueComparator(Map base) {
            this.base = base;
        }

        public int compare(Object a, Object b) {

            if((Double)base.get(a) < (Double)base.get(b)) {
                return 1;
            } else if((Double)base.get(a) == (Double)base.get(b)) {
                return 0;
            } else {
                return -1;
            }
        }
    }
    
    /**
     * Sorts a HashMap by the values and returns it as a TreeMap.
     * @param map The HashMap to sort.
     * @return The sorted HashMap as a TreeMap.
     * URL: http://stackoverflow.com/questions/109383/how-to-sort-a-mapkey-value-on-the-values-in-java
     */
    public static TreeMap<String,Integer> sortHashMapByIntegerValue(HashMap<String,Integer> map){
        IntegerValueComparator bvc =  new IntegerValueComparator(map);
        TreeMap<String,Integer> sorted_map = new TreeMap(bvc);
        sorted_map.putAll(map);
        return sorted_map;
    }
    
    static class IntegerValueComparator implements Comparator {
        Map base;
        public IntegerValueComparator(Map base) {
            this.base = base;
        }

        public int compare(Object a, Object b) {

            if((Integer)base.get(a) < (Integer)base.get(b)) {
                return 1;
            } else if((Integer)base.get(a) == (Integer)base.get(b)) {
                return 0;
            } else {
                return -1;
            }
        }
    }
    
    /**
     * Round a number to a raster size.
     * @param number The number to round.
     * @param raster The raster/grid size.
     * @return  The rounded number.
     */
    public static int roundRastered(float number, int raster){
        return FastMath.round(number/raster)*raster;
    }
    
    /**
     * Returns the higher result of a quadratic equation of the format ax^2+bx+c.
     * @param a
     * @param b
     * @param c
     * @return The higher of the two possible results.
     */
    public static double quadraticEquationMax(double a,double b,double c){
        double x = (-b+Math.sqrt(Math.pow(b, 2)-4*a*c))/(2*a);
        double y = (-b-Math.sqrt(Math.pow(b, 2)-4*a*c))/(2*a);
        return Math.max(x, y);
    }
    
    /**
     * Returns the lower result of a quadratic equation of the format ax^2+bx+c.
     * @param a
     * @param b
     * @param c
     * @return The lower of the two possible results.
     */
    public static double quadraticEquationMin(double a,double b,double c){
        double x = (-b+Math.sqrt(Math.pow(b, 2)-4*a*c))/(2*a);
        double y = (-b-Math.sqrt(Math.pow(b, 2)-4*a*c))/(2*a);
        return Math.min(x, y);
    }
    
    /**
     * a^2+b^2=c^2
     * @param a
     * @param b
     * @return c
     */
    public static double pythagoras(double a,double b){
        return Math.sqrt(Math.pow(a,2)+Math.pow(b,2));
    }
    
    public static int nearestHighPowerOfTwo(int x){
        return (int) Math.ceil(Math.log(x)/Math.log(2));
    }
    
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static Random rnd = new Random();
    /**
     * Generate a random string of given length
     * @param len Length of the string
     * @return 
     */
    public static String randomString( int len ){
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }
}
