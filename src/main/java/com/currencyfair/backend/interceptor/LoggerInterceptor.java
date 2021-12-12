package com.currencyfair.backend.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class LoggerInterceptor implements HandlerInterceptor {
	
	public static final String LOGIN_USER = "login_user";
	public static final Logger logger = LoggerFactory.getLogger(LoggerInterceptor.class);
    /**
     * Executed before actual handler is executed
     **/
    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
    	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	String loggedInUser;
    	
    	if (principal instanceof UserDetails) {
    		loggedInUser = ((UserDetails)principal).getUsername();
    	} else {
    		loggedInUser = principal.toString();
    	}
    	
    	if(!ObjectUtils.isEmpty(loggedInUser)) {
        	MDC.put(LOGIN_USER, loggedInUser);
    	}
    	long startTime = System.currentTimeMillis();
		request.setAttribute("startTime", startTime); 
    	
		logger.info("[preHandle][" + request.getMethod() + "]" + request.getRequestURI() + " [requestUser: " + MDC.get(LOGIN_USER) + "]");
        return true;
    }

    /**
     * Executed before after handler is executed
     **/
    @Override
    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final ModelAndView modelAndView) throws Exception {
       
    }

    /**
     * Executed after complete request is finished
     **/
    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception ex) throws Exception {     
   
    	long startTime = (Long)request.getAttribute("startTime");
		long endTime = System.currentTimeMillis();
		long executeTime = endTime - startTime;
			
		StringBuffer result = new StringBuffer(100);
		result.append("[postHandle][" + request.getMethod() + "]" + request.getRequestURI()+ " [executeTime : " + executeTime + "ms]" + " [requestUser: " + MDC.get(LOGIN_USER) + "]");
		
		if (ex != null) {
			result.append(" [exception: " + ex.getMessage() + "]");
		}
		logger.info(result.toString());
		MDC.remove(LOGIN_USER);		  	
    }
	
}