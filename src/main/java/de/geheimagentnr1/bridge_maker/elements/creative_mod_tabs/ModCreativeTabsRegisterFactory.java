package de.geheimagentnr1.bridge_maker.elements.creative_mod_tabs;

import de.geheimagentnr1.bridge_maker.elements.blocks.ModBlocksRegisterFactory;
import net.minecraft.core.registries.Registries;
import lombok.RequiredArgsConstructor;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.bus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;


@RequiredArgsConstructor
public class ModCreativeTabsRegisterFactory {
	
	
	@NotNull
	private final ModBlocksRegisterFactory modBlocksRegisterFactory;
	
	@SubscribeEvent
	public void handleRegistryEvent( @NotNull RegisterEvent event ) {
		
		if( event.getRegistryKey().equals( Registries.CREATIVE_MODE_TAB ) ) {
			BridgeMakerCreativeModeTabFactory factory = new BridgeMakerCreativeModeTabFactory( modBlocksRegisterFactory );
			event.register(
				Registries.CREATIVE_MODE_TAB,
				registerHelper -> registerHelper.register(
					factory.getRegistryName(),
					factory.get()
				)
			);
		}
	}
}
