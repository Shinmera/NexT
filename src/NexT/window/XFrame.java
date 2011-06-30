/**********************\
  file: XFrame.java
  package: NexT.window
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package NexT.window;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

public class XFrame extends JFrame{

    public XFrame(){
        super();
        setLayout(null);

        addWindowListener(new WindowAdapter() { //used for catching the close button
                public void windowClosing(WindowEvent we) {
                    onWindowClosed();
                }
            });
        
        addComponentListener(new ComponentListener(){
                public void componentResized(ComponentEvent ce) {

                    onWindowResized(ce.getComponent().getWidth(),ce.getComponent().getWidth());
                }
                public void componentMoved(ComponentEvent ce) {}
                public void componentShown(ComponentEvent ce) {}
                public void componentHidden(ComponentEvent ce) {}
            });

        setVisible(true);
    }


    public void onWindowClosed(){System.exit(0);}

    public void onWindowResized(int newW,int newH){}

}
