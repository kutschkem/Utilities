package kutschke.higherClass;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

public class Binding<ArgType, ResultType> implements GeneralOperation<ArgType, ResultType> {

	protected GeneralOperation<ArgType,? extends ResultType> bound;
	protected TreeMap<Integer,ArgType> bindings = new TreeMap<Integer,ArgType>();
	
	public Binding(GeneralOperation<ArgType, ? extends ResultType> inner){
		bound = inner;
	}
	
	public Binding<ArgType,ResultType> bind(int parameter, ArgType value){
		bindings.put(parameter, value);
		return this;
	}
	
	public Map<Integer,ArgType> getBindings(){
		return Collections.unmodifiableSortedMap(bindings);
	}
	
	public GeneralOperation<ArgType,? extends ResultType> getInner(){
		return bound;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ResultType apply(ArgType[] arg) throws Exception {
		LinkedList<ArgType> params = new LinkedList<ArgType>(Arrays.asList(arg));
		for(Entry<Integer,ArgType> e : bindings.entrySet()){
			params.add(e.getKey(), e.getValue());
		}
		return bound.apply(params.toArray(
				(ArgType[]) Array.newInstance(
						arg.getClass().getComponentType(), params.size())));
	}

}
