package kutschke.algo;

/**
 * Disjoint Set Datastructure, uses Union by Rank and Path Compression
 * heuristics
 * 
 * @author Michael
 * 
 */
public class UnionFindSet {

	private UnionFindSet parent = this;
	private int rank = 0;

	/**
	 * returns the set that contains this element
	 * 
	 * @return
	 */
	public UnionFindSet find() {
		if (parent == this)
			return this;
		parent = parent.find();
		return parent;
	}

	/**
	 * Unites the sets. In equal rank case, the other set is chosen as the new
	 * root.
	 * 
	 * @param other
	 */
	public void merge(UnionFindSet other) {
		UnionFindSet root = find();
		UnionFindSet root2 = other.find();
		if (root == root2)
			return;
		if (root.rank == root2.rank)
			root2.rank++;
		if (root.rank > root2.rank)
			root2.parent = root;
		else
			root.parent = root2;
	}

	/**
	 * upper bound for the height of the tree rooted at this element
	 * @return
	 */
	public int rank() {
		return rank;
	}

}
