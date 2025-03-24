package assertions.conditions;

import assertions.Condition;
import io.restassured.response.ValidatableResponse;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;

@RequiredArgsConstructor
public class JwtConditions implements Condition {
    @Override
    public void check(ValidatableResponse response) {
        String jwt = response.extract().jsonPath().getString("token");
        Assertions.assertNotNull(jwt);
    }
}
