package NexT.util;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 
 * @author Shinmera
 * @license GPLv3
 * @version 0.0.0
 */
public final class StringUtils {
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
     * Trims the string with begin and end limiters.
     *
     * @param stack The source String.
     * @param begin The left side limiter.
     * @param end The right side limiter.
     * @return The trimmed string.
     */
    public static String inBetween(String stack,String begin,String end){
        return inBetween(stack, begin, end, 0);
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
     * Glues together the array with delim inserted between elements.
     * 
     * @param ary The array to be glued together.
     * @param delim The delimiter to be put between elements.
     * @return The glued array.
     */
    public static String implode(Object[] ary, String delim) {
        return implode(ary, delim, 0, ary.length);
    }

    /**
     * Glues together the array from start out with delim inserted between each
     * element.
     * 
     * @param ary The array to be glued together.
     * @param delim The delimiter to be put between elements.
     * @return The glued array.
     */
    public static String implode(Object[] ary, String delim, int start) {
        return implode(ary, delim, start, ary.length);
    }

    /**
     * Glues together the array between start and stop with delim inserted
     * between each element.
     * 
     * @param ary The array to be glued together.
     * @param delim The delimiter to be put between elements.
     * @param start The start index.
     * @param stop The stop index.
     * @return The glued array.
     */
    public static String implode(Object[] ary, String delim, int start, int stop) {
        if(start<0)         throw new IllegalArgumentException("Start cannot be below zero.");
        if(stop<start)      throw new IllegalArgumentException("Stop cannot be smaller than start.");
        if(stop>ary.length) throw new IllegalArgumentException("Stop cannot be larger than the array.");
        StringBuilder out = new StringBuilder();
        for(int i=start; i<stop; i++) {
            if(i!=0) { out.append(delim); }
            out.append(ary[i].toString());
        }
        return out.toString();
    }
    
    /**
     * Glues together the map with keyvaluedelim inserted between each key and
     * value and pairdelim between each key-value pair.
     * 
     * @param map The map to be glued together.
     * @param keyvaluedelim The delimiter to put between keys and values.
     * @param pairdelim The delimiter to put between pairs.
     * @return The glued map.
     */
    public static String implode(Map map, String keyvaluedelim, String pairdelim){
        StringBuilder out = new StringBuilder();
        for(Object o : map.keySet()){
            if(out.length() != 0)out.append(pairdelim);
            out.append(o).append(keyvaluedelim).append(map.get(o));
        }
        return out.toString();
    }

    /**
     * Turns a string into a hash map, using newline and equals as delimiters.
     *
     * @param s the string to map-ify
     * @return The resulting key->value map
     * @see StringUtils#stringToMap(java.lang.String, java.lang.String, java.lang.String) 
     */
    public static HashMap<String,String> stringToMap(String s){
        return stringToMap(s,"\n","=");
    }

    /**
     * Turns a string into a hash map.
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
     * Generates a string of length m, with 0-padding fill.
     * @param n The number to unify.
     * @param m The minimum length of the resulting string.
     * @return 
     */
    public static String unifyNumberString(int n,int m){
        return padding(n+"", m, "0");
    }

    /**
     * Return the String with added padding.
     * 
     * @param in The string to append the spaces to.
     * @param maxlen The maximum size of the row.
     * @param padding The string to use as padding.
     * @return the new String with appended spaces.
     */
    public static String padding(String in, int maxlen, String padding){
        return repeatString(padding, (maxlen-in.length())/padding.length());
    }
    
    /**
     * Returns a given string n amount of times.
     * @param s The string to be duplicated.
     * @param n The amount of times to append.
     * @return The final string.
     */
    public static String repeatString(String s,int n){
        if(n<=0)return "";
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
    
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static Random rnd = new Random();
    /**
     * Generate a random string of given length
     * @param len Length of the string
     * @return 
     */
    public static String randomString( int len ){
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ ){
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        }
        return sb.toString();
    }
    
    /**
     * Turns every word's first character uppercase.
     * @param s
     * @return 
     */
    public static String wordsToUpper(String s){
        final StringBuilder result = new StringBuilder(s.length());
        String[] words = s.split("\\s");
        for(int i=0,l=words.length;i<l;++i) {
            if(i>0) result.append(" ");      
            result.append(Character.toUpperCase(words[i].charAt(0)))
                  .append(words[i].substring(1));
        }
        return result.toString();
    }
    
    /**
     * Turns the first character in the string uppercase
     * @param s
     * @return 
     */
    public static String firstToUpper(String s){
        char[] stringArray = s.toCharArray();
        stringArray[0] = Character.toUpperCase(stringArray[0]);
        return new String(stringArray);
    }
    
    /**
     * Checks if the specified string is alphanumeric (a-Z0-9.-_ ).
     * @param s The string to check
     * @return 
     * @see StringUtils#sanitizeString(java.lang.String) 
     */
    public static boolean isAlphanumeric(String s){
        return s.equals(sanitizeString(s));
    }
    
    /**
     * Removes all characters that are non-alphanumeric (a-Z0-9.-_ ) from the
     * string.
     * @param s The string to sanitise.
     * @return A stripped, sanitised string.
     * @see StringUtils#sanitizeString(java.lang.String, java.lang.String) 
     */
    public static String sanitizeString(String s){
        return sanitizeString(s, "a-zA-Z0-9\\s\\.\\-_");
    }
    
    /**
     * Removes all characters that are not contained within the allowed string.
     * Note that allowed is a bunch of regex characters and they need to be
     * escaped as such.
     * 
     * @param s The string to sanitise.
     * @param allowed The allowed characters.
     * @return A stripped, sanitised string.
     */
    public static String sanitizeString(String s, String allowed){
        return s.replaceAll("[^allowed]", "");
    }
    
    public static final SimpleDateFormat defaultSDF = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    
    /**
     * Returns a string representation of the timestamp.
     * @param timestamp
     * @return 
     * @see StringUtils#toHumanTime(long, java.lang.String) 
     */
    public static String toHumanTime(long timestamp){return toHumanTime(timestamp,null);}
    
    /**
     * Returns a string representation of the timestamp using the specified
     * format. The format has to conform to the SimpleDateFormat formatting
     * rules. If the format is set to null, a default time format of 
     * yyyy.MM.dd HH:mm:ss is used.
     * @param timestamp
     * @param format
     * @return 
     * @see SimpleDateFormat#SimpleDateFormat(java.lang.String) 
     */
    public static String toHumanTime(long timestamp, String format){
        return (format==null)? defaultSDF.format(timestamp) : new SimpleDateFormat(format).format(timestamp);
    }
}
