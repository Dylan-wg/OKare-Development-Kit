package com.okc.odk.Commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.okc.odk.Monitor.Monitor;
import com.okc.odk.Port.AnalogPort;
import com.okc.odk.Port.DigitalPort;
import com.okc.odk.Port.FlagPort;
import com.okc.odk.Port.Port;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;
import java.util.Set;

import static com.okc.odk.OdkMain.*;
import static net.minecraft.server.command.CommandManager.*;

public class PortCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess){

        //odk port set <name> [<type>]
        dispatcher.register(literal("odk").then(literal("port").then(literal("set").then(argument("name",StringArgumentType.string()).then(argument("type",StringArgumentType.string()).suggests((context, builder) -> {
            builder.suggest("digital");
            builder.suggest("analog");
            builder.suggest("flag");
            return builder.buildFuture();
        }).executes(context -> {
            if (Objects.equals(StringArgumentType.getString(context, "type"), "digital") && ODKPOS != null && !PORTS.containsKey(StringArgumentType.getString(context,"name"))){
                PORTS.put(StringArgumentType.getString(context,"name"),new DigitalPort(StringArgumentType.getString(context,"name"),ODKPOS));
                PORTS.get(StringArgumentType.getString(context,"name")).valueInitialize(context.getSource().getWorld());
                context.getSource().getPlayer().sendMessage(Text.literal("Port "+StringArgumentType.getString(context,"name")+" was created.").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
            } else if (Objects.equals(StringArgumentType.getString(context, "type"), "analog") && ODKPOS != null && !PORTS.containsKey(StringArgumentType.getString(context,"name"))) {
                PORTS.put(StringArgumentType.getString(context,"name"),new AnalogPort(StringArgumentType.getString(context,"name"),ODKPOS));
                PORTS.get(StringArgumentType.getString(context,"name")).valueInitialize(context.getSource().getWorld());
                context.getSource().getPlayer().sendMessage(Text.literal("Port "+StringArgumentType.getString(context,"name")+" was created.").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
            } else if (ODKPOS == null) {
                context.getSource().getPlayer().sendMessage(Text.literal("ODKPOS is NULL now!").setStyle(Style.EMPTY.withColor(Formatting.RED)));
            } else if (PORTS.containsKey(StringArgumentType.getString(context,"name"))) {
                context.getSource().getPlayer().sendMessage(Text.literal("Port "+StringArgumentType.getString(context,"name")+" is already exists!").setStyle(Style.EMPTY.withColor(Formatting.RED)));
            } else if (Objects.equals(StringArgumentType.getString(context, "type"), "flag") && ODKPOS != null && !PORTS.containsKey(StringArgumentType.getString(context,"name"))) {
                PORTS.put(StringArgumentType.getString(context, "name"),new FlagPort(StringArgumentType.getString(context, "name"),ODKPOS));
                PORTS.get(StringArgumentType.getString(context,"name")).stateInitialize(context.getSource().getWorld());
                context.getSource().getPlayer().sendMessage(Text.literal("Port "+StringArgumentType.getString(context,"name")+" was created.").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
            } else if (!Objects.equals(StringArgumentType.getString(context, "type"), "digital") && !Objects.equals(StringArgumentType.getString(context,"type"),"analog") && !Objects.equals(StringArgumentType.getString(context,"type"),"flag")){
                context.getSource().getPlayer().sendMessage(Text.literal("Type error.").setStyle(Style.EMPTY.withColor(Formatting.RED)));
            }
            return 0;
        }))))));

        //odk port remove <name>
        dispatcher.register(literal("odk").then(literal("port").then(literal("remove").then(argument("name",StringArgumentType.string()).executes(context -> {
            if (PORTS.containsKey(StringArgumentType.getString(context,"name"))){
                PORTS.remove(StringArgumentType.getString(context, "name"));
                context.getSource().getPlayer().sendMessage(Text.literal("Port " + StringArgumentType.getString(context, "name") + " was removed.").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
                for (Monitor m : MONITORS.values()){
                    if (m.Ports.containsKey(StringArgumentType.getString(context,"name"))){
                        m.Ports.remove(StringArgumentType.getString(context, "name"));
                    }
                }
                context.getSource().getPlayer().sendMessage(Text.literal("The port associated with monitor has been removed.").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
            } else if (!PORTS.containsKey(StringArgumentType.getString(context,"name"))) {
                context.getSource().getPlayer().sendMessage(Text.literal("Port "+StringArgumentType.getString(context,"name")+" is not exist.").setStyle(Style.EMPTY.withColor(Formatting.RED)));
            }
            return 0;
        })))));

        //odk port remove all
        dispatcher.register(literal("odk").then(literal("port").then(literal("remove").then(literal("all").executes(context -> {
            if (!PORTS.isEmpty()){
                PORTS.clear();
                for (Monitor m : MONITORS.values()){
                    m.Ports.clear();
                }
                context.getSource().getPlayer().sendMessage(Text.literal("All the ports were removed.").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
            } else if (PORTS.isEmpty()) {
                context.getSource().getPlayer().sendMessage(Text.literal("No port exist.").setStyle(Style.EMPTY.withColor(Formatting.RED)));
            }
            return 0;
        })))));

        //odk port get <name>
        dispatcher.register(literal("odk").then(literal("port").then(literal("get").then(argument("name",StringArgumentType.string()).executes(context -> {
            if (PORTS.containsKey(StringArgumentType.getString(context,"name"))) {
                Port p = PORTS.get(StringArgumentType.getString(context, "name"));
                int output;
                if (p instanceof DigitalPort){
                    output = p.getDigital(context.getSource().getWorld());
                    context.getSource().getPlayer().sendMessage(Text.literal("Port " + StringArgumentType.getString(context, "name") + " -> " + String.valueOf(output)).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
                } else if (p instanceof AnalogPort) {
                    output = p.getAnalog(context.getSource().getWorld());
                    context.getSource().getPlayer().sendMessage(Text.literal("Port " + StringArgumentType.getString(context, "name") + " -> " + String.valueOf(output)).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
                }
            } else {
                context.getSource().getPlayer().sendMessage(Text.literal("Port "+StringArgumentType.getString(context,"name")+" is not exist!").setStyle(Style.EMPTY.withColor(Formatting.RED)));
            }
            return 0;
        })))));

        //odk port reset <name>
        dispatcher.register(literal("odk").then(literal("port").then(literal("reset").then(argument("name",StringArgumentType.string()).executes(context -> {
            String name = StringArgumentType.getString(context,"name");
            if (PORTS.containsKey(name)){
                PORTS.get(name).setPos(ODKPOS);
                context.getSource().getPlayer().sendMessage(Text.literal("Port "+name+" was reset.").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
            } else if (!PORTS.containsKey(name)) {
                context.getSource().getPlayer().sendMessage(Text.literal("Port "+name+" is not exist.").setStyle(Style.EMPTY.withColor(Formatting.RED)));
            }
            return 0;
        })))));

        //odk port reset <name> [<type>]
        dispatcher.register(literal("odk").then(literal("port").then(literal("reset").then(argument("name",StringArgumentType.string()).then(argument("type",StringArgumentType.string()).suggests(((context, builder) -> {
            builder.suggest("digital");
            builder.suggest("analog");
            builder.suggest("flag");
            return builder.buildFuture();
        })).executes(context -> {
            String name = StringArgumentType.getString(context,"name");
            String type = StringArgumentType.getString(context,"type");
            if (PORTS.containsKey(name)){
                if (Objects.equals(type, "digital")){
                    PORTS.remove(name);
                    PORTS.put(name,new DigitalPort(name,ODKPOS));
                    PORTS.get(name).valueInitialize(context.getSource().getWorld());
                    context.getSource().getPlayer().sendMessage(Text.literal("Port "+name+" was reset.").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
                } else if (Objects.equals(type, "analog")) {
                    PORTS.remove(name);
                    PORTS.put(name, new AnalogPort(name, ODKPOS));
                    PORTS.get(name).valueInitialize(context.getSource().getWorld());
                    context.getSource().getPlayer().sendMessage(Text.literal("Port " + name + " was reset.").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
                } else if (Objects.equals(type,"flag")) {
                    PORTS.remove(name);
                    PORTS.put(name,new FlagPort(name,ODKPOS));
                    PORTS.get(name).stateInitialize(context.getSource().getWorld());
                    context.getSource().getPlayer().sendMessage(Text.literal("Port "+name+" was reset.").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
                } else if (!Objects.equals(StringArgumentType.getString(context, "type"), "digital") && !Objects.equals(StringArgumentType.getString(context,"type"),"analog") && !Objects.equals(StringArgumentType.getString(context,"type"),"flag")) {
                    context.getSource().getPlayer().sendMessage(Text.literal("Type error.").setStyle(Style.EMPTY.withColor(Formatting.RED)));
                }
            } else if (!PORTS.containsKey(name)) {
                context.getSource().getPlayer().sendMessage(Text.literal("Port "+name+" is not exist.").setStyle(Style.EMPTY.withColor(Formatting.RED)));
            }
            return 0;
        }))))));

        //odk port rename <old name> <new name>
        dispatcher.register(literal("odk").then(literal("port").then(literal("rename").then(argument("old name",StringArgumentType.string()).then(argument("new name",StringArgumentType.string()).executes(context -> {
            String oldName = StringArgumentType.getString(context,"old name");
            String newName = StringArgumentType.getString(context,"new name");
            if (PORTS.containsKey(oldName) && !PORTS.containsKey(newName)) {
                Port p = PORTS.get(oldName);
                PORTS.remove(oldName);
                p.setName(newName);
                PORTS.put(newName,p);
                for (Monitor m : MONITORS.values()){
                    if (m.Ports.containsKey(oldName)){
                        m.Ports.remove(oldName);
                        m.Ports.put(p.name,p);
                    }
                }
                context.getSource().getPlayer().sendMessage(Text.literal("Port "+oldName+" was renamed.").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
            } else if (!PORTS.containsKey(oldName) || PORTS.containsKey(newName)) {
                context.getSource().getPlayer().sendMessage(Text.literal("Port rename failed.").setStyle(Style.EMPTY.withColor(Formatting.RED)));
            }
            return 0;
        }))))));

        //odk port showDetails <name>
        dispatcher.register(literal("odk").then(literal("port").then(literal("showDetails").then(argument("name",StringArgumentType.string()).executes(context -> {
            String name = StringArgumentType.getString(context,"name");
            World world = context.getSource().getWorld();
            String[] details = PORTS.get(name).getDetails();
            if (PORTS.get(name) instanceof AnalogPort && PORTS.containsKey(name)) {
                String[] Details = {"Port information:", "  [name] " + details[0], "  [type] " + details[1], "  [position] " + details[2], "  [output value] " + String.valueOf(PORTS.get(name).getAnalog(world))};
                for (String s : Details){
                    context.getSource().getPlayer().sendMessage(Text.literal(s).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
                }
            } else if (PORTS.get(name) instanceof DigitalPort && PORTS.containsKey(name)) {
                String[] Details = {"Port information:", "  [name] " + details[0], "  [type] " + details[1], "  [position] " + details[2], "  [output value] " + String.valueOf(PORTS.get(name).getDigital(world))};
                for (String s : Details){
                    context.getSource().getPlayer().sendMessage(Text.literal(s).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
                }
            } else if (!PORTS.containsKey(name)) {
                context.getSource().getPlayer().sendMessage(Text.literal("Port "+name+"is not exist.").setStyle(Style.EMPTY.withColor(Formatting.RED)));
            }
            return 0;
        })))));

        //odk port list
        dispatcher.register(literal("odk").then(literal("port").then(literal("list").executes(context -> {
            Set<String> keySet = PORTS.keySet();
            context.getSource().getPlayer().sendMessage(Text.literal("Port list:").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
            for (String key : keySet){
                context.getSource().getPlayer().sendMessage(Text.literal("  "+key).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
            }
            return 0;
        }))));

        //odk port tp <name>
        dispatcher.register(literal("odk").then(literal("port").then(literal("tp").then(argument("name",StringArgumentType.string()).executes(context -> {
            String name = StringArgumentType.getString(context,"name");
            BlockPos pos = PORTS.get(name).pos;
            if (PORTS.containsKey(name)){
                context.getSource().getPlayer().teleport(pos.getX(), pos.getY(), pos.getZ());
                context.getSource().getPlayer().sendMessage(Text.literal("You've been teleported to (" + pos.toShortString() + ")").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
            } else if (!PORTS.containsKey(name)) {
                context.getSource().getPlayer().sendMessage(Text.literal("Port "+name+"is not exist.").setStyle(Style.EMPTY.withColor(Formatting.RED)));
            }
            return 0;
        })))));

    }
}