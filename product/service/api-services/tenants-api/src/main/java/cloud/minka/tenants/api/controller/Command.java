package cloud.minka.tenants.api.controller;

import cloud.minka.service.model.tenant.TenantConfiguration;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/")
public class Command {

    @POST
    public String createTenantConfiguration(TenantConfiguration tenantConfiguration) {
        System.out.println("tenantConfiguration = " + tenantConfiguration);
        return tenantConfiguration.toString();
    }


}
