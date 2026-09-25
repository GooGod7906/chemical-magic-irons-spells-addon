package com.example.examplemod.Client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.example.examplemod.Items.HeadMountedGasBottleItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import com.mojang.blaze3d.vertex.VertexConsumer;

public class HeadMountedGasBottleRenderer extends GeoArmorRenderer<HeadMountedGasBottleItem> {
    private static final float HEAD_OFFSET_DOWN = 0.25f;

    public HeadMountedGasBottleRenderer() {
        super(new HeadMountedGasBottleModel());
        // The geo model is authored in compact bottle units; enlarge it to head scale.
        withScale(4.0f);
    }

    @Override
    public void preRender(PoseStack poseStack, HeadMountedGasBottleItem animatable, BakedGeoModel model,
                          @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer,
                          boolean isReRender, float partialTick, int packedLight, int packedOverlay,
                          int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick,
                packedLight, packedOverlay, colour);

        Entity entity = getCurrentEntity();
        if (!isReRender && entity != null && this.head != null) {
            // Keep the offset on the head bone so head pitch rotates the bottle with it.
            this.head.setPosY(this.head.getPosY()
                    + (entity.getBbHeight() - HEAD_OFFSET_DOWN) * 16.0f);
        }
    }

    @Override
    public GeoBone getHeadBone(GeoModel<HeadMountedGasBottleItem> model) {
        return model.getBone("armorHead").orElse(null);
    }
}
