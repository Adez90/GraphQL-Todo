package com.adez.graphql.graphql;

import static graphql.Scalars.GraphQLID;
import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLInputObjectField.newInputObjectField;
import graphql.Scalars;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLInputObjectField;
import graphql.schema.GraphQLNonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.adez.graphql.model.dto.AddressDto;
import com.adez.graphql.model.dto.CompanyDto;
import com.adez.graphql.model.dto.DepartmentDto;
import com.adez.graphql.model.dto.UserDto;

public class Mutations {
	private GraphQLFieldDefinition createAddress;
	private GraphQLFieldDefinition createCompany;
	private GraphQLFieldDefinition createDepartment;
	private GraphQLFieldDefinition createTodo;
	private GraphQLFieldDefinition createUser;
	
	private GraphQLFieldDefinition registration;
	
	private Schema schema;
	
	public Mutations(Schema schema) {
        this.schema = schema;

        createAddressMutation();
        createCompanyMutation(); 
        createDepartmentMutation();     
        createTodoMutation();
        createUserMutation();
        
        registrationMutation();
    }
	
	private GraphQLFieldDefinition getViewerField() {
        GraphQLFieldDefinition viewer = newFieldDefinition()
                .name("viewer")
                .type(schema.getUserType())
                .staticValue(schema.getActiveUser("1"))
                .build();
        return viewer;
    }
	
	public List<GraphQLFieldDefinition> getFields() {
        return Arrays.asList(createAddress, createCompany, createDepartment, createUser, createTodo, registration);
    }
	
	private void createAddressMutation() {
    	GraphQLInputObjectField streetAddressField = newInputObjectField()
                .name("streetAddress")
                .type(Scalars.GraphQLString)
                .build();
        
        GraphQLInputObjectField zipCodeField = newInputObjectField()
                .name("zipCode")
                .type(Scalars.GraphQLInt)
                .build();
        
        GraphQLInputObjectField cityField = newInputObjectField()
                .name("city")
                .type(Scalars.GraphQLString)
                .build();
        
        GraphQLInputObjectField countryField = newInputObjectField()
                .name("country")
                .type(Scalars.GraphQLString)
                .build();

        List<GraphQLInputObjectField> inputFields = Arrays.asList(streetAddressField, zipCodeField, cityField, countryField);

        List<GraphQLFieldDefinition> outputFields = Arrays.asList(getViewerField());

        DataFetcher mutate = environment -> {
            Map<String, Object> input = environment.getArgument("input");
            String streetAddress = (String) input.get("streetAddress");
            Integer zipCode = (Integer) input.get("zipCode");
            String city = (String) input.get("city");
            String country = (String) input.get("country");
            if(streetAddress == null || streetAddress.isEmpty() || zipCode == null || city == null || city.isEmpty() || country == null || country.isEmpty()){
            	String fields = "";
            	if(streetAddress == null || streetAddress.isEmpty()){
            		fields = "streetAddress";
            	}
            	if(zipCode == null){
            		if(fields.isEmpty())
            			fields = "zipCode";
            		else
            			fields = fields + ", zipCode";
            	}
            	if(city == null || city.isEmpty()){
            		if(fields.isEmpty())
            			fields = "city";
            		else
            			fields = fields + ", city";
            	}
            	if(country == null || country.isEmpty()){
            		if(fields.isEmpty())
            			fields = "country";
            		else
            			fields= fields+ ", country";
            	}
            	throw new Error("Requiered fields not filled: pleas note that the following fields have to be entered correctly - " + fields);
            }
            AddressDto address = new AddressDto(streetAddress, zipCode, city, country);
            AddressDto newAddress = addressService.saveAddress(address);
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("clientMutationId", (String) input.get("clientMutationId"));
            result.put("addressId", newAddress.getId());
            return result;
        };

        createAddress = schema.getRelay().mutationWithClientMutationId("CreateAddress", "createAddress", inputFields, outputFields, mutate);
    }
	
