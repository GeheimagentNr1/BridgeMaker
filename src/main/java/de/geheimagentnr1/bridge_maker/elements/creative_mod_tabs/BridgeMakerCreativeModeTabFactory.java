package de.geheimagentnr1.bridge_maker.elements.creative_mod_tabs;

import de.geheimagentnr1.bridge_maker.BridgeMakerMod;
import de.geheimagentnr1.bridge_maker.elements.blocks.BlockItemInterface;
import de.geheimagentnr1.bridge_maker.elements.blocks.ModBlocks;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;


public class BridgeMakerCreativeModeTabFactory implements CreativeModeTabFactory {
	
	
	@Override
	public String getModId() {
		
		return BridgeMakerMod.MODID;
	}
	
	@Override
	public String getRegistryName() {
		
		return BridgeMakerMod.MODID;
	}
	
	@Override
	public Item getDisplayItem() {
		
		return ModBlocks.BRIDGE_MAKER.asItem();
	}
	
	@Override
	public List<ItemStack> getDisplayItems() {
		
		return ModBlocks.BLOCKS.stream()
			.filter( registryEntry -> registryEntry.getValue() instanceof BlockItemInterface )
			.map( registryEntry -> new ItemStack( registryEntry.getValue() ) )
			.toList();
	}
}
