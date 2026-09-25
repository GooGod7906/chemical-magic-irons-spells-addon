package com.example.examplemod.content.headmountedgasbottle.client;

import com.example.examplemod.ChemicalMagic;
import com.example.examplemod.content.headmountedgasbottle.HeadMountedGasBottleContent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

public final class HeadMountedGasBottleClient {
    private HeadMountedGasBottleClient() {
    }

    public static void register(IEventBus modEventBus) {
        modEventBus.addListener(HeadMountedGasBottleClient::onClientSetup);
        modEventBus.addListener(HeadMountedGasBottleClient::addHeadMountedGasBottleLayers);
    }

    private static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> EntityRenderers.register(
                HeadMountedGasBottleContent.PROJECTILE.get(),
                HeadMountedGasBottleProjectileRenderer::new));
        ChemicalMagic.LOGGER.info("Head-mounted gas bottle client feature initialized for {}",
                Minecraft.getInstance().getUser().getName());
    }

    private static void addHeadMountedGasBottleLayers(EntityRenderersEvent.AddLayers event) {
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
