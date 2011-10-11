package restdisp.urltree;

import java.util.List;

public class UrlDescriptor {
	private List<String> urlVariables;
	private Leaf leaf;
	
	public UrlDescriptor(List<String> urlVariables, Leaf leaf) {
		this.urlVariables = urlVariables;
		this.leaf = leaf;
	}
	
	public List<String> getUrlVariables() {
		return urlVariables;
	}
	
	public Leaf getLeaf() {
		return leaf;
	}
}
