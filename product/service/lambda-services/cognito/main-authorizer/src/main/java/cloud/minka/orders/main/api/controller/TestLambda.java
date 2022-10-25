package cloud.minka.orders.main.api.controller;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import javax.inject.Inject;
import javax.inject.Named;

@Named("test")
public final class TestLambda implements RequestHandler<InputObject, OutputObject> {

    @Inject
    ProcessingService service;

    @Override
    public OutputObject handleRequest(InputObject input, Context context) {
        return service.process(input).setRequestId(context.getAwsRequestId());
    }
}
