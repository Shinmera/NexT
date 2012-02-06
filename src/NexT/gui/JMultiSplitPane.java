/**********************\
  file: JMultiSplitPane.java
  package: NexT.gui
  author: Shinmera
  team: NexT
  license: -
\**********************/

package NexT.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.JPanel;

public class JMultiSplitPane extends JPanel implements MouseMotionListener{
    private ArrayList<JPanel> panels = new ArrayList<JPanel>();
    private int thickness = 3;
    
    public JMultiSplitPane(){
        
    }
    
    public void autoFit(){
        int panelHeight = (getHeight()-(thickness*(panels.size()-1)))/panels.size();
        for(int i=0;i<panels.size();i++){
            panels.get(i).setSize(getWidth(),panelHeight);
            panels.get(i).setLocation(0,(panelHeight+thickness)*i);
        }
    }
    
    public void autoFitKeepAspect(){
        
    }
    
    public void addPanel(JPanel panel,int position){
        panels.add(position, panel);
        add(panel);
        autoFit();
    }
    
    public void addPanel(JPanel panel){addPanel(panel,0);}

    public final void mouseDragged(MouseEvent e) {
        
    }

    public final void mouseMoved(MouseEvent e) {
        
    }
    
}
