package de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker;

import de.geheimagentnr1.bridge_maker.elements.blocks.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;


public class BridgeMakerTile extends TileEntity implements INamedContainerProvider {
	
	
	private final BridgeMakerInventory inventory = new BridgeMakerInventory(
		this::setChanged,
		player -> {
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
	);
	
	private boolean[] setBlocks = new boolean[inventory.getContainerSize()];
	
	public BridgeMakerTile() {
		
		super( ModBlocks.BRIDGE_MAKER_TILE );
	}
	
	//package-private
	BridgeMakerInventory getInventory() {
		
		return inventory;
	}
	
	//package-private
	void setSetBocksArray( boolean[] _setBlocks ) {
		
		setBlocks = _setBlocks;
		setChanged();
	}
	
	//package-private
	boolean[] getSetBlocks() {
		
		return setBlocks;
	}
	
	@Override
	public void load( @Nonnull BlockState state, @Nonnull CompoundNBT nbt ) {
		
		super.load( state, nbt );
		ItemStackHelper.loadAllItems( nbt, inventory.getItems() );
		byte[] setBlocksByte = nbt.getByteArray( "setBlocks" );
		if( setBlocksByte.length == setBlocks.length ) {
			for( int i = 0; i < setBlocks.length; i++ ) {
				setBlocks[i] = setBlocksByte[i] == 1;
			}
		}
		ListNBT blockStateNBT = (ListNBT)nbt.get( "blockStates" );
		if( blockStateNBT != null ) {
			List<BlockState> blockStates = inventory.getBlockStates();
			for( INBT inbt : blockStateNBT ) {
				int index = ( (CompoundNBT)inbt ).getByte( "Index" );
				blockStates.set( index, NBTUtil.readBlockState( (CompoundNBT)inbt ) );
			}
		}
	}
	
	@Nonnull
	@Override
	public CompoundNBT save( @Nonnull CompoundNBT compound ) {
		
		super.save( compound );
		ItemStackHelper.saveAllItems( compound, inventory.getItems(), false );
		byte[] setBlocksByte = new byte[setBlocks.length];
		for( int i = 0; i < setBlocks.length; i++ ) {
			setBlocksByte[i] = (byte)( setBlocks[i] ? 1 : 0 );
		}
		compound.putByteArray( "setBlocks", setBlocksByte );
		ListNBT blockStateNBT = new ListNBT();
		List<BlockState> blockStates = inventory.getBlockStates();
		for( int i = 0; i < blockStates.size(); i++ ) {
			if( blockStates.get( i ) != null ) {
				CompoundNBT compoundNBT = NBTUtil.writeBlockState( blockStates.get( i ) );
				compoundNBT.putByte( "Index", (byte)i );
				blockStateNBT.add( compoundNBT );
			}
		}
		compound.put( "blockStates", blockStateNBT );
		return compound;
	}
	
	@Nonnull
	@Override
	public ITextComponent getDisplayName() {
		
		return new TranslationTextComponent( Util.makeDescriptionId(
			"container",
			ModBlocks.BRIDGE_MAKER.getRegistryName()
		) );
	}
	
	@Nullable
	@Override
	public Container createMenu(
		int windowID,
		@Nonnull PlayerInventory playerInventory,
		@Nonnull PlayerEntity playerEntity ) {
		
		return new BridgeMakerContainer( windowID, playerInventory, inventory );
	}
}
