package com.okc.odk.Monitor;

import com.okc.odk.Port.FlagPort;
import com.okc.odk.Port.Port;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class Monitor {

    public String name;
    public HashMap<String, Port> Ports = new HashMap<String,Port>();
    public Boolean judgement = false;
    public Port startPort = null;
    public Port stopPort = null;
    public int tick = -1;
    public LinkedHashMap<String[],String> Data = new LinkedHashMap<String[],String>();
    public boolean isEnabled;
    public static boolean enableWrite = true;
    public boolean isRegistry = false;
    public boolean startRegistry = false;
    public boolean stopRegistry = false;
    public boolean saveEnable = false;
    public Monitor(String name) {
        this.isEnabled = false;
        this.name = name;
    }

    public void setName(String name){
        this.name =  name;
    }

    public void addPort(Port port){
        this.Ports.put(port.name,port);
    }

    public void removePort(Port port){
        this.Ports.remove(port.name);
    }

    public void start(PlayerEntity player,World world){
        this.Data.clear();
        this.tick = -1;  //tick reset
        if (this.judgement){
            this.saveEnable = this.isEnabled;
            this.isEnabled = false;
            player.sendMessage(Text.literal("Monitor "+name+" started.").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
            if (!this.isRegistry){
                ClientTickEvents.START_CLIENT_TICK.register(playerEntity -> {
                    if (this.judgement) {
                        tick++;

                        //Asynchronous thread
                        CompletableFuture.runAsync(() -> {
                            for (Port p : this.Ports.values()) {
                                if (p.detect(world)) {
                                    if (p instanceof FlagPort) {
                                        String[] dKey = {String.valueOf(this.tick), p.name};
                                        this.Data.put(dKey, String.valueOf(((FlagPort) p).flagValue));
                                    } else {
                                        String[] dKey = {String.valueOf(this.tick), p.name};
                                        this.Data.put(dKey, String.valueOf(p.value));
                                    }
                                }
                            }
                        });

                    }
                });
            }
        }
    }

    public void stop(PlayerEntity player,World world){
        if (this.judgement){
            this.judgement = false;
            this.isRegistry = true;
            this.isEnabled = true;
        }
        player.sendMessage(Text.literal("Monitor "+name+" stopped.").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
    }

    public void setStartFlag(Port fp){
        this.startPort = fp;
    }

    public void setStopFlag(Port fp){
        this.stopPort = fp;
    }

    public void enabled(){
        this.isEnabled = true;
    }

    public void disabled(){
        this.isEnabled = false;
    }

    public void flagProcess(PlayerEntity player,World world){
        if (this.isEnabled){

            if (!startRegistry){
                ClientTickEvents.END_CLIENT_TICK.register(playerEntity -> {
                    CompletableFuture.runAsync(() -> {
                        if (this.isEnabled) {
                            if (this.startPort.detect(world) && this.startPort != null && !this.judgement) {
                                this.judgement = true;
                                this.start(player, world);
                                this.isEnabled = false;
                            }
                        }
                    });
                });
            }

            if (!stopRegistry){
                ClientTickEvents.END_CLIENT_TICK.register(playerEntity -> {
                    CompletableFuture.runAsync(() -> {
                        if (!this.isEnabled) {
                            if (this.stopPort.detect(world) && this.stopPort != null && this.judgement) {
                                this.stop(player, world);
                                this.isEnabled = true;
                            }
                        }
                    });
                });
            }

        }else {
            player.sendMessage(Text.literal("Flags are disabled.").setStyle(Style.EMPTY.withColor(Formatting.RED)));
        }
    }

    public Boolean saveData(String filename){

       String dPath = "config\\odk\\monitorData";
       Path fPath = Paths.get(dPath);
       String[] preS = null;

       if (!Files.exists(fPath)){
           try {
               Files.createDirectories(fPath);
               enableWrite = true;
           }catch (IOException e){
               enableWrite = false;
           }
       }

        String path = "config\\odk\\monitorData\\"+filename+".txt";
        if (enableWrite){
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(path));

                writer.write("Information:\n");
                writer.write("  [name] " + this.name + " \n");
                writer.write("  [ports] { ");
                for (Port p : this.Ports.values()) {
                    writer.write(p.name + " ");
                }
                writer.write("}\n");
                if (this.startPort != null) {
                    writer.write("  [startFlag] " + this.startPort.name + "\n");
                }
                if (this.stopPort != null) {
                    writer.write("  [stopFlag] " + this.stopPort.name + "\n");
                }
                writer.write("  [flagEnable] "+String.valueOf(this.saveEnable));
                writer.write("\n<start>");
                for (String[] s : this.Data.keySet()) {
                    if (preS == null || !Objects.equals(preS[0],s[0])){
                        writer.write("\n  " + s[0] + "t. " + s[1] + " -> " + this.Data.get(s));
                    } else if (preS != null || Objects.equals(preS[0],s[0])) {
                        writer.write("\n");
                        for (int i = 0;i < s[0].length()+5;i++){
                            writer.write(" ");
                        }
                        writer.write(s[1]+" -> "+this.Data.get(s));
                    }
                    preS = s;
                }
                writer.write("\n<stop>");
                writer.write("\n\n[total time] " + String.valueOf(this.tick) + "t");
                writer.close();

                return true;
            } catch (IOException e) {
                return false;
            }
        } else  {
            return false;
        }
    }

}
