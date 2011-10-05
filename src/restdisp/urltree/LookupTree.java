package restdisp.urltree;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import restdisp.validation.RoutingException;

public class LookupTree {
	public static UrlDescriptor getPath(Node root, String httpMethod, String url) throws RoutingException {
		url = "/" + httpMethod + url;
		String[] nodes = url.replaceFirst("/", "").split("(?<=\\w)/");
		
		FetchResult desc = getUrlPath(root, nodes);

		if (null == desc.getLeaf()) {
			throw new RoutingException(String.format("Wrong url requested [%s]", url));
		}
		
		return new UrlDescriptor(desc.getResult(), desc.getLeaf());
	}

	private static class FetchResult {
		private boolean nonempty;
		Map<String, String> result;
		Leaf leaf;
		
		FetchResult(boolean nonempty, Map<String, String> result, Leaf leaf) {
			this.nonempty = nonempty;
			this.result = result;
			this.leaf = leaf;
		}
		FetchResult(boolean nonempty, Map<String, String> result) {
			this(nonempty, result, null);
		}
		public boolean isNonempty() {
			return nonempty;
		}
		public Map<String, String> getResult() {
			return result;
		}
		public Leaf getLeaf() {
			return leaf;
		}
		public void setNonempty(boolean nonempty) {
			this.nonempty = nonempty;
		}
		public void setResult(Map<String, String> result) {
			this.result = result;
		}
		public void setLeaf(Leaf leaf) {
			this.leaf = leaf;
		}
	}
	
	private static FetchResult getUrlPath(Node root, String[] nodes) {
		if (nodes.length == 0) { // reached the leaf
			return new FetchResult(true, null, root.getLeaf());
		}
		
		String curNode = nodes[0];
		List<Node> children = root.getChildren();
		
		FetchResult result = null;
		for (Node node : children) {
			if (node.getName().equalsIgnoreCase(curNode) && !node.isVar()) {
				result = getUrlPath(node, Arrays.copyOfRange(nodes, 1, nodes.length));
				if (result.isNonempty()) {
					return result;
				}
			} else if (node.isVar()) {
				FetchResult resultChild = getUrlPath(node, Arrays.copyOfRange(nodes, 1, nodes.length));
				if (resultChild.isNonempty()) {
					Map<String, String> resMap = new LinkedHashMap<String, String>();
					resMap.put(node.getName(), curNode);
					
					if (resultChild.getResult() != null) {
						resMap.putAll(resultChild.getResult());
					}
					resultChild.setNonempty(true);
					resultChild.setResult(resMap);
					resultChild.setLeaf(resultChild.getLeaf());
					return resultChild;
				}
			}
		}
		return new FetchResult(false, null); //node not found
	}
}
