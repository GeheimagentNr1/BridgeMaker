package de.geheimagentnr1.bridge_maker.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import de.geheimagentnr1.bridge_maker.BridgeMakerMod;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;


public class ClientConfig {
	
	
	private static final Logger LOGGER = LogManager.getLogger( ClientConfig.class );
	
	private static final String MOD_NAME = ModLoadingContext.get().getActiveContainer().getModInfo().getDisplayName();
	
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	
	public static final ForgeConfigSpec CONFIG;
	
	private static final ForgeConfigSpec.BooleanValue USE_VANILLA_TAB;
	
	static {
		
		USE_VANILLA_TAB = BUILDER.comment( "If true, the bridge maker is added to the redstone creative tab. " +
			"If false, the bridge maker is added to the bridge maker creative tab." )
			.worldRestart()
			.define( "use_vanilla_tab", false );
		CONFIG = BUILDER.build();
	}
	
	public static void load() {
		
		CommentedFileConfig configData = CommentedFileConfig.builder( FMLPaths.CONFIGDIR.get()
			.resolve(
				BridgeMakerMod.MODID + "-" + ModConfig.Type.CLIENT.name().toLowerCase( Locale.ENGLISH ) + ".toml"
			) )
			.sync()
			.autosave()
			.writingMode( WritingMode.REPLACE )
			.build();
		configData.load();
		CONFIG.setConfig( configData );
		configData.close();
	}
	
	public static void printConfig() {
		
		LOGGER.info( "Loading \"{}\" Client Config", MOD_NAME );
		LOGGER.info( "{} = {}", USE_VANILLA_TAB.getPath(), USE_VANILLA_TAB.get() );
		LOGGER.info( "\"{}\" Client Config loaded", MOD_NAME );
	}
	
	public static boolean getUseVanillaTab() {
		
		return USE_VANILLA_TAB.get();
	}
}