	private void createCompanyMutation() {
    	GraphQLInputObjectField companyNameField = newInputObjectField()
                .name("companyName")
                .type(Scalars.GraphQLString)
                .build();
    	
    	GraphQLInputObjectField companyHomePageField = newInputObjectField()
                .name("companyHomePage")
                .type(Scalars.GraphQLString)
                .build();
        
        GraphQLInputObjectField companyEmailField = newInputObjectField()
                .name("companyEmail")
                .type(Scalars.GraphQLString)
                .build();
        
        GraphQLInputObjectField companyPhoneNumberField = newInputObjectField()
                .name("companyPhoneNumber")
                .type(Scalars.GraphQLString)
                .build();
        
        GraphQLInputObjectField organisationNumberField = newInputObjectField()
                .name("companyOrganisationNumber")
                .type(Scalars.GraphQLString)
                .build();
        
        GraphQLInputObjectField cityField = newInputObjectField()
                .name("companyCity")
                .type(Scalars.GraphQLString)
                .build();
        
        GraphQLInputObjectField countryField = newInputObjectField()
                .name("companyCountry")
                .type(Scalars.GraphQLString)
                .build();
        
        GraphQLInputObjectField streetAddressField = newInputObjectField()
                .name("companyStreetAddress")
                .type(Scalars.GraphQLString)
                .build();
        
        GraphQLInputObjectField zipCodeField = newInputObjectField()
                .name("companyZipCode")
                .type(Scalars.GraphQLInt)
                .build();
        
        GraphQLInputObjectField userIdField = newInputObjectField()
                .name("userId")
                .type(new GraphQLNonNull(GraphQLID))
                .build();

        List<GraphQLInputObjectField> inputFields = Arrays.asList(companyNameField, companyHomePageField, companyEmailField, companyPhoneNumberField, organisationNumberField, cityField, countryField, streetAddressField, zipCodeField, userIdField);
        
        GraphQLFieldDefinition companyEdge = newFieldDefinition()
                .name("companyEdge")
                .type(schema.getCompanyEdge())
                .dataFetcher(environment -> {
                    Map source = (Map) environment.getSource();
                    String companyId = (String) source.get("companyId");
                    CompanyDto company = schema.getCompany(companyId);
                    return new Edge(company, schema.getSimpleConnectionFromUserToCompany().cursorForObjectInConnection(company));
                })
                .build();

        List<GraphQLFieldDefinition> outputFields = Arrays.asList(companyEdge, getViewerField());

        DataFetcher mutate = environment -> {
            Map<String, Object> input = environment.getArgument("input");
            String companyName = (String) input.get("companyName");
            String homePage = (String) input.get("companyHomePage");
            String email = (String) input.get("companyEmail");
            String phoneNumber = (String) input.get("companyPhoneNumber");
            String organisationNumber = (String) input.get("companyOrganisationNumber");
            String city = (String) input.get("companyCity");
            String country = (String) input.get("companyCountry");
            String streetAddress = (String) input.get("companyStreetAddress");
            Integer zipCode = (Integer) input.get("companyZipCode");
            String graphQLUserId = (String) input.get("userId");
            String userId = schema.getRelay().fromGlobalId(graphQLUserId).getId();
            UserDto user = schema.getActiveUser(userId);
            
            if(companyName == null || companyName.isEmpty() || organisationNumber == null || organisationNumber.isEmpty()){
            	String fields = "";
            	if(companyName == null || companyName.isEmpty())
            		fields = "comapnyName";
            	if(organisationNumber == null || organisationNumber.isEmpty()){
            		if(fields.isEmpty())
            			fields = "organisationNumber";
            		else
            			fields = fields + ", organisationNumber";
            	}
            	throw new Error("Requiered fields not filled for company: pleas note that the following fields have to be entered correctly - " + fields);
            }
            
            if(streetAddress == null || streetAddress.isEmpty() || zipCode == null || city == null || city.isEmpty() || country == null || country.isEmpty()){
            	String fields = "";
            	if(streetAddress == null || streetAddress.isEmpty()){
            		fields = "streetAddress";
            	}
            	if(zipCode == null){
            		if(fields.isEmpty())
            			fields = "zipCode";
            		else
            			fields = fields + ", zipCode";
            	}
            	if(city == null || city.isEmpty()){
            		if(fields.isEmpty())
            			fields = "city";
            		else
            			fields = fields + ", city";
            	}
            	if(country == null || country.isEmpty()){
            		if(fields.isEmpty())
            			fields = "country";
            		else
            			fields= fields+ ", country";
            	}
            	throw new Error("Requiered fields not filled for address: pleas note that the following fields have to be entered correctly - " + fields);
            }
            
            //Creating and saving Objects
            AddressDto address = new AddressDto(streetAddress, zipCode, city, country, country.substring(0, 3));
            CompanyDto company = new CompanyDto(companyName, organisationNumber, address, email, phoneNumber, homePage);
            company = companyService.save(company);
            
            String companyId = String.valueOf(company.getId());
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("clientMutationId", (String) input.get("clientMutationId"));
            result.put("companyId", companyId);
            return result;
        };

        createCompany = schema.getRelay().mutationWithClientMutationId("CreateCompany", "createCompany", inputFields, outputFields, mutate);
    }
	
