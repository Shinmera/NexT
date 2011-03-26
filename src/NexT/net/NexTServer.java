/**********************\
  file: NexTServer
  package: NexT.net
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package NexT.net;
import NexT.util.SimpleSet;
import java.io.*;
import java.net.*;

public class NexTServer{

    static  Socket clientSocket = null;
    static  ServerSocket serverSocket = null;
    int port_number=2222;
    public int max_clients = 500;

    clientThread t[] = new clientThread[max_clients];

    public static void main(String args[]) {
        NexTServer server = new NexTServer();
    }

    public NexTServer(){

        try {
	    serverSocket = new ServerSocket(port_number);
        }
        catch (IOException e)
	    {System.out.println(e);}
        
	while(true){
	    try {
		clientSocket = serverSocket.accept();
		for(int i=0; i<=max_clients; i++){
		    if(t[i]==null)
			{
			    (t[i] = new clientThread(clientSocket)).start();
			    break;
			}
		}
	    }
	    catch (IOException e) {
		System.out.println(e);}
	}
    }

    class clientThread extends Thread{

        DataInputStream is = null;
        PrintStream os = null;
        Socket socket = null;
        SimpleSet options;

        public clientThread(Socket clientSocket){
            this.socket=clientSocket;
        }

        public void run(){
            String line;
            try{
	    is = new DataInputStream(socket.getInputStream());
	    os = new PrintStream(socket.getOutputStream());
            SimpleSet temp = new SimpleSet();temp.put("connected", "true");
            composeResponse(temp,os);

	    while (true) {
		line = is.readLine();
                if(!deparseProtocol(line)) break;
	    }

	    for(int i=0; i<=max_clients; i++)
		if (t[i]==this) t[i]=null;
            
	    is.close();
	    os.close();
	    socket.close();
            }catch(IOException e){};
        }

        private boolean deparseProtocol(String input){
            SimpleSet request = new SimpleSet();
            return true;
        }

        private void composeResponse(SimpleSet keys,PrintStream os){
            String response="<response><time>"+java.util.Calendar.getInstance().getTime().toString()+"</time>";
            for(int j=0;j<keys.size();j++)
                response+="<value key='"+keys.getKey(j)+"'>"+keys.get(j)+"</value>";
            response+="</response>";
            os.println(response);
        }
    }
}
