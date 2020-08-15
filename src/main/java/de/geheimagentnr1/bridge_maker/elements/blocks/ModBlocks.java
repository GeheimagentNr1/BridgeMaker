package de.geheimagentnr1.bridge_maker.elements.blocks;

import de.geheimagentnr1.bridge_maker.BridgeMakerMod;
import de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker.BridgeMaker;
import de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker.BridgeMakerContainer;
import de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker.BridgeMakerTile;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;


@SuppressWarnings( { "StaticNonFinalField", "PublicField", "PublicStaticArrayField", "unused" } )
public class ModBlocks {
	
	//TODO:
	// B - Block Textur fertig
	// C - Cullface korrekt
	// P - Partikel fertig
	// F - Funktion fertig
	// I - Item fertig
	// N - Name und Registierungsname vorhanden und fertig
	// R - Rezept fertig
	// L - Loottable fertig
	// T - Tags fertig
	
	public static final Block[] BLOCKS = new Block[] {//BCPFINRLT
		new BridgeMaker(),//BCPFINRLT
	};
	
	@ObjectHolder( BridgeMakerMod.MODID + ":" + BridgeMaker.registry_name )
	public static BridgeMaker BRIDGE_MAKER;
	
	@ObjectHolder( BridgeMakerMod.MODID + ":" + BridgeMaker.registry_name )
	public static TileEntityType<BridgeMakerTile> BRIDGE_MAKER_TILE;
	
	@ObjectHolder( BridgeMakerMod.MODID + ":" + BridgeMaker.registry_name )
	public static ContainerType<BridgeMakerContainer> BRIDGE_MAKER_CONTAINER;
}
