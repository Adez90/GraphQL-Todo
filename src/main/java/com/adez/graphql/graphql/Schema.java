package com.adez.graphql.graphql;

import static graphql.Scalars.GraphQLID;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLObjectType.newObject;
import static graphql.schema.GraphQLSchema.newSchema;
import graphql.Scalars;
import graphql.relay.Connection;
import graphql.relay.Relay;
import graphql.relay.SimpleListConnection;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLArgument;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLInterfaceType;
import graphql.schema.GraphQLNonNull;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import graphql.schema.GraphQLTypeReference;
import graphql.schema.TypeResolverProxy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.adez.graphql.model.dto.AddressDto;
import com.adez.graphql.model.dto.CompanyDto;
import com.adez.graphql.model.dto.DepartmentDto;
import com.adez.graphql.model.dto.TodoDto;
import com.adez.graphql.model.dto.UserDto;
import com.adez.graphql.service.AddressService;
import com.adez.graphql.service.CompanyService;
import com.adez.graphql.service.DepartmentService;
import com.adez.graphql.service.TodoService;
import com.adez.graphql.service.UserService;

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
    
    //Services for database connections
    private AddressService addressService = new AddressService();
    private CompanyService companyService = new CompanyService();
    private DepartmentService departmentService = new DepartmentService();
    private TodoService todoService = new TodoService();
    private UserService userService = new UserService();
    
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
    
    private void createAddressType(){
    	addressType = newObject()
                .name("Address")
                .field(newFieldDefinition()
                        .name("id")
                        .type(new GraphQLNonNull(GraphQLID))
                        .dataFetcher(environment -> {
                                    AddressDto address = (AddressDto) environment.getSource();
                                    return relay.toGlobalId("Address", address.getId().toString());
                                }
                        )
                        .build())
                .field(newFieldDefinition()
                        .name("streetAddress")
                        .type(Scalars.GraphQLString)
                        .build())
                .field(newFieldDefinition()
                        .name("zipCode")
                        .type(Scalars.GraphQLInt)
                        .build())
                .field(newFieldDefinition()
                        .name("city")
                        .type(Scalars.GraphQLString)
                        .build())
                .field(newFieldDefinition()
                        .name("country")
                        .type(Scalars.GraphQLString)
                        .build())
                .field(newFieldDefinition()
                        .name("countryCode")
                        .type(Scalars.GraphQLString)
                        .build())                        
                .withInterface(nodeInterface)
                .build();
    }
    
    private Object getDepartmentDataFetcher(DataFetchingEnvironment environment){
    	CompanyDto company = (CompanyDto) environment.getSource();
        departments = new ArrayList<DepartmentDto>();
    	if(activeUser.getDepartments() != null){
    		for(int i = 0; i < activeUser.getDepartments().size(); i++){
    			if(activeUser.getDepartments().get(i).getCompany().getId().equals(company.getId())){
    				departments.add(activeUser.getDepartments().get(i));
    			}
    		}
    	}
    	SimpleListConnection departmentDataFetcher = new SimpleListConnection(departments);
    	return departmentDataFetcher.get(environment);
    }
    
    private void createCompanyType(){
    	companyType = newObject()
                .name("Company")
                .field(newFieldDefinition()
                        .name("id")
                        .type(new GraphQLNonNull(GraphQLID))
                        .dataFetcher(environment -> {
                                    CompanyDto company = (CompanyDto) environment.getSource();
                                    return relay.toGlobalId("Company", company.getId().toString());
                                }
                        )
                        .build())
                .field(newFieldDefinition()
                        .name("companyName")
                        .type(Scalars.GraphQLString)
                        .build())
                .field(newFieldDefinition()
                        .name("companyAddress")
                        .type(addressType)
                        .build())
                .field(newFieldDefinition()
                        .name("companyHomePage")
                        .type(Scalars.GraphQLString)
                        .build())
                .field(newFieldDefinition()
                        .name("companyEmail")
                        .type(Scalars.GraphQLString)
                        .build())  
                .field(newFieldDefinition()
                        .name("companyPhoneNumber")
                        .type(Scalars.GraphQLString)
                        .build())
                .field(newFieldDefinition()
                        .name("organisationNumber")
                        .type(Scalars.GraphQLString)
                        .build())
                .field(newFieldDefinition()
                        .name("departmentList")
                        .type(connectionFromCompanyToDepartmentList)
                        .argument(relay.getConnectionFieldArguments())
                        .dataFetcher(environment -> {return getDepartmentDataFetcher(environment);
                        })
                        .build())
//                .field(newFieldDefinition()
//                        .name("company")
//                        .type(new GraphQLTypeReference("Company"))
//                        .build())
//                .field(newFieldDefinition()
//                        .name("affiliateList")
//                        .type(new GraphQLTypeReference("Company"))
//                        .build()) 
//                .field(newFieldDefinition()
//                        .name("companyImage")
//                        .type(Scalars.GraphQLString)
//                        .build())                         
                .withInterface(nodeInterface)
                .build();
    }
    
    private Object getLicensDataFetcher(DataFetchingEnvironment environment){
    	DepartmentDto department = (DepartmentDto) environment.getSource();
    	licenses = new ArrayList<LicensDto>();
    	if(activeUser.getActivatedLicensList() != null){
    		for(int i = 0; i < activeUser.getActivatedLicensList().size(); i++){
    			if(activeUser.getActivatedLicensList().get(i).getLicens().getDepartment().getId().equals(department.getId())){
    				licenses.add(activeUser.getActivatedLicensList().get(i).getLicens());
    			}
    		}
    	}
    	SimpleListConnection licensDataFetcher = new SimpleListConnection(licenses);
    	return licensDataFetcher.get(environment);
    }
    
    private void createDepartmentType(){
    	departmentType = newObject()
                .name("Department")
                .field(newFieldDefinition()
                        .name("id")
                        .type(new GraphQLNonNull(GraphQLID))
                        .dataFetcher(environment -> {
                                    DepartmentDto department = (DepartmentDto) environment.getSource();
                                    return relay.toGlobalId("Company", department.getId().toString());
                                }
                        )
                        .build())
                .field(newFieldDefinition()
                        .name("company")
                        .type(new GraphQLTypeReference("Company"))
                        .build())
                .field(newFieldDefinition()
                        .name("departmentName")
                        .type(Scalars.GraphQLString)
                        .build())
                .field(newFieldDefinition()
                        .name("departmentEmail")
                        .type(Scalars.GraphQLString)
                        .build())
                .field(newFieldDefinition()
                        .name("departmentHomePage")
                        .type(Scalars.GraphQLString)
                        .build())
                .field(newFieldDefinition()
                        .name("departmentPhonenNmber")
                        .type(Scalars.GraphQLString)
                        .build())
                .field(newFieldDefinition()
                        .name("departmentAddress")
                        .type(addressType)
                        .build())
                .field(newFieldDefinition()
                        .name("licensList")
                        .type(connectionFromDepartmentToLicensList)
                        .argument(relay.getConnectionFieldArguments())
                        .dataFetcher(environment -> {return getLicensDataFetcher(environment);
                        })
                        .build())
                .field(newFieldDefinition()
                        .name("departmentImage")
                        .type(Scalars.GraphQLString)
                        .build())
                .withInterface(nodeInterface)
                .build();
    }
    
    private Object getTodoDataFetcher(DataFetchingEnvironment environment){
    	events = new ArrayList<EventDto>();
    	if(eventService.getByUser(activeUser) != null){
	    	if(environment.getArgument("fromDate") != null && environment.getArgument("toDate") != null){
	    		String fromDate = environment.getArgument("fromDate");
	    		String toDate = environment.getArgument("toDate");
	    		if(eventService.getByDate(activeUser, fromDate, toDate) != null)
	    			events = eventService.getByDate(activeUser, fromDate, toDate);
	    	}
	    	else if(environment.getArgument("fromDate") != null){
	    		String fromDate = environment.getArgument("fromDate");
	    		if(eventService.getFromDate(activeUser, fromDate) != null)
	    			events = eventService.getFromDate(activeUser, fromDate);
	    	}
	    	else if(environment.getArgument("toDate") != null){
	    		String toDate = environment.getArgument("toDate");
	    		if(eventService.getToDate(activeUser, toDate) != null)
	    			events = eventService.getToDate(activeUser, toDate);
	    	}
	    	else{
	    		if(eventService.getByUser(activeUser) != null)
	    			events = eventService.getByUser(activeUser);
	    	}
    	}
    	
    	SimpleListConnection eventDataFetcher = new SimpleListConnection(events);
    	return eventDataFetcher.get(environment);
    }
    
    private void createUserType() {
    	userType = newObject()
                .name("User")
                .field(newFieldDefinition()
                        .name("id")
                        .type(new GraphQLNonNull(GraphQLID))
                        .dataFetcher(environment -> {
                                    UserDto user = (UserDto) environment.getSource();
                                    if(user.getFirstName() == null){
                                    	UserDto noUser = getActiveUser("1");
                                    	return relay.toGlobalId("User", noUser.getId().toString());
                                    }
                                    else{
                                    	return relay.toGlobalId("User", user.getId().toString());
                                    }
                                }
                        )
                        .build())
                .field(newFieldDefinition()
                        .name("email")
                        .type(Scalars.GraphQLString)
                        .build())
                .field(newFieldDefinition()
                        .name("password")
                        .type(Scalars.GraphQLString)
                        .build())
                .field(newFieldDefinition()
                        .name("firstName")
                        .type(Scalars.GraphQLString)
                        .build())
                .field(newFieldDefinition()
                        .name("lastName")
                        .type(Scalars.GraphQLString)
                        .build())
                 .field(newFieldDefinition()
                        .name("homepage")
                        .type(Scalars.GraphQLString)
                        .build())
                .field(newFieldDefinition()
                        .name("phonenumber")
                        .type(Scalars.GraphQLString)
                        .build())
                .field(newFieldDefinition()
                        .name("address")
                        .type(addressType)
                        .build())
                .field(newFieldDefinition()
                        .name("token")
                        .type(Scalars.GraphQLString)
                        .build())
                .field(newFieldDefinition()
                        .name("authenticated")
                        .type(Scalars.GraphQLBoolean)
                        .build())
                .field(newFieldDefinition()
                        .name("authorities")
                        .type(Scalars.GraphQLString)
                        .build())
                .field(newFieldDefinition()
                        .name("goalList")
                        .type(Scalars.GraphQLString)
                        .build())                        
                .field(newFieldDefinition()
                        .name("language")
                        .type(Scalars.GraphQLString)
                        .build())
                .field(newFieldDefinition()
                		.name("imageList")
                        .type(connectionFromUserToImageList)
                        .argument(relay.getConnectionFieldArguments())
                        .dataFetcher(environment -> {return getImageDataFetcher(environment);
                        })
                        .build())
                .field(newFieldDefinition()
                        .name("eventList")
                        .type(connectionFromUserToEventList)
                        .argument(createEventArguemntList())
                        .dataFetcher(environment -> {return getEventDataFetcher(environment);
                        })
                        .build())
                .field(newFieldDefinition()
                        .name("resultList")
                        .type(connectionFromUserToResultList)
                        .argument(createResultArguemntList())
                        .dataFetcher(environment -> {return getResultDataFetcher(environment);
                        })
                        .build())
                .field(newFieldDefinition()
                        .name("companyList")
                        .type(connectionFromUserToCompanyList)
                        .argument(relay.getConnectionFieldArguments())
                        .dataFetcher(environment -> {return getCompanyDataFetcher(environment);
                        })
                        .build())
                .field(newFieldDefinition()
                        .name("activatedLicensList")
                        .type(connectionFromUserToActivatedLicensList)
                        .argument(relay.getConnectionFieldArguments())
                        .dataFetcher(environment -> {return getActivatedLicensDataFetcher(environment);
                        })
                        .build())
                .field(newFieldDefinition()
                        .name("productList")
                        .type(connectionFromUserToProductList)
                        .argument(relay.getConnectionFieldArguments())
                        .dataFetcher(environment -> {return getProductDataFetcher(environment);
                        })
                        .build())
                .withInterface(nodeInterface)
                .build();
    }
    
    private List <GraphQLArgument> createTodoArguemntList(){
	    List <GraphQLArgument> arguemntList = relay.getConnectionFieldArguments();
	    arguemntList.add(new GraphQLArgument("fromDate", "fromDate", Scalars.GraphQLString, "all"));
	    arguemntList.add(new GraphQLArgument("toDate", "toDate", Scalars.GraphQLString, "all"));
	    return arguemntList;
    }
    
    private List <GraphQLArgument> createTodoArguemntList(){
	    List <GraphQLArgument> arguemntList = relay.getConnectionFieldArguments();
	    arguemntList.add(new GraphQLArgument("fromDate", "fromDate", Scalars.GraphQLString, "all"));
	    arguemntList.add(new GraphQLArgument("toDate", "toDate", Scalars.GraphQLString, "all"));
	    return arguemntList;
    }

    private void createTodoType() {
        eventType = newObject()
                .name("Event")
                .field(newFieldDefinition()
                        .name("id")
                        .type(new GraphQLNonNull(GraphQLID))
                        .dataFetcher(environment -> {
                                    EventDto event = (EventDto) environment.getSource();
                                    return relay.toGlobalId("Event", event.getId().toString());
                                }
                        )
                        .build())
                .field(newFieldDefinition()
                        .name("user")
                        .type(new GraphQLTypeReference("User"))
                        .build())
                .field(newFieldDefinition()
                        .name("category")
                        .type(categoryType)
                        .build())
                .field(newFieldDefinition()
                        .name("motivation")
                        .type(Scalars.GraphQLFloat)
                        .build())
                .field(newFieldDefinition()
                        .name("reaction")
                        .type(Scalars.GraphQLFloat)
                        .build())
                .field(newFieldDefinition()
                        .name("result")
                        .type(Scalars.GraphQLFloat)
                        .build())
                .field(newFieldDefinition()
                        .name("createDate")
                        .type(Scalars.GraphQLString)
                        .build())
                .field(newFieldDefinition()
                        .name("finishDate")
                        .type(Scalars.GraphQLString)
                        .build())
                .field(newFieldDefinition()
                        .name("createNote")
                        .type(Scalars.GraphQLString)
                        .build())
                .field(newFieldDefinition()
                        .name("finishNote")
                        .type(Scalars.GraphQLString)
                        .build())
                 .field(newFieldDefinition()
                        .name("isFinished")
                        .type(Scalars.GraphQLBoolean)
                        .build())
                .field(newFieldDefinition()
                        .name("tagList")
                        .type(Scalars.GraphQLString)
                        .build())
                .field(newFieldDefinition()
                        .name("unitList")
                        .type(Scalars.GraphQLString)
                        .build())
                .field(newFieldDefinition()
                        .name("ImageList")
                        .type(Scalars.GraphQLString)
                        .build()) 
                .field(newFieldDefinition()
                        .name("unplanned")
                        .type(Scalars.GraphQLBoolean)
                        .build())
                .withInterface(nodeInterface)
                .build();
    }
    
    private void createConnectionFromUserToTodoList() {
        eventEdge = relay.edgeType("Event", eventType, nodeInterface, Collections.<GraphQLFieldDefinition>emptyList());
        GraphQLFieldDefinition completed = newFieldDefinition()
                .name("completed")
                .type(Scalars.GraphQLBoolean)
                .dataFetcher(environment -> {
                    Connection connection = (Connection) environment.getSource();
                    return (int) connection.getEdges().stream().filter(edge -> ((EventDto) edge.getNode()).isFinished()).count();
                })
                .build();
        GraphQLFieldDefinition all = newFieldDefinition()
                .name("all")
                .type(Scalars.GraphQLInt)
                .dataFetcher(environment -> {
                    Connection connection = (Connection) environment.getSource();
                    return (int) connection.getEdges().size();
                })
                .build();
        connectionFromUserToEventList = relay.connectionType("Event", eventEdge, Arrays.asList(completed, all));
    }
    
    private void createConnectionFromCompanyToDepartmentList() {
        departmentEdge = relay.edgeType("Department", departmentType, nodeInterface, Collections.<GraphQLFieldDefinition>emptyList());
        GraphQLFieldDefinition all = newFieldDefinition()
                .name("all")
                .type(Scalars.GraphQLInt)
                .dataFetcher(environment -> {
                    Connection connection = (Connection) environment.getSource();
                    return (int) connection.getEdges().size();
                })
                .build();
        connectionFromCompanyToDepartmentList = relay.connectionType("Department", departmentEdge, Arrays.asList(all));
    }
    
    private void createConnectionFromDepartmentToUserList() {
        departmentEdge = relay.edgeType("Department", departmentType, nodeInterface, Collections.<GraphQLFieldDefinition>emptyList());
        GraphQLFieldDefinition all = newFieldDefinition()
                .name("all")
                .type(Scalars.GraphQLInt)
                .dataFetcher(environment -> {
                    Connection connection = (Connection) environment.getSource();
                    return (int) connection.getEdges().size();
                })
                .build();
        connectionFromCompanyToDepartmentList = relay.connectionType("Department", departmentEdge, Arrays.asList(all));
    }
    
    public SimpleListConnection getSimpleConnectionFromUserToTodo() {
        return new SimpleListConnection(eventService.getByUser(activeUser));
    }
    
    public SimpleListConnection getSimpleConnectionFromCompanyToDepartment() {
        return new SimpleListConnection(activeUser.getDepartments());
    }
    
    public SimpleListConnection getSimpleConnectionFromDepartmentToUser() {
        return new SimpleListConnection(activeUser.getDepartments());
    }
    
    public GraphQLObjectType getAddressType() {
    	return addressType;
    }
    
    public GraphQLObjectType getCompanyType() {
    	return companyType;
    }
    
    public GraphQLObjectType getDepartmentType() {
    	return departmentType;
    }

    public GraphQLObjectType getUserType() {
        return userType;
    }

    public GraphQLObjectType getTodoType() {
        return todoType;
    }
    
    public GraphQLObjectType getAddressEdge() {
    	return addressEdge;
    }
    
    public GraphQLObjectType getCompanyEdge() {
    	return companyEdge;
    }
    
    public GraphQLObjectType getDepartmentEdge() {
    	return departmentEdge;
    }

    public GraphQLObjectType getUserEdge() {
        return userEdge;
    }
    
    public GraphQLObjectType getTodoEdge() {
        return todoEdge;
    }
    
    public Relay getRelay() {
        return relay;
    }
    
    public UserDto getActiveUser(String id) {
    	if(userService.getById(Long.parseLong(id)) != null){
    		activeUser = userService.getById(Long.parseLong(id));
    	}
    	
    	else{
    		activeUser = userService.getById(Long.parseLong("1"));
    	}
    	
        return activeUser;
    }
    
    public UserDto getActiveUserByEmail(String email) {
    	if(userService.getByEmail(email) != null){
    		activeUser = userService.getByEmail(email);
    	}
    	
    	else{
    		activeUser = userService.getById(Long.parseLong("1"));
    	}
    	
        return activeUser;
    }
    
    public CompanyDto getCompany(String id) {
    	return companyService.getById(Long.parseLong(id));
    }
    
    public DepartmentDto getDepartment(String id) {
    	return departmentService.getById(Long.parseLong(id));
    }
    
    public UserDto getUser(String id) {
        return userService.getById(Long.parseLong(id));
    }
    
    public TodoDto getTodo(String id) {
        return todoService.getById(Long.parseLong(id));
    }
    
    public List<TodoDto> getTodoList() {
    	todos = todoService.getByUser(activeUser);
        return todos;
    }
    
    public void setTodoList(){
		todos = new ArrayList<TodoDto>();
	}
    
    public void setTodoList(String userName){
		todos = todoService.getByUser(activeUser);
	}
    
    public GraphQLSchema getSchema() {
        return schema;
    }
}