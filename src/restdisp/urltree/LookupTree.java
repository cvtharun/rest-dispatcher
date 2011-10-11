package restdisp.urltree;

import java.util.ArrayList;
import java.util.List;

import restdisp.validation.RoutingException;

public class LookupTree {
	public static UrlDescriptor getPath(Node root, String httpMethod, String url) throws RoutingException {
		url = "/" + httpMethod + url;
		String[] nodes = url.replaceFirst("/", "").split("(?<=\\w)/");
		
		FetchResult desc = getUrlPath(root, nodes, 0);

		if (null == desc.getLeaf()) {
			throw new RoutingException(String.format("Path not defined [%s]", url));
		}
		
		return new UrlDescriptor(desc.getResult(), desc.getLeaf());
	}

	private static class FetchResult {
		private boolean nonempty;
		List<String> result;
		Leaf leaf;
		
		FetchResult(boolean nonempty, List<String> result, Leaf leaf) {
			this.nonempty = nonempty;
			this.result = result;
			this.leaf = leaf;
		}
		FetchResult(boolean nonempty, List<String> result) {
			this(nonempty, result, null);
		}
		public boolean isNonempty() {
			return nonempty;
		}
		public List<String> getResult() {
			return result;
		}
		public Leaf getLeaf() {
			return leaf;
		}
		public void setResult(List<String> result) {
			this.result = result;
		}
	}
	
	private static FetchResult getUrlPath(Node root, String[] nodes, int lvl) {
		if (nodes.length == lvl) { // reached the leaf
			return new FetchResult(true, null, root.getLeaf());
		}
		
		String curNode = nodes[lvl];
		List<Node> children = root.getChildren();
		
		FetchResult result = null;
		for (Node node : children) {
			if (node.getName().equalsIgnoreCase(curNode) && !node.isVar()) {
				result = getUrlPath(node, nodes, lvl + 1);
				if (result.isNonempty()) {
					return result;
				}
			} else if (node.isVar()) {
				FetchResult resultChild = getUrlPath(node, nodes, lvl + 1);
				if (resultChild.isNonempty()) {
					List<String> resLst = resultChild.getResult();
					if (resLst != null) {
						resLst.add(0, curNode);
					} else {
						resLst = new ArrayList<String>();
						resLst.add(curNode);
						resultChild.setResult(resLst);
					}
					return resultChild;
				}
			}
		}
		return new FetchResult(false, null); //node not found
	}
}
