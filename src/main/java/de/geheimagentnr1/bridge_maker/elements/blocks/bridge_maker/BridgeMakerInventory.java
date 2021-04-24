package de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


//package-private
class BridgeMakerInventory implements IInventory {
	
	
	private final NonNullList<ItemStack> items = NonNullList.withSize( 27, ItemStack.EMPTY );
	
	private final List<BlockState> blockStates = new ArrayList<>( Arrays.asList( new BlockState[27] ) );
	
	private final MarkDirtyListener markDirtyListener;
	
	private final IsUsableByPlayerListener isUsableByPlayerListener;
	
	//package-private
	BridgeMakerInventory( MarkDirtyListener _markDirtyListener, IsUsableByPlayerListener _isUsableByPlayerListener ) {
		
		markDirtyListener = _markDirtyListener;
		isUsableByPlayerListener = _isUsableByPlayerListener;
	}
	
	//package-private
	NonNullList<ItemStack> getItems() {
		
		return items;
	}
	
	/**
	 * Returns the number of slots in the inventory.
	 */
	@Override
	public int getContainerSize() {
		
		return items.size();
	}
	
	//package-private
	List<BlockState> getBlockStates() {
		
		return blockStates;
	}
	
	@Override
	public boolean isEmpty() {
		
		for( ItemStack itemstack : items ) {
			if( !itemstack.isEmpty() ) {
				return false;
			}
		}
		return true;
	}
	
	@Nonnull
	@Override
	public ItemStack getItem( int index ) {
		
		return items.get( index );
	}
	
	//package-private
	BlockState getBlockStateForSlot( int index ) {
		
		return blockStates.get( index ) == null ||
			blockStates.get( index ).getBlock() != ( (BlockItem)items.get( index ).getItem() ).getBlock()
			? ( (BlockItem)items.get( index ).getItem() ).getBlock().defaultBlockState()
			: blockStates.get( index );
	}
	
	@Nonnull
	@Override
	public ItemStack removeItem( int index, int count ) {
		
		ItemStack stack = ItemStackHelper.removeItem( items, index, count );
		
		if( !stack.isEmpty() ) {
			blockStates.set( index, null );
			setChanged();
		}
		return stack;
	}
	
	@Nonnull
	@Override
	public ItemStack removeItemNoUpdate( int index ) {
		
		blockStates.set( index, null );
		return ItemStackHelper.takeItem( items, index );
	}
	
	@Override
	public int getMaxStackSize() {
		
		return 1;
	}
	
	/**
	 * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think it
	 * hasn't changed and skip it.
	 */
	@Override
	public void setChanged() {
		
		markDirtyListener.reactToEvent();
	}
	
	@Override
	public void setItem( int index, @Nonnull ItemStack stack ) {
		
		items.set( index, stack );
		blockStates.set( index, null );
		if( stack.getCount() > getMaxStackSize() ) {
			stack.setCount( getMaxStackSize() );
		}
		setChanged();
	}
	
	//package-private
	void setItem( int index, ItemStack itemStack, BlockState blockState ) {
		
		setItem( index, itemStack );
		blockStates.set( index, blockState );
	}
	
	@Override
	public boolean stillValid( @Nonnull PlayerEntity player ) {
		
		return isUsableByPlayerListener.reactToEvent( player );
	}
	
	@Override
	public void clearContent() {
		
		items.clear();
		blockStates.clear();
	}
}
