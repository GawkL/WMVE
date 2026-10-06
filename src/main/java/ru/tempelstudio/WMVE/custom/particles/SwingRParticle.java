package ru.tempelstudio.WMVE.custom.particles;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import ru.tempelstudio.WMVE.custom.Render.WMVERenderLayers;

public class SwingRParticle extends SingleQuadParticle {

    private final Layer layer;
    private final SpriteSet sprites;
    private final Quaternionf fixedRotation = new Quaternionf();

    @Override
    public Particle scale(float scale) {
        this.quadSize = 0.5F * scale;
        return this;
    }

    public SwingRParticle(ClientLevel clientLevel, double x, double y, double z,
                          SpriteSet sprites) {
        super(clientLevel, x, y, z, sprites.first());
        this.sprites = sprites;
        this.setSpriteFromAge(sprites);
        this.layer = WMVERenderLayers.DOUBLE_SIDED;
        this.xd = 0.0f;
        this.yd = 0.0f;
        this.zd = 0.0f;
        this.lifetime = 5;
        this.hasPhysics = false;
        this.gravity = 0.0f;
        this.alpha = 0.7f;
        this.scale(1);
        LocalPlayer player = Minecraft.getInstance().player;

        float yaw = player.getYHeadRot();
        float pitch = player.getXRot();

        this.fixedRotation.identity();
        this.fixedRotation
                .rotateY(-Mth.DEG_TO_RAD * yaw)
                .rotateX(Mth.DEG_TO_RAD * pitch);

        this.fixedRotation.rotateX(Mth.DEG_TO_RAD * 90f);
        this.fixedRotation.rotateY(Mth.DEG_TO_RAD * 180f);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.setSpriteFromAge(this.sprites);
        }
    }

    @Override
    protected int getLightCoords(float partialTick) {
        return LightCoordsUtil.FULL_BRIGHT;
    }

    @Override
    protected void extractRotatedQuad(
            QuadParticleRenderState state,
            Quaternionf rotation,
            float x,
            float y,
            float z,
            float partialTick
    ) {
        super.extractRotatedQuad(state, fixedRotation, x, y, z, partialTick);
    }

    @Override
    protected Layer getLayer() {
        return this.layer;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType parameters, ClientLevel world,
                                       double x, double y, double z,
                                       double velocityX, double velocityY, double velocityZ, RandomSource random) {
            return new SwingRParticle(world, x, y, z, this.spriteSet);
        }
    }
}
