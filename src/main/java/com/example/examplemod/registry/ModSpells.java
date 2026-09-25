package com.example.examplemod.registry;

import com.example.examplemod.ChemicalMagic;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ModSpells {
    public static final DeferredRegister<AbstractSpell> SPELLS =
            DeferredRegister.create(SpellRegistry.SPELL_REGISTRY_KEY, ChemicalMagic.MOD_ID);

    private ModSpells() {
    }

    public static <T extends AbstractSpell> Supplier<T> register(String id, Supplier<T> spell) {
        return SPELLS.register(id, spell);
    }
}
