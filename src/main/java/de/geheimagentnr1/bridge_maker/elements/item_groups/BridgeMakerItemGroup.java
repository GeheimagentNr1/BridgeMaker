package de.geheimagentnr1.bridge_maker.elements.item_groups;

import de.geheimagentnr1.bridge_maker.BridgeMakerMod;
import de.geheimagentnr1.bridge_maker.elements.blocks.ModBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;


public class BridgeMakerItemGroup extends ItemGroup {
	
	
	public BridgeMakerItemGroup() {
		
		super( BridgeMakerMod.MODID );
	}
	
	@Nonnull
	@Override
	public ItemStack createIcon() {
		
		return new ItemStack( ModBlocks.BRIDGE_MAKER );
	}
}
