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
	@SuppressWarnings( "OverridableMethodCallDuringObjectConstruction" )
	BridgeMakerContainer( int windowID, PlayerInventory playerInventory, IInventory _inventory ) {
		
		super( ModBlocks.BRIDGE_MAKER_CONTAINER, windowID );
		assertInventorySize( _inventory, 27 );
		inventory = _inventory;
		_inventory.openInventory( playerInventory.player );
		
		for( int i = 0; i < 3; ++i ) {
			for( int j = 0; j < 9; ++j ) {
				addSlot(
					new BridgeMakerSlot( inventory, j + i * 9, 8 + j * 18,
						18 + i * 18 ) );
			}
		}
		for( int i = 0; i < 3; ++i ) {
			for( int j = 0; j < 9; ++j ) {
				addSlot(
					new Slot( playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18 ) );
			}
		}
		for( int i = 0; i < 9; ++i ) {
			addSlot( new Slot( playerInventory, i, 8 + i * 18, 142 ) );
		}
		
	}
	
	
	@Override
	public boolean canInteractWith( @Nonnull PlayerEntity playerIn ) {
		
		return inventory.isUsableByPlayer( playerIn );
	}
	
	@Nonnull
	@Override
	public ItemStack transferStackInSlot( PlayerEntity playerIn, int index ) {
		
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get( index );
		if( slot != null && slot.getHasStack() ) {
			ItemStack stackInSlot = slot.getStack();
			stack = stackInSlot.copy();
			if( index < inventory.getSizeInventory() ) {
				if( !mergeItemStack( stackInSlot, inventory.getSizeInventory(), inventorySlots.size(), true ) ) {
					return ItemStack.EMPTY;
				}
			} else {
				if( !mergeItemStack( stackInSlot, 0, inventory.getSizeInventory(), false ) ) {
					return ItemStack.EMPTY;
				}
			}
			
			if( stackInSlot.isEmpty() ) {
				slot.putStack( ItemStack.EMPTY );
			} else {
				slot.onSlotChanged();
			}
		}
		
		return stack;
	}
	
	@Override
	public void onContainerClosed( PlayerEntity playerIn ) {
		
		super.onContainerClosed( playerIn );
		inventory.closeInventory( playerIn );
	}
}
