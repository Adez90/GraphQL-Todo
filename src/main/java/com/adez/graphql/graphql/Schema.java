package com.adez.graphql.graphql;

import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLObjectType.newObject;
import static graphql.schema.GraphQLSchema.newSchema;
import graphql.Scalars;
import graphql.relay.Relay;
import graphql.relay.SimpleListConnection;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLArgument;
import graphql.schema.GraphQLInterfaceType;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import graphql.schema.TypeResolverProxy;

import java.util.List;

import com.adez.graphql.model.dto.AddressDto;
import com.adez.graphql.model.dto.CompanyDto;
import com.adez.graphql.model.dto.DepartmentDto;
import com.adez.graphql.model.dto.TodoDto;
import com.adez.graphql.model.dto.UserDto;

public class Schema {
	private GraphQLSchema schema;
    
    //Types
    private GraphQLObjectType addressType;
    private GraphQLObjectType companyType;
    private GraphQLObjectType departmentType;
    private GraphQLObjectType userType;
    private GraphQLObjectType todoType;

    //User ConnectionTypes
    private GraphQLObjectType connectionFromUserToTodoList;
    
    //Company ConnectionTypes
    private GraphQLObjectType connectionFromCompanyToDepartmentList;
    
    //Department ConnectionType
    private GraphQLObjectType connectionFromDepartmentUserList;
    
    private GraphQLInterfaceType nodeInterface;
    
    //Edges
    private GraphQLObjectType addressEdge;
    private GraphQLObjectType companyEdge;
    private GraphQLObjectType departmentEdge;
    private GraphQLObjectType userEdge;
    private GraphQLObjectType todoEdge;
    
    //Lists for connections
    private List<DepartmentDto> departments;
    private List<UserDto> users;
    private List<TodoDto> todos;

    private UserDto activeUser;
    
  //User Connections
    private SimpleListConnection simpleConnectionFromUserToTodo;
    
    //Company Connection
    private SimpleListConnection simpleConnectionFromCompanyToDepartment;
    
    //Department Connection
    private SimpleListConnection simpleConnectionFromDepartmentToUser;

    private Relay relay = new Relay();

    public Schema() {
    	activeUser = getActiveUser("1");
        createSchema();
    }
    
    private void createSchema() {
        TypeResolverProxy typeResolverProxy = new TypeResolverProxy();
        nodeInterface = relay.nodeInterface(typeResolverProxy);
        
        simpleConnectionFromUserToTodo = new SimpleListConnection(todos);
        simpleConnectionFromDepartmentToUser = new SimpleListConnection(users);
        simpleConnectionFromCompanyToDepartment = new SimpleListConnection(departments);
        
        createAddressType();
        createTodoType();
        createConnectionFromUserToTodoList();
        createConnectionFromDepartmentToUserList();
        createDepartmentType();
        createConnectionFromCompanyToDepartmentList();
        createCompanyType();
        createUserType();

        typeResolverProxy.setTypeResolver(object -> {
            if (object instanceof UserDto) {
                return userType;
            }
            else if (object instanceof AddressDto) {
                return addressType;
            }
            else if (object instanceof CompanyDto) {
                return companyType;
            }
            else if (object instanceof DepartmentDto) {
                return departmentType;
            }
            else if (object instanceof TodoDto) {
                return todoType;
            }
            return null;
        });

        DataFetcher userDataFetcher = environment -> {
        	return activeUser;
        };
        
        GraphQLArgument id =
        	    GraphQLArgument
        	      .newArgument()
        	      .name("id")
        	      .type(Scalars.GraphQLID)
        	      .build();
        
        GraphQLObjectType QueryRoot = newObject()
                .name("Query")
                .field(newFieldDefinition()
                        .name("viewer")
                        .type(userType)
                        .argument(id)
                        .dataFetcher(userDataFetcher)
                        .build())
                .field(relay.nodeField(nodeInterface, userDataFetcher))
                .build();
        
        Mutations Mutations = new Mutations(this);

        GraphQLObjectType mutationType = newObject()
                .name("Mutation")
                .fields(Mutations.getFields())
                .build();

        schema = newSchema()
                .query(QueryRoot)
                .mutation(mutationType)
                .build();
    }
}
