package com.hxnry.arduino;

import net.runelite.client.config.*;

import java.awt.*;

@ConfigGroup("arduino_suite")
public interface ArduinoConfig extends Config {


    @ConfigSection(
            name = "Connection",
            description = "Connection",
            position = 0
    )
    String connectionSettings = "connectionSettings";

    @ConfigSection(
            name = "Setup",
            description = "setup",
            position = 1
    )
    String setupSettings = "setupSettings";

    @ConfigSection(
            name = "Fishing",
            description = "Settings relating fishing scripts",
            position = 2
    )
    String fishSettings = "fishingSettings";


    @ConfigItem(
            position = 0,
            keyName = "comPort",
            name = "COM Port",
            description = "communications port ID for the device",
            section = connectionSettings
    )
    default int serialPort() {
        return -1;
    }

    @ConfigItem(
            position = 3,
            keyName = "baudRate",
            name = "Baud Rate",
            description = "baud rate for connection",
            section = connectionSettings
    )
    default int baudRate() {
        return 9600;
    }



    @ConfigItem(
            position = 0,
            keyName = "isFishingStarted",
            name = "Fish",
            description = "start fishing",
            section = fishSettings
    )
    default boolean isFishingStarted() {
        return false;
    }

    enum OptionEnum {
        SHRIMP_LUMBRIDGE, SHRIMP_AL_KHARID
    }

    @ConfigItem(
            position = 1,
            keyName = "enumFish",
            name = "Fishing Method",
            description = "...",
            section = fishSettings
    )
    default OptionEnum enumFishConfig() {
        return OptionEnum.SHRIMP_AL_KHARID;
    }




    @ConfigItem(
            position = 0,
            keyName = "monitorSizeConfig",
            name = "Monitor",
            description = "...",
            section = setupSettings
    )
    default Dimension monitorSizeConfig() {
        return new Dimension(3440, 1440);
    }
}
