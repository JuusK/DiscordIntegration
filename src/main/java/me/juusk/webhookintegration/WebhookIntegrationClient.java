package me.juusk.webhookintegration;

import me.juusk.webhookintegration.util.Config;
import me.juusk.webhookintegration.util.DiscordWebhook;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.Screen;

import java.awt.*;
import java.io.IOException;
import java.text.DecimalFormat;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class WebhookIntegrationClient implements ClientModInitializer {

    public static DiscordWebhook webhook;
    public  static WebhookIntegrationClient INSTANCE = new WebhookIntegrationClient();



    @Override
    public void onInitializeClient() {
        Config.HANDLER.load();
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
        });
    }

    public void onOpenScreen(Screen screen) {
        assert MinecraftClient.getInstance().player != null;
        if (webhook == null) return;
        if (screen instanceof DeathScreen) {
            sendDeathMessage();

        }
    }


    public static void setWebhookURL(String url) {
        Config.webhookUrl = url;
        onWebhookURLChanged();
    }
    public static String getWebhookURL() {
        return Config.webhookUrl;
    }

    private static void onWebhookURLChanged() {
        if(!Config.webhookUrl.isEmpty()) {
            webhook = new DiscordWebhook(Config.webhookUrl);
        }
    }

    public static void setEnabled(Boolean enabled) {
        Config.enabled = enabled;
        onEnabledChanged();
    }

    public static Boolean getEnabled() {
        return Config.enabled;
    }

    private static void onEnabledChanged() {
        Config.HANDLER.save();
    }

    public static void setEmbedColor(Color embedColor) {
        Config.embedColor = embedColor;
        onEmbedColorChanged();
    }

    public static Color getEmbedColor() {
        return Config.embedColor;
    }

    private static void onEmbedColorChanged() {
        Config.HANDLER.save();
    }


    public static void setMessageTitle(String messageTitle) {
        Config.messageTitle = messageTitle;
        onMessageTitleChanged();
    }

    public static String getMessageTitle() {
        return Config.messageTitle;
    }

    private static void onMessageTitleChanged() {
        Config.HANDLER.save();
    }

    public static void setUserID(String userId) {
        Config.userId = userId;
        onUserIDChanged();
    }

    public static String getUserID() {
        return Config.userId;
    }

    private static void onUserIDChanged() {
        Config.HANDLER.save();
    }

    public static void setMessageCoordinates(Boolean messageCoordinates) {
        Config.messageCoordinates = messageCoordinates;
        onMessageCoordinatesChanged();
    }

    public static Boolean getMessageCoordinates() {
        return Config.messageCoordinates;
    }

    private static void onMessageCoordinatesChanged() {
        Config.HANDLER.save();
    }

    public static void setMessageWorldName(Boolean messageWorldName) {
        Config.messageWorldName = messageWorldName;
        onMessageWorldNameChanged();
    }

    public static Boolean getMessageWorldName() {
        return Config.messageWorldName;
    }

    private static void onMessageWorldNameChanged() {
        Config.HANDLER.save();
    }

    public static void setMention(Boolean mention) {
        Config.mention = mention;
        onMentionChanged();
    }

    public static Boolean getMention() {
        return Config.mention;
    }

    private static void onMentionChanged() {
        Config.HANDLER.save();
    }



    public static void sendDeathMessage() {
        assert MinecraftClient.getInstance().player != null;
        if (webhook == null) return;

        if(Config.mention == true) {
            webhook.setContent("<@" + Config.userId + ">");
        }
        String title = Config.messageTitle;
        if(title.contains("{name}")) {
            title = title.replace("{name}", MinecraftClient.getInstance().player.getName().getString());
            System.out.println("Replaced {name} with playername");
        }



        DiscordWebhook.EmbedObject embedObject = new DiscordWebhook.EmbedObject();
        embedObject.setTitle(title);
        embedObject.setColor(Config.embedColor);
        DecimalFormat df = new DecimalFormat("###.###");
        if(Config.messageCoordinates) {
            embedObject.addField("Coordinates:", "X: " + df.format(MinecraftClient.getInstance().player.getX()) + " Y: " + df.format(MinecraftClient.getInstance().player.getY()) + " Z: " + df.format(MinecraftClient.getInstance().player.getZ()), false);
        }
        if(Config.messageWorldName) {
            embedObject.addField("World:", MinecraftClient.getInstance().world.getRegistryKey().getValue().getPath(), false);
        }


        webhook.addEmbed(embedObject);

        try {
            webhook.execute();
        } catch(IOException exc) {
            exc.printStackTrace();
        }
        webhook.clearEmbeds();


    }

}
