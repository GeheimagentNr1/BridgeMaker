package de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker;

import net.minecraft.block.DoorBlock;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;


//package-private
class BridgeMakerSlot extends Slot {
	
	
	//package-private
	BridgeMakerSlot( IInventory inventoryIn, int index, int xPosition, int yPosition ) {
		
		super( inventoryIn, index, xPosition, yPosition );
	}
	
	@Override
	public boolean isItemValid( ItemStack stack ) {
		
		return stack.getItem() instanceof BlockItem &&
			!( ( (BlockItem)stack.getItem() ).getBlock() instanceof DoorBlock );
	}
}
