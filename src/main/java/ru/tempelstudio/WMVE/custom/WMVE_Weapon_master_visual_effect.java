package ru.tempelstudio.WMVE.custom;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLevelEvents;

// Клиент и GUI
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.KeyMapping;

// Сущности (Разделены на логические пакеты в маппингах Mojang)
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.entity.animal.cow.Cow;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.animal.rabbit.Rabbit;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.monster.skeleton.Skeleton;
import net.minecraft.world.entity.monster.skeleton.WitherSkeleton;
import net.minecraft.world.entity.monster.spider.Spider;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.player.Player;

// Инвентарь и Предметы
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;

// Частицы
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;

// Скорборды и Команды
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.PlayerTeam;

// Звуки и Текст
import net.minecraft.sounds.SoundEvents;
import net.minecraft.network.chat.Component;

// Математика и Векторы (Важное изменение: Vec3d -> Vec3)
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.block.state.BlockState;
import ru.tempelstudio.WMVE.custom.particles.CustomParticles;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WMVE_Weapon_master_visual_effect {
    static double turn;
    static double swingRaw = 0.0;
    static double swing = 0.0;
    static double b = -swingRaw;
    // static boolean isCurrentlyPressed;
    private static boolean wasPressed = false;
    static Vec3 playerPos;
    static Vec3 playerRotation;
    static double playerYaw;
    static BlockState checkBlock;
    static ParticleOptions particleType;
    static ItemStack hand;
    static boolean playedSoundE = false;
    static boolean playedSoundB = false;
    static boolean in_Dungeon;
    static boolean was_open = false;
    static int timer = 40;
    static boolean playerIsBers = false;
    static List <Entity> entityList = new ArrayList<>();
    private static List<PlayerInfo> playerList = new ArrayList<>();
    private static List<String> playerStringList = new ArrayList<>();
    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            clickregister(client);
            detectDungeon(client);
        });
        ClientTickEvents.START_CLIENT_TICK.register(WMVE_Weapon_master_visual_effect::clickregister);
        ClientLevelEvents.AFTER_CLIENT_LEVEL_CHANGE.register( (minecraftClient, clientWorld) -> {
            swingRaw = 0;
            was_open = false;
            playerIsBers = false;
            in_Dungeon = false;
        });
    }
    private static void clickregister(Minecraft client) {
            // Убедимся, что клиент и игрок существуют (могут быть null во время загрузки мира)
            if (client == null || client.player == null) return;
            // Считываем состояние ЛКМ напрямую через GLFW, чтобы избежать коллизии с биндами
            KeyMapping attackKey = client.options.keyAttack; // привязка не посредственно к кнопке удара
            boolean attackPressed = attackKey.isDown(); //
            if (turn <= swing) {
                // Анимация создаёт по три пака партиклов за раз
                particlegen(client);
                turn=turn+swing/10;
                particlegen(client);
                turn=turn+swing/10;
                particlegen(client);
                turn=turn+swing/10;
            }

        // Если в данже
        if (in_Dungeon) {
            if (!playerIsBers) {
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
                String regexBers = client.player.getName().toString().replaceAll("literal\\{", "").replaceAll("}", "") + ".{0,10}Berserk";
                Pattern patternBers = Pattern.compile(regexBers);
                Matcher matcherBers = patternBers.matcher(playerStringList.toString());
                if (matcherBers.find()) playerIsBers = true;
            }
            if (swingRaw == 0) {
                if (client.player == null) return;
                if (!was_open) {
                    if (timer == 0) {
                        client.player.connection.sendCommand("stats");
                        was_open = true;
                    }
                    else timer--;
                }
                if (client.screen instanceof ContainerScreen containerScreen) {
                    Pattern pattern = Pattern.compile("Swing Range\\D*\\d+\\.*\\d*");
                    Pattern pattern1 = Pattern.compile("Combat Stats");
                    Pattern patternSword = Pattern.compile("(SWORD)|(LONGSWORD)");

                    Item.TooltipContext tooltipContext = Item.TooltipContext.of(client.level);
                    net.minecraft.world.item.TooltipFlag tooltipFlag = net.minecraft.world.item.TooltipFlag.Default.NORMAL;

                    for (net.minecraft.world.inventory.Slot slot : containerScreen.getMenu().slots) {
                        ItemStack stackInSlot = slot.getItem();

                        if (stackInSlot.isEmpty() || !stackInSlot.is(Items.STONE_SWORD)) {
                            continue; // Пропускаем пустые слоты и не каменные мечи
                        }

                        // Получаем тултип предмета в слоте
                        String tooltipString = stackInSlot.getTooltipLines(tooltipContext, client.player, tooltipFlag).toString();
                        // Получаем отображаемое (измененное) имя предмета
                        String displayName = stackInSlot.getHoverName().getString();

                        Matcher matcherSwingMenu = pattern.matcher(tooltipString);
                        Matcher matcherSBP = pattern1.matcher(displayName);

                        if (matcherSwingMenu.find() && matcherSBP.find()) {
                            // Предмет успешно найден в интерфейсе
                            String rawSwing = matcherSwingMenu.group().replaceAll("[^0-9.]", "");
                            swingRaw = Double.parseDouble(rawSwing) + 0.001;

                            // Проверяем предмет в главной руке игрока
                            ItemStack handItem = client.player.getMainHandItem();
                            String handTooltipStr = handItem.getTooltipLines(tooltipContext, client.player, tooltipFlag).toString();

                            Matcher matcherSwingHand = pattern.matcher(handTooltipStr);
                            Matcher matcherSword = patternSword.matcher(handTooltipStr);

                            if (matcherSwingHand.find() && matcherSword.find()) {
                                String rawHandSwing = matcherSwingHand.group().replaceAll("[^0-9.]", "");
                                swingRaw = swingRaw - Double.parseDouble(rawHandSwing) + 0.001;
                            }

                            // Выводим сообщение и закрываем экран
                            client.player.sendSystemMessage(Component.literal("успех"));
                            client.setScreen(null);
                            break; // Выходим из цикла
                        }
                    }
                }
            }
            else {
                // Если была нажата кнопка удара
                if (attackPressed && !wasPressed && Minecraft.getInstance().screen == null && playerIsBers) {
                    // Если меч в руке
                    hand = client.player.getInventory().getSelectedItem();
                    String regexSword = "(SWORD)|(LONGSWORD)";
                    Pattern patternSword = Pattern.compile(regexSword);
                    Matcher matcherSword = patternSword.matcher(hand.getTooltipLines(Item.TooltipContext.of(client.level),client.player ,TooltipFlag.Default.NORMAL).toString());
                    if (matcherSword.find()) { // Если в руках меч и кнопка нажалась, то сбрасываем состояния и запускаем цикл
                        String regex = "Swing Range\\D*\\d+\\.*\\d*"; // Можно вписать свой рыгокс
                        Pattern pattern = Pattern.compile(regex);
                        Matcher matcherSwingHand = pattern.matcher(hand.getTooltipLines(Item.TooltipContext.of(client.level),client.player ,TooltipFlag.Default.NORMAL).toString());
                        boolean find = matcherSwingHand.find();
                        if (find) swing = swingRaw + (Double.parseDouble(matcherSwingHand.group().replaceAll("[^0-9^.]", "")) + 0.001);
                        else swing = swingRaw;
                        turn = -swing;
                        playerPos = client.player.position();
                        playerRotation = client.player.calculateViewVector(client.player.getXRot(), client.player.getYRot());
                        playerYaw = Math.toRadians(client.player.getYRot());
                        playedSoundE = false;
                        playedSoundB = false;
                        entityList = new ArrayList<>();
                    }
                }
                // Сброс статуса для одиночного нажатия
                wasPressed = attackPressed;
            }
        }
    }

    private static void particlegen(Minecraft client) {
        if (client == null || client.player == null) return;
        //Сдвиг в сторону
        for (b = -swing;b < swing; b=b+swing/10) {
            //Сброс типа партикла
            if (turn == b) {
                particleType = CustomParticles.SWING_PARTICLE;
                // Линия вперёд
                for (double i = 0; i < swing; i=i+swing/10) {
                    // Расчёт места спавна партикла
                    double x = playerPos.x + playerRotation.x * i + Math.cos(playerYaw)*(b/swing*i);
                    double y = playerPos.y + playerRotation.y * i;
                    double z = playerPos.z + playerRotation.z * i + Math.sin(playerYaw)*(b/swing*i);
                    // Проверка врезалось ли в стену
                    BlockPos pos = new BlockPos((int)x, (int)Math.ceil(y),(int)z);
                    checkBlock = client.level.getBlockState(pos);
                    if (checkBlock.isCollisionShapeFullBlock(client.level,pos) && !checkBlock.hasBlockEntity()) {
                        particleType = ParticleTypes.END_ROD;
                        if (!playedSoundB) {
                            // client.player.playSound(SoundEvents.BLOCK_GRINDSTONE_USE);
                            playedSoundB = true;
                        }
                        break;
                    }
                    // Ищем врагов
                    AABB box = new AABB(pos);
                    for (Entity entity : client.level.getEntitiesOfClass(Entity.class, box, e -> true)) {
                        if (entity instanceof Skeleton
                                || entity instanceof Zombie
                                || entity instanceof Player && entity != client.player
                                || entity instanceof EnderMan
                                || entity instanceof WitherBoss
                                || entity instanceof WitherSkeleton
                                || entity instanceof Guardian
                                || entity instanceof EnderDragon
                                || entity instanceof Sheep
                                || entity instanceof Rabbit
                                || entity instanceof Cow
                                || entity instanceof Chicken
                                || entity instanceof IronGolem
                                || entity instanceof Spider
                                || entity instanceof Bat
                        ) {
                            if (!entityList.contains(entity)) { // маркер попадания по мобу (1 раз за удар)
                                entityList.add(entity);
                                client.player.crit(entity);
                            }
                            if (!playedSoundE) { // Делаем звук (не больше 1 раза за удар)
                                client.player.playSound(SoundEvents.PLAYER_ATTACK_CRIT);
                                playedSoundE = true;
                            }
                            break;
                        }
                    }
                    //Партикл
                    if (i > 0) client.level.addParticle(particleType, x, y+1, z, 0, -1, 0);
                }
            }
        }
    }

    public static void detectDungeon (Minecraft client) {
        // Убедимся, что клиент и игрок существуют (могут быть null во время загрузки мира)
        if (client == null || client.level == null) {
            in_Dungeon = false;
            return;
        }
        Scoreboard scoreboard = client.level.getScoreboard();
        for (PlayerTeam team : scoreboard.getPlayerTeams()) {
            if (team == null) return;
            String regex = ".*The.Catac.*"; // Можно вписать свой рыгокс
            Pattern pattern = Pattern.compile(regex);
            if (pattern.matcher(team.getPlayerPrefix().getString()).find()) {
                in_Dungeon = true;
                break;
            }
            else in_Dungeon = false;
        }
    }

}