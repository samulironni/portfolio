package filter;

import java.io.IOException;
import java.util.Hashtable;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;

// Add these pom.xml
//<!-- https://mvnrepository.com/artifact/commons-codec/commons-codec -->
//<dependency>
//  <groupId>commons-codec</groupId>
//  <artifactId>commons-codec</artifactId>
//  <version>1.15</version>
//</dependency>   


/**
 * Servlet Filter implementation class AuthFilter
 */
@WebFilter(urlPatterns = {"/*"})//Which URI's come through this filter
//@WebFilter(dispatcherTypes = {
//		DispatcherType.REQUEST, 
//		DispatcherType.FORWARD, 
//		DispatcherType.INCLUDE, 
//		DispatcherType.ERROR
//}
//, urlPatterns = {"/*"})//Which URI's come through this filter
public class AuthFilter implements Filter {
	/*
	 * Attribute valiUsers, where the credentials of the valid users are added.
	 * This is used in this example, but a better way were to use database
	 */
    Hashtable<String, String> validUsers = new Hashtable<>();
    
    public AuthFilter() {
    }

	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		doFilter((HttpServletRequest)request, (HttpServletResponse)response, chain);
	}
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		/*
		 * Getting the Authorization string from the request header.
		 * It looks like: Basic aGtqaGtqaGtqOg==
		 * Starting with Basic and the crypted part is crypted version of 
		 * pattern someuser:somepassword
		 */
        String auth = request.getHeader("Authorization");

        // Check if the user is allowed?
        if (!allowUser(auth)) {
            // The client (browser) is not allowed, so report the situation to the browser
            response.setHeader("WWW-Authenticate", "BASIC realm=\"Robot secret app\"");
            response.sendError(response.SC_UNAUTHORIZED);
        } else {
            // The client is allowed to forward the request to the URI "/sercretservlet"
    		chain.doFilter(request, response);
        }
	}
	public void init(FilterConfig fConfig) throws ServletException {
	}

	/*
	 * Adding some users to the HashTable validUsers
	 * This could be done in the init method
	 * And... the users might be got from a database
	 */
	public void getUser() {
        validUsers.put("samppa:kukkuu","authorized");
        validUsers.put("rony:kukkuu","authorized");
        validUsers.put("mikael:kukkuu","authorized");
        validUsers.put("samuli:kukkuu","authorized");
	}

    protected boolean allowUser(String auth) throws IOException {
        getUser();//calling the getUser method
        //In the first place the auth parameter is null
        if (auth == null) {
            return false;  
        }
        //If the authentication method is not BASIC
        if (!auth.toUpperCase().startsWith("BASIC ")) { 
            return false;
        }
        /*
         *  Getting the encoded (crypted) username and password from the variable auth
         */
        String userpassEncoded = auth.substring(6);
        /* 
         * Decode (decrypt) it, using any base 64 decoder
         * This example uses Base64 - see the dependency added to the pom.xml file
         * Here we get a readable version of the username:password string
         */
        Base64 base64 = new Base64();
        String userpassDecoded = new String(base64.decode(userpassEncoded.getBytes()));
        
        // Check our user list to see if that user and password are "allowed"
        /*
         * Here we get value from the HashTable validUsers by key userpassDecoded,
         * which is of form 'username:password'. If the value got is 'authorized'
         * the client will be authorized. 
         */
        if ("authorized".equals(validUsers.get(userpassDecoded))) {
            return true;
        } else {
            return false;
        }
    }
}
