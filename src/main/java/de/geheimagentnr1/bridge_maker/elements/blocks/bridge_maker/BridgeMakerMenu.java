package de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker;

import de.geheimagentnr1.bridge_maker.elements.blocks.ModBlocksRegisterFactory;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;


public class BridgeMakerMenu extends AbstractContainerMenu {
	
	
	@NotNull
	private final Container container;
	
	public BridgeMakerMenu( int _windowId, @NotNull Inventory inventory ) {
		
		this( _windowId, inventory, new SimpleContainer( 27 ) );
	}
	
	//package-private
	BridgeMakerMenu( int _windowId, @NotNull Inventory inventory, @NotNull Container _container ) {
		
		super( ModBlocksRegisterFactory.BRIDGE_MAKER_CONTAINER, _windowId );
		checkContainerSize( _container, 27 );
		container = _container;
		_container.startOpen( inventory.player );
		initContainer( inventory );
	}
	
	private void initContainer( @NotNull Inventory playerInventory ) {
		
		for( int row = 0; row < 3; row++ ) {
			for( int column = 0; column < 9; column++ ) {
				addSlot( new BridgeMakerSlot( container, column + row * 9, 8 + column * 18, 18 + row * 18 ) );
			}
		}
		for( int row = 0; row < 3; row++ ) {
			for( int column = 0; column < 9; column++ ) {
				addSlot( new Slot( playerInventory, column + row * 9 + 9, 8 + column * 18, 84 + row * 18 ) );
			}
		}
		for( int column = 0; column < 9; column++ ) {
			addSlot( new Slot( playerInventory, column, 8 + column * 18, 142 ) );
		}
	}
	
	
	@Override
	public boolean stillValid( @NotNull Player player ) {
		
		return container.stillValid( player );
	}
	
	@NotNull
	@Override
	public ItemStack quickMoveStack( @NotNull Player player, int index ) {
		
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = slots.get( index );
		if( slot.hasItem() ) {
			ItemStack stackInSlot = slot.getItem();
			stack = stackInSlot.copy();
			if( index < container.getContainerSize() ) {
				if( !moveItemStackTo( stackInSlot, container.getContainerSize(), slots.size(), true ) ) {
					return ItemStack.EMPTY;
				}
			} else {
				if( !moveItemStackTo( stackInSlot, 0, container.getContainerSize(), false ) ) {
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
	public void removed( @NotNull Player player ) {
		
		super.removed( player );
		container.stopOpen( player );
	}
}
