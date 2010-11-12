package kutschke.graphs;

public class DisjointSet {

	private DisjointSet parent = this;
	private int size = 1;
	
	/**
	 * returns the set that contains this element
	 * @return
	 */
	public DisjointSet find(){
		if(parent == this) return this;
		parent = parent.find();
		return parent;
	}
	
	public void merge(DisjointSet other){
		DisjointSet root = find();
		DisjointSet root2 = other.find();
		root.parent = root2;
		if(root.parent != root)
			root2.size += root.size;
	}
	
	public int size(){
		return size;
	}
	
}
