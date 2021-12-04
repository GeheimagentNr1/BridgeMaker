package de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker;

import de.geheimagentnr1.bridge_maker.elements.blocks.ModBlocks;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class BridgeMakerEntity extends BaseContainerBlockEntity {
	
	
	private static final int CONTAINER_SIZE = 27;
	
	private final NonNullList<ItemStack> itemStacks = NonNullList.withSize( CONTAINER_SIZE, ItemStack.EMPTY );
	
	private final List<BlockState> blockStates = new ArrayList<>( Arrays.asList( new BlockState[CONTAINER_SIZE] ) );
	
	private boolean[] setBlocks = new boolean[CONTAINER_SIZE];
	
	public BridgeMakerEntity( BlockPos pos, BlockState state ) {
		
		super( ModBlocks.BRIDGE_MAKER_ENTITY, pos, state );
	}
	
	@Nonnull
	@Override
	protected Component getDefaultName() {
		
		return new TranslatableComponent( Util.makeDescriptionId(
			"container",
			ModBlocks.BRIDGE_MAKER.getRegistryName()
		) );
	}
	
	@Nonnull
	@Override
	protected AbstractContainerMenu createMenu( int containerId, @Nonnull Inventory inventory ) {
		
		return new BridgeMakerMenu( containerId, inventory, this );
	}
	
	@Override
	public int getContainerSize() {
		
		return CONTAINER_SIZE;
	}
	
	@Override
	public int getMaxStackSize() {
		
		return 1;
	}
	
	@Override
	public boolean isEmpty() {
		
		return itemStacks.stream().allMatch( ItemStack::isEmpty );
	}
	
	@Nonnull
	@Override
	public ItemStack getItem( int index ) {
		
		return itemStacks.get( index );
	}
	
	//package-private
	BlockState getBlockStateForSlot( int index ) {
		
		return blockStates.get( index ) == null ||
			blockStates.get( index ).getBlock() != ( (BlockItem)itemStacks.get( index ).getItem() ).getBlock()
			? ( (BlockItem)itemStacks.get( index ).getItem() ).getBlock().defaultBlockState()
			: blockStates.get( index );
	}
	
	//package-private
	boolean[] getSetBlocks() {
		
		return setBlocks;
	}
	
	@Nonnull
	@Override
	public ItemStack removeItem( int index, int count ) {
		
		ItemStack stack = ContainerHelper.removeItem( itemStacks, index, count );
		
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
		return ContainerHelper.takeItem( itemStacks, index );
	}
	
	@Override
	public void setItem( int index, @Nonnull ItemStack stack ) {
		
		itemStacks.set( index, stack );
		if( stack.getCount() > getMaxStackSize() ) {
			stack.setCount( getMaxStackSize() );
		}
		setChanged();
	}
	
	//package-private
	void setItem( int index, ItemStack stack, BlockState state ) {
		
		blockStates.set( index, state );
		setItem( index, stack );
	}
	
	//package-private
	void setSetBocksArray( boolean[] _setBlocks ) {
		
		setBlocks = _setBlocks;
		setChanged();
	}
	
	@Override
	public boolean stillValid( @Nonnull Player player ) {
		
		if( Objects.requireNonNull( level ).getBlockEntity( worldPosition ) == this ) {
			return player.distanceToSqr(
				worldPosition.getX() + 0.5D,
				worldPosition.getY() + 0.5D,
				worldPosition.getZ() + 0.5D
			) <= 64.0D;
		} else {
			return false;
		}
	}
	
	@Override
	public void clearContent() {
		
		itemStacks.clear();
		blockStates.clear();
	}
	
	@Override
	public void load( @Nonnull CompoundTag nbt ) {
		
		super.load( nbt );
		ContainerHelper.loadAllItems( nbt, itemStacks );
		byte[] setBlocksByte = nbt.getByteArray( "setBlocks" );
		if( setBlocksByte.length == setBlocks.length ) {
			for( int i = 0; i < setBlocks.length; i++ ) {
				setBlocks[i] = setBlocksByte[i] == 1;
			}
		}
		ListTag blockStatesNbt = (ListTag)nbt.get( "blockStates" );
		if( blockStatesNbt != null ) {
			for( Tag blockStatesElementNbt : blockStatesNbt ) {
				if( blockStatesElementNbt.getId() == Tag.TAG_COMPOUND ) {
					CompoundTag blockStateNbt = ( (CompoundTag)blockStatesElementNbt );
					int index = blockStateNbt.getByte( "Index" );
					blockStates.set( index, NbtUtils.readBlockState( blockStateNbt ) );
				}
			}
		}
	}
	
	@Override
	public void saveAdditional( @Nonnull CompoundTag nbt ) {
		
		ContainerHelper.saveAllItems( nbt, itemStacks, false );
		byte[] setBlocksByte = new byte[setBlocks.length];
		for( int i = 0; i < setBlocks.length; i++ ) {
			setBlocksByte[i] = (byte)( setBlocks[i] ? 1 : 0 );
		}
		nbt.putByteArray( "setBlocks", setBlocksByte );
		ListTag blockStatesNbt = new ListTag();
		for( int i = 0; i < blockStates.size(); i++ ) {
			if( blockStates.get( i ) != null ) {
				CompoundTag blockStateNbt = NbtUtils.writeBlockState( blockStates.get( i ) );
				blockStateNbt.putByte( "Index", (byte)i );
				blockStatesNbt.add( blockStateNbt );
			}
		}
		nbt.put( "blockStates", blockStatesNbt );
	}
}
