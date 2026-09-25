package com.example.examplemod;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = ChemicalMagic.MOD_ID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = ChemicalMagic.MOD_ID, value = Dist.CLIENT)
public class ChemicalMagicClient {
    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        ChemicalMagic.LOGGER.info("Chemical Magic client initialized for {}", Minecraft.getInstance().getUser().getName());
    }
}
