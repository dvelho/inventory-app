package cloud.minka.tenants.api.controller;

import cloud.minka.service.model.location.Address;
import cloud.minka.service.model.tenant.TenantConfiguration;
import cloud.minka.service.model.tenant.TenantRegion;
import cloud.minka.service.model.tenant.TenantStatus;
import cloud.minka.service.model.tenant.TenantStylePersonalization;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/")
public class Query {

    /**
     * Hello string.
     *
     * @return the string
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public TenantConfiguration getTenant() {
        //test
        return new TenantConfiguration(
                "PK",
                "SK",
                new Address("add1", "add2", "city", "state", "zip", "countryCode"),
                TenantStatus.ACTIVE,
                new TenantStylePersonalization("logoUrl",
                        "primaryColor",
                        "backgroundColor",
                        "textColor",
                        "linkColor",
                        "linkHoverColor",
                        "linkActiveColor",
                        "linkVisitedColor"
                ),
                TenantRegion.EU_WEST_1
        );
    }


}
