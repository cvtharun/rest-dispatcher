package restdisp.urltree;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import restdisp.io.IOUtils;
import restdisp.validation.ConfigParser;
import restdisp.validation.ConfigurationException;
import restdisp.worker.AbstractWorker;

public class UrlTreeBuilder {
	private final Map<String, AbstractWorker> workerMap = new HashMap<String, AbstractWorker>();
	private final Map<String, Class<AbstractWorker>> workerClassMap = new HashMap<String, Class<AbstractWorker>>();
	
	public Node buildUrlTree(InputStream is) throws ConfigurationException {
		String confString = null;
		try {
			confString = IOUtils.toString(is);
		} catch (IOException e) {
			throw new ConfigurationException("Could not read stream", e);
		}
		String[] strEntries = ConfigParser.parse(confString);

		Node root = new Node("root", new ArrayList<Node>(), null, false);
		for (String entry : strEntries) {
			if (entry.length() > 0 && entry.charAt(0) != '#') {
				addEntryToTree(root, entry);
			}
		}
		return root;
	}
	
	private void addEntryToTree(Node root, String entry) throws ConfigurationException {
		String[] items = entry.split(" ");
		try {
			if (items.length != 3) {
				throw new ConfigurationException(String.format("Wrong configuration entry [%s]", entry));
			}
			ConfigParser.validateUrl(items[1]);
			ConfigParser.validateHttpMethod(items[0]);
			
			items[1] = String.format("/%s%s", items[0], items[1]);
			String[] nodes = items[1].replaceFirst("/", "").split("(?<=\\w|})/");
			
			String[] classAndMethodArr = items[2].split("\\:");
			if (classAndMethodArr.length != 2) {
				throw new ConfigurationException(String.format("Wrong class method entry [%s]", entry));
			}
		
			addBranchToTree(root, nodes, classAndMethodArr);
		} catch (ConfigurationException e) {
			throw new ConfigurationException(String.format("Failed to add branch [%s]", entry), e);
		}
	}

	private void addBranchToTree(Node root, String[] nodes, String[] classAndMethodArr) throws ConfigurationException {
		List<Node> children = root.getChildren();
		i: for (int i = 0; i < nodes.length; i++) {
			Node tmpNode = new Node(getVarname(nodes[i]));
			
			if (!children.contains(tmpNode)) {
				children.add(tmpNode);
				tmpNode.setVar(isVar(nodes[i]));
				children = new ArrayList<Node>();
				tmpNode.setChildren(children);
				if (i + 1 == nodes.length){
					tmpNode.setLeaf(buildLeaf(classAndMethodArr, nodes));
				}
			} else {
				for (Node node : children) {
					if (node.equals(tmpNode)) {
						if (i + 1 == nodes.length && node.getLeaf() == null) {
							node.setLeaf(buildLeaf(classAndMethodArr, nodes));
						}
						children = node.getChildren();
						continue i;
					}
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private Leaf buildLeaf(String[] classAndMethodArr, String[] nodes) throws ConfigurationException {
		String className = classAndMethodArr[0];
		try {
			AbstractWorker worker = workerMap.get(className);
			Class<AbstractWorker> cls = workerClassMap.get(className);
			
			if (worker == null) {
				cls = (Class<AbstractWorker>) Class.forName(className);
				Constructor<AbstractWorker> constructor = cls.getConstructor();
				worker = constructor.newInstance();
				workerMap.put(className, worker);
				workerClassMap.put(className, cls);
			}
			int varCnt = getVariableCount(nodes);
			Method meth = getMethod(cls, classAndMethodArr[1], varCnt);
			return new Leaf(cls, worker, meth, Casters.getCasters(meth));
		} catch (ClassNotFoundException e) {
			throw new ConfigurationException(String.format("Failed to build leaf. Class not found [%s].", classAndMethodArr[0]), e);
		} catch (NoSuchMethodException e) {
			throw new ConfigurationException(String.format("Failed to build leaf. Default constructor not found [%s].", classAndMethodArr[0]), e);
		} catch (InstantiationException e) {
			throw new ConfigurationException(String.format("Failed to instantiate worker [%s]", className), e);
		} catch (IllegalAccessException e) {
			throw new ConfigurationException(String.format("Illegal acces to worker [%s]",className), e);
		} catch (InvocationTargetException e) {
			throw new ConfigurationException(String.format("Constructor invocation exception [%s]", className), e);
		}
	}
	
	private int getVariableCount(String[] nodes) {
		int cnt = 0;
		for (String str : nodes) {
			if (str.charAt(0) == '{'){
				cnt++;
			}
		}
		return cnt;
	}
	
	private Method getMethod(Class<?> cls, String methodName, int varCnt) throws ConfigurationException {
		Method meth = null;
		Method[] mths = cls.getMethods();
		for (Method curMet : mths) {
			if (curMet.getName().equals(methodName) && curMet.getParameterTypes().length == varCnt) {
				meth = curMet;
				break;
			}
		}
		
		if (null == meth) {
			throw new ConfigurationException(String.format("Method not found [%s:%s]. Variables count [%s].", cls.toString(), methodName, varCnt));
		}
		
		try {
			ConfigParser.validateMethArgs(meth.getParameterTypes());
		} catch (ConfigurationException e) {
			throw new ConfigurationException(String.format("Class method has unsupported argument [%s.%s]", cls.getName(), methodName), e);
		}
		
		return meth;
	}

	private boolean isVar(String url) {
		return url.contains("{") || url.contains("}"); 
	}
	
	private String getVarname(String url) {
		return url.replaceAll("\\{|\\}", "");
	}
}
