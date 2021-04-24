package de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker;

import de.geheimagentnr1.bridge_maker.elements.blocks.ModBlocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;


public class BridgeMakerContainer extends Container {
	
	
	private final IInventory inventory;
	
	public BridgeMakerContainer( int windowID, PlayerInventory playerInventory ) {
		
		this( windowID, playerInventory, new BridgeMakerTile().getInventory() );
	}
	
	//package-private
	BridgeMakerContainer( int windowID, PlayerInventory playerInventory, IInventory _inventory ) {
		
		super( ModBlocks.BRIDGE_MAKER_CONTAINER, windowID );
		checkContainerSize( _inventory, 27 );
		inventory = _inventory;
		_inventory.startOpen( playerInventory.player );
		initContainer( playerInventory );
	}
	
	private void initContainer( PlayerInventory playerInventory ) {
		
		for( int i = 0; i < 3; ++i ) {
			for( int j = 0; j < 9; ++j ) {
				addSlot( new BridgeMakerSlot( inventory, j + i * 9, 8 + j * 18, 18 + i * 18 ) );
			}
		}
		for( int i = 0; i < 3; ++i ) {
			for( int j = 0; j < 9; ++j ) {
				addSlot( new Slot( playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18 ) );
			}
		}
		for( int i = 0; i < 9; ++i ) {
			addSlot( new Slot( playerInventory, i, 8 + i * 18, 142 ) );
		}
	}
	
	
	@Override
	public boolean stillValid( @Nonnull PlayerEntity playerIn ) {
		
		return inventory.stillValid( playerIn );
	}
	
	@Nonnull
	@Override
	public ItemStack quickMoveStack( @Nonnull PlayerEntity playerIn, int index ) {
		
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = slots.get( index );
		if( slot != null && slot.hasItem() ) {
			ItemStack stackInSlot = slot.getItem();
			stack = stackInSlot.copy();
			if( index < inventory.getContainerSize() ) {
				if( !moveItemStackTo( stackInSlot, inventory.getContainerSize(), slots.size(), true ) ) {
					return ItemStack.EMPTY;
				}
			} else {
				if( !moveItemStackTo( stackInSlot, 0, inventory.getContainerSize(), false ) ) {
					return ItemStack.EMPTY;
				}
			}
			
			if( stackInSlot.isEmpty() ) {
				slot.set( ItemStack.EMPTY );
			} else {
				slot.setChanged();
			}
		}
		
		return stack;
	}
	
	@Override
	public void removed( @Nonnull PlayerEntity playerIn ) {
		
		super.removed( playerIn );
		inventory.stopOpen( playerIn );
	}
}
