package de.geheimagentnr1.bridge_maker.elements.blocks;

import com.mojang.serialization.Codec;
import de.geheimagentnr1.bridge_maker.BridgeMakerMod;
import de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker.BridgeMaker;
import de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker.BridgeMakerEntity;
import de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker.BridgeMakerMenu;
import de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker.BridgeMakerScreen;
import de.geheimagentnr1.bridge_maker.util.CodeNetworkHelper;
import de.geheimagentnr1.minecraft_modding_api.elements.blocks.BlocksRegisterFactory;
import de.geheimagentnr1.minecraft_modding_api.registry.RegistryEntry;
import de.geheimagentnr1.minecraft_modding_api.registry.RegistryHelper;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.minecraft.core.component.DataComponentType;
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
	
	@NotNull
	@Override
	protected List<RegistryEntry<Block>> blocks() {
		
		return List.of(//BCPFINRLT
			RegistryEntry.create( BridgeMakerMod.MODID, BridgeMaker.registry_name, new BridgeMaker() )//BCPFINRLT
		);
	}
	
	@NotNull
	@Override
	protected List<RegistryEntry<BlockEntityType<?>>> blockEntityTypes() {
		
		return List.of(
			RegistryEntry.create(
				BridgeMakerMod.MODID,
				BridgeMaker.registry_name,
				RegistryHelper.buildBlockEntity( BridgeMaker.registry_name, BridgeMakerEntity::new, BRIDGE_MAKER )
			)
		);
	}
	
	@Override
	protected @NotNull List<RegistryEntry<DataComponentType<?>>> dataComponentTypes() {
		
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
	@Override
	protected List<RegistryEntry<MenuType<?>>> menuTypes() {
		
		return List.of(
			RegistryEntry.create(
				BridgeMakerMod.MODID,
				BridgeMaker.registry_name,
				IMenuTypeExtension.create( ( windowId, inv, data ) -> new BridgeMakerMenu( windowId, inv ) )
			)
		);
	}
	
	@SubscribeEvent
	public void handleRegisterMenuScreensEvent( @NotNull RegisterMenuScreensEvent event ) {
		
		event.register( BRIDGE_MAKER_CONTAINER, BridgeMakerScreen::new );
	}
}
