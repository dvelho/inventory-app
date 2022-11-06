package cloud.minka.cognito.signup.util;

import cloud.minka.cognito.signup.converter.TenantConverter;
import cloud.minka.cognito.signup.repository.CognitoTenantRepository;
import cloud.minka.cognito.signup.repository.TenantRepository;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logmanager.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class DataLoader {
    private static final Logger LOGGER = Logger.getLogger("ListenerBean");

    @Inject
    TenantRepository tenantRepository;

    @Inject
    CognitoTenantRepository cognitoTenantRepository;

    @Inject
    TenantConverter tenantConverter;

    @ConfigProperty(name = "cloud.minka.tenant.table", defaultValue = "dev-tenants-info-minka-cloud")
    String tableName;

  /*  void onStart(@Observes @Priority(value = 1) StartupEvent ev) {
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
        TenantCreate tenantCreate = new TenantCreate(
                tenantDomain,
                tenantDomain,
                tenantAdminEmail,
                PENDING_CONFIGURATION,
                HOSTED,
                "user-pool-id");
        tenantRepository.createTenantTable(tableName);

        tenantRepository.insertTenantIntoTable(tenantConverter.convertTenantToPutItemRequest(tableName, tenantCreate));
      /*   CreateUserPoolResponse response = cognitoTenantRepository.createUSerPool(tenantDomain);
        LOGGER.info("User pool created: " + response.userPool().id());*/
    /* }*/
}
