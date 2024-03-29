package config.owner;

import org.aeonbits.owner.Config;

@Config.Sources({
        "classpath:${envMobile}.properties"
})

public interface BrowserstackConfigOwner extends Config {
    @Key("bsUrl")
    String url();

    @Key("osType")
    String osType();

    @Key("osVersion")
    String osVersion();

    @Key("mobileDevice")
    String device();

    @Key("app")
    String app();

}
