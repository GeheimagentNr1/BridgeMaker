package de.geheimagentnr1.bridge_maker;


import de.geheimagentnr1.bridge_maker.setup.ClientProxy;
import de.geheimagentnr1.bridge_maker.setup.IProxy;
import de.geheimagentnr1.bridge_maker.setup.ModSetup;
import de.geheimagentnr1.bridge_maker.setup.ServerProxy;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;


@Mod( BridgeMakerMod.MODID )
public class BridgeMakerMod {
	
	
	public static final String MODID = "bridge_maker";
	
	public static final IProxy proxy = DistExecutor.safeRunForDist( () -> ClientProxy::new, () -> ServerProxy::new );
	
	public static final ModSetup setup = new ModSetup();
}
