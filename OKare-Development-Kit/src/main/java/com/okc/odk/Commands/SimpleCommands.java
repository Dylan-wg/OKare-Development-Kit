package com.okc.odk.Commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.block.Blocks;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.item.Items;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import static net.minecraft.server.command.CommandManager.*;
import static com.okc.odk.OdkMain.ODKPOS;

public class SimpleCommands {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess){

        dispatcher.register(literal("odk").executes(context -> {
            context.getSource().getPlayer().sendMessage(Text.literal("OKare Development Kit 1.2 for Minecraft 1.19!").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
            return 0;
        }).then(literal("tool").executes(context -> {
            context.getSource().getPlayer().giveItemStack(Items.IRON_SHOVEL.getDefaultStack());
            context.getSource().getPlayer().sendMessage(Text.literal("You succeeded in acquiring a tool!").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
            return 0;
        })));

        dispatcher.register(literal("odk").then(literal("pos").executes(context -> {
            ODKPOS = context.getSource().getPlayer().getBlockPos();
            context.getSource().getPlayer().sendMessage(Text.literal("ODKPOS ("+ODKPOS.toShortString()+")").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
            return 0;
        })));

        dispatcher.register(literal("odk").then(literal("showPOS").executes(context -> {
            if (ODKPOS != null){
                context.getSource().getPlayer().sendMessage(Text.literal("ODKPOS (" + ODKPOS.toShortString() + ")").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
            }else if (ODKPOS == null){
                context.getSource().getPlayer().sendMessage(Text.literal("ODKPOS is NULL now!").setStyle(Style.EMPTY.withColor(Formatting.RED)));
            }
            return 0;
        })));

        dispatcher.register(literal("odk").then(literal("clear").executes(context -> {
            World world = context.getSource().getWorld();
            BlockPos writePos = ODKPOS;
            for (int i = 0; i < 8; i++) {
                world.setBlockState(writePos, Blocks.AIR.getDefaultState());
                int y = writePos.getY()-2;
                writePos = new BlockPos(writePos.getX(),y,writePos.getZ());
            }
            return 0;
        })));
    }

}
