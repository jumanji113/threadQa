package utils;

import com.github.javafaker.Faker;
import models.swagger.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class RandomTestData {


    private static Random random = new Random();

    private static Faker faker = new Faker();

    public static GamesItem getRandomGame(){
        SimilarDlc similarDlc = SimilarDlc.builder()
                .isFree(false)
                .dlcNameFromAnotherGame(faker.funnyName().name())
                .build();
        DlcsItem dlcsItem = DlcsItem.builder()
                .similarDlc(similarDlc)
                .rating(faker.random().nextInt(10))
                .price(faker.random().nextInt(100))
                .description(faker.funnyName().name())
                .dlcName(faker.dragonBall().character())
                .isDlcFree(true)
                .build();
        Requirements requirements = Requirements.builder()
                .ramGb(faker.random().nextInt(10))
                .videoCard(faker.book().title())
                .hardDrive(faker.random().nextInt(32))
                .osName(faker.animal().name())
                .build();
        return GamesItem.builder()
                .requirements(requirements)
                .genre(faker.book().genre())
                .price(random.nextInt(100))
                .description("CS 2")
                .rating(faker.random().nextInt(10))
                .title(faker.book().title())
                .publishDate(LocalDateTime.now().toString())
                .requiredAge(random.nextBoolean())
                .tags(Arrays.asList("shooter", "cars"))
                .dlcs(Collections.singletonList(dlcsItem))
                .company(faker.company().name())
                .isFree(false)
                .build();
    }

    public static FullUser getRandomUserWithGames(){
        int randomNumber = Math.abs(random.nextInt());
        GamesItem gamesItem = getRandomGame();
        return FullUser.builder()
                .login(faker.name().username() + randomNumber)
                .pass(faker.internet().password())
                .games(Collections.singletonList(gamesItem))
                .build();
    }

    public static FullUser getRandomUser(){
        int randomNumber = Math.abs(random.nextInt());
        return FullUser.builder().login("Unhuman" + randomNumber).pass("Jumanji" + randomNumber)
                .build();
    }

    public static FullUser getAdminUser(){
        return FullUser.builder().login("admin").pass("admin").build();
    }
}
