package ru.tempelstudio.WMVE;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import ru.tempelstudio.WMVE.custom.particles.CustomParticles;
import ru.tempelstudio.WMVE.custom.particles.SwingParticle;

public class WMVEClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Регистрация через актуальный метод провайдера
        ParticleProviderRegistry.getInstance().register(CustomParticles.SWING_PARTICLE, SwingParticle.Provider::new);
    }
}
