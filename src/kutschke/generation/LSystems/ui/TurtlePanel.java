package kutschke.generation.LSystems.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import kutschke.generation.LSystems.TurtleInterpreter;

public class TurtlePanel extends JPanel {

    String commands = "";
    public TurtleInterpreter turt = new TurtleInterpreter();

    public TurtlePanel() {
        setPreferredSize(new Dimension(500, 500));
    }

    @Override
    protected void paintComponent(Graphics g) {
        // TODO Auto-generated method stub
        super.paintComponent(g);

        turt.run((Graphics2D) g, commands);
    }
    
    public void setCommands(String commands){
    	this.commands = commands;
    }

}
