package de.geheimagentnr1.bridge_maker.elements.blocks;

import de.geheimagentnr1.bridge_maker.BridgeMakerMod;
import de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker.BridgeMaker;
import de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker.BridgeMakerEntity;
import de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker.BridgeMakerMenu;
import de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker.BridgeMakerScreen;
import de.geheimagentnr1.minecraft_forge_api.elements.blocks.BlocksRegisterFactory;
import de.geheimagentnr1.minecraft_forge_api.registry.RegistryEntry;
import de.geheimagentnr1.minecraft_forge_api.registry.RegistryHelper;
import de.geheimagentnr1.minecraft_forge_api.registry.RegistryKeys;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ObjectHolder;
import org.jetbrains.annotations.NotNull;

import java.util.List;


@SuppressWarnings( "StaticNonFinalField" )
public class ModBlocksRegisterFactory extends BlocksRegisterFactory {
	
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
	
	@ObjectHolder( registryName = RegistryKeys.BLOCKS, value = BridgeMakerMod.MODID + ":" + BridgeMaker.registry_name )
	public static BridgeMaker BRIDGE_MAKER;
	
	@ObjectHolder( registryName = RegistryKeys.BLOCK_ENTITY_TYPES,
		value = BridgeMakerMod.MODID + ":" + BridgeMaker.registry_name )
	public static BlockEntityType<BridgeMakerEntity> BRIDGE_MAKER_ENTITY;
	
	@ObjectHolder( registryName = RegistryKeys.MENU_TYPES,
		value = BridgeMakerMod.MODID + ":" + BridgeMaker.registry_name )
	public static MenuType<BridgeMakerMenu> BRIDGE_MAKER_CONTAINER;
	
	@NotNull
	@Override
	protected List<RegistryEntry<Block>> blocks() {
		
		return List.of(//BCPFINRLT
			RegistryEntry.create( BridgeMaker.registry_name, new BridgeMaker() )//BCPFINRLT
		);
	}
	
	@NotNull
	@Override
	protected List<RegistryEntry<BlockEntityType<?>>> blockEntityTypes() {
		
		return List.of(
			RegistryEntry.create(
				BridgeMaker.registry_name,
				RegistryHelper.buildBlockEntity( BridgeMaker.registry_name, BridgeMakerEntity::new, BRIDGE_MAKER )
			)
		);
	}
	
	@NotNull
	@Override
	protected List<RegistryEntry<MenuType<?>>> menuTypes() {
		
		return List.of(
			RegistryEntry.create(
				BridgeMaker.registry_name,
				IForgeMenuType.create( ( windowId, inv, data ) -> new BridgeMakerMenu( windowId, inv ) )
			)
		);
	}
	
	@SubscribeEvent
	@Override
	public void handleFMLClientSetupEvent( @NotNull FMLClientSetupEvent event ) {
		
		MenuScreens.register( BRIDGE_MAKER_CONTAINER, BridgeMakerScreen::new );
	}
}
