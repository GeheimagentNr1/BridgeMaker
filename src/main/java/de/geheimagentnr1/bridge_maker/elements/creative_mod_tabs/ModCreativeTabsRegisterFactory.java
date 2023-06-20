package de.geheimagentnr1.bridge_maker.elements.creative_mod_tabs;

import de.geheimagentnr1.bridge_maker.config.ClientConfig;
import de.geheimagentnr1.bridge_maker.elements.blocks.ModBlocksRegisterFactory;
import de.geheimagentnr1.minecraft_forge_api.elements.creative_mod_tabs.CreativeModeTabFactory;
import de.geheimagentnr1.minecraft_forge_api.elements.creative_mod_tabs.CreativeModeTabRegisterFactory;
import lombok.RequiredArgsConstructor;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;


@RequiredArgsConstructor
public class ModCreativeTabsRegisterFactory extends CreativeModeTabRegisterFactory {
	
	
	@NotNull
	private final ClientConfig clientConfig;
	
	@NotNull
	private final ModBlocksRegisterFactory modBlocksRegisterFactory;
	
	@NotNull
	@Override
	protected List<CreativeModeTabFactory> factories() {
		
		if( clientConfig.getUseVanillaTab() ) {
			return List.of();
		} else {
			return List.of(
				new BridgeMakerCreativeModeTabFactory( modBlocksRegisterFactory )
			);
		}
	}
	
	@SubscribeEvent
	@Override
	public void handleBuildCreativeModeTabContentsEvent( @NotNull BuildCreativeModeTabContentsEvent event ) {
		
		if( clientConfig.getUseVanillaTab() ) {
			modBlocksRegisterFactory.getBlocks().forEach( registryEntry ->
				event.accept( new ItemStack( registryEntry.getValue().asItem() ) )
			);
		}
	}
}
