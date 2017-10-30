package com.adez.graphql.configuration;

import com.auth0.client.auth.AuthAPI;
import com.auth0.exception.APIException;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.UserInfo;
import com.auth0.net.Request;

public class Auth0Client {
	private final String domain;
	private final String clientId;
	private final String clientSecret;
    private final AuthAPI auth0;

    public Auth0Client(String domain, String clientId, String clientSecret) {
        this.clientId = clientId;
        this.domain = domain;
        this.clientSecret = clientSecret;
        this.auth0 = new AuthAPI(clientId, domain, clientSecret);
    }

    public String getUsername(String token) {
    	Request<UserInfo> request = auth0.userInfo(token);
    	String email = "";
    	try {
    	    UserInfo info = request.execute();
    	    email = info.getValues().get("email").toString();
    	} catch (APIException exception) {
    		System.out.println(exception.toString());
    		return null;
    	} catch (Auth0Exception exception) {
    		System.out.println(exception.toString());
    		return null;
    	}
        return email;
    }
}
