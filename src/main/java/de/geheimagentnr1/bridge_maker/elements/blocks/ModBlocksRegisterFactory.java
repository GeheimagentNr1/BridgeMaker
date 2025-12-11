package de.geheimagentnr1.bridge_maker.elements.blocks;

import com.mojang.serialization.Codec;
import de.geheimagentnr1.bridge_maker.BridgeMakerMod;
import de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker.BridgeMaker;
import de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker.BridgeMakerEntity;
import de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker.BridgeMakerMenu;
import de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker.BridgeMakerScreen;
import de.geheimagentnr1.bridge_maker.util.CodeNetworkHelper;
import de.geheimagentnr1.bridge_maker.registry.RegistryEntry;
import de.geheimagentnr1.bridge_maker.registry.RegistryHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.minecraft.core.component.DataComponentType;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.bus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;


import lombok.Getter;


@SuppressWarnings( "StaticNonFinalField" )
public class ModBlocksRegisterFactory {
	
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
	
	@Getter
	private List<RegistryEntry<Block>> blocks;
	
	public static BridgeMaker BRIDGE_MAKER;
	
	public static BlockEntityType<BridgeMakerEntity> BRIDGE_MAKER_ENTITY;
	
	@NotNull
	public static final DataComponentType<List<BlockState>> BLOCK_STATES =
		DataComponentType.<List<BlockState>> builder()
			.persistent( BlockState.CODEC.listOf() )
			.networkSynchronized( StreamCodec.of(
				CodeNetworkHelper::toNetwork,
				CodeNetworkHelper::fromNetwork
			) )
			.build();
	
	@NotNull
	public static final DataComponentType<List<Boolean>> SET_BLOCKS =
		DataComponentType.<List<Boolean>> builder()
			.persistent( Codec.BOOL.listOf() )
			.networkSynchronized( ByteBufCodecs.BOOL.apply( ByteBufCodecs.list() ) )
			.build();
	
	public static MenuType<BridgeMakerMenu> BRIDGE_MAKER_CONTAINER;
	
	@SubscribeEvent
	public void handleRegistryEvent( @NotNull RegisterEvent event ) {
		
		RegistryHelper.registerElements( event, Registries.BLOCK, this::blocks );
		
		if( event.getRegistryKey().equals( Registries.ITEM ) ) {
			event.register(
				Registries.ITEM, ( registerHelper ) -> {
					blocks().forEach( ( registryEntry ) -> {
						Block block = registryEntry.getValue();
						if( block instanceof BlockItemInterface blockItem ) {
							registerHelper.register(
								registryEntry.getRegistryName(),
								blockItem.getBlockItem( block, new Item.Properties() )
							);
						}
					} );
				}
			);
		}
		RegistryHelper.registerElements( event, Registries.BLOCK_ENTITY_TYPE, this::blockEntityTypes );
		RegistryHelper.registerElements( event, Registries.MENU, this::menuTypes );
		RegistryHelper.registerElements( event, Registries.DATA_COMPONENT_TYPE, this::dataComponentTypes );
	}
	
	@NotNull
	private List<RegistryEntry<Block>> blocks() {
		
		if( blocks == null ) {
			BRIDGE_MAKER = new BridgeMaker();
			blocks = List.of(//BCPFINRLT
				RegistryEntry.create( BridgeMakerMod.MODID, BridgeMaker.registry_name, BRIDGE_MAKER )//BCPFINRLT
			);
		}
		return blocks;
	}
	
	@NotNull
	private List<RegistryEntry<BlockEntityType<?>>> blockEntityTypes() {
		
		BRIDGE_MAKER_ENTITY = RegistryHelper.buildBlockEntity( BridgeMaker.registry_name, BridgeMakerEntity::new, BRIDGE_MAKER );
		return List.of(
			RegistryEntry.create(
				BridgeMakerMod.MODID,
				BridgeMaker.registry_name,
				BRIDGE_MAKER_ENTITY
			)
		);
	}
	
	@NotNull
	private List<RegistryEntry<DataComponentType<?>>> dataComponentTypes() {
		
		return List.of(
			RegistryEntry.create(
				BridgeMakerMod.MODID,
				"block_states",
				BLOCK_STATES
			),
			RegistryEntry.create(
				BridgeMakerMod.MODID,
				"set_blocks",
				SET_BLOCKS
			)
		);
	}
	
	@NotNull
	private List<RegistryEntry<MenuType<?>>> menuTypes() {
		
		BRIDGE_MAKER_CONTAINER = IMenuTypeExtension.create( ( windowId, inv, data ) -> new BridgeMakerMenu( windowId, inv ) );
		return List.of(
			RegistryEntry.create(
				BridgeMakerMod.MODID,
				BridgeMaker.registry_name,
				BRIDGE_MAKER_CONTAINER
			)
		);
	}
	
	@SubscribeEvent
	public void handleRegisterMenuScreensEvent( @NotNull RegisterMenuScreensEvent event ) {
		
		event.register( BRIDGE_MAKER_CONTAINER, BridgeMakerScreen::new );
	}
}
