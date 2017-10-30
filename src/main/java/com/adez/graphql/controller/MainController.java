package com.adez.graphql.controller;

import graphql.ExecutionResult;
import graphql.GraphQL;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.adez.graphql.configuration.Auth0Client;
import com.adez.graphql.graphql.Schema;
import com.adez.graphql.service.UserService;

@Controller
@Component
public class MainController {
	@Autowired
    private Auth0Client auth0Client;
	
	@Autowired
	UserService userService;
	
    Schema schema = new Schema();
    GraphQL graphql = GraphQL.newGraphQL(schema.getSchema()).build();

    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    @RequestMapping(value = "/graphql", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object executeOperation(@RequestBody Map body) { 	
        String query = (String) body.get("query");
        ExecutionResult executionResult;
        Map<String, Object> variables = (Map<String, Object>) body.get("variables");
        executionResult = graphql.execute(query, (Object) null, variables);
        Map<String, Object> result = new LinkedHashMap<>();
        if (executionResult.getErrors().size() > 0) {
            result.put("errors", executionResult.getErrors());
            log.error("Errors: {}", executionResult.getErrors());
        }
        result.put("data", executionResult.getData());
        return result;
    }
    
    @RequestMapping(value = "/getSchema", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object getSchema(@RequestBody Map body) {
        String query = (String) body.get("query");
        ExecutionResult executionResult;
        Map<String, Object> variables = (Map<String, Object>) body.get("variables");
        executionResult = graphql.execute(query, (Object) null, variables);
        Map<String, Object> result = new LinkedHashMap<>();
        if (executionResult.getErrors().size() > 0) {
            result.put("errors", executionResult.getErrors());
            log.error("Errors: {}", executionResult.getErrors());
        }
        result.put("data", executionResult.getData());
        return result;
    }
}
