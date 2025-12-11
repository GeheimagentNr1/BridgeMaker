package de.geheimagentnr1.bridge_maker.registry;

import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;


public class RegistryHelper {
	
	
	public static <T> void registerElements(
		@NotNull RegisterEvent event,
		@NotNull ResourceKey<Registry<T>> registryKey,
		@NotNull Supplier<List<RegistryEntry<T>>> elements ) {
		
		if( event.getRegistryKey() == registryKey ) {
			event.register(
				registryKey,
				registerHelper -> elements.get().forEach( registryEntry -> registerHelper.register(
					registryEntry.getRegistryName(),
					registryEntry.getValue()
				) )
			);
		}
	}
	
	@NotNull
	@SuppressWarnings( "unchecked" )
	public static <T extends BlockEntity> BlockEntityType<T> buildBlockEntity(
		@NotNull String registryName,
		@NotNull BlockEntityType.BlockEntitySupplier<T> blockEntitySupplier,
		@NotNull Block... blocks ) {
		
		//noinspection DataFlowIssue Null is valid
		return (BlockEntityType<T>) BlockEntityType.Builder.of( blockEntitySupplier, blocks )
			.build( Util.fetchChoiceType( References.BLOCK_ENTITY, registryName ) );
	}
}
