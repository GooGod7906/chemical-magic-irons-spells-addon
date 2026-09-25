# Chemical Magic

Chemical Magic is a NeoForge 1.21.1 mod that adds spells for Iron's Spells 'n Spellbooks.

## Development

Use Java 21 and run the Gradle wrapper from the project root:

```text
gradlew.bat build
gradlew.bat runClient
```

The development client requires Iron's Spells 'n Spellbooks and its runtime dependencies. The mod's current spell is `chemicalmagic:head_mounted_gas_bottle`.

## Code Organization

Common registries live in `src/main/java/com/example/examplemod/registry`. They only own
the NeoForge `DeferredRegister` instances and are wired once by `ModContent`.

Each spell is organized as a feature under `content/<spell-name>`:

- the feature class declares the spell's item, entity, and spell registrations;
- common spell behavior and entities stay in the feature package;
- client renderers and client event subscribers stay in the feature's `client` package.

To add another spell, create a new feature package with its content declarations, add that
feature to `ModContent`, and keep its resource IDs under the same feature name. This avoids
expanding a shared registry or client class with spell-specific behavior.
