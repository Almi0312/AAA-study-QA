package ui;

@org.aeonbits.owner.Config.Sources("classpath: selenium.properties")
public interface Config extends org.aeonbits.owner.Config {
    @org.aeonbits.owner.Config.Key("path")
    String path();
}
