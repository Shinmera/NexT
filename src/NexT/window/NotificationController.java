/**********************\
  file: NotificationController
  package: NexT.window
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package NexT.window;

import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.Icon;

public class NotificationController {
    private ArrayList<Notification> notes;
    private boolean shutdown = false;
    private Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    private CThread c;
    private Icon icon;

    public NotificationController(){
        notes = new ArrayList();
        c = new CThread();
        c.start();
    }

    public void setStandardIcon(Icon icon){
        this.icon=icon;
    }

    public void addNote(String title,String text){
        int cur_height = 10;
        if(notes.size()>0) cur_height = notes.get(notes.size()).x+notes.get(notes.size()).w+10;
        if(icon==null)notes.add(new Notification(title,text,screen.width-Notification.DEFAULT_WIDTH-10,cur_height));
        else notes.add(new Notification(title,text,icon,screen.width-Notification.DEFAULT_WIDTH-10,cur_height));
    }

    public void addNote(String title,String text,Icon icon){
        int cur_height = notes.get(notes.size()).x+notes.get(notes.size()).w+10;
        notes.add(new Notification(title,text,icon,screen.width-Notification.DEFAULT_WIDTH-10,cur_height));
    }

    public int getActive(){
        return notes.size();
    }

    public boolean isDone(int i){
        if(notes.get(i).isDone())return true;
        if(notes.get(i) == null) return true;
        return false;
    }

    public void remove(int i){
        notes.get(i).disappear();
        notes.remove(i);
    }

    public void shutdown(){
        shutdown = true;
        for(int i=0;i<getActive();i++)remove(i);
        try{c.yield();
        c.interrupt();}catch(Exception e){}
    }

    public void restart(){
        c = new CThread();
    }

    private class CThread extends Thread{
        public CThread(){setDaemon(true);}
        public void run(){
            while(!isInterrupted()&&!shutdown){
                for(int i=0;i<getActive();i++)if(isDone(i))remove(i);
                try{Thread.sleep(200);}catch(Exception e){e.printStackTrace();}
            }
        }
    }
}