	private void createDepartmentMutation() {
    	GraphQLInputObjectField departmentNameField = newInputObjectField()
                .name("departmentName")
                .type(Scalars.GraphQLString)
                .build();
        
        GraphQLInputObjectField departmentEmailField = newInputObjectField()
                .name("departmentEmail")
                .type(Scalars.GraphQLString)
                .build();
        
        GraphQLInputObjectField departmentHomePageField = newInputObjectField()
                .name("departmentHomePage")
                .type(Scalars.GraphQLString)
                .build();
        
        GraphQLInputObjectField departmentPhoneNumberField = newInputObjectField()
                .name("departmentPhoneNumber")
                .type(Scalars.GraphQLString)
                .build();
        
        GraphQLInputObjectField departmentStreetAddressField = newInputObjectField()
                .name("departmentStreetAddress")
                .type(Scalars.GraphQLString)
                .build();
        
        GraphQLInputObjectField departmentZipCodeField = newInputObjectField()
                .name("departmentZipCode")
                .type(Scalars.GraphQLInt)
                .build();
        
        GraphQLInputObjectField departmentCityField = newInputObjectField()
                .name("departmentCity")
                .type(Scalars.GraphQLString)
                .build();
        
        GraphQLInputObjectField departmentCountryField = newInputObjectField()
                .name("departmentCountry")
                .type(Scalars.GraphQLString)
                .build();
        
        GraphQLInputObjectField userIdField = newInputObjectField()
                .name("userId")
                .type(new GraphQLNonNull(GraphQLID))
                .build();
        
        GraphQLInputObjectField companyIdField = newInputObjectField()
                .name("companyId")
                .type(new GraphQLNonNull(GraphQLID))
                .build();

        List<GraphQLInputObjectField> inputFields = Arrays.asList(departmentNameField, departmentEmailField, departmentHomePageField, departmentPhoneNumberField, departmentStreetAddressField, departmentZipCodeField, departmentCityField, departmentCountryField, userIdField, companyIdField);
        
        GraphQLFieldDefinition departmentEdge = newFieldDefinition()
                .name("departmentEdge")
                .type(schema.getDepartmentEdge())
                .dataFetcher(environment -> {
                    Map source = (Map) environment.getSource();
                    String departmentId = (String) source.get("departmentId");
                    DepartmentDto department = schema.getDepartment(departmentId);
                    return new Edge(department, schema.getSimpleConnectionFromCompanyToDepartment().cursorForObjectInConnection(department));
                })
                .build();
        
        GraphQLFieldDefinition companyField = newFieldDefinition()
                .name("company")
                .type(schema.getCompanyType())
                .dataFetcher(environment -> {
                    Map source = (Map) environment.getSource();
                    String companyId = (String) source.get("companyId");
                    CompanyDto company = schema.getCompany(companyId);
                    return company;
                })
                .build();
        
        List<GraphQLFieldDefinition> outputFields = Arrays.asList(departmentEdge, companyField);

        DataFetcher mutate = environment -> {
            Map<String, Object> input = environment.getArgument("input");
            String name = (String) input.get("departmentName");
            String email = (String) input.get("departmentEmail");
            String homePage = (String) input.get("departmentHomePage");
            String phoneNumber = (String) input.get("departmentPhonenNumber");
            String streetAddress = (String) input.get("departmentStreetAddress");
            Integer zipCode = (Integer) input.get("departmentZipCode");
            String city = (String) input.get("departmentCity");
            String country = (String) input.get("departmentCountry");
            String graphQLUserId = (String) input.get("userId");
            String userId = schema.getRelay().fromGlobalId(graphQLUserId).getId();
            UserDto user = schema.getActiveUser(userId);
            String graphQLCompanyId = (String) input.get("companyId");
            String companyId = schema.getRelay().fromGlobalId(graphQLCompanyId).getId();
            CompanyDto company = schema.getCompany(companyId);
            
            if(name == null || name.isEmpty()){
            	throw new Error("Requiered fields not filled for department: pleas note that the following fields have to be entered correctly - " + "departmentName");
            }
            
            if(streetAddress == null || streetAddress.isEmpty() || zipCode == null || city == null || city.isEmpty() || country == null || country.isEmpty()){
            	String fields = "";
            	if(streetAddress == null || streetAddress.isEmpty()){
            		fields = "streetAddress";
            	}
            	if(zipCode == null){
            		if(fields.isEmpty())
            			fields = "zipCode";
            		else
            			fields = fields + ", zipCode";
            	}
            	if(city == null || city.isEmpty()){
            		if(fields.isEmpty())
            			fields = "city";
            		else
            			fields = fields + ", city";
            	}
            	if(country == null || country.isEmpty()){
            		if(fields.isEmpty())
            			fields = "country";
            		else
            			fields= fields+ ", country";
            	}
            	throw new Error("Requiered fields not filled for address: pleas note that the following fields have to be entered correctly - " + fields);
            }
            
            //Creating and saving Objects
            AddressDto address = new AddressDto(streetAddress, zipCode, city, country, country.substring(0, 3));
            DepartmentDto department = new DepartmentDto(company, name, address, homePage, email, phoneNumber);
            department.addUser(user);
            department = departmentService.save(department);
            
            String departmentId = String.valueOf(department.getId());
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("clientMutationId", (String) input.get("clientMutationId"));
            result.put("companyId", companyId);
            result.put("departmentId", departmentId);
            return result;
        };

        createDepartment = schema.getRelay().mutationWithClientMutationId("CreateDepartment", "createDepartment", inputFields, outputFields, mutate);
    }
	
