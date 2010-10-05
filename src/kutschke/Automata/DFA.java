package kutschke.Automata;

import java.util.HashMap;

/**
 * a deterministic finite Automata - implements functions to check a sequence of Objects on proper order based on the Automata.
 * <br/> can be used, for example, for regular expressions.
 * @author Michael Kutschke
 *
 * @param <T>
 */
public class DFA<T> {
	
	protected int ID = -1;
	protected HashMap<T,DFA<T>> edges = new HashMap<T,DFA<T>>();
	protected boolean accepting = false;
	
	public boolean isAccepting() {
		return accepting;
	}

	public void setAccepting(boolean accepting) {
		this.accepting = accepting;
	}

	public DFA(){
		
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getID() {
		return ID;
	}
	
	public void addEdge(T key, DFA<T> neighbour){
		edges.put(key, neighbour);
	}
	
	public void removeEdge(T key){
		edges.remove(key);
	}
	
	public DFA<T> getNeighbour(T key){
		return edges.get(key);
	}

	/**
	 * checks if the word is accepted by the automata, that means after the sequence of keys the automata stops in an accepting state
	 * @param word the sequence of Objects that is checked
	 * @return <li>true - the automata accepted the word</li>
	 * 		   <li>false - the automata did not accept the word</li>
	 */
	public boolean check(T[] word){
		DFA<T> actual = this;
		for(int i = 0; i < word.length; i++){
			actual = actual.getNeighbour(word[i]);
			if(actual == null) return false;
		}
		return actual.isAccepting();
	}
	
}
