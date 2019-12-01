package de.geheimagentnr1.bridge_maker.handlers;

import de.geheimagentnr1.bridge_maker.BridgeMakerMod;
import de.geheimagentnr1.bridge_maker.elements.blocks.BlockItemInterface;
import de.geheimagentnr1.bridge_maker.elements.blocks.ModBlocks;
import de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker.BridgeMaker;
import de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker.BridgeMakerContainer;
import de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker.BridgeMakerTile;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;


@SuppressWarnings( "unused" )
@Mod.EventBusSubscriber( bus = Mod.EventBusSubscriber.Bus.MOD )
public class RegistryEventHandler {
	
	
	@SubscribeEvent
	public static void setup( FMLCommonSetupEvent event ) {
		
		BridgeMakerMod.proxy.init();
	}
	
	@SubscribeEvent
	public static void onBlocksRegistry( RegistryEvent.Register<Block> blockRegistryEvent ) {
		
		blockRegistryEvent.getRegistry().registerAll( ModBlocks.BLOCKS );
	}
	
	@SubscribeEvent
	public static void onItemsRegistry( RegistryEvent.Register<Item> itemRegistryEvent ) {
		
		Item.Properties properties = new Item.Properties().group( BridgeMakerMod.setup.bridgeMakerItemGroup );
		
		for( Block block : ModBlocks.BLOCKS ) {
			if( block instanceof BlockItemInterface ) {
				BlockItemInterface blockItem = (BlockItemInterface)block;
				itemRegistryEvent.getRegistry().register( blockItem.getBlockItem( properties ) );
			}
		}
	}
	
	@SuppressWarnings( "ConstantConditions" )
	@SubscribeEvent
	public static void onTileEntityRegistry( RegistryEvent.Register<TileEntityType<?>> event ) {
		
		event.getRegistry().register( TileEntityType.Builder.create( BridgeMakerTile::new,
			ModBlocks.BRIDGE_MAKER ).build( null ).setRegistryName( BridgeMaker.registry_name ) );
	}
	
	@SubscribeEvent
	public static void onContainerRegistry( RegistryEvent.Register<ContainerType<?>> event ) {
		
		event.getRegistry().register( IForgeContainerType.create( ( windowId, inv, data ) ->
			new BridgeMakerContainer( windowId, inv ) ).setRegistryName( BridgeMaker.registry_name ) );
	}
}