	private void createTodoMutation() {
        GraphQLInputObjectField categoryIdField = newInputObjectField()
                .name("categoryId")
                .type(new GraphQLNonNull(GraphQLID))
                .build();
        
        GraphQLInputObjectField motivationField = newInputObjectField()
                .name("motivation")
                .type(Scalars.GraphQLFloat)
                .build();
        
        GraphQLInputObjectField createNoteField = newInputObjectField()
                .name("createNote")
                .type(Scalars.GraphQLString)
                .build();
        
        GraphQLInputObjectField createDateField = newInputObjectField()
                .name("createDate")
                .type(new GraphQLNonNull(GraphQLString))
                .build();
        
        GraphQLInputObjectField userIdField = newInputObjectField()
                .name("userId")
                .type(new GraphQLNonNull(GraphQLID))
                .build();
        
        GraphQLInputObjectField unplannedField = newInputObjectField()
                .name("unplanned")
                .type(Scalars.GraphQLBoolean)
                .build();

        List<GraphQLInputObjectField> inputFields = Arrays.asList(categoryIdField, motivationField, createNoteField, createDateField, userIdField, unplannedField);
        
        GraphQLFieldDefinition eventEdge = newFieldDefinition()
                .name("eventEdge")
                .type(schema.getEventEdge())
                .dataFetcher(environment -> {
                    Map source = (Map) environment.getSource();
                    String eventId = (String) source.get("eventId");
                    EventDto event = schema.getEvent(eventId);
                    String stringCreateDate = event.getCreateDate();
                    Date createDate = null;
                    try {
        				DateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        				createDate = formatter.parse(stringCreateDate);
        			} catch (ParseException e) {
        				e.printStackTrace();
        			}
                    if(createDate!= null){
                    	DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	                    stringCreateDate = formatter.format(createDate);
	                    event.setCreateDate(stringCreateDate);
                    }
                    schema.setEventList(event.getUser().getEmail());
                    return new Edge(event, schema.getSimpleConnectionFromUserToEvent().cursorForObjectInConnection(event));
                })
                .build();

        List<GraphQLFieldDefinition> outputFields = Arrays.asList(getViewerField(), eventEdge);

        DataFetcher mutate = environment -> {
            Map<String, Object> input = environment.getArgument("input");
            String categoryId = (String) input.get("categoryId");
            Double motivation = (Double) input.get("motivation");
            String createEventNote = (String) input.get("createNote");
            String stringCreateDate = (String) input.get("createDate");
            Boolean unplanned = (Boolean) input.get("unplanned");
            
            String graphQLUserId = (String) input.get("userId");
            String userId = schema.getRelay().fromGlobalId(graphQLUserId).getId();
            EventDto event = new EventDto(schema.getActiveUser(userId), categoryService.getById(Long.parseLong(categoryId)), motivation, stringCreateDate, createEventNote, unplanned);
            event.isFinished(false);
            event = eventService.save(event);
            schema.setEventList(event.getUser().getEmail());
            String eventId = String.valueOf(event.getId());
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("clientMutationId", (String) input.get("clientMutationId"));
            result.put("eventId", eventId);
            return result;
        };
        
        createEvent = schema.getRelay().mutationWithClientMutationId("CreateEvent", "createEvent", inputFields, outputFields, mutate);
    }
	
