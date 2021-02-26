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
	public int getSizeInventory() {
		
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
	public ItemStack getStackInSlot( int index ) {
		
		return items.get( index );
	}
	
	//package-private
	BlockState getBlockStateForSlot( int index ) {
		
		return blockStates.get( index ) == null ||
			blockStates.get( index ).getBlock() != ( (BlockItem)items.get( index ).getItem() ).getBlock()
			? ( (BlockItem)items.get( index ).getItem() ).getBlock().getDefaultState()
			: blockStates.get( index );
	}
	
	@Nonnull
	@Override
	public ItemStack decrStackSize( int index, int count ) {
		
		ItemStack stack = ItemStackHelper.getAndSplit( items, index, count );
		
		if( !stack.isEmpty() ) {
			blockStates.set( index, null );
			markDirty();
		}
		return stack;
	}
	
	@Nonnull
	@Override
	public ItemStack removeStackFromSlot( int index ) {
		
		blockStates.set( index, null );
		return ItemStackHelper.getAndRemove( items, index );
	}
	
	@Override
	public int getInventoryStackLimit() {
		
		return 1;
	}
	
	/**
	 * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think it
	 * hasn't changed and skip it.
	 */
	@Override
	public void markDirty() {
		
		markDirtyListener.reactToEvent();
	}
	
	@Override
	public void setInventorySlotContents( int index, @Nonnull ItemStack stack ) {
		
		items.set( index, stack );
		blockStates.set( index, null );
		if( stack.getCount() > getInventoryStackLimit() ) {
			stack.setCount( getInventoryStackLimit() );
		}
		markDirty();
	}
	
	//package-private
	void setInventorySlotContents( int index, ItemStack itemStack, BlockState blockState ) {
		
		setInventorySlotContents( index, itemStack );
		blockStates.set( index, blockState );
	}
	
	@Override
	public boolean isUsableByPlayer( @Nonnull PlayerEntity player ) {
		
		return isUsableByPlayerListener.reactToEvent( player );
	}
	
	@Override
	public void clear() {
		
		items.clear();
		blockStates.clear();
	}
}
