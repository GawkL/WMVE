package ru.tempelstudio.WMVE.custom.Events;

import net.minecraft.client.Minecraft;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import ru.tempelstudio.WMVE.custom.Utils.RegexHelper;
import ru.tempelstudio.WMVE.custom.data.ClientState;

public class DungeonDetector {
    public static void detectDungeon (Minecraft client, ClientState state) {
        // Убедимся, что клиент и игрок существуют (могут быть null во время загрузки мира)
        if (client == null || client.level == null) {
            state.inDungeon = false;
            return;
        }
        Scoreboard scoreboard = client.level.getScoreboard();
        for (PlayerTeam team : scoreboard.getPlayerTeams()) {
            if (team == null) return;
            if (RegexHelper.DUNGEON.matcher(team.getPlayerPrefix().getString()).find()) {
                state.inDungeon = true;
                break;
            }
            else state.inDungeon = false;
        }
    }
}
