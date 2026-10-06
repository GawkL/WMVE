package ru.tempelstudio.WMVE.custom.particles;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public record SlashMark(
        Vec3 pos,
        Direction face,
        float angle,
        float length,
        float width,
        int age,
        int lifetime
) {}
