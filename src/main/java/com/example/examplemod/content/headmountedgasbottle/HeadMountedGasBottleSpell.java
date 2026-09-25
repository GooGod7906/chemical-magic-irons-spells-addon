package com.example.examplemod.content.headmountedgasbottle;

import com.example.examplemod.ChemicalMagic;
import com.example.examplemod.content.headmountedgasbottle.entity.HeadMountedGasBottleProjectile;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.config.SpellConfigManager;
import io.redspace.ironsspellbooks.api.config.SpellConfigParameter;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastResult;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class HeadMountedGasBottleSpell extends AbstractSpell {
    private int lastCastSpellLevel = 1;
    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.COMMON)
            .setSchoolResource(SchoolRegistry.NATURE_RESOURCE)
            .setMaxLevel(10)
            .setCooldownSeconds(3)
            .build();

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
    public int getManaCost(int spellLevel) {
        SpellRarity rarity = getRarity(Math.max(1, spellLevel));
        int baseCost = 20 + rarity.getValue() * 5;
        return (int) (baseCost * SpellConfigManager.getSpellConfigValue(
                this, SpellConfigParameter.MANA_MULTIPLIER));
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        return true;
    }

    @Override
    public CastResult canBeCastedBy(int spellLevel, CastSource castSource, MagicData playerMagicData,
                                    net.minecraft.world.entity.player.Player player) {
        return super.canBeCastedBy(spellLevel, normalizeCastSource(castSource), playerMagicData, player);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource,
                       MagicData playerMagicData) {
        lastCastSpellLevel = spellLevel;
        if (level instanceof ServerLevel serverLevel) {
            HeadMountedGasBottleProjectile projectile = new HeadMountedGasBottleProjectile(
                    HeadMountedGasBottleContent.PROJECTILE.get(), entity, serverLevel);
            projectile.setSpellLevel(spellLevel);
            projectile.setRandomRoll(serverLevel.random.nextFloat() * 360.0f);
            projectile.shootFromRotation(entity, entity.getXRot(), entity.getYRot(), 0.0f, 0.8f, 0.0f);
            projectile.setYRot(entity.getYRot());
            projectile.setXRot(entity.getXRot());
            projectile.yRotO = projectile.getYRot();
            projectile.xRotO = projectile.getXRot();
            serverLevel.addFreshEntity(projectile);
        }

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    @Override
    public void castSpell(Level world, int spellLevel, ServerPlayer serverPlayer,
                          CastSource castSource, boolean triggerCooldown) {
        CastSource effectiveCastSource = normalizeCastSource(castSource);
        super.castSpell(world, spellLevel, serverPlayer, effectiveCastSource,
                triggerCooldown || effectiveCastSource == CastSource.SPELLBOOK);
    }

    private CastSource normalizeCastSource(CastSource castSource) {
        return castSource == CastSource.NONE || castSource == CastSource.SWORD
                || castSource == CastSource.SCROLL
                ? CastSource.SPELLBOOK
                : castSource;
    }

    public int getLastCastSpellLevel() {
        return lastCastSpellLevel;
    }

    public double getCooldownMultiplier(int spellLevel) {
        return switch (getRarity(Math.max(1, spellLevel))) {
            case COMMON -> 1.0;
            case UNCOMMON -> 0.9;
            case RARE -> 0.8;
            case EPIC -> 0.7;
            case LEGENDARY -> 0.6;
        };
    }

    public void applyHitEffects(ServerLevel level, net.minecraft.world.entity.Entity projectile,
                                LivingEntity caster, LivingEntity target, int spellLevel) {
        if (!target.isAlive()) {
            return;
        }

        target.hurt(getDamageSource(projectile, caster), 2.0f + spellLevel * 0.75f);
        target.addEffect(new MobEffectInstance(
                MobEffects.POISON,
                60 + spellLevel * 12,
                Math.min(1, (spellLevel - 1) / 5),
                false,
                true,
                true));

        if (target.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
            target.setItemSlot(EquipmentSlot.HEAD, new ItemStack(HeadMountedGasBottleContent.ITEM.get()));
        }

        spawnGasEffects(level, target, spellLevel);
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
