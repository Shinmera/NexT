/**********************\
  file: Arguments
  package: NexT.util
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package NexT.util;
import java.util.*;

public class Arguments {
    String desc = "";
    ArrayList<Character>       opts  = new ArrayList();
    ArrayList<String>          alis  = new ArrayList();
    HashMap<Character,String>  alias = new HashMap();
    HashMap<Character,String>  descr = new HashMap();
    HashMap<Character,String>  nvals = new HashMap();
    HashMap<Character,String>  vals  = new HashMap();

    public static void main(String[] args){
        Arguments a = new Arguments();
        a.addArgument('a',"Show all options");
        System.out.println("RET: "+a.eval(args));
    }
    public Arguments(){}
    public Arguments(String desc){ this.desc = desc;}

    public void addArgument(char args,String explanation){
        if(!opts.contains(args)){
            opts.add(args);
            alis.add("");
            alias.put(args,"");
            descr.put(args,explanation);
            nvals.put(args,"");
        }
    }

    public void addArgument(char args,String explanation,String value){
        if(!opts.contains(args)){
            opts.add(args);
            alis.add("");
            alias.put(args,"");
            descr.put(args,explanation);
            nvals.put(args,value);
        }
    }

    public void addArgument(char args,String aliass,String explanation,String value){
        if(!opts.contains(args)&&!alis.contains(aliass)){
            opts.add(args);
            alis.add(aliass);
            alias.put(args,aliass);
            descr.put(args,explanation);
            nvals.put(args,value);
        }
    }

    public HashMap<Character,String> getVals(){
        return vals;
    }

    public String eval(String[] args){
        char prev = ' ';String all = "";

        //loop through arguments
        for(int i=0;i<args.length;i++){
            if(args[i].startsWith("--")){ 
                if(args[i].equals("--help")){
                    System.out.println(desc);
                    System.out.println("\nAvailable arguments: ");
                    for(int j=0;j<opts.size();j++){
                        System.out.println(Toolkit.insertSpaces("-"+opts.get(j),3)+
                                       Toolkit.insertSpaces("<"+nvals.get(opts.get(j))+">",15)+
                                           Toolkit.insertSpaces(alias.get(opts.get(j)),15)+
                                                                descr.get(opts.get(j)));
                    }
                }else{
                    if(!alias.containsValue(args[i].substring(2))){//alias argument
                        System.out.println("Invalid argument: "+args[i]+"\nUse --help to get a list of possible options.");
                        return "!";
                    }
                    prev = opts.get(alis.indexOf(args[i].substring(2)));
                    all += prev;
                }
            }else if(args[i].startsWith("-")){ //char argument
                args[i] = args[i].substring(1);
                for(int j=0;j<args[i].length();j++){
                    if(!opts.contains(args[i].charAt(j))){
                        System.out.println("Invalid argument: "+args[i].charAt(j)+"\nUse --help to get a list of possible options.");
                        return "!";
                    }
                    prev = args[i].charAt(j);
                    all += prev;
                }
            }else{ //value
                if(prev != ' ')vals.put(prev,args[i]);
                prev = ' ';
            }
        }
        //check for missing values
        for(int i=0;i<all.length();i++){
            if(!vals.containsKey(all.charAt(i))&&!nvals.get(all.charAt(i)).equals("")){
                System.out.println("Missing value for argument: "+all.charAt(i)+"\nUse --help to get a list of possible options.");
                return "!";
            }
        }
        return all;
    }


}
