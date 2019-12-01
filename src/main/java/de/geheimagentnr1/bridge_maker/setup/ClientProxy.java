package de.geheimagentnr1.bridge_maker.setup;

import de.geheimagentnr1.bridge_maker.elements.blocks.ModBlocks;
import de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker.BridgeMakerScreen;
import net.minecraft.client.gui.ScreenManager;


public class ClientProxy implements IProxy {
	
	
	@Override
	public void init() {
		
		ScreenManager.registerFactory( ModBlocks.BRIDGE_MAKER_CONTAINER, BridgeMakerScreen::new );
	}
}
