/**
 * 
 */
package kutschke.interpreter;

import java.util.ConcurrentModificationException;

import kutschke.higherClass.Binding;
import kutschke.higherClass.GeneralOperation;

/**
 * used for optimization. Is not to be modified after the begin call to the
 * interpreter.
 * @author Michael
 *
 */
final class SealableBinding extends Binding<Object,Object>{

	private boolean sealed;

	public void seal(){
		sealed = true;
	}
	
	public boolean isSealed(){
		return sealed;
	}
	
	public SealableBinding(GeneralOperation<Object, ? extends Object> inner) {
		super(inner);
	}
	
	@Override
	public SealableBinding bind(int parameter, Object value){
		if(isSealed()){
			throw new ConcurrentModificationException("Binding sealed");
		}
		return (SealableBinding) super.bind(parameter, value);
	}
	
	@Override
	public String toString(){
		if(sealed)
			return "sealed("+super.toString()+")";
		else
			return super.toString();
	}
	
}