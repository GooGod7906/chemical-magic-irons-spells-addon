package com.example.examplemod;

import com.example.examplemod.content.headmountedgasbottle.HeadMountedGasBottleContent;
import com.example.examplemod.registry.ModContent;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(ChemicalMagic.MOD_ID)
public class ChemicalMagic {
    public static final String MOD_ID = "chemicalmagic";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ChemicalMagic(IEventBus modEventBus, ModContainer modContainer) {
        ModContent.register(modEventBus);
        HeadMountedGasBottleContent.registerCommonEvents();
        LOGGER.info("Chemical Magic registries initialized");
    }
}
