package de.geheimagentnr1.bridge_maker.handlers;

import de.geheimagentnr1.bridge_maker.config.ClientConfig;
import de.geheimagentnr1.bridge_maker.elements.blocks.BlockItemInterface;
import de.geheimagentnr1.bridge_maker.elements.blocks.ModBlocks;
import de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker.BridgeMaker;
import de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker.BridgeMakerEntity;
import de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker.BridgeMakerMenu;
import de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker.BridgeMakerScreen;
import de.geheimagentnr1.bridge_maker.elements.creative_mod_tabs.ModCreativeTabs;
import de.geheimagentnr1.minecraft_forge_api.events.ModEventHandlerInterface;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;


public class ModEventHandler implements ModEventHandlerInterface {
	
	
	private final ClientConfig clientConfig;
	
	public ModEventHandler( ClientConfig clientConfig ) {
		
		this.clientConfig = clientConfig;
	}
	
	@SubscribeEvent
	@Override
	public void handleClientSetupEvent( FMLClientSetupEvent event ) {
		
		MenuScreens.register( ModBlocks.BRIDGE_MAKER_CONTAINER, BridgeMakerScreen::new );
	}
	
	@SubscribeEvent
	@Override
	public void handleCreativeModeTabRegisterEvent( CreativeModeTabEvent.Register event ) {
		
		if( clientConfig.getUseVanillaTab() ) {
			ModBlocks.BLOCKS.forEach( registryEntry ->
				CreativeModeTabs.REDSTONE_BLOCKS.getDisplayItems()
					.add( new ItemStack( registryEntry.getValue().asItem() ) )
			);
		} else {
			ModCreativeTabs.CREATIVE_TAB_FACTORIES.forEach( creativeModeTabFactory ->
				event.registerCreativeModeTab( creativeModeTabFactory.getName(), creativeModeTabFactory ) );
		}
	}
	
	@SubscribeEvent
	@Override
	public void handleBlockRegistryEvent( RegisterEvent event ) {
		
		event.register(
			ForgeRegistries.Keys.BLOCKS,
			registerHelper -> ModBlocks.BLOCKS.forEach( registryEntry -> registerHelper.register(
				registryEntry.getRegistryName(),
				registryEntry.getValue()
			) )
		);
	}
	
	@SubscribeEvent
	@Override
	public void handleItemRegistryEvent( RegisterEvent event ) {
		
		Item.Properties properties = new Item.Properties();
		event.register(
			ForgeRegistries.Keys.ITEMS,
			registerHelper -> ModBlocks.BLOCKS.forEach( registryEntry -> {
				if( registryEntry.getValue() instanceof BlockItemInterface blockItem ) {
					registerHelper.register(
						registryEntry.getRegistryName(),
						blockItem.getBlockItem( properties )
					);
				}
			} )
		);
	}
	
	@SubscribeEvent
	@Override
	public void handleBlockEntityTypeRegistryEvent( RegisterEvent event ) {
		
		if( event.getRegistryKey().equals( ForgeRegistries.Keys.BLOCK_ENTITY_TYPES ) ) {
			event.register(
				ForgeRegistries.Keys.BLOCK_ENTITY_TYPES,
				registerHelper -> registerHelper.register(
					BridgeMaker.registry_name,
					BlockEntityType.Builder.of( BridgeMakerEntity::new, ModBlocks.BRIDGE_MAKER )
						.build( Util.fetchChoiceType( References.BLOCK_ENTITY, BridgeMaker.registry_name ) )
				)
			);
		}
	}
	
	@SubscribeEvent
	@Override
	public void handleMenuTypeRegistryEvent( RegisterEvent event ) {
		
		if( event.getRegistryKey().equals( ForgeRegistries.Keys.MENU_TYPES ) ) {
			event.register(
				ForgeRegistries.Keys.MENU_TYPES,
				registerHelper -> registerHelper.register(
					BridgeMaker.registry_name,
					IForgeMenuType.create( ( containerId, inv, data ) -> new BridgeMakerMenu( containerId, inv ) )
				)
			);
		}
	}
	
	
	/*@OnlyIn( Dist.CLIENT )
	@SubscribeEvent
	public static void handleClientSetupEvent( FMLClientSetupEvent event ) {
		
		MenuScreens.register( ModBlocks.BRIDGE_MAKER_CONTAINER, BridgeMakerScreen::new );
	}
	
	@SubscribeEvent
	public static void handleBlockRegistryEvent( RegisterEvent event ) {
		
		if( event.getRegistryKey().equals( ForgeRegistries.Keys.BLOCKS ) ) {
			event.register(
				ForgeRegistries.Keys.BLOCKS,
				registerHelper -> ModBlocks.BLOCKS.forEach( registryEntry -> registerHelper.register(
					registryEntry.getRegistryName(),
					registryEntry.getValue()
				) )
			);
		}
	}
	
	@SubscribeEvent
	public static void handleItemRegistryEvent( RegisterEvent event ) {
		
		if( event.getRegistryKey().equals( ForgeRegistries.Keys.ITEMS ) ) {
			Item.Properties properties = new Item.Properties();
			event.register(
				ForgeRegistries.Keys.ITEMS,
				registerHelper -> ModBlocks.BLOCKS.forEach( registryEntry -> {
					if( registryEntry.getValue() instanceof BlockItemInterface blockItem ) {
						registerHelper.register(
							registryEntry.getRegistryName(),
							blockItem.getBlockItem( properties )
						);
					}
				} )
			);
		}
	}
	
	@SubscribeEvent
	public static void handleCreativeModeTabRegisterEvent( CreativeModeTabEvent.Register event ) {
		
		if( ClientConfig.getUseVanillaTab() ) {
			ModBlocks.BLOCKS.forEach( registryEntry ->
				CreativeModeTabs.REDSTONE_BLOCKS.getDisplayItems()
					.add( new ItemStack( registryEntry.getValue().asItem() ) )
			);
		} else {
			ModCreativeTabs.CREATIVE_TAB_FACTORIES.forEach( creativeModeTabFactory ->
				event.registerCreativeModeTab( creativeModeTabFactory.getName(), creativeModeTabFactory ) );
		}
	}
	
	@SuppressWarnings( "ConstantConditions" )
	@SubscribeEvent
	public static void handleBlockEntityTypeRegistryEvent( RegisterEvent event ) {
		
		if( event.getRegistryKey().equals( ForgeRegistries.Keys.BLOCK_ENTITY_TYPES ) ) {
			event.register(
				ForgeRegistries.Keys.BLOCK_ENTITY_TYPES,
				registerHelper -> registerHelper.register(
					BridgeMaker.registry_name,
					BlockEntityType.Builder.of( BridgeMakerEntity::new, ModBlocks.BRIDGE_MAKER )
						.build( Util.fetchChoiceType( References.BLOCK_ENTITY, BridgeMaker.registry_name ) )
				)
			);
		}
	}
	
	@SubscribeEvent
	public static void handleMenuTypeRegistryEvent( RegisterEvent event ) {
		
		if( event.getRegistryKey().equals( ForgeRegistries.Keys.MENU_TYPES ) ) {
			event.register(
				ForgeRegistries.Keys.MENU_TYPES,
				registerHelper -> registerHelper.register(
					BridgeMaker.registry_name,
					IForgeMenuType.create( ( containerId, inv, data ) -> new BridgeMakerMenu( containerId, inv ) )
				)
			);
		}
	}*/
}
