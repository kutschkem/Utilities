package kutschke.generation.LSystems.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import kutschke.generation.LSystems.LExpander;

public class Main extends JFrame {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Main main = new Main();
        main.setVisible(true);
        main.setDefaultCloseOperation(EXIT_ON_CLOSE);

        LExpander lexp = new LExpander(rules());
        String s = "x";
        int iterations = 3;
        for (int i = 0; i < iterations; i++) {
            s = lexp.expand(s);
        }
        

        final TurtlePanel pnl = new TurtlePanel();
        pnl.turt.setX(250);
        pnl.turt.setLength(80);
        pnl.turt.setAngle(-Math.PI / 2);
        pnl.turt.setReductionFactor(0.5);
        pnl.turt.setAngleChange(Math.PI / 3);
        pnl.turt.setY(250);
        pnl.setCommands(s);
        main.add(pnl);
        
        JPanel pnl2 = new JPanel();
        JLabel lbl = new JLabel("x ->");
        pnl2.add(lbl);
        final JTextField txt = new JTextField();
        pnl2.add(txt);
        JButton button = new JButton("Submit");
        
        button.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Map<Character, String> rules = new HashMap<Character,String>();
				rules.put('x', txt.getText());
				LExpander lexp = new LExpander(rules );
		        String s = "x";
		        int iterations = 3;
		        for (int i = 0; i < iterations; i++) {
		            s = lexp.expand(s);
		        }
				pnl.setCommands(s);
		        pnl.turt.setX(250);
		        pnl.turt.setLength(80);
		        pnl.turt.setAngle(-Math.PI / 2);
		        pnl.turt.setReductionFactor(0.5);
		        pnl.turt.setAngleChange(Math.PI / 3);
		        pnl.turt.setY(250);
				pnl.repaint();
			}
        	
        });
        
        pnl2.add(button);
        txt.setPreferredSize(new Dimension(400,button.getPreferredSize().height));
        
        pnl2.setLayout(new FlowLayout());
        
        main.add(pnl2);
        
        main.setLayout(new FlowLayout());
        
        
        main.pack();

    }

    private static Map<Character, String> rules() {
        Map<Character, String> map = new HashMap<Character, String>();
        map.put('x', "x[-lx][-x][-rx]");
        return map;
    }

}
