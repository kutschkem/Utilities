package kutschke.generation.LSystems;

import java.awt.Graphics2D;
import java.util.Stack;

public class TurtleInterpreter {

    private class TurtleState implements Cloneable {
        private double x, y, angle;
        private double length, reductionFactor;
        private double angleChange;

        public TurtleState clone() {
            try {
                return (TurtleState) super.clone();
            } catch (CloneNotSupportedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }
    }

    private TurtleState state = new TurtleState();

    private Stack<TurtleState> stack = new Stack<TurtleState>();

    public void run(Graphics2D g, String commands) {
        for (char c : commands.toCharArray()) {
            switch (c) {
            case 'x':
                double newx = getX() + Math.cos(getAngle()) * getLength();
                double newy = getY() + Math.sin(getAngle()) * getLength();
                g.drawLine((int) getX(), (int) getY(), (int) newx, (int) newy);
                setX(newx);
                setY(newy);
                break;
            case 'l':
                setAngle(getAngle() + getAngleChange());
                break;
            case 'r':
                setAngle(getAngle() - getAngleChange());
                break;
            case '-':
                setLength(getLength() * getReductionFactor());
                break;
            case '[':
                stack.push(state);
                state = state.clone();
                break;
            case ']':
                state = stack.pop();
                break;

            }
        }
    }

    public double getReductionFactor() {
        return state.reductionFactor;
    }

    public void setReductionFactor(double reductionFactor) {
        this.state.reductionFactor = reductionFactor;
    }

    public double getLength() {
        return state.length;
    }

    public void setLength(double length) {
        this.state.length = length;
    }

    public double getX() {
        return state.x;
    }

    public void setX(double x) {
        this.state.x = x;
    }

    public double getY() {
        return state.y;
    }

    public void setY(double y) {
        this.state.y = y;
    }

    public double getAngle() {
        return state.angle;
    }

    public void setAngle(double angle) {
        this.state.angle = angle;
    }

    public double getAngleChange() {
        return state.angleChange;
    }

    public void setAngleChange(double angleChange) {
        this.state.angleChange = angleChange;
    }

}
