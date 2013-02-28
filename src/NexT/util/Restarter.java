package NexT.util;

import NexT.Commons;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.List;

/**
 * Attempts to perform an application restart by autodetecting launch
 * parameters, building a new launch command and starting a new process during
 * the VM shutdown process. Note that this might only work under some Java VM
 * implementations and might fail spectacularly to detect the proper launch
 * parameters. In such a case, it is advised to set these parameters manually
 * through the set functions.
 * 
 * Adapted from http://java.dzone.com/articles/programmatically-restart-java
 * 
 * @author Shinmera
 * @license GPLv3
 * @version 0.0.0
 */
public class Restarter {    
    /** 
    * Sun property pointing the main class and its arguments. 
    * Might not be defined on non Hotspot VM implementations.
    */
    public static final String SUN_JAVA_COMMAND = "sun.java.command";
    
    private String javaBin;
    private List<String> vmArgs;
    private String[] addArgs;
    private String execCmd;
    private String finalCommand;

    public Restarter(){
        reset();
    }
    
    /**
     * Immediately restarts the application with autodetected settings.
     */
    public static void restart(){
        new Restarter().run();
    }

    /**
     * Builds the startup command with the saved properties.
     * @return 
     */
    public String buildCommand(){
        StringBuffer vmArgsOneLine = new StringBuffer();
        for (String arg : vmArgs) {
            // if it's the agent argument : we ignore it otherwise the
            // address of the old application and the new one will be in conflict
            if (!arg.contains("-agentlib")) {
                vmArgsOneLine.append(arg);
                vmArgsOneLine.append(" ");
            }
        }
        final StringBuffer cmd = new StringBuffer("\"" + javaBin + "\" " + vmArgsOneLine);

        String[] cmdArgs = execCmd.split(" ");
        if(cmdArgs[0].endsWith(".jar")) cmd.append("-jar " + new File(cmdArgs[0]).getPath());
        else                            cmd.append("-cp \"" + System.getProperty("java.class.path") + "\" " + cmdArgs[0]);

        for (int i = 1; i < cmdArgs.length; i++) {
            cmd.append(" ").append(cmdArgs[i]);
        }

        for(String arg : addArgs){
            cmd.append(" ").append(arg);
        }
        
        this.finalCommand=cmd.toString();
        Commons.log.info("[Restarter] Generated restart command: "+finalCommand);
        return finalCommand;
    }
    
    /**
     * Resets all properties to their autodetected values. This gets rid of all
     * the changes you might have made through the setters.
     * @return The newly rebuilt restart command.
     */
    public String reset(){        
        javaBin = System.getProperty("java.home") + "/bin/java";
        vmArgs = ManagementFactory.getRuntimeMXBean().getInputArguments();
        execCmd = System.getProperty(SUN_JAVA_COMMAND);
        addArgs = new String[0];
        return buildCommand();
    }
    
    /**
     * Returns the detected path of the java binary used to execute this.
     * @return 
     */
    public String getJavaBin(){return javaBin;}
    
    /**
     * Returns the detected Virtual Machine arguments used to start this
     * application.
     * @return 
     */
    public String[] getVmArgs(){return vmArgs.toArray(new String[vmArgs.size()]);}
    
    /**
     * Returns the detected startup command used to start this application.
     * @return 
     */
    public String getExecCommand(){return execCmd;}
    
    /**
     * Returns the additional arguments that were set manually.
     * @return 
     */
    public String[] getAdditionalArgs(){return addArgs;}
    
    /**
     * Returns the built startup command used to re-launch the application.
     * @return 
     */
    public String getBuiltStartupCommand(){return finalCommand;}
    
    /**
     * Sets the java binary path manually and rebuilds the restart command.
     * @param javaBin
     * @return The newly built startup command.
     */
    public String setJavaBinPath(String javaBin){this.javaBin=javaBin;return buildCommand();}
    
    /**
     * Sets the Virtual Machine arguments and rebuilds the restart command.
     * @param vmArgs
     * @return The newly built startup command.
     */
    public String setVmArgs(String[] vmArgs){this.vmArgs=Arrays.asList(vmArgs);return buildCommand();}
    
    /**
     * Sets the startup command and rebuilds the restart command.
     * @param execCmd
     * @return  The newly built startup command.
     */
    public String setExecCommand(String execCmd){this.execCmd=execCmd;return buildCommand();}
    
    /**
     * Sets the additional, manual command arguments and rebuilds the restart
     * command
     * @param args
     * @return  The newly built startup command.
     */
    public String setAdditionalArgs(String[] args){
        if(args == null) addArgs = new String[0];
        else             addArgs = args;
        return buildCommand();
    }
    
    /**
     * Attempts to restart the application by using an automatically generated
     * startup command. The current application will be terminated as a cause
     * of this. Note that the start command will be executed as a shutdown hook,
     * so all cleanup actions should be finished before this hits.
     * 
     * @throws IOException 
     * @see Runtime#addShutdownHook(java.lang.Thread) 
     */
    public void run(){run(null);}
    
    /**
     * Attempts to restart the application by using an automatically generated
     * startup command. The current application will be terminated as a cause
     * of this. Note that the start command will be executed as a shutdown hook,
     * so all cleanup actions should be finished before this hits.
     * 
     * @param shutdownHandler A Runnable that will be executed prior to the
     * System.exit() call. Use this to clean up your application.
     * @see Runtime#addShutdownHook(java.lang.Thread) 
     */
    public void run(Runnable shutdownHandler){run(finalCommand, shutdownHandler);}
    
    /**
     * Attempts to restart the application by using the provided startup
     * command. The current application will be terminated as a cause of this. 
     * Note that the start command will be executed as a shutdown hook, so all
     * cleanup actions should be finished before this hits.
     * 
     * @param startupCommand The command to use to start up the application.
     * @param shutdownHandler A Runnable that will be executed prior to the
     * System.exit() call. Use this to clean up your application.
     * @see Runtime#addShutdownHook(java.lang.Thread) 
     */
    public void run(final String startCommand, Runnable shutdownHandler){
        Commons.log.severe("[Restarter] Running restart...");
        Commons.log.info("[Restarter] Registering shutdown hook to launch application.");
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try{Runtime.getRuntime().exec(startCommand);
                }catch(IOException e){e.printStackTrace();}
            }
        });

        if(shutdownHandler!= null){
            Commons.log.info("[Restarter] Running shutdown handler.");
            shutdownHandler.run();
        }

        Commons.log.info("[Restarter] Performing exit.");
        System.exit(0);
    }
}
