package restdisp.worker;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import restdisp.io.IOUtils;

public abstract class AbstractWorker {
	public static final String DEF_ENCODING = "UTF-8";
	private final ThreadLocal<HttpServletRequest> request = new  ThreadLocal<HttpServletRequest>();
	private final ThreadLocal<HttpServletResponse> response = new  ThreadLocal<HttpServletResponse>();
	private final ThreadLocal<ServletContext> servletContext = new  ThreadLocal<ServletContext>();
	
	public String getPayload() {
		String encoding = request.get().getCharacterEncoding();
		encoding = encoding == null ? DEF_ENCODING : encoding;
		return getPayload(encoding);
	}
	
	public String getPayload(String encoding) {
		try {
			return IOUtils.toString(request.get().getInputStream(), encoding);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void setPayloadJson(Object object) {
		getResponse().setContentType("application/json");
		 ObjectMapper mapper = new ObjectMapper();
	         mapper.setSerializationInclusion(Inclusion.NON_NULL);
	         String str="";
		try {
			str = mapper.writeValueAsString(object);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	         setPayload(str, DEF_ENCODING);
	}
	
	public void setPayloadXml(Object object) {
		getResponse().setContentType("application/xml");
		String str=convertToXml(object, object.getClass());
		setPayload(str, DEF_ENCODING);
	}
	
	public void setPayload(String str) {
		setPayload(str, DEF_ENCODING);
	}
	
	public void setPayload(String str, String encoding) {
		try {
			getResponse().getOutputStream().write(str.getBytes(encoding));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private String convertToXml(Object source, Class... type) {
        String result;
        StringWriter sw = new StringWriter();
        try {
            JAXBContext carContext = JAXBContext.newInstance(type);
            Marshaller carMarshaller = carContext.createMarshaller();
            carMarshaller.marshal(source, sw);
            result = sw.toString();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        return result;
    	}
	
	public HttpServletRequest getRequest() {
		return request.get();
	}
	
	public HttpServletResponse getResponse() {
		return response.get();
	}
	
	public ServletContext getServletContext() {
		return servletContext.get();
	}

	void setRequest(HttpServletRequest request) {
		this.request.set(request);
	}

	void setResponse(HttpServletResponse response) {
		this.response.set(response);
	}
	
	void setServletContext(ServletContext servletContext) {
		this.servletContext.set(servletContext);
	}
}
