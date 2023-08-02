package com.okc.odk;

import Monitor.Monitor;
import com.okc.odk.Commands.MonitorCommands;
import com.okc.odk.Commands.PortCommand;
import com.okc.odk.Commands.SimpleCommands;
import com.okc.odk.Commands.WriteCommands;

import com.okc.odk.Port.Port;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.item.Items;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class OdkMain implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("odk");
    public static BlockPos ODKPOS = null;
    public static HashMap<String, Monitor> MONITORS = new HashMap<String,Monitor>();
    public static HashMap<String, Port> PORTS = new HashMap<String,Port>();

    @Override
    public void onInitialize() {

        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandRegistryAccess, registrationEnvironment) -> SimpleCommands.register(commandDispatcher,commandRegistryAccess));
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandRegistryAccess, registrationEnvironment) -> WriteCommands.register(commandDispatcher,commandRegistryAccess));
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandRegistryAccess, registrationEnvironment) -> PortCommand.register(commandDispatcher,commandRegistryAccess));
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandRegistryAccess, registrationEnvironment) -> MonitorCommands.register(commandDispatcher,commandRegistryAccess));

        PlayerBlockBreakEvents.BEFORE.register((world, playerEntity, blockPos, blockState, blockEntity) -> {
            if (playerEntity.getMainHandStack().isOf(Items.IRON_SHOVEL)){
                ODKPOS = blockPos;
                playerEntity.sendMessage(Text.literal("ODKPOS (" + ODKPOS.toShortString() + ")").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
                return false;
            }
            return true;
        });

    }
}
