package ru.tempelstudio.WMVE.custom.particles;


import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle; // Новый базовый класс
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

public class SwingParticle extends SingleQuadParticle {

    private final SingleQuadParticle.Layer layer;

    public SwingParticle(ClientLevel clientLevel, double x, double y, double z,
                         SpriteSet spriteSet, double xSpeed, double ySpeed, double zSpeed) {
        super(clientLevel, x, y, z, spriteSet.get(net.minecraft.util.RandomSource.create()));

        this.xd = xSpeed;
        this.yd = ySpeed;
        this.zd = zSpeed;

        var sprite = spriteSet.get(RandomSource.create());
        this.setSprite(sprite);
        this.layer = SingleQuadParticle.Layer.bySprite(sprite);

        this.xd = 0.0f;
        this.yd = 0.0f;
        this.zd = 0.0f;
        this.lifetime = (int) (Math.random() * 5) + 3;
        this.hasPhysics = false;
        this.gravity = 0.0f;
        this.rCol = 1.0f;
        this.gCol = 1.0f;
        this.bCol = 1.0f;
        this.quadSize = 0.125f;
        this.alpha = 0.7f;
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
                                       double velocityX, double velocityY, double velocityZ,RandomSource random) {
            return new SwingParticle(world, x, y, z, this.spriteSet, velocityX, velocityY, velocityZ);
        }
    }
}
