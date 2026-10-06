package ru.tempelstudio.WMVE.custom;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLevelEvents;

// Клиент и GUI
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.minecraft.client.Minecraft;

// Звуки и Текст
import net.minecraft.network.chat.Component;

// Математика и Векторы (Важное изменение: Vec3d -> Vec3)
import ru.tempelstudio.WMVE.custom.Debug.Debug;
import ru.tempelstudio.WMVE.custom.Events.AttackHandler;
import ru.tempelstudio.WMVE.custom.Events.BerserkDetector;
import ru.tempelstudio.WMVE.custom.Events.DungeonDetector;
import ru.tempelstudio.WMVE.custom.Events.GenParticle;
import ru.tempelstudio.WMVE.custom.Render.SlashRenderer;
import ru.tempelstudio.WMVE.custom.Utils.SwingCalculator;
import ru.tempelstudio.WMVE.custom.data.ClientState;


public class WMVE_Weapon_master_visual_effect {

    private static final ClientState state = new ClientState();

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(WMVE_Weapon_master_visual_effect::tick);

        LevelRenderEvents.AFTER_TRANSLUCENT_FEATURES.register(context -> {
            SlashRenderer.render(context);
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> SlashRenderer.tick());

        ClientLevelEvents.AFTER_CLIENT_LEVEL_CHANGE.register((minecraftClient, clientWorld) -> reset());
    }

    private static void tick(Minecraft client) {
        if (client == null) return;

        DungeonDetector.detectDungeon(client, state);

        if (client.player == null) return;

        if (state.turn) {
            GenParticle.particlegen(client, state);
            state.turn = false;
        }

        if (!(state.inDungeon || Debug.Dungeon())) return;

        BerserkDetector.Check(client, state);

        if (state.swingRaw == 0) {
            double result = SwingCalculator.calculate(client);

            if (result > 0) {
                state.swingRaw = result;

                if (Debug.Messages())
                    client.player.sendSystemMessage(Component.literal("Успех! Swing Range: " + state.swingRaw));

                client.setScreen(null);
            }

            return;
        }

        AttackHandler.tick(client, state);
    }

    private static void reset() {
        state.swingRaw = 0;
        state.wasOpen = false;
        state.playerIsBers = false;
        state.inDungeon = false;
        state.timer = 40;
    }
}