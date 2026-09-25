# Project Agent Notes

## Project

- NeoForge 1.21.1 mod for Iron's Spells 'n Spellbooks.
- Use Java 21.
- Mod id: `chemicalmagic`.
- Current spell feature: `headmountedgasbottle`.

## Architecture

The project is organized around spell features rather than global per-spell folders.

- `src/main/java/com/example/examplemod/ChemicalMagic.java` is the common mod entry point.
- `src/main/java/com/example/examplemod/ChemicalMagicClient.java` is the client-only entry point.
- `src/main/java/com/example/examplemod/registry` contains only shared NeoForge `DeferredRegister` objects and their small registration helpers.
- `src/main/java/com/example/examplemod/registry/ModContent.java` is the single common registration wiring point.
- `src/main/java/com/example/examplemod/content/<spell-name>` contains one spell feature and its common behavior.
- `src/main/java/com/example/examplemod/content/<spell-name>/client` contains that feature's renderers and client event registration.
- `src/main/resources/assets/chemicalmagic` contains the feature resources and translations. Keep existing registry ids and resource paths stable unless a content migration is intentional.

The current feature is implemented in:

- `content/headmountedgasbottle/HeadMountedGasBottleContent.java`: item, projectile, spell declarations, and common event registration.
- `content/headmountedgasbottle/HeadMountedGasBottleSpell.java`: spell behavior.
- `content/headmountedgasbottle/item`: the spell item.
- `content/headmountedgasbottle/entity`: the spell projectile.
- `content/headmountedgasbottle/client`: client registration, models, renderers, and layers.

## Adding A Spell Feature

1. Create `content/<spell-name>` using a lowercase registry-safe feature name.
2. Add one feature content class that declares the feature's spell, items, entities, and feature-specific common event listeners through the shared registry helpers.
3. Add the feature initialization call to `ModContent.register` before the shared registries are attached to the mod event bus.
4. Put common behavior in the feature package and client-only code in its `client` package.
5. Register client event listeners from the client feature through `ChemicalMagicClient`; do not add spell-specific logic to the root entry points or another spell's package.
6. Add models, textures, translations, and other assets under `src/main/resources/assets/chemicalmagic` while preserving the declared ids.
7. Run the build before handing off the change.

## Official Documentation

- Iron's Spells 'n Spellbooks developer documentation: https://iron.wiki/developers/
- NeoForge ModDev Gradle documentation: https://github.com/neoforged/ModDevGradle

## Common Commands

```text
gradlew.bat build
gradlew.bat runClient
```

## Build Environment

- Use Java 21. The current Microsoft JDK `21.0.11` can fail during `:createMinecraftArtifacts` with `Fatal error: unable to close compiler resources` while NeoForm recompiles Minecraft.
- A verified working JDK in the development environment is JetBrains Runtime `21.0.6` at `C:\Program Files\JetBrains\PyCharm Community Edition 2025.1.1.1\jbr`.
- Do not change the user-level or system-level `JAVA_HOME`. When a different JDK is needed, set it only for the current PowerShell process and restore it when finished:

```powershell
$previousJavaHome = $env:JAVA_HOME
$env:JAVA_HOME = 'C:\Program Files\JetBrains\PyCharm Community Edition 2025.1.1.1\jbr'
try {
    .\gradlew.bat -g D:\PROJECTS\DEV-IRON\gradle build --offline --no-daemon --no-parallel --max-workers=1
}
finally {
    $env:JAVA_HOME = $previousJavaHome
}
```

- If the default Gradle user home is not writable, use the workspace cache with `-g D:\PROJECTS\DEV-IRON\gradle`. This is a per-command option and does not change global Gradle settings.
- The build produces the mod JAR under `build/libs`.
- If NeoForm fails before `compileJava`, treat it as a JDK/cache/toolchain issue first. If `compileJava` reports missing Iron's Spells 'n Spellbooks classes, verify that the full mod remains an `implementation` dependency.

## Iron's Spells 'n Spellbooks Dependencies

The project uses the full Iron's Spells 'n Spellbooks artifact at compile and runtime. Keep these dependency roles unless the source is deliberately migrated to API-only packages:

```groovy
localRuntime "io.redspace:irons_lib:${irons_lib_version}"
implementation "io.redspace:irons_spellbooks:${irons_spells_version}"
```

Do not replace `implementation` with only `compileOnly ...:api` without verifying all feature code and the dedicated-server/runtime classpath.
