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
import net.minecraft.Util;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;


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
			Item.Properties properties = new Item.Properties().tab( ModItemGroups.getItemGroup() );
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
	}
}
