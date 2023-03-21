package de.geheimagentnr1.bridge_maker;


import de.geheimagentnr1.bridge_maker.config.ClientConfig;
import de.geheimagentnr1.bridge_maker.handlers.ModEventHandler;
import de.geheimagentnr1.minecraft_forge_api.AbstractMod;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;


@Mod( BridgeMakerMod.MODID )
public class BridgeMakerMod extends AbstractMod {
	
	
	public static final String MODID = "bridge_maker";
	
	public BridgeMakerMod() {
		
		super( MODID, 23, "test" );
	}
	
	@Override
	protected void initMod() {
		
		registerConfig( ModConfig.Type.CLIENT, ClientConfig::new );
		ClientConfig clientConfig = getConfig( ModConfig.Type.CLIENT, ClientConfig.class ).orElseThrow();
		clientConfig.load();
		registerModEventHandler( new ModEventHandler( clientConfig ) );
	}
}
