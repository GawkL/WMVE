package ru.tempelstudio.WMVE.custom.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import net.minecraft.client.Camera;

public class SwingParticle extends SingleQuadParticle {

    private final SingleQuadParticle.Layer layer;
    private final SpriteSet sprites;
    private final org.joml.Quaternionf fixedRotation = new org.joml.Quaternionf();

    @Override
    public Particle scale(float scale) {
        this.quadSize = 0.5F * scale;
        return this;
    }

    public SwingParticle(ClientLevel clientLevel, double x, double y, double z,
                         SpriteSet sprites) {
        super(clientLevel, x, y, z, sprites.first());
        this.sprites = sprites;
        this.setSpriteFromAge(sprites);
        this.layer = SingleQuadParticle.Layer.bySprite(sprite);
        this.xd = 0.0f;
        this.yd = 0.0f;
        this.zd = 0.0f;
        this.lifetime = 5;
        this.hasPhysics = false;
        this.gravity = 0.0f;
        this.alpha = 0.7f;
        this.scale(1);
        Camera camera = net.minecraft.client.Minecraft.getInstance().gameRenderer.getMainCamera();
        this.fixedRotation.set(camera.rotation());
        this.fixedRotation.rotateX(net.minecraft.util.Mth.DEG_TO_RAD * -90.0F);
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
    protected void extractRotatedQuad(QuadParticleRenderState particleTypeRenderState, Quaternionf rotation, float x, float y, float z, float partialTickTime) {
        // 1. Отрисовываем партикл "лицом" вперед (используем наш зафиксированный fixedRotation)
        super.extractRotatedQuad(particleTypeRenderState, this.fixedRotation, x, y, z, partialTickTime);
        // 2. Создаем зеркальный кватернион, развернутый на 180 градусов по горизонтали (ось Y)
        Quaternionf backFaceRotation = new Quaternionf(this.fixedRotation).rotateY((float) Math.PI);
        // 3. Отрисовываем ту же самую геометрию, но развернутую "спиной".
        super.extractRotatedQuad(particleTypeRenderState, backFaceRotation, x, y, z, partialTickTime);
    }

    @Override
    protected SingleQuadParticle.Layer getLayer() {
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
            return new SwingParticle(world, x, y, z, this.spriteSet);
        }
    }
}
