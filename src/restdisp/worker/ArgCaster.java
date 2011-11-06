package restdisp.worker;

import restdisp.validation.RoutingException;

public interface ArgCaster {
	Object cast(String str) throws RoutingException;
}
