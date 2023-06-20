package de.geheimagentnr1.bridge_maker;


import de.geheimagentnr1.bridge_maker.config.ClientConfig;
import de.geheimagentnr1.bridge_maker.elements.blocks.ModBlocksRegisterFactory;
import de.geheimagentnr1.bridge_maker.elements.creative_mod_tabs.ModCreativeTabsRegisterFactory;
import de.geheimagentnr1.minecraft_forge_api.AbstractMod;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;


@Mod( BridgeMakerMod.MODID )
public class BridgeMakerMod extends AbstractMod {
	
	
	@NotNull
	public static final String MODID = "bridge_maker";
	
	@NotNull
	@Override
	public String getModId() {
		
		return MODID;
	}
	
	@Override
	protected void initMod() {
		
		ClientConfig clientConfig = registerConfig( ClientConfig::new );
		ModBlocksRegisterFactory modBlocksRegisterFactory = registerEventHandler( new ModBlocksRegisterFactory() );
		registerEventHandler( new ModCreativeTabsRegisterFactory( clientConfig, modBlocksRegisterFactory ) );
	}
}
