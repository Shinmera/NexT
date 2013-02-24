/**********************\
  file: TextEditor.java
  package: NexT.window
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package NexT.window;

import NexT.Commons;
import NexT.util.Toolkit;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.logging.Logger;
import javax.swing.*;

public class TextEditor extends JDialog implements ActionListener{
    private JTextArea area;
    private File file;

    public TextEditor(){
        JMenuBar m_bar = new JMenuBar();
        JMenu m_file = new JMenu("File");
        area = new JTextArea();
        JScrollPane scroll = new JScrollPane(area);

        final JMenuItem i_save = new JMenuItem("Save");
        final JMenuItem i_reload=new JMenuItem("Reload");
        final JMenuItem i_quit = new JMenuItem("Quit");

        i_save.addActionListener(this);
        i_reload.addActionListener(this);
        i_quit.addActionListener(this);

        area.setFont(new Font("Monospaced",Font.PLAIN,12));
        area.getActionMap().put("save", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {i_save.doClick();}
        });
        InputMap inputMap = area.getInputMap();
        inputMap.put(KeyStroke.getKeyStroke("control S"), "save");

        m_file.add(i_save);
        m_file.add(i_reload);
        m_file.addSeparator();
        m_file.add(i_quit);
        m_bar.add(m_file);
        add(scroll);

        setJMenuBar(m_bar);
        setSize(500,500);
        setVisible(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    public static void main(String[] args){
        JFileChooser jfc = new JFileChooser();
        jfc.showOpenDialog(null);
        showTextEditor(jfc.getSelectedFile());
    }

    public boolean loadFile(File f){
        Commons.log.info("[NexT][TextEditor] Loading '"+f.getName()+"'");
        file=f;
        String temp = Toolkit.loadFileToString(f);
        if(!temp.equals("")){
            area.setText(temp);
            return true;
        }else return false;
    }

    public boolean saveFile(File f){
        Commons.log.info("[NexT][TextEditor] Saving to '"+f.getName()+"'");
        return Toolkit.saveStringToFile(area.getText(), f);
    }

    public static void showTextEditor(File f){
        TextEditor editor = new TextEditor();
        if(f!=null)editor.loadFile(f);
    }

    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand().toLowerCase().trim();

        if(s.equals("save")){
            saveFile(file);
        }
        if(s.equals("reload")){
            loadFile(file);
        }
        if(s.equals("quit")){
            dispose();
        }
    }
}
