package cloud.minka.cognito.signup.repository;

import cloud.minka.cognito.signup.model.cloudformation.TenantStatus;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Map;

import static cloud.minka.cognito.signup.model.cloudformation.TenantStatus.PENDING_CONFIGURATION;

@ApplicationScoped
public class TenantRepository {
    @Inject
    DynamoDbClient client;
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

    public void updateTenant(String tableName, String tenantDomain, TenantStatus active) {
        AttributeValue pk = AttributeValue.builder().s(tenantDomain).build();
        AttributeValue sk = AttributeValue.builder().s("tenant").build();
        AttributeValue status = AttributeValue.builder().s(String.valueOf(active)).build();
        UpdateItemRequest request = UpdateItemRequest.builder()
                .tableName(tableName)
                .key(Map.of("PK", pk, "SK", sk))
                .updateExpression("set #status = :status")
                .expressionAttributeNames(Map.of("#status", "status"))
                .expressionAttributeValues(Map.of(":status", status))
                .build();
        client.updateItem(request);
    }
}
