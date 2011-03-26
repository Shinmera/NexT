/**********************\
  file: Popup
  package: NexT.window
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package NexT.window;
import java.awt.Color;
import javax.swing.*;

public class Popup extends JDialog{
    public Popup(JComponent component){
        setTitle("Popup");
        setModal(false);
        setLocationRelativeTo(null);
        setSize(component.getWidth(),component.getHeight());
        add(component);
        setVisible(true);
    }
    public Popup(String title,JComponent component){
        setTitle(title);
        setModal(false);
        setLocationRelativeTo(null);
        setSize(component.getWidth(),component.getHeight());
        add(component);
        setVisible(true);
    }
    public Popup(String title,JComponent component,boolean modal){
        setTitle(title);
        setModal(modal);
        setLocationRelativeTo(null);
        setSize(component.getWidth(),component.getHeight());
        add(component);
        setVisible(true);
    }
    public Popup(String title,JComponent component,boolean modal,ImageIcon icn){
        setTitle(title);
        setModal(false);
        setLocationRelativeTo(null);
        setSize(component.getWidth(),component.getHeight());
        setIconImage(icn.getImage());
        add(component);
        setVisible(true);
    }
    public Popup(String title,JComponent component,boolean modal,Color bg,ImageIcon icn){
        setTitle(title);
        setModal(false);
        setLocationRelativeTo(null);
        setSize(component.getWidth(),component.getHeight());
        getContentPane().setBackground(bg);
        setIconImage(icn.getImage());
        add(component);
        setVisible(true);
    }
}
