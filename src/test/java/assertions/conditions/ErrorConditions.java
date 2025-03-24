package assertions.conditions;

import assertions.Condition;
import io.restassured.response.ValidatableResponse;
import lombok.RequiredArgsConstructor;
import models.swagger.ErrorAuth;
import models.swagger.InfoResult;
import org.junit.jupiter.api.Assertions;

@RequiredArgsConstructor
public class ErrorConditions implements Condition {

    private final String expectedMessage;

    @Override
    public void check(ValidatableResponse response) {
        ErrorAuth error = response.extract().as(ErrorAuth.class);
        Assertions.assertEquals(error.getError(), expectedMessage);
    }
}
