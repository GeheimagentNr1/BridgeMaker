package de.geheimagentnr1.bridge_maker.config;

import de.geheimagentnr1.minecraft_forge_api.AbstractMod;
import de.geheimagentnr1.minecraft_forge_api.config.AbstractConfig;
import net.minecraftforge.fml.config.ModConfig;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class ClientConfig extends AbstractConfig {
	
	
	@NotNull
	private static final String USE_VANILLA_TAB_KEY = "use_vanilla_tab";
	
	public ClientConfig( @NotNull AbstractMod _abstractMod ) {
		
		super( _abstractMod );
	}
	
	@NotNull
	@Override
	public ModConfig.Type type() {
		
		return ModConfig.Type.CLIENT;
	}
	
	@Override
	public boolean isEarlyLoad() {
		
		return true;
	}
	
	@Override
	protected void registerConfigValues() {
		
		registerConfigValue(
			List.of(
				"If true, the bridge maker is added to the redstone creative tab. ",
				"If false, the bridge maker is added to the bridge maker creative tab."
			),
			USE_VANILLA_TAB_KEY,
			false,
			true
		);
	}
	
	public boolean getUseVanillaTab() {
		
		return getValue( Boolean.class, USE_VANILLA_TAB_KEY );
	}
}
