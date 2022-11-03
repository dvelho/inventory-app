package cloud.minka.cognito.signup.repository;


import cloud.minka.service.model.tenant.TenantStatus;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Map;

import static cloud.minka.service.model.tenant.TenantStatus.PENDING_CONFIGURATION;


@ApplicationScoped
public class TenantRepository {
    @Inject
    DynamoDbClient client;

    public void createTenantTable(String tenantTable) {
        //Create the tenant on dynamodb
        try {
            //Create the table with primary key PK and SK as partition and sort key
            client.createTable(createTenantTableRequest(tenantTable));
        } catch (ResourceInUseException e) {
            System.out.println("event::cognito::signup::request::tenant::table::exists:" + e.getMessage());
        }

    }

    private CreateTableRequest createTenantTableRequest(String tenantTable) {
        //Create the table with primary key PK and SK as partition and sort key
        return CreateTableRequest.builder()
                .tableName(tenantTable)
                .keySchema(
                        KeySchemaElement.builder()
                                .attributeName("PK")
                                .keyType(KeyType.HASH)
                                .build(),
                        KeySchemaElement.builder()
                                .attributeName("SK")
                                .keyType(KeyType.RANGE)
                                .build()
                )
                .attributeDefinitions(
                        AttributeDefinition.builder()
                                .attributeName("PK")
                                .attributeType(ScalarAttributeType.S)
                                .build(),
                        AttributeDefinition.builder()
                                .attributeName("SK")
                                .attributeType(ScalarAttributeType.S)
                                .build()
                )
                .provisionedThroughput(
                        ProvisionedThroughput.builder()
                                .readCapacityUnits(1L)
                                .writeCapacityUnits(1L)
                                .build()
                )
                .build();
    }

    public void insertTenantIntoTable(String tenantTable, String tenantDomain, String userEmail) {
        //Insert the tenant on dynamodb
        AttributeValue pk = AttributeValue.builder().s(tenantDomain).build();
        AttributeValue sk = AttributeValue.builder().s(tenantDomain).build();
        AttributeValue adminEmail = AttributeValue.builder().s(userEmail).build();
        AttributeValue status = AttributeValue.builder().s(String.valueOf(PENDING_CONFIGURATION)).build();
        PutItemRequest request = PutItemRequest.builder()
                .tableName(tenantTable)
                .item(Map.of("PK", pk, "SK", sk, "adminEmail", adminEmail, "status", status))
                .build();
        client.putItem(request);
    }

    public GetItemResponse getTenantFromTable(String tenantTable, String tenantDomain) {
        //Get the tenant from dynamodb
        AttributeValue pk = AttributeValue.builder().s(tenantDomain).build();
        AttributeValue sk = AttributeValue.builder().s(tenantDomain).build();
        GetItemRequest request = GetItemRequest.builder()
                .tableName(tenantTable)
                .key(Map.of("PK", pk, "SK", sk))
                .build();
        return client.getItem(request);
    }

    public void updateTenant(String tableName, String tenantDomain, TenantStatus status) {
        AttributeValue pk = AttributeValue.builder().s(tenantDomain).build();
        AttributeValue sk = AttributeValue.builder().s("tenant").build();
        AttributeValue statusA = AttributeValue.builder().s(String.valueOf(status)).build();
        UpdateItemRequest request = UpdateItemRequest.builder()
                .tableName(tableName)
                .key(Map.of("PK", pk, "SK", sk))
                .updateExpression("set #status = :status")
                .expressionAttributeNames(Map.of("#status", "status"))
                .expressionAttributeValues(Map.of(":status", statusA))
                .build();
        client.updateItem(request);
    }
}
