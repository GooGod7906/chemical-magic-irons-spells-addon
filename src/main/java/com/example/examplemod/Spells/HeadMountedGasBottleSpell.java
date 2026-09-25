package com.example.examplemod.Spells;

import com.example.examplemod.ChemicalMagic;
import com.example.examplemod.Items.ModItems;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class HeadMountedGasBottleSpell extends AbstractSpell {
    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.COMMON)
            .setSchoolResource(SchoolRegistry.NATURE_RESOURCE)
            .setMaxLevel(10)
            .setCooldownSeconds(20)
            .build();

    public HeadMountedGasBottleSpell() {
        this.baseManaCost = 40;
        this.manaCostPerLevel = 10;
    }

    @Override
    public ResourceLocation getSpellResource() {
        return ResourceLocation.fromNamespaceAndPath(ChemicalMagic.MOD_ID, "head_mounted_gas_bottle");
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    @Override
    public CastType getCastType() {
        return CastType.INSTANT;
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        return Utils.preCastTargetHelper(level, entity, playerMagicData, this, 48, 0.25f, false);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        if (playerMagicData.getAdditionalCastData() instanceof TargetEntityCastData targetData) {
            LivingEntity target = targetData.getTarget(serverLevel);
            if (target != null && target.isAlive()) {
                target.hurt(getDamageSource(entity), 2.0f + spellLevel * 0.75f);
                target.addEffect(new MobEffectInstance(
                        MobEffects.POISON,
                        60 + spellLevel * 12,
                        Math.min(1, (spellLevel - 1) / 5),
                        false,
                        true,
                        true));

                if (target.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
                    target.setItemSlot(EquipmentSlot.HEAD, new ItemStack(ModItems.HEAD_MOUNTED_GAS_BOTTLE.get()));
                }

                spawnGasEffects(serverLevel, target, spellLevel);
            }
        }

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private void spawnGasEffects(ServerLevel level, LivingEntity target, int spellLevel) {
        double x = target.getX();
        double y = target.getY() + target.getBbHeight() + 0.15;
        double z = target.getZ();

        level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                x, y, z, 4 + spellLevel, 0.25, 0.2, 0.25, 0.015);
        level.sendParticles(ParticleTypes.BUBBLE,
                x, y - 0.1, z, 8 + spellLevel * 2, 0.35, 0.35, 0.35, 0.04);
        level.sendParticles(ParticleTypes.GLOW,
                x, y, z, 5 + spellLevel, 0.3, 0.35, 0.3, 0.01);
        target.addEffect(new MobEffectInstance(
                MobEffects.GLOWING,
                60 + spellLevel * 10,
                0,
                false,
                false,
                true));
    }
}
