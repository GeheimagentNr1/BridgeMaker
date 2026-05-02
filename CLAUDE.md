# CLAUDE.md - Bridge Maker

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
├── BridgeMakerMod.java                                        # Haupt-Mod-Klasse
├── elements/
│   ├── blocks/
│   │   ├── BlockItemInterface.java                            # Interface für Block-Items
│   │   ├── ModBlocksRegisterFactory.java                      # Block-Registry
│   │   └── bridge_maker/
│   │       ├── BridgeMaker.java                               # Block-Klasse
│   │       ├── BridgeMakerEntity.java                         # Block-Entity
│   │       ├── BridgeMakerMenu.java                           # Container-Menu
│   │       ├── BridgeMakerScreen.java                         # Client-Screen
│   │       └── BridgeMakerSlot.java                           # Inventory-Slot
│   ├── creative_mod_tabs/
│   │   ├── BridgeMakerCreativeModeTabFactory.java
│   │   ├── CreativeModeTabFactory.java
│   │   └── ModCreativeTabsRegisterFactory.java
│   └── gametests/
│       └── BridgeMakerGameTests.java
├── registry/
│   ├── RegistryEntry.java                                     # Registry-Utility
│   └── RegistryHelper.java                                    # Registry-Helper
└── util/
    └── CodeNetworkHelper.java                                 # Netzwerk-Utilities
```

## Besonderheiten

- **Block-Entity mit GUI**: `BridgeMakerEntity`, `BridgeMakerMenu`, `BridgeMakerScreen` für Inventar-UI
- **Eigenes Registry-System**: Eigene `RegistryEntry` und `RegistryHelper` Klassen

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

## Testing

### Java-Versionen

Verschiedene Java-Versionen sind unter `C:\Program Files\Eclipse Adoptium` installiert. Für einen Gradle-Build muss die passende Java-Version gewählt werden:

```powershell
# Java 21 für MC 1.20.5+ (NeoForge)
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.9.10-hotspot"
./gradlew build
```

### Unit Tests (JUnit 5)

Für reine Logik-Tests ohne Minecraft-Abhängigkeiten:

```bash
./gradlew test
```

Tests liegen unter `src/test/java/`. Ergebnisse: `build/reports/tests/test/index.html`

### NeoForge GameTest Framework

Für Integration Tests in einer echten Minecraft-Umgebung:

```bash
./gradlew runGameTestServer
```

GameTest-Klassen werden mit `@GameTestHolder` annotiert und liegen unter `src/main/java/.../elements/gametests/`.

### CI/CD (GitHub Actions)

Der Workflow `.github/workflows/build-and-test.yml` führt automatisch aus:
1. **Build**: Kompiliert den Mod
2. **Unit Tests**: Führt JUnit Tests aus
3. **GameTests**: Startet GameTestServer (optional)

### Was kann automatisiert getestet werden?

| Aspekt | Automatisiert? | Methode |
|--------|----------------|---------|
| Utility-Klassen | ✅ | JUnit |
| Config-Parsing | ✅ | JUnit |
| Commands | ✅ | GameTest |
| Block/Item-Verhalten | ✅ | GameTest |
| Multi-MC-Version | ⚠️ Pro Branch | CI Matrix |

## Referenzen

- [NeoForge Migration Primer](https://docs.neoforged.net/primer/docs/) — Dokumentiert API-Aenderungen zwischen Minecraft/NeoForge-Versionen; nuetzlich fuer die Pruefung von Breaking Changes beim Upgrade auf neue Versionen
