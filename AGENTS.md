# AGENTS.md - Bridge Maker

## Projekt-Übersicht

**Bridge Maker** ist ein NeoForge Minecraft Mod für Minecraft 1.21.1.
- **Mod ID**: `bridge_maker`
- **Package**: `de.geheimagentnr1.bridge_maker`
- **Java Version**: 21
- **NeoForge Version**: 21.1.x

Fügt den Bridge Maker hinzu, der Blöcke in einer Entfernung von 27 Blöcken platzieren/abbauen kann.

## Abhängigkeiten

Keine Mod-Abhängigkeiten - eigenständiger Mod.

## Projektstruktur

```
src/main/java/de/geheimagentnr1/bridge_maker/
├── BridgeMakerMod.java                    # Haupt-Mod-Klasse
├── elements/
│   ├── blocks/
│   │   ├── BlockItemInterface.java        # Interface für Block-Items
│   │   └── ModBlocksRegisterFactory.java  # Block-Registry
│   └── creative_mod_tabs/                 # Creative-Tab Registration
├── registry/
│   ├── RegistryEntry.java                 # Registry-Utility
│   └── RegistryHelper.java                # Registry-Helper
└── util/
    └── CodeNetworkHelper.java             # Netzwerk-Utilities
```

## Besonderheiten

- **Eigenes Registry-System**: Eigene `RegistryEntry` und `RegistryHelper` Klassen
- **Block-Items**: Blöcke mit zugehörigen Items

## Code-Stil

- **Annotations**: `@NotNull` aus `org.jetbrains.annotations`
- **Lombok**: Projekt nutzt Lombok
- **Formatierung**: Leerzeichen nach `(` und vor `)` bei Methodenaufrufen

## Build & Test

```bash
./gradlew build
./gradlew runClient
./gradlew runServer
```

## Deployment

- **CurseForge**: `./gradlew curseforge`
- **Modrinth**: `./gradlew modrinth`
