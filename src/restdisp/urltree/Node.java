package restdisp.urltree;

import java.util.List;

public class Node {
	private String name;
	private List<Node> children;
	private Leaf leaf;
	private boolean var;
	
	public Node(String name) {
		this.name = name;
	}
	
	public Node(String name, List<Node> children, Leaf leaf, boolean var) {
		this.name = name;
		this.children = children;
		this.leaf = leaf;
		this.var = var;
	}
	
	public boolean equals(Object obj) {
		if(obj != null && obj instanceof Node){
			return name.equals(((Node)obj).getName());
		}
		
		return false;
	}

	public String getName() {
		return name;
	}

	public List<Node> getChildren() {
		return children;
	}
	
	public void setChildren(List<Node> children) {
		this. children = children;
	}
	
	public Leaf getLeaf() {
		return leaf;
	}
	
	public void setLeaf(Leaf leaf) {
		this.leaf = leaf;
	}
	
	public boolean isVar() {
		return var;
	}

	public void setVar(boolean var) {
		this.var = var;
	}
}
