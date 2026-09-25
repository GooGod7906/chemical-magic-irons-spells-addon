package com.example.examplemod;

import com.example.examplemod.Client.HeadMountedGasBottleProjectileRenderer;
import com.example.examplemod.Client.HeadMountedGasBottleLayer;
import com.example.examplemod.Entities.ModEntities;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = ChemicalMagic.MOD_ID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = ChemicalMagic.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ChemicalMagicClient {
    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> EntityRenderers.register(
                ModEntities.HEAD_MOUNTED_GAS_BOTTLE_PROJECTILE.get(),
                HeadMountedGasBottleProjectileRenderer::new));
        ChemicalMagic.LOGGER.info("Chemical Magic client initialized for {}", Minecraft.getInstance().getUser().getName());
    }

    @SubscribeEvent
    static void addHeadMountedGasBottleLayers(EntityRenderersEvent.AddLayers event) {
        for (EntityType<?> entityType : event.getEntityTypes()) {
            EntityRenderer<?> renderer = event.getRenderer(entityType);
            if (renderer instanceof LivingEntityRenderer<?, ?> livingRenderer
                    && !(livingRenderer.getModel() instanceof net.minecraft.client.model.HumanoidModel<?>)) {
                addLayerUnchecked(livingRenderer, event.getEntityModels());
            }
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void addLayerUnchecked(LivingEntityRenderer<?, ?> renderer,
                                          net.minecraft.client.model.geom.EntityModelSet modelSet) {
        ((LivingEntityRenderer) renderer).addLayer(
                new HeadMountedGasBottleLayer((net.minecraft.client.renderer.entity.RenderLayerParent) renderer,
                        modelSet));
    }
}