	private void createUserMutation() {
    	GraphQLInputObjectField emailField = newInputObjectField()
                .name("email")
                .type(Scalars.GraphQLString)
                .build();
        
        GraphQLInputObjectField passwordField = newInputObjectField()
                .name("password")
                .type(Scalars.GraphQLString)
                .build();
        
        GraphQLInputObjectField firstNameField = newInputObjectField()
                .name("firstName")
                .type(Scalars.GraphQLString)
                .build();
        
        GraphQLInputObjectField lastNameField = newInputObjectField()
                .name("lastName")
                .type(Scalars.GraphQLString)
                .build();
        
        GraphQLInputObjectField homePageField = newInputObjectField()
                .name("homPage")
                .type(Scalars.GraphQLString)
                .build();
        
        GraphQLInputObjectField phoneNumberField = newInputObjectField()
                .name("phoneNumber")
                .type(Scalars.GraphQLString)
                .build();
        
        GraphQLInputObjectField streetAddressField = newInputObjectField()
                .name("streetAddress")
                .type(Scalars.GraphQLString)
                .build();
        
        GraphQLInputObjectField zipCodeField = newInputObjectField()
                .name("zipCode")
                .type(Scalars.GraphQLInt)
                .build();
        
        GraphQLInputObjectField cityField = newInputObjectField()
                .name("city")
                .type(Scalars.GraphQLString)
                .build();
        
        GraphQLInputObjectField countryField = newInputObjectField()
                .name("country")
                .type(Scalars.GraphQLString)
                .build();
        
        GraphQLInputObjectField tokenField = newInputObjectField()
                .name("token")
                .type(Scalars.GraphQLString)
                .build();
        
        GraphQLInputObjectField activationCodeField = newInputObjectField()
                .name("activationCode")
                .type(Scalars.GraphQLString)
                .build();
        
        GraphQLInputObjectField languageField = newInputObjectField()
                .name("language")
                .type(Scalars.GraphQLString)
                .build();

        List<GraphQLInputObjectField> inputFields = Arrays.asList(emailField, passwordField, firstNameField, lastNameField, homePageField, phoneNumberField, streetAddressField, zipCodeField, cityField, countryField, tokenField, activationCodeField, languageField);

        List<GraphQLFieldDefinition> outputFields = Arrays.asList(getViewerField());

        DataFetcher mutate = environment -> {
            Map<String, Object> input = environment.getArgument("input");
            String email = (String) input.get("email");
            String password = (String) input.get("password");
            String firstname = (String) input.get("firstname");
            String lastname = (String) input.get("lastname");
            String homePage = (String) input.get("homepage");
            String phoneNumber = (String) input.get("phonenumber");
            String streetAddress = (String) input.get("streetaddress");
            Integer zipCode = (Integer) input.get("zipcode");
            String city = (String) input.get("city");
            String country = (String) input.get("country");
            String token = (String) input.get("token");
            String language = (String) input.get("language");
            
            if(email == null || email.isEmpty() || firstname == null || firstname.isEmpty() || lastname == null || lastname.isEmpty()){
            	String fields = "";
            	if(email == null || email.isEmpty()){
            		fields = "email";
            	}
            	if(firstname == null || firstname.isEmpty()){
            		if(fields.isEmpty())
            			fields = "firstname";
            		else
            			fields = fields + ", firstname";
            	}
            	if(lastname == null || lastname.isEmpty() ){
            		if(fields.isEmpty())
            			fields = "lastname";
            		else
            			fields = fields + ", lastname";
            	}
            	
            	throw new Error("Requiered fields not filled for user: pleas note that the following fields have to be entered correctly - " + fields);
            }
            
            if(streetAddress == null || streetAddress.isEmpty() || zipCode == null || city == null || city.isEmpty() || country == null || country.isEmpty()){
            	String fields = "";
            	if(streetAddress == null || streetAddress.isEmpty()){
            		fields = "streetAddress";
            	}
            	if(zipCode == null){
            		if(fields.isEmpty())
            			fields = "zipCode";
            		else
            			fields = fields + ", zipCode";
            	}
            	if(city == null || city.isEmpty()){
            		if(fields.isEmpty())
            			fields = "city";
            		else
            			fields = fields + ", city";
            	}
            	if(country == null || country.isEmpty()){
            		if(fields.isEmpty())
            			fields = "country";
            		else
            			fields= fields+ ", country";
            	}
            	throw new Error("Requiered fields not filled for address: pleas note that the following fields have to be entered correctly - " + fields);
            }
            
            UserDto newUser = userService.save(new UserDto(firstname, lastname, email, password, homePage, phoneNumber, new AddressDto(streetAddress, zipCode, city, country), false, token, new AuthorityDto(email, "ROLE_USER"), language));
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("clientMutationId", (String) input.get("clientMutationId"));
            result.put("userId", newUser.getId());
            return result;
        };

        createUser = schema.getRelay().mutationWithClientMutationId("CreateUser", "createUser", inputFields, outputFields, mutate);
    }
	
