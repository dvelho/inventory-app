package cloud.minka.cognito.signup.service;

import cloud.minka.cognito.signup.model.cloudformation.CognitoSignupEvent;
import cloud.minka.cognito.signup.model.cloudformation.ResponseSignup;

import cloud.minka.cognito.signup.model.cloudformation.TenantStatus;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Map;

import static cloud.minka.cognito.signup.model.cloudformation.TenantStatus.PENDING_CONFIGURATION;

@ApplicationScoped
public final class ProcessingService {
    @Inject
    private DynamoDbClient client;

    /**
     * Process pre signup response signup.
     * If the tenant does not exist, then we will create it.
     * If the tenant does exist, but the user is not the admin, then we will return an error.
     *
     * @param input the input
     * @return the response signup
     */
    public ResponseSignup processPreSignup(CognitoSignupEvent input) {
        String tableName = "tenant";
        createTenantTable(tableName);
        String userEmail = input.request().userAttributes().email();
        String tenantDomain = userEmail.split("@")[1];
        System.out.println("event::cognito::signup::request::tenant::domain:" + tenantDomain);
        GetItemResponse tenant = getTenantFromTable(tableName, tenantDomain);
        System.out.println("event::cognito::signup::request::tenant::response:" + tenant);
        if (tenant.item().size() == 0) {
            insertTenantIntoTable(tableName, tenantDomain);
            return new ResponseSignup(false, false, false);
        } else {
            //Check if the tenant is in pending configuration
            System.out.println("event::cognito::signup::request::tenant::exists");
            TenantStatus tenantStatus = TenantStatus.valueOf(tenant.item().get("status").s());
            switch (tenantStatus) {
                case PENDING_CONFIGURATION:
                    throw new IllegalArgumentException("You domain exists but is not yet fully configured. Please contact the person responsible for your Organization.");
                case ACTIVE:
                    return new ResponseSignup(false, false, false);
                default:
                    throw new IllegalArgumentException("You domain exists but is not yet fully configured. Please contact the person responsible for your Organization.");
            }
        }
    }

    public void createTenantTable(String tenantTable) {
        //Create the tenant on dynamodb
        try {
            client.createTable(tableRequest ->
                    tableRequest.tableName(tenantTable)
                            .keySchema(keySchema -> keySchema.attributeName("PK").keyType(KeyType.HASH))
                            .attributeDefinitions(attrDef -> attrDef.attributeName("PK").attributeType(ScalarAttributeType.S))
                            .provisionedThroughput(throughput -> throughput.writeCapacityUnits(1L).readCapacityUnits(1L)));
        } catch (ResourceInUseException e) {
            System.out.println("event::cognito::signup::request::tenant::table::exists:" + e.getMessage());
        }

    }

    public void insertTenantIntoTable(String tenantTable, String tenantId) {
        //Insert the tenant on dynamodb
        AttributeValue pk = AttributeValue.builder().s(tenantId).build();
        AttributeValue sk = AttributeValue.builder().s("tenant").build();
        AttributeValue status = AttributeValue.builder().s(String.valueOf(PENDING_CONFIGURATION)).build();
        PutItemRequest request = PutItemRequest.builder()
                .tableName(tenantTable)
                .item(Map.of("PK", pk, "SK", sk, "status", status))
                .build();
        client.putItem(request);
    }

    public GetItemResponse getTenantFromTable(String tenantTable, String tenantId) {
        //Get the tenant from dynamodb
        AttributeValue pk = AttributeValue.builder().s(tenantId).build();
        GetItemRequest request = GetItemRequest.builder()
                .tableName(tenantTable)
                .key(Map.of("PK", pk))
                .build();
        return client.getItem(request);
    }
}


