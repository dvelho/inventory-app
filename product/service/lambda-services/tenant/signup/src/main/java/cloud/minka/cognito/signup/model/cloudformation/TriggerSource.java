package cloud.minka.cognito.signup.model.cloudformation;

public enum TriggerSource {
    PreSignUp_ExternalProvider,
    PreSignUp_AdminCreateUser,
    PreSignUp_Registration,
    PreSignUp_SignUp,
    PostConfirmation_ConfirmSignUp,
    PostConfirmation_ConfirmForgotPassword,

    PreAuthentication_Authentication,
    PreAuthentication_RefreshToken,
    PostAuthentication_Authentication,
    PostAuthentication_RefreshToken,
    CustomMessage_SignUp,
    CustomMessage_AdminCreateUser,
    CustomMessage_ResendCode,
    CustomMessage_ForgotPassword,
    CustomMessage_UpdateUserAttribute,
    CustomMessage_VerifyUserAttribute,
    CustomMessage_Authentication,
    DefineAuthChallenge_Authentication,
    CreateAuthChallenge_Authentication,
    VerifyAuthChallengeResponse_Authentication,

}
