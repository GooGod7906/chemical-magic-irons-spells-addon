# Project Agent Notes

## Project

- NeoForge 1.21.1 mod for Iron's Spells 'n Spellbooks.
- Use Java 21.
- Main spell source: `src/main/java/com/example/examplemod/Spells/HeadMountedGasBottleSpell.java`

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
- The verified command above completed `build` successfully. It produced the mod JAR under `build/libs`.
- If NeoForm fails before `compileJava`, treat it as a JDK/cache/toolchain issue first. If `compileJava` reports missing `io.redspace.ironsspellbooks.*` implementation classes, check the dependency mode below.

## Iron's Spells 'n Spellbooks Dependencies

- The official NeoForge setup uses `localRuntime` for `irons_lib` and the full mod when only stable API packages are referenced.
- This project uses implementation classes such as `TargetEntityCastData` and `SpellDamageSource`, so `irons_spellbooks` must remain an `implementation` dependency rather than only `compileOnly ...:api`:

```groovy
localRuntime "io.redspace:irons_lib:${irons_lib_version}"
implementation "io.redspace:irons_spellbooks:${irons_spells_version}"
```

- Spell registration is defined in `src/main/java/com/example/examplemod/Spells/Spells.java` and the spell implementation is in `src/main/java/com/example/examplemod/Spells/HeadMountedGasBottleSpell.java`.
