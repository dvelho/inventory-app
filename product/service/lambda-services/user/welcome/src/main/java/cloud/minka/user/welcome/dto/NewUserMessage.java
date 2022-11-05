package cloud.minka.user.welcome.dto;

import cloud.minka.service.model.cognito.SignupUser;
import cloud.minka.service.model.tenant.TenantCreate;

public record NewUserMessage(TenantCreate tenantCreate, SignupUser signupUser) {


}
