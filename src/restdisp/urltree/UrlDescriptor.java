package restdisp.urltree;

import java.util.Map;

public class UrlDescriptor {
	private Map<String, String> urlVariables;
	private Leaf leaf;
	
	public UrlDescriptor(Map<String, String> urlVariables, Leaf leaf) {
		this.urlVariables = urlVariables;
		this.leaf = leaf;
	}
	
	public Map<String, String> getUrlVariables() {
		return urlVariables;
	}
	
	public Leaf getLeaf() {
		return leaf;
	}
}
