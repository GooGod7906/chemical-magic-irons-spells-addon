package com.example.examplemod.Spells;

import com.example.examplemod.ChemicalMagic;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class Spells {
    public static final DeferredRegister<AbstractSpell> SPELLS =
            DeferredRegister.create(SpellRegistry.SPELL_REGISTRY_KEY,
                    ChemicalMagic.MOD_ID);
    public static final Supplier<AbstractSpell> HEAD_MOUNTED_GAS_BOTTLE_SPELL =
            SPELLS.register("head_mounted_gas_bottle", HeadMountedGasBottleSpell::new);
}
