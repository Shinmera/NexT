package NexT.util;

/**********************\
  file: Meta
  package: 
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

public class Meta {
    public static final String version = "V1.5b";
    public static final String developers = "Shinmera";
    public static final int OS_WEIRD = -1;
    public static final int OS_LINUX = 0;
    public static final int OS_WINDOWS=1;
    public static final int OS_APPLE = 2;
    public static final int OS_SOLARIS = 3;

    public static int getOS(){
        String os = System.getProperty("os.name").toLowerCase();
        if(os.contains("linux"))return OS_LINUX;
        if(os.contains("lunix"))return OS_LINUX;
        if(os.contains("win"))return OS_WINDOWS;
        if(os.contains("mac"))return OS_APPLE;
        if(os.contains("solaris"))return OS_SOLARIS;
        if(os.contains("sunos"))return OS_SOLARIS;
        return OS_WEIRD;
    }
}
