package assertions;

import assertions.conditions.ErrorConditions;
import assertions.conditions.JwtConditions;
import assertions.conditions.MessageConditions;
import assertions.conditions.StatusCodeCondition;

public class Conditions {
    public static MessageConditions haseMessage(String expectedMessage){
        return new MessageConditions(expectedMessage);
    }

    public static ErrorConditions haseError(String expectedMessage){
        return new ErrorConditions(expectedMessage);
    }

    public static StatusCodeCondition haseStatus(Integer expectedStatus){
        return new StatusCodeCondition(expectedStatus);
    }

    public static JwtConditions haseJwt(){
        return new JwtConditions();
    }
}
