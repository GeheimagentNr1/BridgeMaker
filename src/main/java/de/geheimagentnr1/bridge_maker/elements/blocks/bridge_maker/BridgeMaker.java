package de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker;

import com.mojang.serialization.MapCodec;
import de.geheimagentnr1.bridge_maker.elements.blocks.BlockItemInterface;
import lombok.extern.log4j.Log4j2;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Log4j2
public class BridgeMaker extends BaseEntityBlock implements BlockItemInterface {
	
	
	@NotNull
	public static final String registry_name = "bridge_maker";
	
	@NotNull
	private static final MapCodec<BridgeMaker> CODEC = simpleCodec( properties -> new BridgeMaker() );
	
	public BridgeMaker() {
		
		super(
			Properties.of()
				.mapColor( MapColor.METAL )
				.strength( 5.0F, 6.0F )
				.requiresCorrectToolForDrops()
				.sound( SoundType.METAL )
		);
	}
	
	@Nullable
	@Override
	public BlockEntity newBlockEntity( @NotNull BlockPos blockPos, @NotNull BlockState blockState ) {
		
		return new BridgeMakerEntity( blockPos, blockState );
	}
	
	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		
		return CODEC;
	}
	
	@NotNull
	@Override
	public RenderShape getRenderShape( @NotNull BlockState pState ) {
		
		return RenderShape.MODEL;
	}
	
	@Override
	protected InteractionResult useWithoutItem(
		BlockState pState,
		Level pLevel,
		BlockPos pPos,
		Player pPlayer,
		BlockHitResult pHitResult ) {
		
		if( pLevel.isClientSide() ) {
			return InteractionResult.SUCCESS;
		}
		if( pPlayer.isSpectator() ) {
			return InteractionResult.CONSUME;
		}
		BlockEntity blockEntity = pLevel.getBlockEntity( pPos );
		if( blockEntity instanceof BridgeMakerEntity bridgeMakerEntity ) {
			pPlayer.openMenu( bridgeMakerEntity );
			return InteractionResult.CONSUME;
		}
		return InteractionResult.PASS;
	}
	
	@Nullable
	@Override
	public BlockState getStateForPlacement( @NotNull BlockPlaceContext context ) {
		
		return defaultBlockState().setValue( BlockStateProperties.POWERED, false )
			.setValue( BlockStateProperties.FACING, context.getNearestLookingDirection().getOpposite() );
	}
	
	@Override
	public void neighborChanged(
		@NotNull BlockState state,
		@NotNull Level level,
		@NotNull BlockPos pos,
		@NotNull Block neighbarBlock,
		@NotNull BlockPos neighborPos,
		boolean isMoving ) {
		
		if( !level.isClientSide() ) {
			boolean isPowered = level.getBestNeighborSignal( pos ) > 0;
			
			if( isPowered != state.getValue( BlockStateProperties.POWERED ) ) {
				level.setBlock( pos, state.setValue( BlockStateProperties.POWERED, isPowered ), 3 );
				BlockEntity blockEntity = level.getBlockEntity( pos );
				if( blockEntity instanceof BridgeMakerEntity bridgeMakerEntity ) {
					bridgeMakerEntity.setSetBocks( isPowered
						? power( bridgeMakerEntity, state, pos, level )
						: unpower( bridgeMakerEntity, bridgeMakerEntity.getSetBlocks(), state, pos, level ) );
				}
			}
		}
	}
	
	private List<Boolean> power(
		@NotNull BridgeMakerEntity bridgeMakerEntity,
		@NotNull BlockState state,
		@NotNull BlockPos pos,
		@NotNull Level level ) {
		
		List<Boolean> setBlocks = bridgeMakerEntity.buildEmptyBooleanList();
		ArrayList<Block> replacableBlocks = new ArrayList<>( Arrays.asList( Blocks.AIR, Blocks.LAVA, Blocks.WATER ) );
		BlockPos nextPos = pos;
		
		for( int i = 0; i < bridgeMakerEntity.getContainerSize(); i++ ) {
			setBlocks.set( i, false );
			ItemStack stack = bridgeMakerEntity.getItem( i );
			nextPos = nextPos.relative( state.getValue( BlockStateProperties.FACING ) );
			if( replacableBlocks.contains( level.getBlockState( nextPos ).getBlock() ) &&
				!stack.isEmpty() ) {
				level.setBlock(
					nextPos,
					updateBlockStateFromTag( stack, bridgeMakerEntity.getBlockStateForSlot( i ) ),
					3
				);
				BlockItem.updateCustomBlockEntityTag( level, null, nextPos, stack );
				updateBlockEntityComponents( level, nextPos, stack );
				bridgeMakerEntity.setItem( i, ItemStack.EMPTY, null );
				setBlocks.set( i, true );
			}
		}
		return setBlocks;
	}
	
	private BlockState updateBlockStateFromTag( ItemStack pStack, BlockState pState ) {
		
		BlockItemStateProperties blockItemStateProperties = pStack.getOrDefault(
			DataComponents.BLOCK_STATE,
			BlockItemStateProperties.EMPTY
		);
		return blockItemStateProperties.apply( pState );
	}
	
	private static void updateBlockEntityComponents( Level pLevel, BlockPos pPoa, ItemStack pStack ) {
		
		BlockEntity blockentity = pLevel.getBlockEntity( pPoa );
		if( blockentity != null ) {
			blockentity.applyComponentsFromItemStack( pStack );
			blockentity.setChanged();
		}
	}
	
	private List<Boolean> unpower(
		@NotNull BridgeMakerEntity bridgeMakerEntity,
		List<Boolean> setBlocks,
		@NotNull BlockState state,
		@NotNull BlockPos pos,
		@NotNull Level level ) {
		
		Direction facing = state.getValue( BlockStateProperties.FACING );
		int containerSize = bridgeMakerEntity.getContainerSize();
		BlockState[] blockStates = new BlockState[containerSize];
		BlockPos nextPos = pos;
		BlockPos collectPos = pos;
		
		for( int i = 0; i < containerSize; i++ ) {
			nextPos = nextPos.relative( facing );
			if( setBlocks.get( i ) && bridgeMakerEntity.getItem( i ).isEmpty() ) {
				blockStates[i] = level.getBlockState( nextPos );
			}
		}
		for( int i = 0; i < containerSize; i++ ) {
			collectPos = collectPos.relative( facing );
			if( level.getBlockState( collectPos ) == Blocks.AIR.defaultBlockState() ) {
				blockStates[i] = null;
			} else {
				if( setBlocks.get( i ) && bridgeMakerEntity.getItem( i ).isEmpty() ) {
					ItemStack blockItemStack = null;
					List<ItemStack> blockDrops = getDrops(
						blockStates[i],
						(ServerLevel)level,
						collectPos,
						level.getBlockEntity( collectPos )
					);
					for( ItemStack drop : blockDrops ) {
						if( drop.getItem() instanceof BlockItem &&
							( (BlockItem)drop.getItem() ).getBlock() == blockStates[i].getBlock() ) {
							blockItemStack = drop;
							break;
						}
					}
					if( blockItemStack == null ) {
						blockItemStack = new ItemStack( blockStates[i].getBlock().asItem() );
					}
					BlockEntity blockEntity = level.getBlockEntity( collectPos );
					if( blockEntity != null ) {
						blockEntity.saveToItem( blockItemStack, level.registryAccess() );
					}
					if( blockItemStack.getItem() instanceof BlockItem ) {
						bridgeMakerEntity.setItem( i, blockItemStack, blockStates[i] );
						level.setBlock( collectPos, Blocks.AIR.defaultBlockState(), 3 );
					}
					setBlocks.set( i, false );
				}
			}
		}
		return setBlocks;
	}
	
	@Override
	protected void createBlockStateDefinition( @NotNull StateDefinition.Builder<Block, BlockState> builder ) {
		
		builder.add( BlockStateProperties.FACING, BlockStateProperties.POWERED );
	}
}
