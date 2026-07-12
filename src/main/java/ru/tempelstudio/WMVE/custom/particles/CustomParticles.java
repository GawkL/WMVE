package ru.tempelstudio.WMVE.custom.particles;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import ru.tempelstudio.WMVE.WMVE;

public class CustomParticles {
    public static final SimpleParticleType SWING_PARTICLE =
            registerParticle("swing_particle", FabricParticleTypes.simple());

    private static SimpleParticleType registerParticle(String name, SimpleParticleType particleType) {
        return Registry.register(
                BuiltInRegistries.PARTICLE_TYPE,
                Identifier.fromNamespaceAndPath(WMVE.MOD_ID, name),
                particleType
        );
    }

    public static void registerParticles() {
        WMVE.LOGGER.info("Registering Particles for " + WMVE.MOD_ID);
    }
}
