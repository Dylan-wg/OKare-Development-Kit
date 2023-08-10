package com.okc.odk.Port;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Port {

    public String name;
    public BlockPos pos;
    public String type;
    public int value;

    public Port(String name, BlockPos pos) {
        this.name = name;
        this.pos = pos;
    }

    public void setPos(BlockPos pos) {this.pos = pos;}

    public void setName(String name) {this.name = name;}

    public int getDigital(World world){
        return 0;
    }

    public int getAnalog(World world){return 0;}

    public String[] getDetails(){
        String strPos,strName,strType;
        strPos = "("+this.pos.toShortString()+")";
        strName = this.name;
        strType = this.type;
        String[] details = {strName,strType,strPos};
        return details;
    }

    public Boolean detect(World world){
        return false;
    }

    public void update(World world){}

    public void stateInitialize(World world){}

    public void valueInitialize(World world){}

}
