package com.okc.odk.Mixin;

import net.minecraft.block.Blocks;
import net.minecraft.block.ObserverBlock;
import net.minecraft.block.RepeaterBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.item.ShovelItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(ShovelItem.class)
public class ToolMixin {

    @Inject(at = @At("HEAD"),method = "useOnBlock",cancellable = true)
    public void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir){
        if (context.getPlayer().getMainHandStack().isOf(Items.IRON_SHOVEL) && (!context.getPlayer().isSneaking())){
            PlayerEntity player = context.getPlayer();
            World world = context.getWorld();
            BlockPos pos = context.getBlockPos();

            //write ROM
            if (world.getBlockState(pos).isOf(Blocks.OBSERVER)){
                world.setBlockState(pos,Blocks.WHITE_STAINED_GLASS.getDefaultState());
                cir.setReturnValue(ActionResult.SUCCESS);
            }else if (world.getBlockState(pos).isOf(Blocks.WHITE_STAINED_GLASS)){
                Direction direction = player.getHorizontalFacing();
                world.setBlockState(pos,Blocks.OBSERVER.getDefaultState().with(ObserverBlock.FACING,direction));
                cir.setReturnValue(ActionResult.SUCCESS);
            }

            //write RAM
            if (world.getBlockState(pos).isOf(Blocks.REPEATER)){
                boolean powered = world.getBlockState(pos).get(RepeaterBlock.POWERED);
                Direction facing = world.getBlockState(pos).get(RepeaterBlock.FACING);
                world.setBlockState(pos,Blocks.REPEATER.getDefaultState().with(RepeaterBlock.POWERED,!powered).with(RepeaterBlock.FACING,facing).with(RepeaterBlock.DELAY,2).with(RepeaterBlock.LOCKED,true));
            }

            cir.setReturnValue(ActionResult.PASS);
        }
    }

}
