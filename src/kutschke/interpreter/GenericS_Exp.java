package kutschke.interpreter;

import java.util.LinkedList;
import java.util.ListIterator;

import kutschke.higherClass.GeneralOperation;

public class GenericS_Exp extends LinkedList<Object> implements S_Exp {

	
	
	@Override
	public Object optimize() {
		ListIterator<Object> it = listIterator();
		while(it.hasNext()){
			Object o = it.next();
			if(o instanceof S_Exp){
				o = ((S_Exp) o).optimize();
				it.set(o);
			}
		}
	/*	boolean constant = true;
		for(Object o : this){
			if(o instanceof S_Exp)
				constant = false;
		}
		if(constant)
			try {
				return this.evaluate();
			} catch (SyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		return this;
	}

	@Override
	public Object evaluate() throws SyntaxException {
		
		ListIterator<Object> it = this.listIterator();
		while(it.hasNext()){
			Object o = it.next();
			while(o instanceof S_Exp){
				o = ((S_Exp) o).evaluate();
			}
			it.set(o);
		}
		
		Object method = this.removeFirst();
		if(method instanceof GeneralOperation){
			try {
				return ((GeneralOperation) method).apply(this.toArray());
			} catch (Exception e) {
				throw new SyntaxException(e);
			}
		}
	throw new SyntaxException("Not a method: " + method);
	}

}
