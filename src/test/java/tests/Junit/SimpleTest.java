package tests.Junit;

import listener.RetryListener;
import models.People;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import utils.JsonHelper;

@Tag("API")
@ExtendWith(RetryListener.class)
public class SimpleTest {

    @AfterAll
    public static void saveFailed(){
        RetryListener.saveFailedTest();
    }

    private static int age = 0;

    @Test
    void checkKate(){
        int a = 10;
        int b = 11;
        Assertions.assertEquals(a, b, "chto to upalo");
    }

    @Test
    void simpleTestone() {
        People people = JsonHelper.fromJson("src/test/resources/Stas.json", People.class);
        System.out.println(people.getName());

        People sasha = new People("Sasha", 20 , "female");
        String json = JsonHelper.jsonFromObj(sasha);
        System.out.println(json);
        People people2 = People.builder().name("alex").sex("male").age(30).build();
        System.out.println(people2);

    }

    @Test
    void peopleTest(){
        People people3 = People.builder().name("alex").sex("female").age(5).build();
        int realAge = people3.getAge() + 30;
    }
}
