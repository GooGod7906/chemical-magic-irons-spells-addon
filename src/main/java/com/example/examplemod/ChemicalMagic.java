package com.example.examplemod;

import com.example.examplemod.Items.ModItems;
import com.example.examplemod.Spells.Spells;
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
        ModItems.ITEMS.register(modEventBus);
        Spells.SPELLS.register(modEventBus);
        LOGGER.info("Chemical Magic registries initialized");
    }
}
