package ru.tempelstudio.WMVE;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.tempelstudio.WMVE.custom.Debug.DebugCommands;
import ru.tempelstudio.WMVE.custom.WMVE_Weapon_master_visual_effect;
import ru.tempelstudio.WMVE.custom.particles.CustomParticles;

public class WMVE implements ModInitializer {
	public static final String MOD_ID = "wmve";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	@Override
	public void onInitialize() {
	}
}