package de.geheimagentnr1.bridge_maker.elements.item_groups;

import de.geheimagentnr1.bridge_maker.BridgeMakerMod;
import de.geheimagentnr1.bridge_maker.elements.blocks.ModBlocks;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;


//package-private
class BridgeMakerItemGroup extends CreativeModeTab {
	
	
	//package-private
	BridgeMakerItemGroup() {
		
		super( BridgeMakerMod.MODID );
	}
	
	@Nonnull
	@Override
	public ItemStack makeIcon() {
		
		return new ItemStack( ModBlocks.BRIDGE_MAKER );
	}
}
