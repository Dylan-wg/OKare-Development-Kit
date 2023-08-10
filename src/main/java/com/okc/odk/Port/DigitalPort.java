package com.okc.odk.Port;

import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DigitalPort extends Port{
    public DigitalPort(String name, BlockPos pos) {
        super(name, pos);
        this.type = "digital";
    }

    @Override
    public int getDigital(World world){
        BlockState state = world.getBlockState(this.pos);
        if (state.isOf(Blocks.REDSTONE_WIRE)) {
            return state.get(RedstoneWireBlock.POWER) == 0 ? 0 : 1;
        } else if (state.isOf(Blocks.REPEATER)) {
            return state.get(RepeaterBlock.POWERED) ? 1 : 0;
        } else if (state.isOf(Blocks.COMPARATOR)) {
            return state.get(ComparatorBlock.POWERED) ? 1 : 0;
        } else if (state.isOf(Blocks.REDSTONE_LAMP)) {
            return state.get(RedstoneLampBlock.LIT) ? 1 : 0;
        } else if (state.isOf(Blocks.OBSERVER)) {
            return state.get(ObserverBlock.POWERED) ? 1 : 0;
        } else if (state.isOf(Blocks.REDSTONE_TORCH)) {
            return state.get(RedstoneTorchBlock.LIT) ? 1 : 0;
        } else if (state.getBlock() instanceof AbstractButtonBlock) {
            return state.get(AbstractButtonBlock.POWERED) ? 1 : 0;
        } else if (state.isOf(Blocks.LEVER)) {
            return state.get(LeverBlock.POWERED) ? 1 : 0;
        } else if (state.isOf(Blocks.SCULK_SENSOR)) {
            return state.get(SculkSensorBlock.POWER) == 0 ? 0 : 1;
        } else if (state.getBlock() instanceof PressurePlateBlock) {
            return state.get(PressurePlateBlock.POWERED) ? 1 : 0;
        } else if (state.getBlock() instanceof DoorBlock) {
            return state.get(DoorBlock.POWERED) ? 1 : 0;
        } else if (state.getBlock() instanceof PistonBlock) {
            return state.get(PistonBlock.EXTENDED) ? 1 : 0;
        } else if (state.getBlock() instanceof TrapdoorBlock) {
            return state.get(TrapdoorBlock.POWERED) ? 1 : 0;
        }
        return 0;
    }

    @Override
    public String[] getDetails(){
        return super.getDetails();
    }

    @Override
    public void update(World world){
        this.value = this.getDigital(world);
    }

    @Override
    public void valueInitialize(World world){
        this.value = this.getDigital(world);
    }

    @Override
    public Boolean detect(World world){
        if (this.value != this.getDigital(world)){
            this.update(world);
            return true;
        }
        return false;
    }

}
