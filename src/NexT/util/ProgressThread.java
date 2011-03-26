/**********************\
  file: ProgressThread
  package: NexT.util
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package NexT.util;

import javax.swing.JFrame;
import javax.swing.JProgressBar;

public class ProgressThread extends Thread{
    private String status;
    private int current,maximum;
    public JProgressBar bar;
    public JFrame f;
    public boolean done = false;

    public ProgressThread(String title){
        status="";current=0;maximum=100;bar = new JProgressBar(0,100);
        f = new JFrame();initFrame(title);
    }
    public ProgressThread(String title,int max){
        status="";current=0;maximum=maximum;bar = new JProgressBar(0,maximum);
        f = new JFrame();initFrame(title);
    }
    public ProgressThread(String title,int max,int cur){
        status="";current=cur;maximum=maximum;bar = new JProgressBar(0,maximum);bar.setValue(current);
        f = new JFrame();initFrame(title);
    }

    private void initFrame(String title){
        f.setTitle(title);
        f.add(bar);
        f.setSize(200,50);
        f.setLocation(java.awt.Toolkit.getDefaultToolkit().getScreenSize().width/2-100, java.awt.Toolkit.getDefaultToolkit().getScreenSize().height/2-25);
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        f.setAlwaysOnTop(true);
        f.setVisible(true);
        System.out.println("ProgressThread started...");
    }

    public void setMaximum(int n){maximum=n;bar.setMaximum(n);}
    public void setCurrent(int n){current=n;bar.setValue(n);}
    public void setPercent(int n){current=(n*maximum)/100;bar.setValue(current);}
    public void setStatus(String s){status=s;bar.setString(status);}
    public int getMaximum(){return maximum;}
    public int getCurrent(){return current;}
    public double getPercent(){return (current*100.0)/maximum;}
    public String getStatus(){return status;}
    public JProgressBar getBar(){
        bar = new JProgressBar(0,maximum);
        bar.setValue(current);
        if(!status.equals(""))bar.setString(status);
        return bar;
    }
    public void setDone(){f.setVisible(false);done=true;}
    public boolean isDone(){return done;}
}
