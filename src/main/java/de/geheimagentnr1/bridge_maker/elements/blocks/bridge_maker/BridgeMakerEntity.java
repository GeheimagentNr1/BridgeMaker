package de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker;

import de.geheimagentnr1.bridge_maker.elements.blocks.ModBlocksRegisterFactory;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class BridgeMakerEntity extends BaseContainerBlockEntity {
	
	
	private static final int CONTAINER_SIZE = 27;
	
	@NotNull
	private NonNullList<ItemStack> itemStacks = NonNullList.withSize( CONTAINER_SIZE, ItemStack.EMPTY );
	
	@NotNull
	private final List<BlockState> blockStates = new ArrayList<>( Arrays.asList( new BlockState[CONTAINER_SIZE] ) );
	
	@NotNull
	private List<Boolean> setBlocks = buildEmptyBooleanList();
	
	public BridgeMakerEntity( @NotNull BlockPos pos, @NotNull BlockState state ) {
		
		super( ModBlocksRegisterFactory.BRIDGE_MAKER_ENTITY, pos, state );
	}
	
	@NotNull
	@Override
	protected Component getDefaultName() {
		
		return Component.translatable( Util.makeDescriptionId(
			"container",
			BuiltInRegistries.BLOCK.getKey( ModBlocksRegisterFactory.BRIDGE_MAKER )
		) );
	}
	
	@Override
	protected void applyImplicitComponents( DataComponentInput pComponentInput ) {
		
		super.applyImplicitComponents( pComponentInput );
		setBlocks = pComponentInput.getOrDefault(
			ModBlocksRegisterFactory.SET_BLOCKS,
			buildEmptyBooleanList()
		);
		List<BlockState> newBlockStates = pComponentInput.getOrDefault(
			ModBlocksRegisterFactory.BLOCK_STATES,
			List.of()
		);
		for( int index = 0; index < blockStates.size(); index++ ) {
			BlockState stack = index < newBlockStates.size() ? newBlockStates.get( index ) : null;
			blockStates.set( index, stack );
		}
	}
	
	@Override
	protected void collectImplicitComponents( DataComponentMap.Builder pComponents ) {
		
		super.collectImplicitComponents( pComponents );
		pComponents.set( ModBlocksRegisterFactory.SET_BLOCKS, setBlocks );
		pComponents.set( ModBlocksRegisterFactory.BLOCK_STATES, blockStates );
	}
	
	@Override
	public void removeComponentsFromTag( CompoundTag pTag ) {
		
		super.removeComponentsFromTag( pTag );
		pTag.remove( "blockStates" );
		pTag.remove( "setBlocks" );
	}
	
	@Override
	protected NonNullList<ItemStack> getItems() {
		
		return itemStacks;
	}
	
	@Override
	protected void setItems( NonNullList<ItemStack> pItemStacks ) {
		
		itemStacks = pItemStacks;
	}
	
	@NotNull
	@Override
	protected AbstractContainerMenu createMenu( int containerId, @NotNull Inventory inventory ) {
		
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
	
	@NotNull
	@Override
	public ItemStack getItem( int index ) {
		
		return itemStacks.get( index );
	}
	
	//package-private
	@NotNull
	BlockState getBlockStateForSlot( int index ) {
		
		return blockStates.get( index ) == null ||
			blockStates.get( index ).getBlock() != ( (BlockItem)itemStacks.get( index ).getItem() ).getBlock()
			? ( (BlockItem)itemStacks.get( index ).getItem() ).getBlock().defaultBlockState()
			: blockStates.get( index );
	}
	
	//package-private
	List<Boolean> getSetBlocks() {
		
		return setBlocks;
	}
	
	List<Boolean> buildEmptyBooleanList() {
		 List<Boolean> list = new ArrayList<>();
		 for( int i = 0; i < CONTAINER_SIZE; i++ ) {
			 list.add( false );
		 }
		 return list;
	}
	
	@NotNull
	@Override
	public ItemStack removeItem( int index, int count ) {
		
		ItemStack stack = ContainerHelper.removeItem( itemStacks, index, count );
		
		if( !stack.isEmpty() ) {
			blockStates.set( index, null );
			setChanged();
		}
		return stack;
	}
	
	@NotNull
	@Override
	public ItemStack removeItemNoUpdate( int index ) {
		
		blockStates.set( index, null );
		return ContainerHelper.takeItem( itemStacks, index );
	}
	
	@Override
	public void setItem( int index, @NotNull ItemStack stack ) {
		
		itemStacks.set( index, stack );
		if( stack.getCount() > getMaxStackSize() ) {
			stack.setCount( getMaxStackSize() );
		}
		setChanged();
	}
	
	//package-private
	void setItem( int index, @NotNull ItemStack stack, BlockState state ) {
		
		blockStates.set( index, state );
		setItem( index, stack );
	}
	
	//package-private
	void setSetBocks( @NotNull List<Boolean> _setBlocks ) {
		
		setBlocks = _setBlocks;
		setChanged();
	}
	
	@Override
	public boolean stillValid( @NotNull Player player ) {
		
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
	protected void loadAdditional( CompoundTag pTag, HolderLookup.Provider pRegistries ) {
		
		super.loadAdditional( pTag, pRegistries );
		ContainerHelper.loadAllItems( pTag, itemStacks, pRegistries );
		byte[] setBlocksByte = pTag.getByteArray( "setBlocks" );
		if( setBlocksByte.length == setBlocks.size() ) {
			for( int i = 0; i < setBlocks.size(); i++ ) {
				setBlocks.set( i, setBlocksByte[i] == 1);
			}
		}
		ListTag blockStatesNbt = (ListTag)pTag.get( "blockStates" );
		if( blockStatesNbt != null ) {
			for( Tag blockStatesElementNbt : blockStatesNbt ) {
				if( blockStatesElementNbt.getId() == Tag.TAG_COMPOUND ) {
					CompoundTag blockStateNbt = ( (CompoundTag)blockStatesElementNbt );
					int index = blockStateNbt.getByte( "Index" );
					blockStates.set(
						index,
						NbtUtils.readBlockState(
							BuiltInRegistries.BLOCK.asLookup(),
							blockStateNbt
						)
					);
				}
			}
		}
	}
	
	@Override
	protected void saveAdditional( CompoundTag pTag, HolderLookup.Provider pRegistries ) {
		
		super.saveAdditional( pTag, pRegistries );
		ContainerHelper.saveAllItems( pTag, itemStacks, pRegistries );
		byte[] setBlocksByte = new byte[setBlocks.size()];
		for( int i = 0; i < setBlocks.size(); i++ ) {
			setBlocksByte[i] = (byte)( setBlocks.get( i ) ? 1 : 0 );
		}
		pTag.putByteArray( "setBlocks", setBlocksByte );
		ListTag blockStatesNbt = new ListTag();
		for( int i = 0; i < blockStates.size(); i++ ) {
			if( blockStates.get( i ) != null ) {
				CompoundTag blockStateNbt = NbtUtils.writeBlockState( blockStates.get( i ) );
				blockStateNbt.putByte( "Index", (byte)i );
				blockStatesNbt.add( blockStateNbt );
			}
		}
		pTag.put( "blockStates", blockStatesNbt );
	}
}
