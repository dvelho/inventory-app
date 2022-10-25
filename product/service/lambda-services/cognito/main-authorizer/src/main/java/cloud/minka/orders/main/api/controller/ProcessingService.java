package cloud.minka.orders.main.api.controller;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public final class ProcessingService {

    public static final String CAN_ONLY_GREET_NICKNAMES = "Sorry I Can only greet nicknames";

    public OutputObject process(InputObject input) {
        if (input.getName().equals("Stuart")) {
            throw new IllegalArgumentException(CAN_ONLY_GREET_NICKNAMES);
        }
        String result = input.getGreeting() + " estupido " + input.getName();
        OutputObject out = new OutputObject();
        out.setResult(result);
        return out;
    }
}
