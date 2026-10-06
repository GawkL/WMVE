package ru.tempelstudio.WMVE.custom.Events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import ru.tempelstudio.WMVE.custom.Utils.RegexHelper;
import ru.tempelstudio.WMVE.custom.data.ClientState;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;

public class BerserkDetector {

    private static List<PlayerInfo> playerList = new ArrayList<>();
    private static List<String> playerStringList = new ArrayList<>();

    public static void Check (Minecraft client,ClientState state) {
        if (!state.playerIsBers) {
            ClientPacketListener networkHandler = Minecraft.getInstance().getConnection();
            if (networkHandler != null) {
                playerList = networkHandler.getOnlinePlayers()
                        .stream()
                        .toList();
                playerStringList = playerList.stream()
                        .map(PlayerInfo::getTabListDisplayName)
                        .filter(Objects::nonNull)
                        .map(Component::getString)
                        .map(String::strip)
                        .toList();
            }
            String playerName = client.player.getName()
                    .getString();

            Matcher matcherBers =
                    RegexHelper.berserk(playerName)
                            .matcher(playerStringList.toString());
            if (matcherBers.find()) state.playerIsBers = true;
        }
    }
}
