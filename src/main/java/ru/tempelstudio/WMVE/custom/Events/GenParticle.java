package ru.tempelstudio.WMVE.custom.Events;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import ru.tempelstudio.WMVE.custom.Render.SlashRenderer;
import ru.tempelstudio.WMVE.custom.Utils.EntityUtils;
import ru.tempelstudio.WMVE.custom.data.ClientState;
import ru.tempelstudio.WMVE.custom.particles.CustomParticles;
import ru.tempelstudio.WMVE.custom.particles.SlashMark;

public class GenParticle {

    public static void particlegen(Minecraft client, ClientState state) {
        if (client == null || client.player == null) return;
        //  Рассчёт марок
        double i = state.swing;
        for (double b = -state.swing; b <= state.swing; b += state.swing / (10 + state.swing * 0.25)) {
            double angle = (b / state.swing) * (Math.PI / 4.0);
            double forward = Math.cos(angle) * i;
            double side = Math.sin(angle) * i;
            double x = state.playerCenterPos.x + state.playerRotation.x * forward + Math.cos(state.playerYaw) * side;
            double y = state.playerCenterPos.y + state.playerRotation.y * forward;
            double z = state.playerCenterPos.z + state.playerRotation.z * forward + Math.sin(state.playerYaw) * side;
            Vec3 target = new Vec3(x, y+0.5, z);
            BlockHitResult result = canHitThrough(client, target);
            if (result.getType() == HitResult.Type.BLOCK) {
                float size = (float) ((3 + result.getLocation().distanceTo(state.playerCenterPos)) * 0.015);
                SlashRenderer.MARKS.add(
                        new SlashMark(
                                result.getLocation().add(
                                        Vec3.atLowerCornerOf(result.getDirection().getUnitVec3i()).scale(0.002)
                                ),
                                result.getDirection(),
                                0f,
                                size,
                                size,
                                0,
                                80
                        )
                );
            }
        }
        i = state.swing / 2;
        // Расчёт места спавна партикла
        double x = state.playerCenterPos.x + state.playerRotation.x * i + Math.cos(state.playerYaw);
        double y = state.playerCenterPos.y + state.playerRotation.y * i;
        double z = state.playerCenterPos.z + state.playerRotation.z * i + Math.sin(state.playerYaw);
        BlockPos pos = new BlockPos((int) x, (int) Math.ceil(y), (int) z);
        // Ищем врагов
        AABB box = new AABB(pos).inflate(i+1);
        for (Entity entity : client.level.getEntitiesOfClass(Entity.class, box, e -> true)) {
            if (!EntityUtils.isAttackable(entity, client.player)) {
                continue;
            }
            if (canHitThrough(client,entity.getBoundingBox().getCenter()).getType() == HitResult.Type.BLOCK) {
                continue;
            }
            if  (!coneCheck(client.player, entity, state.swing)) {
                continue;
            }
            if (!state.attackedEntities.contains(entity)) { // маркер попадания по мобу (1 раз за удар)
                state.attackedEntities.add(entity);
                client.player.crit(entity);
            }
            if (!state.playedEnemySound) { // Делаем звук (не больше 1 раза за удар)
                client.player.playSound(SoundEvents.PLAYER_ATTACK_CRIT);
                state.playedEnemySound = true;
            }
        }
            //Партикл
            client.particleEngine.createParticle(CustomParticles.SWINGR_PARTICLE, x, y, z, 0, 0, 0).scale((float) state.swing);
    }
    private static BlockHitResult canHitThrough(Minecraft client, Vec3 target) {
        BlockHitResult result = client.level.clip(new ClipContext(
                client.player.getBoundingBox().getCenter(),
                target,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                client.player
        ));
        return result;
    }
    private static boolean coneCheck(Player player, Entity entity, double range) {
        Vec3 look = player.getHeadLookAngle().normalize();
        Vec3 worldUp = new Vec3(0, 1.5, 0);
        Vec3 right = worldUp.cross(look);
        if (right.lengthSqr() < 1.0E-6)
            return false;
        right = right.normalize();
        Vec3 up = look.cross(right).normalize();
        Vec3 target = entity.getBoundingBox().getCenter()
                .subtract(player.getBoundingBox().getCenter());
        double depth = target.dot(up);
        if (Math.abs(depth) > 1.5)
            return false;
        double forward = target.dot(look);
        double side = Math.abs(target.dot(right));
        if (forward < 0.0)
            return false;
        if (forward * forward + side * side > range * range)
            return false;
        double maxSide = forward;
        return side <= maxSide;
    }
}
