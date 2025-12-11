package de.geheimagentnr1.bridge_maker.elements.creative_mod_tabs;

import de.geheimagentnr1.bridge_maker.elements.blocks.BlockItemInterface;
import de.geheimagentnr1.bridge_maker.registry.RegistryEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;


public interface CreativeModeTabFactory extends Supplier<CreativeModeTab> {
	
	
	@NotNull
	ResourceLocation getRegistryName();
	
	@NotNull
	@Override
	default CreativeModeTab get() {
		
		return CreativeModeTab.builder()
			.title( Component.translatable( "itemGroup." + getRegistryName().getPath() ) )
			.icon( this::buildIconItemStack )
			.displayItems( this::displayItemsGenerator )
			.build();
	}
	
	@NotNull
	default ItemStack buildIconItemStack() {
		
		return new ItemStack( getIconItem() );
	}
	
	@NotNull
	ItemLike getIconItem();
	
	default void displayItemsGenerator(
		@NotNull CreativeModeTab.ItemDisplayParameters itemDisplayParameters,
		@NotNull CreativeModeTab.Output output ) {
		
		output.acceptAll(
			getDisplayBlocks().stream()
				.filter( registryEntry -> registryEntry.getValue() instanceof BlockItemInterface )
				.flatMap( this::buildItemStacksOfBlockRegistryEntry )
				.toList()
		);
		output.acceptAll(
			getDisplayItems().stream()
				.flatMap( this::buildItemStacksOfItemRegistryEntry )
				.toList()
		);
	}
	
	@NotNull
	default List<RegistryEntry<Block>> getDisplayBlocks() {
		
		return List.of();
	}
	
	@NotNull
	default Stream<ItemStack> buildItemStacksOfBlockRegistryEntry( @NotNull RegistryEntry<Block> registryEntry ) {
		
		return Stream.of( new ItemStack( registryEntry.getValue() ) );
	}
	
	@NotNull
	default List<RegistryEntry<Item>> getDisplayItems() {
		
		return List.of();
	}
	
	@NotNull
	default Stream<ItemStack> buildItemStacksOfItemRegistryEntry( @NotNull RegistryEntry<Item> registryEntry ) {
		
		return Stream.of( new ItemStack( registryEntry.getValue() ) );
	}
}
