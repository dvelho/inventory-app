package cloud.minka.cognito.signup.controller;

//@QuarkusTest
public class ExampleResourceTest {

    // @Inject
    // CognitoIdentityProviderClient cognitoClient;

    //  @Test
    public void testHelloEndpoint() {
        //create a user pool

      /*  CreateUserPoolRequest createUserPoolRequest = CreateUserPoolRequest.builder()
                .poolName("test-pool")
                .build();
        CreateUserPoolResponse response = cognitoClient.createUserPool(createUserPoolRequest);
        //add user to the pool
        AttributeType email = AttributeType.builder()
                .name("email")
                .value("diogo.velho@mindera.com").build();
        AdminCreateUserRequest adminCreateUserRequest = AdminCreateUserRequest.builder()
                .userPoolId(response.userPool().id())
                .username("test-user")
                .userAttributes(email)
                .build();
        cognitoClient.adminCreateUser(adminCreateUserRequest);

        //list users
        String emailT ="diogo.velho@mindera.com";
        ListUsersRequest listUsersRequest = ListUsersRequest.builder()
                .userPoolId(response.userPool().id())
                .filter("email = \"" + emailT + "\"")
                .build();
        ListUsersResponse listUsersResponse = cognitoClient.listUsers(listUsersRequest);
        System.out.println(listUsersResponse.users().size());
*/

    }

}