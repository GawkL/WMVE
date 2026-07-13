package ru.tempelstudio.WMVE.custom.Debug;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.network.chat.Component;

public class DebugCommands {
    public static void register(final CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommands.literal("wmve")
                .then(ClientCommands.literal("debugdungeon")
                        .then(ClientCommands.argument("state", BoolArgumentType.bool())
                                .executes(c -> debugDungeon(c.getSource(), BoolArgumentType.getBool(c, "state")))
                        )
                )
                .then(ClientCommands.literal("debugclasses")
                        .then(ClientCommands.argument("state", BoolArgumentType.bool())
                                .executes(c -> debugClasses(c.getSource(), BoolArgumentType.getBool(c, "state")))
                        )
                )
                .then(ClientCommands.literal("debugmessages")
                        .then(ClientCommands.argument("state", BoolArgumentType.bool())
                                .executes(c -> debugMessages(c.getSource(), BoolArgumentType.getBool(c, "state")))
                        )
                )
        );
    }

    private static int debugDungeon(FabricClientCommandSource source, boolean state) {
        Debug.debugDungeon(state);
        source.sendFeedback(Component.literal("debugDungeon set to: " + state));
        return 1;
    }

    private static int debugClasses(FabricClientCommandSource source, boolean state) {
        Debug.debugClasses(state);
        source.sendFeedback(Component.literal("debugClasses set to: " + state));
        return 1;
    }

    private static int debugMessages(FabricClientCommandSource source, boolean state) {
        Debug.debugMessages(state);
        source.sendFeedback(Component.literal("debugMessages set to: " + state));
        return 1;
    }
}
