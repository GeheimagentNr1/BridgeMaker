package de.geheimagentnr1.bridge_maker.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;


//Stores block states in the format of NbtUtils.writeBlockState and of BlockState.CODEC up to 26.2
//("Name" and "Properties"). BlockState.CODEC writes "id"/"properties" or a plain string since 26.3, so the
//own codec keeps worlds and items of all versions readable and one jar can cover 26.1 - 26.3.
public class BlockStateCodecHelper {
	
	
	@NotNull
	public static final MapCodec<BlockState> MAP_CODEC = RecordCodecBuilder.mapCodec( instance -> instance.group(
		Identifier.CODEC.fieldOf( "Name" ).forGetter( state -> BuiltInRegistries.BLOCK.getKey( state.getBlock() ) ),
		Codec.unboundedMap( Codec.STRING, Codec.STRING )
			.optionalFieldOf( "Properties", Map.of() )
			.forGetter( BlockStateCodecHelper::getPropertyNames )
	).apply( instance, BlockStateCodecHelper::buildBlockState ) );
	
	//null entries (slots without a stored state) are written as air, like in CodeNetworkHelper
	@NotNull
	public static final Codec<List<BlockState>> LIST_CODEC = MAP_CODEC.codec().listOf().xmap(
		states -> states,
		states -> states.stream().map( state -> state == null ? Blocks.AIR.defaultBlockState() : state ).toList()
	);
	
	@NotNull
	private static Map<String, String> getPropertyNames( @NotNull BlockState state ) {
		
		Map<String, String> properties = new TreeMap<>();
		for( Property<?> property : state.getProperties() ) {
			properties.put( property.getName(), getValueName( state, property ) );
		}
		return properties;
	}
	
	@NotNull
	private static <T extends Comparable<T>> String getValueName( @NotNull BlockState state, @NotNull Property<T> property ) {
		
		return property.getName( state.getValue( property ) );
	}
	
	//Like NbtUtils.readBlockState: unknown blocks become air, unknown properties and values are ignored
	@NotNull
	private static BlockState buildBlockState( @NotNull Identifier name, @NotNull Map<String, String> properties ) {
		
		BlockState state = BuiltInRegistries.BLOCK.getValue( name ).defaultBlockState();
		for( Map.Entry<String, String> entry : properties.entrySet() ) {
			Property<?> property = state.getBlock().getStateDefinition().getProperty( entry.getKey() );
			if( property != null ) {
				state = setValue( state, property, entry.getValue() );
			}
		}
		return state;
	}
	
	@NotNull
	private static <T extends Comparable<T>> BlockState setValue(
		@NotNull BlockState state,
		@NotNull Property<T> property,
		@NotNull String valueName ) {
		
		return property.getValue( valueName ).map( value -> state.setValue( property, value ) ).orElse( state );
	}
}
