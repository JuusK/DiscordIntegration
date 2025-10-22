package me.juusk.webhookintegration;

import com.mojang.authlib.GameProfile;
import me.juusk.webhookintegration.util.Config;
import me.juusk.webhookintegration.util.DiscordWebhook;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;

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

    public static void onChatMessage(SignedMessage message, GameProfile sender, MessageType.Parameters params) {
        assert MinecraftClient.getInstance().player != null;
        if(webhook == null) return;


        String title = sender.getName();
        String messageContent = "";


        if(Config.chatEnabled == false) return;






        if(Config.chatEmbed == true) {
            DiscordWebhook.EmbedObject embedObject = new DiscordWebhook.EmbedObject();
            embedObject.setTitle(title);
            embedObject.addField("Message:", message.getContent().getString(), false);

            webhook.addEmbed(embedObject);
        } else {

            messageContent = messageContent + ("\n #" + title);
            messageContent = messageContent + ("\n " + message.getContent());
        }
        webhook.setContent(messageContent);
        try {
            webhook.execute();
        } catch(IOException exc) {
            exc.printStackTrace();
        }
        webhook.clearEmbeds();


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

    public static void setDeathEnabled(Boolean enabled) {
        Config.deathEnabled = enabled;
        onEnabledChanged();
    }
    public static void setDeathEmbed(Boolean enabled) {
        Config.deathEmbed = enabled;
        onEnabledChanged();
    }

    public static void setChatEnabled(Boolean enabled) {
        Config.chatEnabled = enabled;
        onEnabledChanged();
    }

    public static void setChatEmbed(Boolean enabled) {
        Config.chatEmbed = enabled;
        onEnabledChanged();
    }

    public static Boolean getEnabled() {
        return Config.enabled;
    }

    public static Boolean getDeathEnabled() {
        return Config.deathEnabled;
    }

    public static Boolean getDeathEmbed() {
        return Config.deathEmbed;
    }

    public static Boolean getChatEnabled() {
        return Config.chatEnabled;
    }

    public static Boolean getChatEmbed() {
        return Config.chatEmbed;
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
        if(webhook == null) return;


        String title = Config.messageTitle;
        String messageContent = "";


        if(Config.deathEnabled == false) return;

        if(Config.mention == true) {
            messageContent = messageContent + ("<@" + Config.userId + ">");
        }




        if(title.contains("{name}")) {
            title = title.replace("{name}", MinecraftClient.getInstance().player.getName().getString());
            System.out.println("Replaced {name} with playername");
        }


        if(Config.deathEmbed == true) {
            DiscordWebhook.EmbedObject embedObject = new DiscordWebhook.EmbedObject();
            embedObject.setTitle(title);
            embedObject.setColor(Config.embedColor);
            DecimalFormat df = new DecimalFormat("###.###");
            if (Config.messageCoordinates) {
                embedObject.addField("Coordinates:", "X: " + df.format(MinecraftClient.getInstance().player.getX()) + " Y: " + df.format(MinecraftClient.getInstance().player.getY()) + " Z: " + df.format(MinecraftClient.getInstance().player.getZ()), false);
            }
            if (Config.messageWorldName) {
                embedObject.addField("World:", MinecraftClient.getInstance().world.getRegistryKey().getValue().getPath(), false);
            }


            webhook.addEmbed(embedObject);
        } else {

            messageContent = messageContent + ("\n #" + title);
            DecimalFormat df = new DecimalFormat("###.###");
            if (Config.messageCoordinates) {
                messageContent = messageContent + ("\n *Coordinates*" + "\n X: " + df.format(MinecraftClient.getInstance().player.getX()) + "\n Y: " + df.format(MinecraftClient.getInstance().player.getY()) + "\n Z: " + df.format(MinecraftClient.getInstance().player.getZ()));
            }
            if (Config.messageWorldName) {
                messageContent = messageContent + ("\n *World* \n" + MinecraftClient.getInstance().world.getRegistryKey().getValue().getPath());
            }
        }
        webhook.setContent(messageContent);
        try {
            webhook.execute();
        } catch(IOException exc) {
            exc.printStackTrace();
        }
        webhook.clearEmbeds();


    }

}
