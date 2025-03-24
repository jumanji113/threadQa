package assertions.conditions;

import assertions.Condition;
import io.restassured.response.ValidatableResponse;
import lombok.RequiredArgsConstructor;
import models.swagger.InfoResult;
import org.junit.jupiter.api.Assertions;

@RequiredArgsConstructor
public class MessageConditions implements Condition {

    private final String expectedMessage;

    @Override
    public void check(ValidatableResponse response) {
        InfoResult info = response.extract().as(InfoResult.class);
        Assertions.assertEquals(info.getInfo().getMessage(), expectedMessage);
    }
}
