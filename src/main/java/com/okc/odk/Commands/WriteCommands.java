package com.okc.odk.Commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ObserverBlock;
import net.minecraft.block.RepeaterBlock;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import static com.okc.odk.OdkMain.ODKPOS;
import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandManager.argument;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class WriteCommands {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess){

        dispatcher.register(literal("odk").then(literal("writeROM").then(argument("bits", IntegerArgumentType.integer()).executes(context -> write(IntegerArgumentType.getInteger(context,"bits"),context.getSource().getPlayer(),context.getSource().getWorld(),"ROM")))));
        dispatcher.register(literal("odk").then(literal("writeRAM").then(argument("bits", IntegerArgumentType.integer()).executes(context -> write(IntegerArgumentType.getInteger(context,"bits"),context.getSource().getPlayer(),context.getSource().getWorld(),"RAM")))));

    }

    private static int write(int bits, PlayerEntity player, World world, String type){

        String strBits = String.valueOf(bits);
        BlockState[] state = new BlockState[2];
        Boolean flag = true;
        Direction facing = player.getHorizontalFacing();
        BlockPos writePos = ODKPOS;

        if (strBits.length() == 8){
            for (char c : strBits.toCharArray()){
                if (Character.getNumericValue(c) != 0 && Character.getNumericValue(c) != 1){
                    flag = false;
                    player.sendMessage(Text.literal("The Bits format is non-standard!").setStyle(Style.EMPTY.withColor(Formatting.RED)));
                    break;
                }
            }

            if (flag){
                if (type == "ROM"){
                    state[0] = Blocks.WHITE_STAINED_GLASS.getDefaultState();
                    state[1] = Blocks.OBSERVER.getDefaultState().with(ObserverBlock.FACING,facing);
                }else if (type == "RAM"){
                    state[0] = Blocks.REPEATER.getDefaultState().with(RepeaterBlock.POWERED,false).with(RepeaterBlock.FACING,facing).with(RepeaterBlock.LOCKED,true).with(RepeaterBlock.DELAY,2);
                    state[1] = Blocks.REPEATER.getDefaultState().with(RepeaterBlock.POWERED,true).with(RepeaterBlock.FACING,facing).with(RepeaterBlock.LOCKED,true).with(RepeaterBlock.DELAY,2);
                }

                if (ODKPOS != null){
                    for (int i = 0; i < 8; i++) {
                        world.setBlockState(writePos, state[Character.getNumericValue(strBits.charAt(i))]);
                        int y = writePos.getY() - 2;
                        writePos = new BlockPos(writePos.getX(), y, writePos.getZ());
                    }

                    player.sendMessage(Text.literal("You successfully wrote the data!").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
                }else if (ODKPOS == null){
                    player.sendMessage(Text.literal("ODKPOS is NULL now!").setStyle(Style.EMPTY.withColor(Formatting.RED)));
                }

            }


        }else player.sendMessage(Text.literal("Bits should be 8 bits!").setStyle(Style.EMPTY.withColor(Formatting.RED)));

        return 0;
    }

}
