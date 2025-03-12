package utils;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:project.properties")
public interface AppConfig extends Config {
    @Key("url")
    String url();

    @Key("is_production")
    boolean isProad();

    @Key("threads")
    Integer threads();
}
