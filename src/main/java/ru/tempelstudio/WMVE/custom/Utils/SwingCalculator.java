package ru.tempelstudio.WMVE.custom.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;

import net.minecraft.world.inventory.Slot;
import net.minecraft.network.chat.Component;

import java.util.regex.Matcher;


public final class SwingCalculator {


    private SwingCalculator() {
    }
    /**
     * Ищет Swing Range в открытом меню stats
     */
    public static double calculate(Minecraft client) {
        if (client == null ||
                client.player == null ||
                client.screen == null)
            return 0;
        if (!(client.screen instanceof ContainerScreen containerScreen))
            return 0;
        Item.TooltipContext tooltipContext =
                Item.TooltipContext.of(client.level);
        TooltipFlag tooltipFlag =
                TooltipFlag.Default.NORMAL;
        for (Slot slot : containerScreen.getMenu().slots) {
            ItemStack stack = slot.getItem();
            // Нас интересует только Stone Sword
            if (stack.isEmpty()
                    || !stack.is(Items.STONE_SWORD)) {
                continue;
            }
            String tooltip =
                    stack.getTooltipLines(
                            tooltipContext,
                            client.player,
                            tooltipFlag
                    ).toString();
            String name =
                    stack.getHoverName()
                            .getString();
            Matcher swingMatcher =
                    RegexHelper.SWING_RANGE.matcher(tooltip);
            Matcher combatMatcher =
                    RegexHelper.COMBAT_STATS.matcher(name);
            if (swingMatcher.find()
                    && combatMatcher.find()) {
                double swing =
                        extractNumber(
                                swingMatcher.group()
                        );
                swing += 0.001;
                // Проверяем предмет в руке
                double handSwing =
                        getHandSwing(client,
                                tooltipContext,
                                tooltipFlag);
                if (handSwing > 0) {
                    swing =
                            swing - handSwing + 0.001;
                }
                return swing;
            }
        }
        return 0;
    }
    private static double getHandSwing(
            Minecraft client,
            Item.TooltipContext context,
            TooltipFlag flag
    ) {
        ItemStack hand =
                client.player.getMainHandItem();
        String tooltip =
                hand.getTooltipLines(
                        context,
                        client.player,
                        flag
                ).toString();
        Matcher swingMatcher =
                RegexHelper.SWING_RANGE.matcher(tooltip);
        Matcher swordMatcher =
                RegexHelper.SWORD.matcher(tooltip);
        if (swingMatcher.find()
                && swordMatcher.find()) {
            return extractNumber(
                    swingMatcher.group()
            );
        }
        return 0;
    }
    private static double extractNumber(String text) {

        return Double.parseDouble(
                text.replaceAll("[^0-9.]", "")
        );
    }
}
