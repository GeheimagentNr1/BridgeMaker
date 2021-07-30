package de.geheimagentnr1.bridge_maker.handlers;

import de.geheimagentnr1.bridge_maker.BridgeMakerMod;
import de.geheimagentnr1.bridge_maker.config.ClientConfig;
import de.geheimagentnr1.bridge_maker.elements.blocks.BlockItemInterface;
import de.geheimagentnr1.bridge_maker.elements.blocks.ModBlocks;
import de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker.BridgeMaker;
import de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker.BridgeMakerEntity;
import de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker.BridgeMakerMenu;
import de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker.BridgeMakerScreen;
import de.geheimagentnr1.bridge_maker.elements.item_groups.ModItemGroups;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;


@Mod.EventBusSubscriber( modid = BridgeMakerMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD )
public class ModEventHandler {
	
	
	@SubscribeEvent
	public static void handleModConfigLoadingEvent( ModConfigEvent.Loading event ) {
		
		ClientConfig.printConfig();
	}
	
	@SubscribeEvent
	public static void handleModConfigReloadingEvent( ModConfigEvent.Reloading event ) {
		
		ClientConfig.printConfig();
	}
	
	@OnlyIn( Dist.CLIENT )
	@SubscribeEvent
	public static void handleClientSetupEvent( FMLClientSetupEvent event ) {
		
		MenuScreens.register( ModBlocks.BRIDGE_MAKER_CONTAINER, BridgeMakerScreen::new );
	}
	
	@SubscribeEvent
	public static void handleBlockRegistryEvent( RegistryEvent.Register<Block> blockRegistryEvent ) {
		
		blockRegistryEvent.getRegistry().registerAll( ModBlocks.BLOCKS );
	}
	
	@SubscribeEvent
	public static void handleItemRegistryEvent( RegistryEvent.Register<Item> itemRegistryEvent ) {
		
		Item.Properties properties = new Item.Properties().tab( ModItemGroups.getItemGroup() );
		
		for( Block block : ModBlocks.BLOCKS ) {
			if( block instanceof BlockItemInterface blockItem ) {
				itemRegistryEvent.getRegistry().register( blockItem.getBlockItem( properties ) );
			}
		}
	}
	
	@SuppressWarnings( "ConstantConditions" )
	@SubscribeEvent
	public static void handleBlockEntityTypeRegistryEvent( RegistryEvent.Register<BlockEntityType<?>> event ) {
		
		event.getRegistry().register( BlockEntityType.Builder.of( BridgeMakerEntity::new, ModBlocks.BRIDGE_MAKER )
			.build( null )
			.setRegistryName( BridgeMaker.registry_name ) );
	}
	
	@SubscribeEvent
	public static void handleContainerTypeRegistryEvent( RegistryEvent.Register<MenuType<?>> event ) {
		
		event.getRegistry().register(
			IForgeContainerType.create( ( containerId, inv, data ) -> new BridgeMakerMenu( containerId, inv ) )
				.setRegistryName( BridgeMaker.registry_name )
		);
	}
}
