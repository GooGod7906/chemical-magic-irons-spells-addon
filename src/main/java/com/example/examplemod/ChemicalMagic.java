package com.example.examplemod;

import com.example.examplemod.Entities.ModEntities;
import com.example.examplemod.Items.ModItems;
import com.example.examplemod.Spells.HeadMountedGasBottleSpell;
import com.example.examplemod.Spells.Spells;
import com.mojang.logging.LogUtils;
import io.redspace.ironsspellbooks.api.events.SpellCooldownAddedEvent;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import net.neoforged.neoforge.common.NeoForge;

@Mod(ChemicalMagic.MOD_ID)
public class ChemicalMagic {
    public static final String MOD_ID = "chemicalmagic";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ChemicalMagic(IEventBus modEventBus, ModContainer modContainer) {
        ModItems.ITEMS.register(modEventBus);
        ModEntities.ENTITY_TYPES.register(modEventBus);
        Spells.SPELLS.register(modEventBus);
        NeoForge.EVENT_BUS.addListener(ChemicalMagic::onSpellCooldownAdded);
        LOGGER.info("Chemical Magic registries initialized");
    }

    private static void onSpellCooldownAdded(SpellCooldownAddedEvent.Pre event) {
        AbstractSpell registeredSpell = Spells.HEAD_MOUNTED_GAS_BOTTLE_SPELL.get();
        if (event.getSpell() != registeredSpell) {
            return;
        }

        HeadMountedGasBottleSpell spell = (HeadMountedGasBottleSpell) registeredSpell;
        int adjustedCooldown = (int) Math.round(event.getEffectiveCooldown()
                * spell.getCooldownMultiplier(spell.getLastCastSpellLevel()));
        event.setEffectiveCooldown(adjustedCooldown);
    }
}
