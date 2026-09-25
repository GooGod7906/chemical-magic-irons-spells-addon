package com.example.examplemod.content.headmountedgasbottle.client;

import com.example.examplemod.content.headmountedgasbottle.HeadMountedGasBottleContent;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;

import java.lang.reflect.Field;

/** Renders the bottle for entities without a vanilla humanoid armor layer. */
public class HeadMountedGasBottleLayer<T extends LivingEntity, M extends EntityModel<T>>
        extends RenderLayer<T, M> {
    private static final float MODEL_BASE_Y = 1.501f;
    private static final float HEAD_MOUNT_OFFSET = -0.75f;
    private static final float QUADRUPED_FORWARD_OFFSET = -0.26f;
    private final HeadMountedGasBottleEntityRenderer renderer = new HeadMountedGasBottleEntityRenderer();
    private boolean headPartResolved;
    private ModelPart headPart;

    public HeadMountedGasBottleLayer(RenderLayerParent<T, M> parent, EntityModelSet modelSet) {
        super(parent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity,
                       float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks,
                       float netHeadYaw, float headPitch) {
        ItemStack stack = entity.getItemBySlot(EquipmentSlot.HEAD);
        if (!stack.is(HeadMountedGasBottleContent.ITEM.get())) {
            return;
        }

        poseStack.pushPose();
        ModelPart head = getHeadPart();
        if (head != null) {
            head.translateAndRotate(poseStack);
        } else {
            poseStack.translate(0.0f, MODEL_BASE_Y - entity.getEyeHeight(), 0.0f);
            poseStack.mulPose(new Quaternionf().rotationZYX(
                    0.0f,
                    netHeadYaw * Mth.DEG_TO_RAD,
                    headPitch * Mth.DEG_TO_RAD));
        }
        poseStack.translate(0.0f, HEAD_MOUNT_OFFSET, 0.0f);
        if (getParentModel() instanceof QuadrupedModel<?>) {
            poseStack.translate(0.0f, 0.0f, QUADRUPED_FORWARD_OFFSET);
        }
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0f));
        poseStack.scale(1.0f, -1.0f, -1.0f);
        if (entity instanceof Villager || entity instanceof ZombieVillager) {
            poseStack.translate(0.0f, 0.1875f, 0.0f);
        }
        renderer.renderByItem(stack, ItemDisplayContext.HEAD, poseStack, buffer,
                packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
    }

    private ModelPart getHeadPart() {
        if (!headPartResolved) {
            headPartResolved = true;
            M model = getParentModel();
            if (model instanceof HeadedModel headedModel) {
                headPart = headedModel.getHead();
            } else {
                headPart = findHeadPart(model);
            }
        }

        return headPart;
    }

    private static ModelPart findHeadPart(EntityModel<?> model) {
        String[] fieldNames = {"head", "headParts"};
        for (String fieldName : fieldNames) {
            for (Class<?> type = model.getClass(); type != null; type = type.getSuperclass()) {
                try {
                    Field field = type.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    Object value = field.get(model);
                    if (value instanceof ModelPart modelPart) {
                        return modelPart;
                    }
                } catch (NoSuchFieldException ignored) {
                    // Continue through the model hierarchy.
                } catch (IllegalAccessException | RuntimeException ignored) {
                    return null;
                }
            }
        }

        return null;
    }
}
