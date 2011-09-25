/**********************\
  file: ScriptConsole.java
  package: NexT.script
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package NexT.script;

import NexT.util.Toolkit;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.*;

public class ScriptConsole extends JFrame{
    ScriptManager man = new ScriptManager();
    JFileChooser jfc = new JFileChooser();
    JTextArea txt = new JTextArea();
    String previous = "";

    public static void main(String[] args){
        ScriptConsole cons = new ScriptConsole();
        cons.setVisible(true);
        cons.loadScript();
    }

    public ScriptConsole(){
        txt.setBackground(Color.BLACK);
        txt.setForeground(Color.LIGHT_GRAY);
        txt.setFont(new Font("Monospace",Font.BOLD,18));
        JScrollPane scroll = new JScrollPane(txt);
        add(scroll);

        Action enterAction = new AbstractAction(){
            public void actionPerformed(ActionEvent e) {execute(txt.getText().split("\n")[txt.getLineCount()-1]);txt.setCaretPosition(txt.getText().length());}};
        Action tabAction = new AbstractAction(){
            public void actionPerformed(ActionEvent e) {txt.setText(previous);}};
        txt.getInputMap().put(KeyStroke.getKeyStroke("ENTER"),"enter");
        txt.getActionMap().put("enter", enterAction);
        txt.getInputMap().put(KeyStroke.getKeyStroke("TAB"),"tab");
        txt.getActionMap().put("tab", tabAction);

        setSize(500,300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void loadScript(){
        int o = jfc.showOpenDialog(this);
        if(o==jfc.APPROVE_OPTION)
            man.loadScript(jfc.getSelectedFile());
    }

    public void execute(String s){
        s=s.trim();
        if(s.startsWith("exec")){
            previous=s;
            s=s.substring(5);
            String script = s.split(" ")[0];
            String function = s.split(" ")[1];
            txt.append("\n>"+man.s(script).eval(function, null).toString()+"\n\n");
            
        }else if(s.startsWith("load")){
            if(s.contains(" "))man.loadScript(new File(s.substring(s.indexOf(" ")+1)));
            else loadScript();
            txt.append("\n>Loaded.\n\n");
        
        }else if(s.startsWith("set")){
            s=s.substring(4);
            String script = s.split(" ")[0];
            String var = s.split(" ")[1];
            String value = s.substring(s.indexOf(var)+var.length()).trim();
            man.s(script).setVariable(var,new Var(value));
            txt.append("\n>Variable set.\n\n");

        }else if(s.startsWith("get")){
            txt.append("\n>"+man.s(s.split(" ")[1]).v(s.split(" ")[2]).toString()+"\n\n");

        }else if(s.startsWith("funclist")){
            txt.append("\n>"+Toolkit.implode(man.s(s.split(" ")[1]).getFunctions().toArray(),"\n>"));
            txt.append("\n\n");
        }
    }
}
