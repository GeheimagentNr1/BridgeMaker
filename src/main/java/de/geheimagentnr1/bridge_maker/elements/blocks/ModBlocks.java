package de.geheimagentnr1.bridge_maker.elements.blocks;

import de.geheimagentnr1.bridge_maker.BridgeMakerMod;
import de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker.BridgeMaker;
import de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker.BridgeMakerEntity;
import de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker.BridgeMakerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.ObjectHolder;


@SuppressWarnings( { "PublicStaticArrayField", "StaticNonFinalField" } )
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
	public static BlockEntityType<BridgeMakerEntity> BRIDGE_MAKER_ENTITY;
	
	@ObjectHolder( BridgeMakerMod.MODID + ":" + BridgeMaker.registry_name )
	public static MenuType<BridgeMakerMenu> BRIDGE_MAKER_CONTAINER;
}
