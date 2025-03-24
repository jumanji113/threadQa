package assertions.conditions;

import assertions.Condition;
import io.restassured.response.ValidatableResponse;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;

@RequiredArgsConstructor
public class StatusCodeCondition implements Condition {
    private final Integer statuscode;
    @Override
    public void check(ValidatableResponse response) {
        int actualStatus = response.extract().statusCode();
        Assertions.assertEquals(statuscode, actualStatus);
        //либо response.assertThat().statusCode(statuscode);
    }
}
