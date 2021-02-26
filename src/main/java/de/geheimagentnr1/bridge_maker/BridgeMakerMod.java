package de.geheimagentnr1.bridge_maker;


import de.geheimagentnr1.bridge_maker.config.ClientConfig;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;


@SuppressWarnings( "UtilityClassWithPublicConstructor" )
@Mod( BridgeMakerMod.MODID )
public class BridgeMakerMod {
	
	
	public static final String MODID = "bridge_maker";
	
	public BridgeMakerMod() {
		
		ClientConfig.load();
		ModLoadingContext.get().registerConfig( ModConfig.Type.CLIENT, ClientConfig.CONFIG );
	}
}
