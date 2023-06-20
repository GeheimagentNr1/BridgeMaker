package de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker;


import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;


//package-private
class BridgeMakerSlot extends Slot {
	
	
	//package-private
	BridgeMakerSlot( @NotNull Container _container, int _index, int _x, int _y ) {
		
		super( _container, _index, _x, _y );
	}
	
	@Override
	public boolean mayPlace( @NotNull ItemStack stack ) {
		
		return stack.getItem() instanceof BlockItem;
	}
}
