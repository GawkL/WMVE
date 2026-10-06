package ru.tempelstudio.WMVE.custom.data;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class ClientState {

    public boolean turn;

    public double swingRaw;
    public double swing;

    public Vec3 playerPos;
    public Vec3 playerEyePos;
    public Vec3 playerRotation;
    public double playerYaw;

    public boolean inDungeon;
    public boolean playerIsBers;

    public boolean playedEnemySound;
    public boolean playedBlockSound;

    public boolean wasPressed;
    public boolean wasOpen;

    public int timer = 40;

    public List<Entity> attackedEntities = new ArrayList<>();

    public void resetAttack(){
        playedEnemySound = false;
        playedBlockSound = false;
       attackedEntities.clear();

    }
}
