package de.geheimagentnr1.bridge_maker.util;


import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class CodeNetworkHelper {
	
	
	public static List<BlockState> fromNetwork( @NotNull RegistryFriendlyByteBuf buffer ) {
		
		int count = buffer.readVarInt();
		List<BlockState> blockStates = new ArrayList<>();
		for( int i = 0; i < count; i++ ) {
			BlockState state = Block.stateById( buffer.readVarInt() );
			blockStates.add( state );
		}
		return blockStates;
	}
	
	public static void toNetwork( @NotNull RegistryFriendlyByteBuf buffer, @NotNull List<BlockState> states ) {
		
		buffer.writeVarInt( states.size() );
		for( BlockState state : states ) {
			buffer.writeVarInt( Block.getId( state == null ? Blocks.AIR.defaultBlockState() : state ) );
		}
	}
}
