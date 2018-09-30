package edu.self.web.i18n;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * A filter that populates request with a list of supported languages
 * @author Anatolii_Kryvda
 *
 */
public class SupportedLocales extends HandlerInterceptorAdapter {
	private Map<String, String> locales;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		request.setAttribute("locales", locales);
		return true;
	}
	
	public Map<String, String> getLocales() {
		return locales;
	}
	
	public void setLocales(Map<String, String> locales) {
		this.locales = locales;
	}
}
