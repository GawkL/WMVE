package ru.tempelstudio.WMVE.custom.Utils;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.entity.animal.cow.Cow;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.entity.animal.rabbit.Rabbit;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.skeleton.Skeleton;
import net.minecraft.world.entity.monster.skeleton.WitherSkeleton;
import net.minecraft.world.entity.monster.spider.Spider;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.player.Player;

public final class EntityUtils {

    private EntityUtils() {
        // Запрет создания объекта
    }


    /**
     * Проверяет, является ли сущность возможной целью атаки.
     */
    public static boolean isAttackable(Entity entity, Entity player) {

        return entity instanceof Skeleton
                || entity instanceof Zombie
                || entity instanceof Player && entity != player
                || entity instanceof EnderMan
                || entity instanceof WitherBoss
                || entity instanceof WitherSkeleton
                || entity instanceof Guardian
                || entity instanceof EnderDragon
                || entity instanceof Sheep
                || entity instanceof Rabbit
                || entity instanceof Cow
                || entity instanceof Chicken
                || entity instanceof Pig
                || entity instanceof IronGolem
                || entity instanceof Spider
                || entity instanceof Bat;
    }

}
