package cloud.minka.cognito.signup.model.cloudformation;

import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
public record CallerContext(String awsSdkVersion, String clientId) {

}
