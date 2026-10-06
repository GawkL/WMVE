package ru.tempelstudio.WMVE.custom.Utils;

import java.util.regex.Pattern;

public final class RegexHelper {
    private RegexHelper() {
    }
    // Dungeon
    public static final Pattern DUNGEON =
            Pattern.compile(".*The.Catac.*");
    // Weapon / Item
    public static final Pattern SWORD =
            Pattern.compile("(SWORD)|(LONGSWORD)");
    public static final Pattern SWING_RANGE =
            Pattern.compile("Swing Range\\D*\\d+\\.*\\d*");
    public static final Pattern COMBAT_STATS =
            Pattern.compile("Combat Stats");
    // Player classes
    public static Pattern berserk(String playerName) {
        return Pattern.compile(
                Pattern.quote(playerName) + ".{0,10}Berserk"
        );
    }
}
