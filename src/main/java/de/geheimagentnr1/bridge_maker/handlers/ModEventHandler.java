package de.geheimagentnr1.bridge_maker.handlers;

import de.geheimagentnr1.bridge_maker.BridgeMakerMod;
import de.geheimagentnr1.bridge_maker.config.ClientConfig;
import de.geheimagentnr1.bridge_maker.elements.blocks.BlockItemInterface;
import de.geheimagentnr1.bridge_maker.elements.blocks.ModBlocks;
import de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker.BridgeMaker;
import de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker.BridgeMakerContainer;
import de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker.BridgeMakerScreen;
import de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker.BridgeMakerTile;
import de.geheimagentnr1.bridge_maker.elements.item_groups.ModItemGroups;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;


@Mod.EventBusSubscriber( modid = BridgeMakerMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD )
public class ModEventHandler {
	
	
	@SubscribeEvent
	public static void handleModConfigLoadingEvent( ModConfig.Loading event ) {
		
		ClientConfig.printConfig();
	}
	
	@SubscribeEvent
	public static void handleModConfigReloadingEvent( ModConfig.Reloading event ) {
		
		ClientConfig.printConfig();
	}
	
	@OnlyIn( Dist.CLIENT )
	@SubscribeEvent
	public static void handleClientSetupEvent( FMLClientSetupEvent event ) {
		
		ScreenManager.register( ModBlocks.BRIDGE_MAKER_CONTAINER, BridgeMakerScreen::new );
	}
	
	@SubscribeEvent
	public static void handleBlockRegistryEvent( RegistryEvent.Register<Block> blockRegistryEvent ) {
		
		blockRegistryEvent.getRegistry().registerAll( ModBlocks.BLOCKS );
	}
	
	@SubscribeEvent
	public static void handleItemRegistryEvent( RegistryEvent.Register<Item> itemRegistryEvent ) {
		
		Item.Properties properties = new Item.Properties().tab( ModItemGroups.getItemGroup() );
		
		for( Block block : ModBlocks.BLOCKS ) {
			if( block instanceof BlockItemInterface ) {
				BlockItemInterface blockItem = (BlockItemInterface)block;
				itemRegistryEvent.getRegistry().register( blockItem.getBlockItem( properties ) );
			}
		}
	}
	
	@SuppressWarnings( "ConstantConditions" )
	@SubscribeEvent
	public static void handleTileEntityTypeRegistryEvent( RegistryEvent.Register<TileEntityType<?>> event ) {
		
		event.getRegistry().register( TileEntityType.Builder.of( BridgeMakerTile::new, ModBlocks.BRIDGE_MAKER )
			.build( null )
			.setRegistryName( BridgeMaker.registry_name ) );
	}
	
	@SubscribeEvent
	public static void handleContainerTypeRegistryEvent( RegistryEvent.Register<ContainerType<?>> event ) {
		
		event.getRegistry().register( IForgeContainerType.create( ( windowId, inv, data ) -> new BridgeMakerContainer(
			windowId,
			inv
		) ).setRegistryName( BridgeMaker.registry_name ) );
	}
}
