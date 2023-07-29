package com.okc.odk;

import com.okc.odk.Commands.SimpleCommands;
import com.okc.odk.Commands.WriteCommands;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Items;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OdkMain implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("odk");
    public static BlockPos ODKPOS = null;

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register(((commandDispatcher, commandRegistryAccess, registrationEnvironment) -> SimpleCommands.register(commandDispatcher,commandRegistryAccess)));

        CommandRegistrationCallback.EVENT.register(((commandDispatcher, commandRegistryAccess, registrationEnvironment) -> WriteCommands.register(commandDispatcher,commandRegistryAccess)));

        PlayerBlockBreakEvents.BEFORE.register((world, playerEntity, blockPos, blockState, blockEntity) -> {
            if (playerEntity.getMainHandStack().isOf(Items.IRON_SHOVEL)){
                ODKPOS = blockPos;
                playerEntity.sendMessage(Text.literal("ODKPOS (" + ODKPOS.toShortString() + ")").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
                return false;
            }
            return true;
        });

        ClientLifecycleEvents.CLIENT_STARTED.register((player) -> {
            ODKPOS = null;
        });

    }
}
