package de.geheimagentnr1.bridge_maker;


import de.geheimagentnr1.bridge_maker.elements.blocks.ModBlocksRegisterFactory;
import de.geheimagentnr1.bridge_maker.elements.creative_mod_tabs.ModCreativeTabsRegisterFactory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.jetbrains.annotations.NotNull;


@Mod( BridgeMakerMod.MODID )
public class BridgeMakerMod {
	
	
	@NotNull
	public static final String MODID = "bridge_maker";
	
	@SuppressWarnings( "unused" )
	public BridgeMakerMod( IEventBus modEventBus, ModContainer modContainer ) {
		
		ModBlocksRegisterFactory modBlocksRegisterFactory = new ModBlocksRegisterFactory();
		modEventBus.register( modBlocksRegisterFactory );
		
		ModCreativeTabsRegisterFactory modCreativeTabsRegisterFactory = 
			new ModCreativeTabsRegisterFactory( modBlocksRegisterFactory );
		modEventBus.register( modCreativeTabsRegisterFactory );
	}
}
