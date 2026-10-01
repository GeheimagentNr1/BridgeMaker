package de.geheimagentnr1.bridge_maker.registry;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;


@Data
@RequiredArgsConstructor
public class RegistryEntry<T> {
	
	
	@NotNull
	private final Identifier registryName;
	
	@NotNull
	private final T value;
	
	@NotNull
	public static <T> RegistryEntry<T> create( @NotNull String modId, @NotNull String registryName,
	                                           @NotNull T value ) {
		
		return new RegistryEntry<>( Identifier.fromNamespaceAndPath( modId, registryName ), value );
	}
	
	@NotNull
	public static <T> RegistryEntry<T> create( @NotNull Identifier registryName, @NotNull T value ) {
		
		return new RegistryEntry<>( registryName, value );
	}
}
