package cloud.minka.user.welcome.repository;


import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Map;


@ApplicationScoped
public class TenantRepository {
    @Inject
    DynamoDbClient client;


    public GetItemResponse getTenantFromTable(String tenantTable, String tenantDomain) {
        System.out.println("event::cognito::signup::request::tenant::table::get::request:" + tenantTable + " " + tenantDomain);
        AttributeValue pk = AttributeValue.builder().s(tenantDomain).build();
        AttributeValue sk = AttributeValue.builder().s(tenantDomain).build();
        GetItemRequest request = GetItemRequest.builder()
                .tableName(tenantTable)
                .key(Map.of("PK", pk, "SK", sk))
                .build();
        return client.getItem(request);
    }


}
