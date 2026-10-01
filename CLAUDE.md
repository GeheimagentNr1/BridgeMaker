# CLAUDE.md - Bridge Maker

## Projekt-Übersicht

**Bridge Maker** ist ein NeoForge Minecraft Mod.
- **Mod ID**: `bridge_maker`
- **Package**: `de.geheimagentnr1.bridge_maker`
- **Java Version**: 21

Fügt den Bridge Maker hinzu: ein Block mit 27 Slots, der bei Redstone-Signal die eingelegten Blöcke in Blickrichtung als Brücke setzt (Block-Zustand und BlockEntity-Daten, z. B. Shulker-Inhalt, bleiben erhalten) und sie beim Ausschalten wieder einsammelt.

| Branch | MC | Range | NeoForge (kompiliert gegen) | Grund für den Schnitt |
|---|---|---|---|---|
| `develop_1.21.1` | 1.21.1 | `[1.21.1,1.21.2)` | 21.1.x | |
| `develop_1.21.2` | 1.21.2 - 1.21.4 | `[1.21.2,1.21.5)` | `21.2.1-beta` | Block-/Item-IDs (`setId`, `useBlockDescriptionPrefix`), `neighborChanged(.., Orientation, ..)`, `new BlockEntityType<>(..)`. `BlockEntity.saveToItem` (in 1.21.4 entfernt) ist in `BridgeMaker.saveToItem` nachgebaut. `assets/bridge_maker/items/bridge_maker.json` für 1.21.4 |
| `develop_1.21.5` | 1.21.5 | `[1.21.5,1.21.6)` | `21.5.98` | `applyImplicitComponents(DataComponentGetter)`, `CompoundTag`-Getter mit `Optional`; `preRemoveSideEffects` leer überschrieben (sonst wirft Vanilla ab 1.21.5 den Inhalt beim Abbauen aus) |
| `develop_1.21.6` | 1.21.6 - 1.21.10 | `[1.21.6,1.21.11)` | `21.6.20-beta` | `ValueInput`/`ValueOutput` (NBT-Format unverändert), `RenderPipelines.GUI_TEXTURED`, kein eigener `renderBackground`-Aufruf, BlockStates im Netzwerk per ID. Keine `Container.startOpen/stopOpen`-Aufrufe (Signatur ändert sich in 1.21.9, die Methoden sind hier leer) |

Details und Hintergründe: `../Docs/migrations/1.21.1-to-1.21.2.md`, Abschnitt 4c.

## Abhängigkeiten

Keine Mod-Abhängigkeiten - eigenständiger Mod.

## Projektstruktur

```
src/main/java/de/geheimagentnr1/bridge_maker/
├── BridgeMakerMod.java                                        # Haupt-Mod-Klasse
├── elements/
│   ├── blocks/
│   │   ├── BlockItemInterface.java                            # Interface für Block-Items
│   │   ├── ModBlocksRegisterFactory.java                      # Block/Item/BlockEntity/Menu/DataComponent-Registrierung
│   │   └── bridge_maker/
│   │       ├── BridgeMaker.java                               # Block-Klasse (Setzen/Einsammeln)
│   │       ├── BridgeMakerEntity.java                         # Block-Entity (27 Slots, BlockStates, setBlocks)
│   │       ├── BridgeMakerMenu.java                           # Container-Menu
│   │       ├── BridgeMakerScreen.java                         # Client-Screen
│   │       └── BridgeMakerSlot.java                           # Slot (nur BlockItems)
│   └── creative_mod_tabs/
│       ├── BridgeMakerCreativeModeTabFactory.java
│       ├── CreativeModeTabFactory.java
│       └── ModCreativeTabsRegisterFactory.java
├── registry/
│   ├── RegistryEntry.java                                     # Registry-Utility
│   └── RegistryHelper.java                                    # Registry-Helper
└── util/
    └── CodeNetworkHelper.java                                 # StreamCodec für die BlockState-Liste
```

## Besonderheiten

- **Block-Entity mit GUI**: `BridgeMakerEntity`, `BridgeMakerMenu`, `BridgeMakerScreen` für Inventar-UI
- **Gespeicherte Daten**: Items (`Items`), Block-Zustände der eingesammelten Blöcke (`blockStates`, Liste mit `Name`/`Properties`/`Index`) und welche Slots gerade gesetzt sind (`setBlocks`, Byte-Array). Das Format ist in allen Branches gleich, damit Welten beim Versionswechsel ihre Bridge Maker behalten
- **Item-Komponenten**: `bridge_maker:block_states`, `bridge_maker:set_blocks` (und `minecraft:container` über die Loot-Table), damit ein abgebauter Bridge Maker seinen Inhalt behält
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

```powershell
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.12.8-hotspot"
./gradlew build
```

Ein grüner Compile gegen jede Zielversion reicht nicht: zusätzlich `../Docs/testing/tools/bincheck.ps1 -PropNeoForge neoforge_version` laufen lassen (Bytecode-Referenzen je Version vergleichen, die Ausgabe muss `<n> references from ...` zeigen).

### Ingame-Test (pro Jar niedrigste und höchste Version)

1. Craften (3 Eisengitter, glatter Stein + Redstone + glatter Stein, 3 glatter Stein); Item hat Textur
2. GUI: Darstellung, Shift-Klick, nur Blöcke einlegbar
3. Slab + befüllte Shulker-Box + weitere Blöcke einlegen, per Hebel einschalten, Slab nach oben setzen, aus- und wieder einschalten: Slab liegt oben, Shulker-Inhalt erhalten
4. Befüllten Bridge Maker abbauen (Survival und Creative): kein Inhalt fällt heraus; neu gesetzt ist alles erhalten
5. Pick-Block (Strg+Mittelklick) auf befüllten Bridge Maker
6. Server-Neustart im ein- und im ausgeschalteten Zustand
7. Bei geändertem Speichercode: Welt der Vorversion kopieren und die befüllten Bridge Maker prüfen (verifiziert 1.21.4 → 1.21.5 → 1.21.6)

Hinweis: Ein per `/setblock` gesetzter Redstone-Block schaltet den Bridge Maker nicht ein (auch nicht in 1.21.1), automatisierte Server-Tests decken deshalb nur Laden/Speichern ab.

### Unit Tests (JUnit 5)

```bash
./gradlew test
```

Tests liegen unter `src/test/java/`. Ergebnisse: `build/reports/tests/test/index.html`

### NeoForge GameTest Framework

Ab `develop_1.21.2` gibt es keine GameTests: Das Annotations-Framework (`@GameTest`, `@GameTestHolder`) existiert ab 1.21.5 nicht mehr, der triviale Smoke-Test wurde samt `gameTestServer`-Run-Config und CI-Job entfernt (siehe `../Docs/migrations/1.21.10-to-1.21.11.md`).

### CI/CD (GitHub Actions)

Der Workflow `.github/workflows/build-and-test.yml` führt automatisch aus:
1. **Build**: Kompiliert den Mod
2. **Unit Tests**: Führt JUnit Tests aus

## Referenzen

- [NeoForge Migration Primer](https://docs.neoforged.net/primer/docs/) — Dokumentiert API-Aenderungen zwischen Minecraft/NeoForge-Versionen; nuetzlich fuer die Pruefung von Breaking Changes beim Upgrade auf neue Versionen

---

## Wissensdatenbank

Versionsübergreifende Migrations- und Entwicklungs-Erkenntnisse (Breaking Changes, Fixes, Testumgebungs-Patterns) werden zentral in [`../Docs/`](../Docs/) gepflegt. Bei neuen relevanten Erkenntnissen dort ergänzen, nicht nur hier.
