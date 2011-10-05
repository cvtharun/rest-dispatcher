package restdisp;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import restdisp.io.IOUtil;
import restdisp.urltree.UrlDescriptor;
import restdisp.urltree.LookupTree;
import restdisp.urltree.Node;
import restdisp.urltree.UrlTreeBuilder;
import restdisp.validation.ConfigurationException;
import restdisp.validation.HandlerException;
import restdisp.validation.RoutingException;
import restdisp.worker.TreeExecutor;

public class DispatcherServlet extends HttpServlet {
	private static final long serialVersionUID = -8158617186009563920L;
	private static Node urlTree;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String httpMethod = req.getMethod();
		String requestUrl = req.getRequestURI();
		
		UrlDescriptor urlDesc;
		try {
			urlDesc = LookupTree.getPath(urlTree, httpMethod, requestUrl);
			TreeExecutor.exec(urlDesc, req, resp);
		} catch (RoutingException e) {
			resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, String.format("Method not found [%s] [%s]", httpMethod, requestUrl));
		} catch (HandlerException e) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, IOUtil.getStackTrace(e));
		}
	}
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		final String configName = config.getInitParameter("router.conf");
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(configName);
		try {
			urlTree = UrlTreeBuilder.buildUrlTree(is);
		} catch (ConfigurationException e) {
			throw new ServletException("Wrong configuration", e);
		}
	}
}
