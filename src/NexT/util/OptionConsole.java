/**********************\
  file: OptionConsole
  package: util
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package NexT.util;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

/*
 * GUI console class to easily change an OptionSet during run.
 *
 */
public class OptionConsole extends JFrame{
    private JTextArea out;
    private JTextField in;
    private JScrollPane scr;
    private int w=300,h=200;
    private ArrayList<String> actionMap;
    private int last;
    private int pos;
    public OptionManager man;

    private Action releasedAction = new AbstractAction("releasedAction") { //another key binding.
        public void actionPerformed(ActionEvent e) {
            out.append(evaluate(in.getText()+"\n"));
            actionMap.add(in.getText());
            in.setText("");
            pos = actionMap.size()+1;
    }};

    private Action upAction = new AbstractAction("upAction") { //another key binding.
        public void actionPerformed(ActionEvent e) {
            if(pos>0){pos--;in.setText(actionMap.get(pos));}}};

    private Action downAction = new AbstractAction("downAction") { //another key binding.
        public void actionPerformed(ActionEvent e) {
            if(pos<actionMap.size()){pos++; in.setText(actionMap.get(pos));}}};
    
    public OptionConsole(){
        setTitle("Console");
        setSize(w,h);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(null);

        out = new JTextArea();
        in  = new JTextField();
        scr = new JScrollPane(out);

        out.setBackground(Color.BLACK);
        out.setForeground(Color.WHITE);
        in.setBackground(Color.LIGHT_GRAY);
        in.setForeground(Color.BLACK);
        out.setFont(new Font("Courier New",Font.BOLD, 13));
        in.setFont(new Font("Courier New",Font.BOLD, 13));
        out.setEditable(false);
        scr.setHorizontalScrollBar(null);
            scr.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() { //for autoscroll
                public void adjustmentValueChanged(AdjustmentEvent e) {
                    if (last != out.getText().hashCode()) { //prevent autoscroll if nothing new arrived.
                        e.getAdjustable().setValue(e.getAdjustable().getMaximum());
                        last = out.getText().hashCode();
            }}});

         this.addComponentListener(new ComponentAdapter() {
        public void componentResized(ComponentEvent arg0) { //get new dimensions
            w = arg0.getComponent().getWidth();
            h = arg0.getComponent().getHeight();
            resize();}});

        in.getInputMap().put(KeyStroke.getKeyStroke("released ENTER"), "released");
        in.getActionMap().put("released", releasedAction);
        in.getInputMap().put(KeyStroke.getKeyStroke("released UP"), "releasedUp");
        in.getActionMap().put("releasedUp", upAction);
        in.getInputMap().put(KeyStroke.getKeyStroke("released DOWN"), "releasedDown");
        in.getActionMap().put("releasedDown", downAction);

        resize();

        add(in);
        add(scr);
    }

    public String evaluate(String in){
        in = in.trim();
        String command = in.substring(0,in.indexOf(" "));
        String args = in.substring(in.indexOf(" ")+1);
        if(command.equals("help"))
            man.get("help");
        if(command.equals("save")){
            if(man.save())return "Saved successfully!";
            else return "Saving failed!";
        }if(command.equals("load")){
            if(man.load())return "Saved successfully!";
            else return "Saving failed!";
        }
        return "";
    }

    private void resize(){
        scr.setBounds(0,0,w,h-30);
        in.setBounds(0,h-30,w,30);
    }

}
