package de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker;

import de.geheimagentnr1.bridge_maker.elements.blocks.BlockItemInterface;
import de.geheimagentnr1.bridge_maker.elements.blocks.ModBlocks;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class BridgeMaker extends Block implements BlockItemInterface {
	
	
	public static final String registry_name = "bridge_maker";
	
	public BridgeMaker() {
		
		super( AbstractBlock.Properties.of( Material.METAL ).strength( 5.0F, 6.0F ).sound( SoundType.METAL ) );
		setRegistryName( registry_name );
	}
	
	@Override
	public boolean hasTileEntity( BlockState state ) {
		
		return true;
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity( BlockState state, IBlockReader world ) {
		
		return new BridgeMakerTile();
	}
	
	@SuppressWarnings( "deprecation" )
	@Nonnull
	@Override
	public ActionResultType use(
		@Nonnull BlockState state,
		World worldIn,
		@Nonnull BlockPos pos,
		@Nonnull PlayerEntity player,
		@Nonnull Hand handIn,
		@Nonnull BlockRayTraceResult hit ) {
		
		if( !worldIn.isClientSide ) {
			TileEntity tileentity = worldIn.getBlockEntity( pos );
			if( tileentity instanceof BridgeMakerTile ) {
				BridgeMakerTile bridgeMakerTile = (BridgeMakerTile)tileentity;
				player.openMenu( bridgeMakerTile );
				return ActionResultType.SUCCESS;
			} else {
				return ActionResultType.PASS;
			}
		}
		return ActionResultType.SUCCESS;
	}
	
	@Nullable
	@Override
	public BlockState getStateForPlacement( BlockItemUseContext context ) {
		
		return defaultBlockState().setValue( BlockStateProperties.POWERED, false )
			.setValue( BlockStateProperties.FACING, context.getNearestLookingDirection().getOpposite() );
	}
	
	@SuppressWarnings( "deprecation" )
	@Override
	public void neighborChanged(
		@Nonnull BlockState state,
		World worldIn,
		@Nonnull BlockPos pos,
		@Nonnull Block blockIn,
		@Nonnull BlockPos fromPos,
		boolean isMoving ) {
		
		if( !worldIn.isClientSide ) {
			boolean isPowered = worldIn.getBestNeighborSignal( pos ) > 0;
			
			if( isPowered != state.getValue( BlockStateProperties.POWERED ) ) {
				worldIn.setBlock( pos, state.setValue( BlockStateProperties.POWERED, isPowered ), 3 );
				TileEntity tileentity = worldIn.getBlockEntity( pos );
				if( tileentity instanceof BridgeMakerTile ) {
					BridgeMakerTile bridgeMakerTile = (BridgeMakerTile)tileentity;
					BridgeMakerInventory bridgeMakerInventory = bridgeMakerTile.getInventory();
					bridgeMakerTile.setSetBocksArray( isPowered
						? power( bridgeMakerInventory, state, pos, worldIn )
						: unpower( bridgeMakerInventory, bridgeMakerTile.getSetBlocks(), state, pos, worldIn ) );
				}
			}
		}
	}
	
	private boolean[] power(
		BridgeMakerInventory bridgeMakerInventory,
		BlockState state,
		BlockPos pos,
		World worldIn ) {
		
		boolean[] setBlocks = new boolean[bridgeMakerInventory.getContainerSize()];
		ArrayList<Block> replacableBlocks = new ArrayList<>( Arrays.asList( Blocks.AIR, Blocks.LAVA, Blocks.WATER ) );
		BlockPos nextPos = pos;
		
		for( int i = 0; i < bridgeMakerInventory.getContainerSize(); i++ ) {
			setBlocks[i] = false;
			nextPos = nextPos.relative( state.getValue( BlockStateProperties.FACING ) );
			if( replacableBlocks.contains( worldIn.getBlockState( nextPos ).getBlock() ) &&
				!bridgeMakerInventory.getItem( i ).isEmpty() ) {
				worldIn.setBlock( nextPos, bridgeMakerInventory.getBlockStateForSlot( i ), 3 );
				BlockItem.updateCustomBlockEntityTag( worldIn, null, nextPos, bridgeMakerInventory.getItem( i ) );
				bridgeMakerInventory.setItem( i, ItemStack.EMPTY, null );
				setBlocks[i] = true;
			}
		}
		return setBlocks;
	}
	
	private boolean[] unpower(
		BridgeMakerInventory bridgeMakerInventory,
		boolean[] setBlocks,
		BlockState state,
		BlockPos pos,
		World worldIn ) {
		
		Direction facingDirection = state.getValue( BlockStateProperties.FACING );
		int inventorySize = bridgeMakerInventory.getContainerSize();
		BlockState[] blockStates = new BlockState[inventorySize];
		BlockPos nextPos = pos;
		BlockPos collectPos = pos;
		
		for( int i = 0; i < inventorySize; i++ ) {
			nextPos = nextPos.relative( facingDirection );
			if( setBlocks[i] && bridgeMakerInventory.getItem( i ).isEmpty() ) {
				blockStates[i] = worldIn.getBlockState( nextPos );
			}
		}
		for( int i = 0; i < inventorySize; i++ ) {
			collectPos = collectPos.relative( facingDirection );
			if( worldIn.getBlockState( collectPos ) == Blocks.AIR.defaultBlockState() ) {
				blockStates[i] = null;
			} else {
				if( setBlocks[i] && bridgeMakerInventory.getItem( i ).isEmpty() ) {
					ItemStack blockItemStack = null;
					List<ItemStack> drops = getDrops(
						blockStates[i],
						(ServerWorld)worldIn,
						collectPos,
						worldIn.getBlockEntity( collectPos )
					);
					for( ItemStack drop : drops ) {
						if( drop.getItem() instanceof BlockItem &&
							( (BlockItem)drop.getItem() ).getBlock() == blockStates[i].getBlock() ) {
							blockItemStack = drop;
							break;
						}
					}
					if( blockItemStack == null ) {
						blockItemStack = new ItemStack( blockStates[i].getBlock().asItem() );
					}
					if( blockItemStack.getItem() instanceof BlockItem ) {
						bridgeMakerInventory.setItem( i, blockItemStack, blockStates[i] );
						worldIn.setBlock( collectPos, Blocks.AIR.defaultBlockState(), 3 );
					}
					setBlocks[i] = false;
				}
			}
		}
		return setBlocks;
	}
	
	@Override
	protected void createBlockStateDefinition( StateContainer.Builder<Block, BlockState> builder ) {
		
		builder.add( BlockStateProperties.FACING, BlockStateProperties.POWERED );
	}
	
	@Override
	public Item getBlockItem( Item.Properties properties ) {
		
		return new BlockItem( ModBlocks.BRIDGE_MAKER, properties ).setRegistryName( registry_name );
	}
}
