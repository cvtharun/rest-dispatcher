# Java RESTful framework

rest-dispatcher is java framework for building RESTful web services. 
The project goal is to provide simple way to define REST services via single point configuration (routing) file. 

### Getting Started to build REST service
To build REST service using rest-dispatcher you need following:

1. Add restdisp.jar to classpath 
2. Implement custom handler by extending restdisp.worker.AbstractWorker:

		package example;		
		import java.io.IOException;		
		import restdisp.worker.AbstractWorker;		

		public class UserGateway extends AbstractWorker {		
			public void getUser(int id) throws IOException {		
				System.out.println("Requesting user: " + id);		
				setPayload("Mike");		
			}		

			public void saveUser(String name) throws IOException {		
				System.out.println("Saving user: " + name);		
				setPayload("User saved with id: 5");		
			}		
		}	
3. Define usergateway.config and add it to classpath:

		# usergateway.config		
		get        /srv/user/{id}        example.UserGateway:getUser		
		post       /srv/user/{id}        example.UserGateway:saveUser		
4. Add dispatcher servlet configuration to web.xml 

		<servlet>
		        <servlet-name>dispatcher</servlet-name>
		        <servlet-class>restdisp.DispatcherServlet</servlet-class>
		        <init-param>
		                <param-name>router.conf</param-name>
		                <param-value>usergateway.config</param-value>
		        </init-param>
		        <load-on-startup>1</load-on-startup>
		</servlet>
		<servlet-mapping>
		        <servlet-name>dispatcher</servlet-name>
		        <url-pattern>/srv/*</url-pattern>
		</servlet-mapping>