package eu.prechtel.bootcamp;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

	@Override
	public Map<String, Object> getErrorAttributes(ServerRequest request,
												  ErrorAttributeOptions options) {
		Map<String, Object> map = super.getErrorAttributes(request, options);
		map.put("status", HttpStatus.UNPROCESSABLE_ENTITY);
		map.put("message", "An unknown error occured");
		return map;
	}
}
