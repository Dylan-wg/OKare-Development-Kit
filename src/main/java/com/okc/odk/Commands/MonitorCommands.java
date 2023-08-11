package com.okc.odk.Commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.okc.odk.Monitor.Monitor;
import com.okc.odk.Port.AnalogPort;
import com.okc.odk.Port.Port;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static com.okc.odk.OdkMain.*;
import static net.minecraft.server.command.CommandManager.*;
import static com.okc.odk.Monitor.Monitor.enableWrite;

public class MonitorCommands {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess){

        //odk monitor create <name>
        dispatcher.register(literal("odk").then(literal("monitor").then(literal("create").then(argument("name",StringArgumentType.string()).executes(context -> {
            String name = StringArgumentType.getString(context,"name");
            if (!MONITORS.containsKey(name)){
                MONITORS.put(name, new Monitor(name));
                context.getSource().getPlayer().sendMessage(Text.literal("Monitor "+name+" was created.").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
            } else if (MONITORS.containsKey(name)) {
                context.getSource().getPlayer().sendMessage(Text.literal("Monitor "+name+" is already exists.").setStyle(Style.EMPTY.withColor(Formatting.RED)));
            }
            return 0;
        })))));

        //odk monitor add <port name>
        dispatcher.register(literal("odk").then(literal("monitor").then(literal("add").then(argument("name",StringArgumentType.string()).then(argument("port name",StringArgumentType.string()).executes(context -> {
            String pName = StringArgumentType.getString(context,"port name");
            String name = StringArgumentType.getString(context,"name");
            if (MONITORS.containsKey(name) && PORTS.containsKey(pName)){
                MONITORS.get(name).addPort(PORTS.get(pName));
                context.getSource().getPlayer().sendMessage(Text.literal("Port "+pName+" was added.").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
            } else if (!MONITORS.containsKey(name) || !PORTS.containsKey(pName)) {
                context.getSource().getPlayer().sendMessage(Text.literal("Some of them may not exists.").setStyle(Style.EMPTY.withColor(Formatting.RED)));
            }
            return 0;
        }))))));

        //odk monitor remove <name> <port name>
        dispatcher.register(literal("odk").then(literal("monitor").then(literal("remove").then(argument("name",StringArgumentType.string()).then(argument("port name",StringArgumentType.string()).executes(context -> {
            String pName = StringArgumentType.getString(context,"port name");
            String name = StringArgumentType.getString(context,"name");
            if (MONITORS.containsKey(name) && PORTS.containsKey(pName)){
                MONITORS.get(name).removePort(PORTS.get(pName));
                context.getSource().getPlayer().sendMessage(Text.literal("Port "+pName+" was removed.").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
            } else if (!MONITORS.containsKey(name) || !PORTS.containsKey(pName)) {
                context.getSource().getPlayer().sendMessage(Text.literal("Some of them may not exists.").setStyle(Style.EMPTY.withColor(Formatting.RED)));
            }
            return 0;
        }))))));

        //odk monitor reset <name>
        dispatcher.register(literal("odk").then(literal("monitor").then(literal("reset").then(argument("name",StringArgumentType.string()).executes(context -> {
            String name = StringArgumentType.getString(context,"name");
            if (MONITORS.containsKey(name)){
                MONITORS.remove(name);
                MONITORS.put(name,new Monitor(name));
                context.getSource().getPlayer().sendMessage(Text.literal("Monitor "+name+" was reset.").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
            } else if (!MONITORS.containsKey(name)) {
                context.getSource().getPlayer().sendMessage(Text.literal("Monitor "+name+" is not exist.").setStyle(Style.EMPTY.withColor(Formatting.RED)));
            }
            return 0;
        })))));

        //odk monitor rename <old name> <new name>
        dispatcher.register(literal("odk").then(literal("monitor").then(literal("rename").then(argument("old name",StringArgumentType.string()).then(argument("new name",StringArgumentType.string()).executes(context -> {
            String oName = StringArgumentType.getString(context,"old name");
            String nName = StringArgumentType.getString(context,"new name");
            if (MONITORS.containsKey(oName) && !MONITORS.containsKey(nName)) {
                Monitor m = MONITORS.get(oName);
                m.setName(nName);
                MONITORS.remove(oName);
                MONITORS.put(nName,m);
                context.getSource().getPlayer().sendMessage(Text.literal("Monitor "+oName+" was renamed.").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
            } else if (!MONITORS.containsKey(oName) || MONITORS.containsKey(nName)) {
                context.getSource().getPlayer().sendMessage(Text.literal("Monitor rename failed.").setStyle(Style.EMPTY.withColor(Formatting.RED)));
            }
            return 0;
        }))))));

        //odk monitor delete <name>
        dispatcher.register(literal("odk").then(literal("monitor").then(literal("delete").then(argument("name",StringArgumentType.string()).executes(context -> {
            String name = StringArgumentType.getString(context,"name");
            if (MONITORS.containsKey(name)){
                MONITORS.remove(name);
                context.getSource().getPlayer().sendMessage(Text.literal("Monitor "+name+" was deleted.").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
            } else if (!MONITORS.containsKey(name)) {
                context.getSource().getPlayer().sendMessage(Text.literal("Monitor "+name+" is not exist.").setStyle(Style.EMPTY.withColor(Formatting.RED)));
            }
            return 0;
        })))));

        //odk monitor delete all
        dispatcher.register(literal("odk").then(literal("monitor").then(literal("delete").then(literal("all").executes(context -> {
            if (!MONITORS.isEmpty()){
                MONITORS.clear();
                context.getSource().getPlayer().sendMessage(Text.literal("All the monitors was deleted.").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
            } else if (MONITORS.isEmpty()) {
                context.getSource().getPlayer().sendMessage(Text.literal("No monitor exist.").setStyle(Style.EMPTY.withColor(Formatting.RED)));
            }
            return 0;
        })))));

        //odk monitor showDetails <name>
        dispatcher.register(literal("odk").then(literal("monitor").then(literal("showDetails").then(argument("name",StringArgumentType.string()).executes(context -> {
            PlayerEntity player = context.getSource().getPlayer();
            String name = StringArgumentType.getString(context,"name");
            if (MONITORS.containsKey(name)){
                player.sendMessage(Text.literal("Monitor Information:").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
                player.sendMessage(Text.literal("  [name] " + name).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
                String sp = "  [ports] { ";
                for (Port p : MONITORS.get(name).Ports.values()) {
                    sp = sp + p.name+" ";
                }
                sp = sp + "}";
                player.sendMessage(Text.literal(sp).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
                if (MONITORS.get(name).startPort != null){
                    player.sendMessage(Text.literal("  [start flag] " + MONITORS.get(name).startPort.name).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
                }
                if (MONITORS.get(name).stopPort != null){
                    player.sendMessage(Text.literal("  [stop flag] " + MONITORS.get(name).stopPort.name).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
                }
            } else if (!MONITORS.containsKey(name)) {
                player.sendMessage(Text.literal("Monitor "+name+" is not exist.").setStyle(Style.EMPTY.withColor(Formatting.RED)));
            }
            return 0;
        })))));

        //odk monitor setStartFlag <name> <flag name>
        dispatcher.register(literal("odk").then(literal("monitor").then(literal("setStartFlag").then(argument("name",StringArgumentType.string()).then(argument("flag name",StringArgumentType.string()).executes(context -> {
            String name = StringArgumentType.getString(context,"name");
            String fName = StringArgumentType.getString(context,"flag name");
            if (MONITORS.containsKey(name) && PORTS.containsKey(fName)){
                MONITORS.get(name).setStartFlag(PORTS.get(fName));
                if (!MONITORS.get(name).Ports.containsKey(fName)){
                    MONITORS.get(name).addPort(PORTS.get(fName));
                }
                context.getSource().getPlayer().sendMessage(Text.literal("Flag port was set.").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
            } else if (!MONITORS.containsKey(name) || !PORTS.containsKey(fName)) {
                context.getSource().getPlayer().sendMessage(Text.literal("Some of them may not exist.").setStyle(Style.EMPTY.withColor(Formatting.RED)));
            }
            return 0;
        }))))));

        //odk monitor setStopFlag <name> <flag name>
        dispatcher.register(literal("odk").then(literal("monitor").then(literal("setStopFlag").then(argument("name",StringArgumentType.string()).then(argument("flag name",StringArgumentType.string()).executes(context -> {
            String name = StringArgumentType.getString(context,"name");
            String fName = StringArgumentType.getString(context,"flag name");
            if (MONITORS.containsKey(name) && PORTS.containsKey(fName)){
                MONITORS.get(name).setStopFlag(PORTS.get(fName));
                if (!MONITORS.get(name).Ports.containsKey(fName)){
                    MONITORS.get(name).addPort(PORTS.get(fName));
                }
                context.getSource().getPlayer().sendMessage(Text.literal("Flag port was set.").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
            } else if (!MONITORS.containsKey(name) || !PORTS.containsKey(fName)) {
                context.getSource().getPlayer().sendMessage(Text.literal("Some of them may not exist.").setStyle(Style.EMPTY.withColor(Formatting.RED)));
            }
            return 0;
        }))))));

        //odk monitor start <name>
        dispatcher.register(literal("odk").then(literal("monitor").then(literal("start").then(argument("name",StringArgumentType.string()).executes(context -> {
            String name = StringArgumentType.getString(context,"name");
            if (MONITORS.containsKey(name)){
                MONITORS.get(name).start(context.getSource().getPlayer(),context.getSource().getWorld());
            } else if (!MONITORS.containsKey(name)) {
                context.getSource().getPlayer().sendMessage(Text.literal("Monitor "+name+" is not exist."));
            }
            return 0;
        })))));

        //odk monitor stop <name>
        dispatcher.register(literal("odk").then(literal("monitor").then(literal("stop").then(argument("name",StringArgumentType.string()).executes(context -> {
            String name = StringArgumentType.getString(context,"name");
            if (MONITORS.containsKey(name)){
                MONITORS.get(name).stop(context.getSource().getPlayer(),context.getSource().getWorld());
            } else if (!MONITORS.containsKey(name)) {
                context.getSource().getPlayer().sendMessage(Text.literal("Monitor "+name+" is not exist."));
            }
            return 0;
        })))));

        //odk monitor flagEnable <name>
        dispatcher.register(literal("odk").then(literal("monitor").then(literal("flagEnable").then(argument("name",StringArgumentType.string()).executes(context -> {
            String name = StringArgumentType.getString(context,"name");
            if (MONITORS.containsKey(name)){
                MONITORS.get(name).enabled();
                MONITORS.get(name).flagProcess(context.getSource().getPlayer(),context.getSource().getWorld());
                context.getSource().getPlayer().sendMessage(Text.literal("Flags were enabled.").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
            } else if (!MONITORS.containsKey(name)) {
                context.getSource().getPlayer().sendMessage(Text.literal("Monitor "+name+" is not exist.").setStyle(Style.EMPTY.withColor(Formatting.RED)));
            }
            return 0;
        })))));

        //odk monitor flagDisable <name>
        dispatcher.register(literal("odk").then(literal("monitor").then(literal("flagDisable").then(argument("name",StringArgumentType.string()).executes(context -> {
            String name = StringArgumentType.getString(context,"name");
            if (MONITORS.containsKey(name)){
                MONITORS.get(name).disabled();
                context.getSource().getPlayer().sendMessage(Text.literal("Flags were disabled.").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
            } else if (!MONITORS.containsKey(name)) {
                MONITORS.containsKey(name);
                context.getSource().getPlayer().sendMessage(Text.literal("Monitor "+name+" is not exist.").setStyle(Style.EMPTY.withColor(Formatting.RED)));
            }
            return 0;
        })))));

        //odk monitor start <name>
        dispatcher.register(literal("odk").then(literal("monitor").then(literal("start").then(argument("name",StringArgumentType.string()).executes(context -> {
            String name = StringArgumentType.getString(context,"name");
            if (MONITORS.containsKey(name)){
                MONITORS.get(name).judgement = true;
                MONITORS.get(name).start(context.getSource().getPlayer(),context.getSource().getWorld());
            } else if (!MONITORS.containsKey(name)) {
                context.getSource().getPlayer().sendMessage(Text.literal("Monitor "+name+" is not exist.").setStyle(Style.EMPTY.withColor(Formatting.RED)));
            }
            return 0;
        })))));

        //odk monitor stop <name>
        dispatcher.register(literal("odk").then(literal("monitor").then(literal("stop").then(argument("name",StringArgumentType.string()).executes(context -> {
            String name = StringArgumentType.getString(context,"name");
            if (MONITORS.containsKey(name)){
                MONITORS.get(name).stop(context.getSource().getPlayer(),context.getSource().getWorld());
            } else if (!MONITORS.containsKey(name)) {
                context.getSource().getPlayer().sendMessage(Text.literal("Monitor "+name+" is not exist.").setStyle(Style.EMPTY.withColor(Formatting.RED)));
            }
            return 0;
        })))));

        //odk monitor saveData <name> <filename>
        dispatcher.register(literal("odk").then(literal("monitor").then(literal("saveData").then(argument("name",StringArgumentType.string()).then(argument("filename",StringArgumentType.string()).executes(context -> {
            String name = StringArgumentType.getString(context,"name");
            String filename = StringArgumentType.getString(context,"filename");
            if (MONITORS.containsKey(name)){
                if (MONITORS.get(name).saveData(filename) && enableWrite){
                    context.getSource().getPlayer().sendMessage(Text.literal("Data was saved.").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
                }else {
                    context.getSource().getPlayer().sendMessage(Text.literal("Failed.").setStyle(Style.EMPTY.withColor(Formatting.RED)));
                }
            } else if (!MONITORS.containsKey(name)) {
                context.getSource().getPlayer().sendMessage(Text.literal("Monitor "+name+" is not exist.").setStyle(Style.EMPTY.withColor(Formatting.RED)));
            }
            return 0;
        }))))));

        //odk monitor saveData <name>
        dispatcher.register(literal("odk").then(literal("monitor").then(literal("saveData").then(argument("name",StringArgumentType.string()).executes(context -> {
            String name = StringArgumentType.getString(context,"name");
            String filename = name;
            if (MONITORS.containsKey(name)){
                if (MONITORS.get(name).saveData(filename) && enableWrite){
                    context.getSource().getPlayer().sendMessage(Text.literal("Data was saved.").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
                }else {
                    context.getSource().getPlayer().sendMessage(Text.literal("Failed.").setStyle(Style.EMPTY.withColor(Formatting.RED)));
                }
            } else if (!MONITORS.containsKey(name)) {
                context.getSource().getPlayer().sendMessage(Text.literal("Monitor "+name+" is not exist.").setStyle(Style.EMPTY.withColor(Formatting.RED)));
            }
            return 0;
        })))));

        //odk monitor list
        dispatcher.register(literal("odk").then(literal("monitor").then(literal("list").executes(context -> {
            context.getSource().getPlayer().sendMessage(Text.literal("Monitor list:").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
            if (!MONITORS.isEmpty()){
                for (Monitor m : MONITORS.values()) {
                    context.getSource().getPlayer().sendMessage(Text.literal("  " + m.name).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
                }
            } else if (MONITORS.isEmpty()) {
                context.getSource().getPlayer().sendMessage(Text.literal("No monitor exist.").setStyle(Style.EMPTY.withColor(Formatting.RED)));
            }
            return 0;
        }))));

    }
}
