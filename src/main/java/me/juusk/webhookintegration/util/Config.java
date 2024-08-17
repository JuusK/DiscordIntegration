package me.juusk.webhookintegration.util;


import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

import java.awt.*;

public class Config {

    public static ConfigClassHandler<Config> HANDLER = ConfigClassHandler.createBuilder(Config.class)
            .id(new Identifier("discordwebhook", "config"))
                    .serializer(config -> GsonConfigSerializerBuilder.create(config)
                            .setPath(FabricLoader.getInstance().getConfigDir().resolve("settings.json"))
                            .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                            .build())
                    .build();



    @SerialEntry
    public static Color embedColor = Color.RED;

    @SerialEntry
    public static String webhookUrl = "";

    @SerialEntry
    public static String messageTitle = "You died, {name}";

    @SerialEntry
    public static String userId = "";

    @SerialEntry
    public static boolean messageCoordinates = true;

    @SerialEntry
    public static boolean messageWorldName = true;

    @SerialEntry
    public static boolean enabled = true;

    @SerialEntry
    public static boolean mention = false;



}