package framework.configuration;

import org.aeonbits.owner.Config;

@Config.Sources({"file:.env"})
public interface Configuration extends Config {

    @Key("BROWSER")
    @DefaultValue("CHROME")
    SupportedBrowser browser();
    @Key("BASE_URI")
    String baseUri();
    @Key("DEFAULT_TIME_OUT")
    @DefaultValue("10")
    int defaultTimeOut();

}
