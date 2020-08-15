package de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker;

import de.geheimagentnr1.bridge_maker.elements.blocks.BlockItemInterface;
import de.geheimagentnr1.bridge_maker.elements.blocks.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
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
		
		super( Block.Properties.create( Material.IRON ).hardnessAndResistance( 5.0F, 6.0F ).sound( SoundType.METAL ) );
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
	public ActionResultType onBlockActivated( @Nonnull BlockState state, World worldIn, @Nonnull BlockPos pos,
		@Nonnull PlayerEntity player, @Nonnull Hand handIn, @Nonnull BlockRayTraceResult hit ) {
		
		if( !worldIn.isRemote ) {
			TileEntity tileentity = worldIn.getTileEntity( pos );
			if( tileentity instanceof BridgeMakerTile ) {
				BridgeMakerTile bridgeMakerTile = (BridgeMakerTile)tileentity;
				player.openContainer( bridgeMakerTile );
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
		
		return getDefaultState().with( BlockStateProperties.POWERED, false ).with( BlockStateProperties.FACING,
			context.getNearestLookingDirection().getOpposite() );
	}
	
	@SuppressWarnings( "deprecation" )
	@Override
	public void neighborChanged( @Nonnull BlockState state, World worldIn, @Nonnull BlockPos pos,
		@Nonnull Block blockIn, @Nonnull BlockPos fromPos, boolean isMoving ) {
		
		if( !worldIn.isRemote ) {
			boolean isPowered = worldIn.getRedstonePowerFromNeighbors( pos ) > 0;
			
			if( isPowered != state.get( BlockStateProperties.POWERED ) ) {
				worldIn.setBlockState( pos, state.with( BlockStateProperties.POWERED, isPowered ), 3 );
				TileEntity tileentity = worldIn.getTileEntity( pos );
				if( tileentity instanceof BridgeMakerTile ) {
					BridgeMakerTile bridgeMakerTile = (BridgeMakerTile)tileentity;
					BridgeMakerInventory bridgeMakerInventory = bridgeMakerTile.getInventory();
					bridgeMakerTile.setSetBocksArray( isPowered ? power( bridgeMakerInventory, state, pos, worldIn ) :
						unpower( bridgeMakerInventory, bridgeMakerTile.getSetBlocks(), state, pos, worldIn ) );
				}
			}
		}
	}
	
	private boolean[] power( BridgeMakerInventory bridgeMakerInventory, BlockState state, BlockPos pos,
		World worldIn ) {
		
		boolean[] setBlocks = new boolean[bridgeMakerInventory.getSizeInventory()];
		ArrayList<Block> replacableBlocks = new ArrayList<>( Arrays.asList( Blocks.AIR, Blocks.LAVA, Blocks.WATER ) );
		BlockPos nextPos = pos;
		
		for( int i = 0; i < bridgeMakerInventory.getSizeInventory(); i++ ) {
			setBlocks[i] = false;
			nextPos = nextPos.offset( state.get( BlockStateProperties.FACING ) );
			if( replacableBlocks.contains( worldIn.getBlockState( nextPos ).getBlock() ) &&
				!bridgeMakerInventory.getStackInSlot( i ).isEmpty() ) {
				worldIn.setBlockState( nextPos, bridgeMakerInventory.getBlockStateForSlot( i ), 3 );
				BlockItem.setTileEntityNBT( worldIn, null, nextPos, bridgeMakerInventory.getStackInSlot( i ) );
				bridgeMakerInventory.setInventorySlotContents( i, ItemStack.EMPTY, null );
				setBlocks[i] = true;
			}
		}
		return setBlocks;
	}
	
	private boolean[] unpower( BridgeMakerInventory bridgeMakerInventory, boolean[] setBlocks, BlockState state,
		BlockPos pos, World worldIn ) {
		
		Direction facingDirection = state.get( BlockStateProperties.FACING );
		int inventorySize = bridgeMakerInventory.getSizeInventory();
		BlockState[] blockStates = new BlockState[inventorySize];
		BlockPos nextPos = pos;
		BlockPos collectPos = pos;
		
		for( int i = 0; i < inventorySize; i++ ) {
			nextPos = nextPos.offset( facingDirection );
			if( setBlocks[i] && bridgeMakerInventory.getStackInSlot( i ).isEmpty() ) {
				blockStates[i] = worldIn.getBlockState( nextPos );
			}
		}
		for( int i = 0; i < inventorySize; i++ ) {
			collectPos = collectPos.offset( facingDirection );
			if( worldIn.getBlockState( collectPos ) == Blocks.AIR.getDefaultState() ) {
				blockStates[i] = null;
			} else {
				if( setBlocks[i] && bridgeMakerInventory.getStackInSlot( i ).isEmpty() ) {
					ItemStack blockItemStack = null;
					List<ItemStack> drops = getDrops( blockStates[i], (ServerWorld)worldIn, collectPos,
						worldIn.getTileEntity( collectPos ) );
					for( ItemStack drop : drops ) {
						if( drop.getItem() instanceof BlockItem && ( (BlockItem)drop.getItem() ).getBlock() ==
							blockStates[i].getBlock() ) {
							blockItemStack = drop;
							break;
						}
					}
					if( blockItemStack == null ) {
						blockItemStack = new ItemStack( blockStates[i].getBlock().asItem() );
					}
					if( blockItemStack.getItem() instanceof BlockItem ) {
						bridgeMakerInventory.setInventorySlotContents( i, blockItemStack, blockStates[i] );
						worldIn.setBlockState( collectPos, Blocks.AIR.getDefaultState(), 3 );
					}
					setBlocks[i] = false;
				}
			}
		}
		return setBlocks;
	}
	
	@Override
	protected void fillStateContainer( StateContainer.Builder<Block, BlockState> builder ) {
		
		builder.add( BlockStateProperties.FACING, BlockStateProperties.POWERED );
	}
	
	@Override
	public Item getBlockItem( Item.Properties properties ) {
		
		return new BlockItem( ModBlocks.BRIDGE_MAKER, properties ).setRegistryName( registry_name );
	}
}
