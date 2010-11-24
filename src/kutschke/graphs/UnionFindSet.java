package kutschke.graphs;

public class UnionFindSet {

	private UnionFindSet parent = this;
	private int size = 1;
	
	/**
	 * returns the set that contains this element
	 * @return
	 */
	public UnionFindSet find(){
		if(parent == this) return this;
		parent = parent.find();
		return parent;
	}
	
	public void merge(UnionFindSet other){
		UnionFindSet root = find();
		UnionFindSet root2 = other.find();
		root.parent = root2;
		if(root.parent != root)
			root2.size += root.size;
	}
	
	public int size(){
		return size;
	}
	
}
