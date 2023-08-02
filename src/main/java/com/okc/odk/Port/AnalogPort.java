package com.okc.odk.Port;

import net.minecraft.block.*;
import net.minecraft.block.entity.ComparatorBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AnalogPort extends Port{
    public AnalogPort(String name, BlockPos pos) {
        super(name, pos);
        this.type = "analog";
    }

    @Override
    public int getAnalog(World world){
        BlockState state = world.getBlockState(this.pos);
        if (state.isOf(Blocks.REDSTONE_WIRE)) {
            return state.get(RedstoneWireBlock.POWER);
        } else if (state.isOf(Blocks.COMPARATOR)) {
            ComparatorBlockEntity tileEntity = (ComparatorBlockEntity) world.getBlockEntity(pos);
            if (tileEntity != null) {
                return tileEntity.getOutputSignal();
            }
            return 0;
        } else if (state.isOf(Blocks.REPEATER)) {
            return state.get(RepeaterBlock.POWERED) ? 15 : 0;
        } else if (state.isOf(Blocks.REDSTONE_LAMP)) {
            return state.get(RedstoneLampBlock.LIT) ? 15 : 0;
        } else if (state.isOf(Blocks.OBSERVER)) {
            return state.get(ObserverBlock.POWERED) ? 15 : 0;
        } else if (state.isOf(Blocks.REDSTONE_TORCH)) {
            return state.get(RedstoneTorchBlock.LIT) ? 15 : 0;
        } else if (state.getBlock() instanceof AbstractButtonBlock) {
            return state.get(AbstractButtonBlock.POWERED) ? 15 : 0;
        } else if (state.isOf(Blocks.LEVER)) {
            return state.get(LeverBlock.POWERED) ? 15 : 0;
        } else if (state.isOf(Blocks.SCULK_SENSOR)) {
            return state.get(SculkSensorBlock.POWER);
        } else if (state.getBlock() instanceof PressurePlateBlock) {
            return state.get(PressurePlateBlock.POWERED) ? 15 : 0;
        } else if (state.getBlock() instanceof DoorBlock) {
            return state.get(DoorBlock.POWERED) ? 15 : 0;
        } else if (state.getBlock() instanceof PistonBlock) {
            return state.get(PistonBlock.EXTENDED) ? 15 : 0;
        } else if (state.getBlock() instanceof TrapdoorBlock) {
            return state.get(TrapdoorBlock.POWERED) ? 15 : 0;
        }
        return 0;
    }

    @Override
    public String[] getDetails(){
        return super.getDetails();
    }

}
