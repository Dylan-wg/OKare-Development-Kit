package com.okc.odk.Port;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
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
        } else if (state.getBlock() instanceof ShulkerBoxBlock || state.getBlock() instanceof JukeboxBlock || state.getBlock() instanceof HopperBlock || state.getBlock() instanceof BarrelBlock || state.getBlock() instanceof ChestBlock || state.getBlock() instanceof DropperBlock || state.getBlock() instanceof DispenserBlock || state.getBlock() instanceof FurnaceBlock || state.getBlock() instanceof BlastFurnaceBlock) {
            BlockEntity blockEntity = world.getBlockEntity(this.pos);
            return blockEntity.getCachedState().getComparatorOutput(world,this.pos);
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
        this.value = this.getAnalog(world);
    }

    @Override
    public Boolean detect(World world){
        if (this.value != this.getAnalog(world)){
            this.update(world);
            return true;
        }
        return false;
    }

}
