package com.example.examplemod.content.headmountedgasbottle.client;

import com.example.examplemod.content.headmountedgasbottle.item.HeadMountedGasBottleItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

/** Renders the bottle with the vanilla head-item coordinate system. */
public class HeadMountedGasBottleEntityRenderer extends GeoItemRenderer<HeadMountedGasBottleItem> {
    private static final float MODEL_SCALE = 4.3f;

    public HeadMountedGasBottleEntityRenderer() {
        super(new HeadMountedGasBottleModel());
        withScale(MODEL_SCALE);
    }

    @Override
    public void preRender(PoseStack poseStack, HeadMountedGasBottleItem animatable, BakedGeoModel model,
                          @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer,
                          boolean isReRender, float partialTick, int packedLight, int packedOverlay,
                          int colour) {
        itemRenderTranslations = new Matrix4f(poseStack.last().pose());
        scaleModelForRender(scaleWidth, scaleHeight, poseStack, animatable, model,
                isReRender, partialTick, packedLight, packedOverlay);
    }
}
