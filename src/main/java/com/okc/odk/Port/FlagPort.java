package com.okc.odk.Port;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FlagPort extends Port{

    private BlockState state;
    public Boolean flagValue;
    public FlagPort(String name, BlockPos pos) {
        super(name, pos);
        this.type = "flag";
        this.flagValue = false;
    }

    @Override
    public String[] getDetails(){
        return super.getDetails();
    }

    @Override
    public Boolean detect(World world){
        if (!this.state.isOf(world.getBlockState(this.pos).getBlock())){
            this.state = world.getBlockState(this.pos);
            this.flagValue = true;
            return true;
        }
        return false;
    }

    @Override
    public void stateInitialize(World world){
        state = world.getBlockState(this.pos);
    }

}
