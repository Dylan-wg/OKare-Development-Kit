package com.okc.odk.Commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.okc.odk.Port.AnalogPort;
import com.okc.odk.Port.DigitalPort;
import com.okc.odk.Port.Port;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.Objects;
import java.util.Set;

import static net.minecraft.server.command.CommandManager.*;
import static com.okc.odk.OdkMain.ODKPOS;
import static com.okc.odk.OdkMain.PORTS;

public class MonitorCommands {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess){



    }

}
