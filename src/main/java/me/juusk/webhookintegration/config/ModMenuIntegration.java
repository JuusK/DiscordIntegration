package me.juusk.webhookintegration.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.ColorControllerBuilder;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import me.juusk.webhookintegration.WebhookIntegrationClient;
import net.minecraft.text.Text;

import java.awt.*;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parentScreen -> YetAnotherConfigLib.createBuilder()
                .title(Text.literal("WebhookIntegration"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Webhook Integration"))
                        .tooltip(Text.literal("Webhook Integration"))
                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("General"))
                                .description(OptionDescription.of(Text.literal("General options")))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Enabled"))
                                        .description(OptionDescription.of(Text.literal("Turns the mod on/off.")))
                                        .binding(true, () -> WebhookIntegrationClient.enabled, newVal -> WebhookIntegrationClient.enabled = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<String>createBuilder()
                                        .name(Text.literal("Webhook URL"))
                                        .description(OptionDescription.of(Text.literal("The URL for the Webhook to send the messages to.")))
                                        .binding("", () -> WebhookIntegrationClient.getWebhookUrl(), newVal -> WebhookIntegrationClient.setWebhookUrl(newVal))
                                        .controller(StringControllerBuilder::create)
                                        .build())
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Message"))
                                .description(OptionDescription.of(Text.literal("Settings related to the message that gets sent to the Webhook.")))
                                .option(Option.<String>createBuilder()
                                        .name(Text.literal("Message Title"))
                                        .description(OptionDescription.of(Text.literal("Title of the message the webhook sends when you die.")))
                                        .binding("You died, {name}", () -> WebhookIntegrationClient.messageTitle, newVal -> WebhookIntegrationClient.messageTitle = newVal)
                                        .controller(StringControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Coordinates"))
                                        .description(OptionDescription.of(Text.literal("Toggles showing coordinates in the webhook message.")))
                                        .binding(true, () -> WebhookIntegrationClient.messageCoordinates, newVal -> WebhookIntegrationClient.messageCoordinates = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("World Name"))
                                        .description(OptionDescription.of(Text.literal("Toggles showing the world name in the webhook message.")))
                                        .binding(true, () -> WebhookIntegrationClient.messageWorldName, newVal -> WebhookIntegrationClient.messageWorldName = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())

                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Mention"))
                                        .description(OptionDescription.of(Text.literal("Toggles mentioning on/off.")))
                                        .binding(false, () -> WebhookIntegrationClient.mention, newVal -> WebhookIntegrationClient.mention = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<String>createBuilder()
                                        .name(Text.literal("User ID"))
                                        .description(OptionDescription.of(Text.literal("Your User ID, get it by enabling developer mode, right clicking on yourself and then clicking Copy User ID.")))
                                        .binding("", () -> WebhookIntegrationClient.userId, newVal -> WebhookIntegrationClient.userId = newVal)
                                        .controller(StringControllerBuilder::create)
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Text.literal("Color"))
                                        .description(OptionDescription.of(Text.literal("Color of the embed.")))
                                        .binding(Color.RED, () -> WebhookIntegrationClient.embedColor, newVal -> WebhookIntegrationClient.embedColor = newVal)
                                        .controller(ColorControllerBuilder::create)
                                        .build())
                                .build())

                        .build())
                .build().generateScreen(parentScreen);
    }
}
