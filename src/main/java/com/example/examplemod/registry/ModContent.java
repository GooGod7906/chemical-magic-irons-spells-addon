package com.example.examplemod.registry;

import com.example.examplemod.content.headmountedgasbottle.HeadMountedGasBottleContent;
import net.neoforged.bus.api.IEventBus;

public final class ModContent {
    private ModContent() {
    }

    public static void register(IEventBus modEventBus) {
        // Load feature declarations before attaching the registries to the mod bus.
        HeadMountedGasBottleContent.register();
        ModItems.ITEMS.register(modEventBus);
        ModEntities.ENTITY_TYPES.register(modEventBus);
        ModSpells.SPELLS.register(modEventBus);
    }
}
