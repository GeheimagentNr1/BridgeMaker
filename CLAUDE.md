# CLAUDE.md - Bridge Maker

Bridge Maker fügt einen Block mit 27 Slots hinzu: Bei Redstone-Signal setzt er die eingelegten Blöcke nacheinander in Blickrichtung als Brücke (Block-Zustand und BlockEntity-Daten, z. B. Shulker-Inhalt, bleiben erhalten), beim Ausschalten sammelt er sie wieder ein. Mod-ID `bridge_maker`, Package `de.geheimagentnr1.bridge_maker`. **Client+Server** (Block, GUI, Rezept), Mod muss auf beiden Seiten installiert sein.

Diese Datei ist auf allen `develop_*`-Branches identisch; Quelle: `../Docs/mod-claude/BridgeMaker.md` (Änderungen dort, dann `Docs/tools/sync_mod_claude_md.sh`). Versionsstand: [`../Docs/VERSIONEN.md`](../Docs/VERSIONEN.md).

## Versionen

- Forge: `develop_1.14.4` .. `develop_1.21`, `develop_1.21.1_forge` (letzter Forge-Stand 1.21.1); `develop_1.21.3` ist ein unveröffentlichter Forge-Versuch.
- NeoForge: sechs Jars - `develop_1.21.1` (nur 1.21.1), `develop_1.21.2` (1.21.2 - 1.21.4), `develop_1.21.5`, `develop_1.21.6` (1.21.6 - 1.21.10), `develop_1.21.11` und `develop_26.1` (ein Jar 26.1 - 26.3). Die Schnitte kommen von BlockEntity-/Inventar-Umbauten, Details: [`../Docs/migrations/1.21.1-to-1.21.2.md`](../Docs/migrations/1.21.1-to-1.21.2.md) Abschnitt 4c.
- Ranges, Tags und Java-Versionen pro Branch: [`../Docs/VERSIONEN.md`](../Docs/VERSIONEN.md).

## Abhängigkeiten

- Keine Mod-Abhängigkeiten.
- Forge 1.20 - 1.21.1-Forge: MinecraftForgeAPI per `jarJar` gebündelt (`AbstractMod`, `AbstractConfig`), siehe [`../Docs/forge/README.md`](../Docs/forge/README.md).

## Architektur

- `BridgeMakerMod` - Mod-Klasse, registriert `ModBlocksRegisterFactory` und `ModCreativeTabsRegisterFactory` auf dem Mod-Bus.
- `elements/blocks/ModBlocksRegisterFactory` - registriert Block, BlockItem, BlockEntity-Typ, Menü, die Daten-Komponenten `bridge_maker:block_states`/`bridge_maker:set_blocks` (ab 1.20.6) und den Screen (`RegisterMenuScreensEvent`).
- `elements/blocks/bridge_maker/` - `BridgeMaker` (Block mit `FACING`/`POWERED`, Setzen in `power(..)`, Einsammeln samt Drops in `unpower(..)`), `BridgeMakerEntity` (27 Slots, eingesammelte BlockStates, gesetzte Slots), `BridgeMakerMenu`, `BridgeMakerScreen`, `BridgeMakerSlot` (nur BlockItems).
- `elements/creative_mod_tabs/` - eigener Kreativ-Tab; `registry/RegistryEntry`/`RegistryHelper` - eigenes kleines Registrierungs-Hilfssystem (NeoForge).
- `util/CodeNetworkHelper` - StreamCodec für die BlockState-Liste (ab 1.21.6 BlockStates per ID); `util/BlockStateCodecHelper` (ab 26.1) - BlockState-Codec mit festem `Name`/`Properties`-Format.
- Forge-Branches: `config/ClientConfig` (`use_vanilla_tab`: Block im Redstone-Tab statt eigenem Tab); bis 1.16.5 noch alte Struktur (`BridgeMakerTile`, `BridgeMakerContainer`, `item_groups/`).

## Funktionen, Befehle, Konfiguration

