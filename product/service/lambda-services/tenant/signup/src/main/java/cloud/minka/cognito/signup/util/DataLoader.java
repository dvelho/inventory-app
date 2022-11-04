package cloud.minka.cognito.signup.util;

import cloud.minka.cognito.signup.converter.Converter;
import cloud.minka.cognito.signup.repository.CognitoTenantRepository;
import cloud.minka.cognito.signup.repository.TenantRepository;
import cloud.minka.service.model.tenant.Tenant;
import io.quarkus.arc.Priority;
import io.quarkus.runtime.LaunchMode;
import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logmanager.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import static cloud.minka.service.model.tenant.TenantStatus.PENDING_CONFIGURATION;
import static cloud.minka.service.model.tenant.TenantType.HOSTED;

@ApplicationScoped
public class DataLoader {
    private static final Logger LOGGER = Logger.getLogger("ListenerBean");

    @Inject
    TenantRepository tenantRepository;

    @Inject
    CognitoTenantRepository cognitoTenantRepository;

    @Inject
    Converter converter;

    @ConfigProperty(name = "cloud.minka.tenant.table", defaultValue = "dev-tenants-info-minka-cloud")
    String tableName;

    void onStart(@Observes @Priority(value = 1) StartupEvent ev) {
        LOGGER.info("The application is starting...");
        LaunchMode launchMode = io.quarkus.runtime.LaunchMode.current();
        LOGGER.info("Launch mode: " + launchMode);
        if (launchMode == LaunchMode.DEVELOPMENT) {
            LOGGER.info("Loading data...");
            loadData();
        }
    }

    private void loadData() {
        String tenantDomain = "mindera.com";
        String tenantAdminEmail = "diogo.velho@mindera.com";
        Tenant tenant = new Tenant(
                tenantDomain,
                tenantDomain,
                tenantAdminEmail,
                PENDING_CONFIGURATION,
                HOSTED,
                "user-pool-id");
        tenantRepository.createTenantTable(tableName);

        tenantRepository.insertTenantIntoTable(converter.convertTenantToPutItemRequest(tableName, tenant));
      /*   CreateUserPoolResponse response = cognitoTenantRepository.createUSerPool(tenantDomain);
        LOGGER.info("User pool created: " + response.userPool().id());*/
    }
}
