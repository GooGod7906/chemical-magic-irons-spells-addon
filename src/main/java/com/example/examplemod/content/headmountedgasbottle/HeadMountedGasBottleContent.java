package com.example.examplemod.content.headmountedgasbottle;

import com.example.examplemod.content.headmountedgasbottle.entity.HeadMountedGasBottleProjectile;
import com.example.examplemod.content.headmountedgasbottle.item.HeadMountedGasBottleItem;
import com.example.examplemod.registry.ModEntities;
import com.example.examplemod.registry.ModItems;
import com.example.examplemod.registry.ModSpells;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.events.SpellCooldownAddedEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.common.NeoForge;

import java.util.function.Supplier;

public final class HeadMountedGasBottleContent {
    public static final Supplier<HeadMountedGasBottleItem> ITEM = ModItems.register(
            "head_mounted_gas_bottle",
            () -> new HeadMountedGasBottleItem());
    public static final Supplier<EntityType<HeadMountedGasBottleProjectile>> PROJECTILE = ModEntities.register(
            "head_mounted_gas_bottle_projectile",
            () -> EntityType.Builder.<HeadMountedGasBottleProjectile>of(
                            HeadMountedGasBottleProjectile::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f)
                    .clientTrackingRange(64)
                    .updateInterval(1)
                    .build("head_mounted_gas_bottle_projectile"));
    public static final Supplier<AbstractSpell> SPELL = ModSpells.register(
            "head_mounted_gas_bottle", HeadMountedGasBottleSpell::new);

    private HeadMountedGasBottleContent() {
    }

    public static void register() {
        // Referencing this method makes the feature declarations load before registry setup.
    }

    public static void registerCommonEvents() {
        NeoForge.EVENT_BUS.addListener(HeadMountedGasBottleContent::onSpellCooldownAdded);
    }

    private static void onSpellCooldownAdded(SpellCooldownAddedEvent.Pre event) {
        AbstractSpell registeredSpell = SPELL.get();
        if (event.getSpell() != registeredSpell) {
            return;
        }

        HeadMountedGasBottleSpell spell = (HeadMountedGasBottleSpell) registeredSpell;
        int adjustedCooldown = (int) Math.round(event.getEffectiveCooldown()
                * spell.getCooldownMultiplier(spell.getLastCastSpellLevel()));
        event.setEffectiveCooldown(adjustedCooldown);
    }
}
