package com.example.examplemod;

import com.example.examplemod.content.headmountedgasbottle.client.HeadMountedGasBottleClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

// Client-only mod entry point. Feature-specific client events live with each feature.
@Mod(value = ChemicalMagic.MOD_ID, dist = Dist.CLIENT)
public final class ChemicalMagicClient {
    public ChemicalMagicClient(IEventBus modEventBus) {
        HeadMountedGasBottleClient.register(modEventBus);
    }
}
