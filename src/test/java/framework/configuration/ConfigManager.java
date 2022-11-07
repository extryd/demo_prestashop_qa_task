package framework.configuration;

import org.aeonbits.owner.ConfigFactory;

public abstract class ConfigManager {

    public static final Configuration config = ConfigFactory.create(Configuration.class);

}
