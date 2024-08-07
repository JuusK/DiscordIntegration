package me.juusk.webhookintegration;

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


    public static Color embedColor = Color.RED;

    private static String webhookUrl = "";
    public static String messageTitle = "You died, {name}";
    public static String userId = "";

    public static boolean messageCoordinates = true;
    public static boolean messageWorldName = true;
    public static boolean enabled = true;
    public static boolean mention = false;

    public static DiscordWebhook webhook;
    public  static WebhookIntegrationClient INSTANCE = new WebhookIntegrationClient();



    @Override
    public void onInitializeClient() {

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
        });
    }

    /*public Screen createConfig(Screen parentScreen) {
        YetAnotherConfigLib.createBuilder()
                .title(Text.literal("WebhookIntegration"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("General"))
                        .tooltip(Text.literal("General options"))
                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Name of the group"))
                                .description(OptionDescription.of(Text.literal("This text will appear when you hover over the name or focus on the collapse button with Tab.")))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Enabled"))
                                        .description(OptionDescription.of(Text.literal("This text will appear as a tooltip when you hover over the option.")))
                                        .binding(true, () -> this.enabled, newVal -> this.enabled = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())
                        .build())
                .build().generateScreen(parentScreen);
        return parentScreen;
    }*/


    public void onOpenScreen(Screen screen) {
        assert MinecraftClient.getInstance().player != null;
        if (webhook == null) return;
        if (screen instanceof DeathScreen) {
            sendDeathMessage();

        }
    }


    public static void setWebhookUrl(String url) {
        webhookUrl = url;
        onWebhookUrlChanged();
    }
    public static String getWebhookUrl() {
        return webhookUrl;
    }

    private static void onWebhookUrlChanged() {
        if(!webhookUrl.isEmpty()) {
            webhook = new DiscordWebhook(webhookUrl);
        }
    }
    public static void sendDeathMessage() {
        assert MinecraftClient.getInstance().player != null;
        if (webhook == null) return;

        if(mention == true) {
            webhook.setContent("<@" + userId + ">");
        }
        String title = messageTitle;
        if(title.contains("{name}")) {
            title = title.replace("{name}", MinecraftClient.getInstance().player.getName().getString());
            System.out.println("Replaced {name} with playername");
        }



        DiscordWebhook.EmbedObject embedObject = new DiscordWebhook.EmbedObject();
        embedObject.setTitle(title);
        embedObject.setColor(embedColor);
        DecimalFormat df = new DecimalFormat("###.###");
        if(messageCoordinates) {
            embedObject.addField("Coordinates:", "X: " + df.format(MinecraftClient.getInstance().player.getX()) + " Y: " + df.format(MinecraftClient.getInstance().player.getY()) + " Z: " + df.format(MinecraftClient.getInstance().player.getZ()), false);
        }
        if(messageWorldName) {
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
