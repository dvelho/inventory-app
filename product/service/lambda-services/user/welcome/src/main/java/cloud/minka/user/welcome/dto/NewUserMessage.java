package cloud.minka.user.welcome.dto;

import cloud.minka.service.model.cognito.SignupUser;
import cloud.minka.service.model.tenant.Tenant;

public record NewUserMessage(Tenant tenant, SignupUser signupUser) {


}
