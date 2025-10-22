package me.juusk.webhookintegration.mixin;

import com.mojang.authlib.GameProfile;
import me.juusk.webhookintegration.WebhookIntegrationClient;
import net.minecraft.client.network.message.MessageHandler;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MessageHandler.class)
public class MessageHandlerMixin {

    @Inject(method = "onChatMessage", at = @At("HEAD"))
    public void onChatMessage(SignedMessage message, GameProfile sender, MessageType.Parameters params, CallbackInfo ci) {
        WebhookIntegrationClient.onChatMessage(message, sender, params);
    }
}