package de.geheimagentnr1.bridge_maker.elements.item_groups;

import de.geheimagentnr1.bridge_maker.config.ClientConfig;
import net.minecraft.world.item.CreativeModeTab;


public class ModItemGroups {
	
	
	private static CreativeModeTab itemGroup;
	
	public static CreativeModeTab getItemGroup() {
		
		if( itemGroup == null ) {
			if( ClientConfig.getUseVanillaTab() ) {
				itemGroup = CreativeModeTab.TAB_REDSTONE;
			} else {
				itemGroup = new BridgeMakerItemGroup();
			}
		}
		return itemGroup;
	}
}
