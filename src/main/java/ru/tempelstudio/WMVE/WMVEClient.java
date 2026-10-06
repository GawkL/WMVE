package ru.tempelstudio.WMVE;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import ru.tempelstudio.WMVE.custom.Debug.DebugCommands;
import ru.tempelstudio.WMVE.custom.WMVE_Weapon_master_visual_effect;
import ru.tempelstudio.WMVE.custom.particles.CustomParticles;
import ru.tempelstudio.WMVE.custom.particles.SwingLParticle;
import ru.tempelstudio.WMVE.custom.particles.SwingParticle;
import ru.tempelstudio.WMVE.custom.particles.SwingRParticle;

public class WMVEClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        WMVE_Weapon_master_visual_effect.register();
        CustomParticles.registerParticles();
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            DebugCommands.register(dispatcher);
        });
        ParticleProviderRegistry.getInstance().register(CustomParticles.SWING_PARTICLE, SwingParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(CustomParticles.SWINGR_PARTICLE, SwingRParticle.Provider::new);
        ParticleProviderRegistry.getInstance().register(CustomParticles.SWINGL_PARTICLE, SwingLParticle.Provider::new);
    }
}
