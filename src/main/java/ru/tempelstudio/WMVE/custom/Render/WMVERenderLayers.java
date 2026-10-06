package ru.tempelstudio.WMVE.custom.Render;

import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.texture.TextureAtlas;

public class WMVERenderLayers {
    public static final SingleQuadParticle.Layer DOUBLE_SIDED =
            new SingleQuadParticle.Layer(
                    true,
                    TextureAtlas.LOCATION_PARTICLES,
                    WMVERenderPipelines.DOUBLE_SIDED_TRANSLUCENT_PARTICLE
            );
}
