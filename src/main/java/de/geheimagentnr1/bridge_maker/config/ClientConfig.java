package de.geheimagentnr1.bridge_maker.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import de.geheimagentnr1.bridge_maker.BridgeMakerMod;
import de.geheimagentnr1.minecraft_forge_api.AbstractMod;
import de.geheimagentnr1.minecraft_forge_api.config.AbstractConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;


public class ClientConfig extends AbstractConfig {
	
	
	public ClientConfig( @NotNull ModConfig.Type _type, @NotNull AbstractMod _abstractMod ) {
		
		super( _type, _abstractMod );
	}
	
	public void load() {
		
		CommentedFileConfig configData = CommentedFileConfig.builder( FMLPaths.CONFIGDIR.get()
				.resolve(
					BridgeMakerMod.MODID + "-" + ModConfig.Type.CLIENT.name().toLowerCase( Locale.ENGLISH ) + ".toml"
				) )
			.sync()
			.autosave()
			.writingMode( WritingMode.REPLACE )
			.build();
		configData.load();
		getConfig().setConfig( configData );
		configData.close();
	}
	
	@Override
	protected void registerConfigValues() {
		
		registerConfigValue(
			"If true, the bridge maker is added to the redstone creative tab. If false, the bridge maker is added to " +
				"the bridge maker creative tab.",
			"use_vanilla_tab",
			false
		);
	}
	
	public boolean getUseVanillaTab() {
		
		return getValue( Boolean.class, "use_vanilla_tab" ).orElseThrow();
	}
}
