package ru.tempelstudio.WMVE.custom.Debug;

public class Debug {
    private static boolean Dungeon = false;
    private static boolean Classes = false;
    private static boolean Messages = false;

    protected static void debugDungeon(boolean b) {
        Dungeon = b;
    }

    protected static void debugClasses(boolean b) {
        Classes = b;
    }

    protected static void debugMessages(boolean b) {
        Messages = b;
    }

    public static boolean Classes() {
        return Classes;
    }

    public static boolean Dungeon() {
        return Dungeon;
    }

    public static boolean Messages() {
        return Messages;
    }
}
