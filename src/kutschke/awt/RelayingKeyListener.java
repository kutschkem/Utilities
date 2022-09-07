package kutschke.awt;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * This class is used for passing KeyEvents down the Component hierarchy, if the
 * events need to be passed independently of focus.
 * 
 * @author Michael Kutschke
 */
public class RelayingKeyListener implements KeyListener {

	private Component target;
	private boolean ignoreVisibility = false;

	/**
	 * constructs a RelayingKeyListener taking into account visibility (only
	 * passing events to visible components)
	 * 
	 * @param target non-null JComponent
	 */
	public RelayingKeyListener(Component target) {
		this.target = target;
	}

	public RelayingKeyListener(Component target, boolean ignoreVisibility) {
		this.target = target;
		this.ignoreVisibility = ignoreVisibility;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (ignoreVisibility || target.isShowing()) target.dispatchEvent(e);

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (ignoreVisibility || target.isShowing()) target.dispatchEvent(e);

	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (ignoreVisibility || target.isShowing()) target.dispatchEvent(e);

	}

}
