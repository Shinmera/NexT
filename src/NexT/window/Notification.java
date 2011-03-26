/**********************\
  file: Notification
  package: NexT.window
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package NexT.window;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

public class Notification extends JDialog implements MouseListener{
    public static final int DEFAULT_HEIGHT = 100;
    public static final int DEFAULT_WIDTH = 300;
    public int x,y,w,h,s,s_w,s_h;
    private JLabel title,text,icon;
    private boolean done = false;

    public static void main(String[] args){new Notification();}

    public Notification(){
        s_w = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
        s_h = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
        w = DEFAULT_WIDTH;h = DEFAULT_HEIGHT;s=5;x = s_w-w-10;y = 10;
        title = new JLabel("Notification!");text = new JLabel("!!!");icon = new JLabel();
        initWindow();
    }

    public Notification(String title,String text){
        s_w = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
        s_h = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
        w = DEFAULT_WIDTH;h = DEFAULT_HEIGHT;s=5;x = s_w-w-10;y = 10;
        this.title = new JLabel(title);this.text = new JLabel(text);icon = new JLabel();
        initWindow();
    }

    public Notification(String title,String text,int x,int y){
        s_w = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
        s_h = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
        w = DEFAULT_WIDTH;h = DEFAULT_HEIGHT;s=5;this.x = x;this.y = y;
        this.title = new JLabel(title);this.text = new JLabel(text);icon = new JLabel();
        initWindow();
    }

    public Notification(String title,String text,Icon icon){
        s_w = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
        s_h = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
        w = DEFAULT_WIDTH;h = DEFAULT_HEIGHT;s=5;x = s_w-w-10;y = 10;
        this.title = new JLabel(title);this.text = new JLabel(text);this.icon = new JLabel();this.icon.setIcon(icon);
        initWindow();
    }

    public Notification(String title,String text,Icon icon,int x,int y){
        s_w = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
        s_h = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
        w = DEFAULT_WIDTH;h = DEFAULT_HEIGHT;s=5;this.x = x;this.y = y;
        this.title = new JLabel(title);this.text = new JLabel(text);this.icon = new JLabel();this.icon.setIcon(icon);
        initWindow();
    }

    public Notification(String title,String text,Icon icon,int s){
        s_w = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
        s_h = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
        w = DEFAULT_WIDTH;h = DEFAULT_HEIGHT;this.s = s;;x = s_w-w-10;y = 10;
        this.title = new JLabel(title);this.text = new JLabel(text);this.icon = new JLabel();this.icon.setIcon(icon);
        initWindow();
    }

    public Notification(String title,String text,Icon icon,int s,int x,int y,int w,int h){
        s_w = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
        s_h = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
        this.w=w;this.h=h;this.x=x;this.y=y;this.s=s;
        this.title = new JLabel(title);this.text = new JLabel(text);this.icon = new JLabel();this.icon.setIcon(icon);
        initWindow();
    }

    private void initWindow(){
        setSize(w,h);
        setLocation(x,y);
        setUndecorated(true);
        setModal(false);
        setLayout(null);
        setFocusable(false);
        getContentPane().setBackground(new Color(0,0,0,200));

        title.setFont(new Font("Arial", Font.BOLD,22));
        text.setFont(new Font("Arial", Font.PLAIN,12));
        title.setForeground(Color.WHITE);
        text.setForeground(Color.WHITE);

        if(icon.getIcon()!=null){
            icon.setBounds(10,10,36,36);
            title.setBounds(56,10,w-66,30);
            text.setBounds(56,20,w-66,h-30);
        }else{
            title.setBounds(10,10,w-20,30);
            text.setBounds(10,20,w-20,h-30);
        }

        add(icon);
        add(title);
        add(text);

        new Thread(){
            public void run(){
                for(int i=0;i<s;i++){
                    try{Thread.sleep(1000);}catch(Exception e){e.printStackTrace();}
                }
                disappear();

            }
        }.start();

        setVisible(true);
    }

    public boolean isDone(){
        return done;
    }

    public void disappear(){
        try{
            done = true;
            setVisible(false);
            dispose();
            super.finalize();
        }catch(Throwable e){e.printStackTrace();}
    }

    public void mouseClicked(MouseEvent me) {}
    public void mousePressed(MouseEvent me) {}
    public void mouseReleased(MouseEvent me) {}
    public void mouseEntered(MouseEvent me) {}
    public void mouseExited(MouseEvent me) {}
}
