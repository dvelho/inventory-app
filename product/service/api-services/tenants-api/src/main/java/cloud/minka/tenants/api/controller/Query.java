package cloud.minka.tenants.api.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/query")
public class Query {

    /**
     * Hello string.
     *
     * @return the string
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getTenant() {
        return "hello from tenants";
    }

    public void getTenantAdmin() {

    }

    public void getTenantUser() {

    }

    public void getTenantRole() {

    }

    public void getTenantRolePermission() {

    }

    public void getTenantPermission() {

    }

    public void getTenantPermissionGroup() {

    }

    public void getTenantPermissionGroupPermission() {

    }

    public void getTenantPermissionGroupRole() {

    }

    public void getTenantPermissionGroupRolePermission() {

    }


}
