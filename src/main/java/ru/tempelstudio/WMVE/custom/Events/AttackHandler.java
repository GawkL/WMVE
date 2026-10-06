package ru.tempelstudio.WMVE.custom.Events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import ru.tempelstudio.WMVE.custom.data.ClientState;
import ru.tempelstudio.WMVE.custom.Debug.Debug;
import ru.tempelstudio.WMVE.custom.Utils.RegexHelper;


public final class AttackHandler {
    private AttackHandler() {
    }
    public static void tick(Minecraft client, ClientState state) {
        if (client == null || client.player == null)
            return;
        KeyMapping attackKey =
                client.options.keyAttack;
        if (!attackKey.isDown()
                || state.wasPressed
                || client.screen != null
                || !(state.playerIsBers || Debug.Classes())) {

            state.wasPressed = attackKey.isDown();
            return;
        }
        ItemStack hand =
                client.player
                        .getInventory()
                        .getSelectedItem();
        String tooltip =
                hand.getTooltipLines(
                        Item.TooltipContext.of(client.level),
                        client.player,
                        TooltipFlag.Default.NORMAL
                ).toString();
        // Проверяем меч
        if (!RegexHelper.SWORD
                .matcher(tooltip)
                .find()) {
            state.wasPressed = true;
            return;
        }
        // Получаем Swing Range из руки
        var swingMatcher =
                RegexHelper.SWING_RANGE
                        .matcher(tooltip);
        if (swingMatcher.find()) {
            double handSwing =
                    Double.parseDouble(
                            swingMatcher.group()
                                    .replaceAll("[^0-9^.]", "")
                    );
            state.swing =
                    state.swingRaw
                            + handSwing
                            + 0.001;

        }
        else {
            state.swing =
                    state.swingRaw;

        }
        startAttack(client, state);
        state.wasPressed =
                attackKey.isDown();
    }
    private static void startAttack(
            Minecraft client,
            ClientState state
    ) {

        state.turn = true;
        state.playerPos =
                client.player.position();
        state.playerEyePos =
                client.player.getEyePosition();
        state.playerRotation =
                client.player.calculateViewVector(
                        client.player.getXRot(),
                        client.player.getYRot()
                );
        state.playerYaw =
                Math.toRadians(
                        client.player.getYRot()
                );
        state.resetAttack();
    }
}