	private void registrationMutation() {
    	//Company
    	GraphQLInputObjectField companyNameField = newInputObjectField()
                .name("companyName")
                .type(Scalars.GraphQLString)
                .build();
    	
    	GraphQLInputObjectField companyHomePageField = newInputObjectField()
                .name("companyHomePage")
                .type(Scalars.GraphQLString)
                .build();
        
        GraphQLInputObjectField companyEmailField = newInputObjectField()
                .name("companyEmail")
                .type(Scalars.GraphQLString)
                .build();
        
        GraphQLInputObjectField companyPhoneNumberField = newInputObjectField()
                .name("companyPhoneNumber")
                .type(Scalars.GraphQLString)
                .build();
        
        GraphQLInputObjectField companyOrganisationNumberField = newInputObjectField()
                .name("companyOrganisationNumber")
                .type(Scalars.GraphQLString)
                .build();
        
        GraphQLInputObjectField companyCityField = newInputObjectField()
                .name("companyCity")
                .type(Scalars.GraphQLString)
                .build();
        
        GraphQLInputObjectField companyCountryField = newInputObjectField()
                .name("companyCountry")
                .type(Scalars.GraphQLString)
                .build();
        
        GraphQLInputObjectField companyStreetAddressField = newInputObjectField()
                .name("companyStreetAddress")
                .type(Scalars.GraphQLString)
                .build();
        
        GraphQLInputObjectField companyZipCodeField = newInputObjectField()
                .name("companyZipCode")
                .type(Scalars.GraphQLInt)
                .build();
    	
    	//Department
        GraphQLInputObjectField departmentNameField = newInputObjectField()
                .name("departmentName")
                .type(Scalars.GraphQLString)
                .build();
        
        GraphQLInputObjectField departmentEmailField = newInputObjectField()
                .name("departmentEmail")
                .type(Scalars.GraphQLString)
                .build();
        
        GraphQLInputObjectField departmentHomePageField = newInputObjectField()
                .name("departmentHomePage")
                .type(Scalars.GraphQLString)
                .build();
        
        GraphQLInputObjectField departmentPhoneNumberField = newInputObjectField()
                .name("departmentPhoneNumber")
                .type(Scalars.GraphQLString)
                .build();
        
        GraphQLInputObjectField departmentStreetAddressField = newInputObjectField()
                .name("departmentStreetAddress")
                .type(Scalars.GraphQLString)
                .build();
        
        GraphQLInputObjectField departmentZipCodeField = newInputObjectField()
                .name("departmentZipCode")
                .type(Scalars.GraphQLInt)
                .build();
        
        GraphQLInputObjectField departmentCityField = newInputObjectField()
                .name("departmentCity")
                .type(Scalars.GraphQLString)
                .build();
        
        GraphQLInputObjectField departmentCountryField = newInputObjectField()
                .name("departmentCountry")
                .type(Scalars.GraphQLString)
                .build();

        //Licens
        GraphQLInputObjectField licensNameField = newInputObjectField()
                .name("licensName")
                .type(Scalars.GraphQLString)
                .build();
        
        GraphQLInputObjectField licensDescriptionField = newInputObjectField()
                .name("licensDescription")
                .type(Scalars.GraphQLString)
                .build();
        
        GraphQLInputObjectField licensPeriodField = newInputObjectField()
                .name("licensPeriod")
                .type(Scalars.GraphQLInt)
                .build();
        
        GraphQLInputObjectField licensQuantityTotalField = newInputObjectField()
                .name("licensQuantityTotal")
                .type(Scalars.GraphQLInt)
                .build();
        
        GraphQLInputObjectField productIdField = newInputObjectField()
                .name("productId")
                .type(new GraphQLNonNull(GraphQLID))
                .build();
        
        GraphQLInputObjectField userIdField = newInputObjectField()
                .name("userId")
                .type(new GraphQLNonNull(GraphQLID))
                .build();
        
        List<GraphQLInputObjectField> inputFields = Arrays.asList(companyNameField, companyHomePageField, companyEmailField, companyPhoneNumberField, companyOrganisationNumberField, companyCityField, companyCountryField, companyStreetAddressField, companyZipCodeField,
        														  departmentNameField, departmentEmailField, departmentHomePageField, departmentPhoneNumberField, departmentStreetAddressField, departmentZipCodeField, departmentCityField, departmentCountryField,
        														  licensNameField, licensDescriptionField, licensPeriodField, licensQuantityTotalField,
        														  productIdField, userIdField);
        
        GraphQLFieldDefinition companyEdge = newFieldDefinition()
                .name("companyEdge")
                .type(schema.getCompanyEdge())
                .dataFetcher(environment -> {
                    Map source = (Map) environment.getSource();
                    String companyId = (String) source.get("companyId");
                    CompanyDto company = schema.getCompany(companyId);
                    return new Edge(company, schema.getSimpleConnectionFromUserToCompany().cursorForObjectInConnection(company));
                })
                .build();
        
        GraphQLFieldDefinition departmentEdge = newFieldDefinition()
                .name("departmentEdge")
                .type(schema.getDepartmentEdge())
                .dataFetcher(environment -> {
                    Map source = (Map) environment.getSource();
                    String departmentId = (String) source.get("departmentId");
                    DepartmentDto department = schema.getDepartment(departmentId);
                    return new Edge(department, schema.getSimpleConnectionFromCompanyToDepartment().cursorForObjectInConnection(department));
                })
                .build();
        
        GraphQLFieldDefinition licensEdge = newFieldDefinition()
                .name("licensEdge")
                .type(schema.getLicensEdge())
                .dataFetcher(environment -> {
                    Map source = (Map) environment.getSource();
                    String licensId = (String) source.get("licensId");
                    LicensDto licens = schema.getLicens(licensId);
                    return new Edge(licens, schema.getSimpleConnectionFromDepartmentToLicens().cursorForObjectInConnection(licens));
                })
                .build();
        
        GraphQLFieldDefinition activatedLicensEdge = newFieldDefinition()
                .name("activatedLicensEdge")
                .type(schema.getActivatedLicensEdge())
                .dataFetcher(environment -> {
                    Map source = (Map) environment.getSource();
                    String activatedLicensId = (String) source.get("activatedLicensId");
                    ActivatedLicensDto activatedLicens = schema.getActivatedLicens(activatedLicensId);
                    return new Edge(activatedLicens, schema.getSimpleConnectionFromUserToActivatedLicens().cursorForObjectInConnection(activatedLicens));
                })
                .build();
        
        List<GraphQLFieldDefinition> outputFields = Arrays.asList(companyEdge, departmentEdge, licensEdge, activatedLicensEdge, getViewerField());
        
        DataFetcher mutate = environment -> {
        	//Company
            Map<String, Object> input = environment.getArgument("input");
            String companyName = (String) input.get("companyName");
            String companyHomePage = (String) input.get("companyHomePage");
            String companyEmail = (String) input.get("companyEmail");
            String companyPhoneNumber = (String) input.get("companyPhoneNumber");
            String companyOrganisationNumber = (String) input.get("companyOrganisationNumber");
            String companyCity = (String) input.get("companyCity");
            String companyCountry = (String) input.get("companyCountry");
            String companyStreetAddress = (String) input.get("companyStreetAddress");
            Integer companyZipCode = (Integer) input.get("companyZipCode");
            
            //Department
            String departmentName = (String) input.get("departmentName");
            String departmentEmail = (String) input.get("departmentEmail");
            String departmentHomePage = (String) input.get("departmentHomePage");
            String departmentPhoneNumber = (String) input.get("departmentPhonenNumber");
            String departmentStreetAddress = (String) input.get("departmentStreetAddress");
            Integer departmentZipCode = (Integer) input.get("departmentZipCode");
            String departmentCity = (String) input.get("departmentCity");
            String departmentCountry = (String) input.get("departmentCountry");
            
            //User
            String graphQLUserId = (String) input.get("userId");
            String userId = schema.getRelay().fromGlobalId(graphQLUserId).getId();
            
            //Licens
            String graphQLProductId = (String) input.get("productId");
            String productId = schema.getRelay().fromGlobalId(graphQLProductId).getId();
            String licensName = (String) input.get("licensName");
            String licensDescription = (String) input.get("licensDescription");
            Integer licensPeriod = (Integer) input.get("licensPeriod");
            Integer licensQuantityTotal = (Integer) input.get("licensQuantityTotal");
            Date creationDate = null;
            Date expireDate = null;
            
            //Creating and saving Objects
            
            if(companyName == null || companyName.isEmpty() || companyOrganisationNumber == null || companyOrganisationNumber.isEmpty()){
            	String fields = "";
            	if(companyName == null || companyName.isEmpty())
            		fields = "comapnyName";
            	if(companyOrganisationNumber == null || companyOrganisationNumber.isEmpty()){
            		if(fields.isEmpty())
            			fields = "organisationNumber";
            		else
            			fields = fields + ", organisationNumber";
            	}
            	throw new Error("Requiered fields not filled for company: pleas note that the following fields have to be entered correctly - " + fields);
            }
            
            if(companyStreetAddress == null || companyStreetAddress.isEmpty() || companyZipCode == null || companyCity == null || companyCity.isEmpty() || companyCountry == null || companyCountry.isEmpty()){
            	String fields = "";
            	if(companyStreetAddress == null || companyStreetAddress.isEmpty()){
            		fields = "streetAddress";
            	}
            	if(companyZipCode == null){
            		if(fields.isEmpty())
            			fields = "zipCode";
            		else
            			fields = fields + ", zipCode";
            	}
            	if(companyCity == null || companyCity.isEmpty()){
            		if(fields.isEmpty())
            			fields = "city";
            		else
            			fields = fields + ", city";
            	}
            	if(companyCountry == null || companyCountry.isEmpty()){
            		if(fields.isEmpty())
            			fields = "country";
            		else
            			fields= fields+ ", country";
            	}
            	throw new Error("Requiered fields not filled for company address: pleas note that the following fields have to be entered correctly - " + fields);
            }
            //Company
            AddressDto companyAddress = new AddressDto(companyStreetAddress, companyZipCode, companyCity, companyCountry, companyCountry.substring(0, 3));
            CompanyDto company = new CompanyDto(companyName, companyOrganisationNumber, companyAddress, companyEmail, companyPhoneNumber, companyHomePage);
            company = companyService.save(company);
            
            //User
            UserDto user = schema.getActiveUser(userId);
	        user = userService.save(user);
	        
	        if(departmentName == null || departmentName.isEmpty()){
            	throw new Error("Requiered fields not filled for department: pleas note that the following fields have to be entered correctly - " + "departmentName");
            }
            
            if(departmentStreetAddress == null || departmentStreetAddress.isEmpty() || departmentZipCode == null || departmentCity == null || departmentCity.isEmpty() || departmentCountry == null || departmentCountry.isEmpty()){
            	String fields = "";
            	if(departmentStreetAddress == null || departmentStreetAddress.isEmpty()){
            		fields = "streetAddress";
            	}
            	if(departmentZipCode == null){
            		if(fields.isEmpty())
            			fields = "zipCode";
            		else
            			fields = fields + ", zipCode";
            	}
            	if(departmentCity == null || departmentCity.isEmpty()){
            		if(fields.isEmpty())
            			fields = "city";
            		else
            			fields = fields + ", city";
            	}
            	if(departmentCountry == null || departmentCountry.isEmpty()){
            		if(fields.isEmpty())
            			fields = "country";
            		else
            			fields= fields+ ", country";
            	}
            	throw new Error("Requiered fields not filled for address: pleas note that the following fields have to be entered correctly - " + fields);
            }
            //Department
            AddressDto departmentAddress = new AddressDto(departmentStreetAddress, departmentZipCode, departmentCity, departmentCountry, departmentCountry.substring(0, 3));
            DepartmentDto department = new DepartmentDto(company, departmentName, departmentAddress, departmentHomePage, departmentEmail, departmentPhoneNumber);
            department.addUser(user);
            department = departmentService.save(department);
            
            if(licensPeriod == null || licensQuantityTotal == null){
            	String fields = "";
            	if(licensPeriod == null)
            		fields = "licensPeriod";
            	if(licensQuantityTotal == null){
            		if(fields.isEmpty())
            			fields = "quantityTotal";
            		else
            			fields = fields + ", quantityTotal";
            	}
            	throw new Error("Requiered fields not filled for licens: pleas note that the following fields have to be entered correctly - " + fields);
            }         
            //Licens
            ProductDto product = schema.getProduct(productId);
            LicensDto licens = new LicensDto(licensName, licensDescription, department, licensPeriod, 3, licensPeriod, licensQuantityTotal, product, UUID.randomUUID().toString(), creationDate, expireDate, user);
            licens.setDepartment(department);
            licens = licensService.save(licens);
            
			//ActivatedLicens
			ActivatedLicensDto activatedLicens = new ActivatedLicensDto(licens, user, new Date());
			activatedLicens = activatedLicensService.save(activatedLicens);
			user.addActivatedLicensList(activatedLicens);
			user = userService.save(user);
			
            String companyId = String.valueOf(company.getId());
            String departmentId = String.valueOf(department.getId());
            userId = String.valueOf(user.getId());
            String licensId = String.valueOf(licens.getId());
			String activatedLicensId = String.valueOf(activatedLicens.getId());
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("clientMutationId", (String) input.get("clientMutationId"));
            result.put("companyId", companyId);
            result.put("departmentId", departmentId);
            result.put("userId", userId);
            result.put("licensId", licensId);
            result.put("activatedLicensId", activatedLicensId);
            return result;
        };

        registration = schema.getRelay().mutationWithClientMutationId("Registration", "registration", inputFields, outputFields, mutate);
    }
}