- Keine Befehle. Rezept: 3 Eisengitter, glatter Stein + Redstone + glatter Stein, 3 glatter Stein.
- NeoForge ohne Config; Forge: Client-Config `use_vanilla_tab`.
- Gespeichert in der BlockEntity: `Items`, `blockStates` (Liste mit `Name`/`Properties`/`Index`), `setBlocks` (Byte-Array). **Das Format ist auf allen Branches gleich**, damit Welten beim Versionswechsel ihre Bridge Maker behalten. Abgebaute Bridge Maker behalten ihren Inhalt über die Item-Komponenten und `minecraft:container` aus der Loot-Table.

## Fallstricke

- **1.21.4**: `BlockEntity.saveToItem` entfernt - in `BridgeMaker.saveToItem` nachgebaut (Pick-Block mit Inhalt).
- **1.21.5**: Container-BlockEntities werfen beim Entfernen ihren Inhalt aus (`preRemoveSideEffects`, auch im Creative, kein Compile-Fehler) - leer überschrieben.
- **1.21.9**: `Container.startOpen/stopOpen(Player)` → `(ContainerUser)` gibt `NoSuchMethodError` beim GUI-Öffnen - die leeren Aufrufe werden weggelassen.
- **26.3**: `BlockState.CODEC` schreibt ein anderes Format (deshalb `BlockStateCodecHelper`), Loot-Tables nutzen `modifier`/`condition` - die Loot-Table enthält beide Formate; `codec()` ohne `@Override`, GUI-`blit` über die `Identifier`-Überladung, damit ein Jar 26.1 - 26.3 reicht.
- Mehrversions-Jars immer per [`../Docs/testing/tools/bincheck.ps1`](../Docs/testing/tools/bincheck.ps1) prüfen (`NoSuchMethodError` kompiliert grün). Alle Brüche: [`../Docs/migrations/BREAKING_CHANGES.md`](../Docs/migrations/BREAKING_CHANGES.md).
- GameTest-Smoke-Test nur auf `develop_1.21.1`; ab 1.21.2 entfernt (Annotation-GameTests ab 1.21.5 weg).

## Testen

Pro Jar niedrigste und höchste Version:
1. Craften, Item hat Textur; GUI: Darstellung, Shift-Klick, nur Blöcke einlegbar.
2. Slab + befüllte Shulker-Box + weitere Blöcke einlegen, per Hebel einschalten, Slab nach oben setzen, aus- und wieder einschalten: Slab liegt oben, Shulker-Inhalt erhalten.
3. Befüllten Bridge Maker abbauen (Survival und Creative): nichts fällt heraus, neu gesetzt ist alles da; Pick-Block (Strg+Mittelklick) auf befüllten Bridge Maker.
4. Server-Neustart im ein- und ausgeschalteten Zustand; bei geändertem Speichercode die Welt der Vorversion übernehmen und befüllte Bridge Maker prüfen (verifiziert 1.21.4 → 1.21.5 → 1.21.6).

Ein per `/setblock` gesetzter Redstone-Block schaltet den Bridge Maker nicht ein (auch in 1.21.1 nicht) - automatisierte Server-Tests decken nur Laden/Speichern ab, das Schalten braucht den Client. Allgemeine Regeln: [`../Docs/testing/README.md`](../Docs/testing/README.md).

## Links

- Wiki: https://github.com/GeheimagentNr1/BridgeMaker/wiki
- CurseForge: https://www.curseforge.com/minecraft/mc-mods/bridge-maker (Projekt-ID 388140)
- Modrinth: https://modrinth.com/mod/Fod8zMxK (Projekt-ID `Fod8zMxK`)
- [`../Docs/migrations/1.21.1-to-1.21.2.md`](../Docs/migrations/1.21.1-to-1.21.2.md) (4c), [`../Docs/migrations/1.21.11-to-26.1.md`](../Docs/migrations/1.21.11-to-26.1.md), [`../Docs/releasing/release-process.md`](../Docs/releasing/release-process.md)
