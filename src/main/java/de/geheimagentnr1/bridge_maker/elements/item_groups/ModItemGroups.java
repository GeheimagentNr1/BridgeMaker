package de.geheimagentnr1.bridge_maker.elements.item_groups;

import de.geheimagentnr1.bridge_maker.config.ClientConfig;
import net.minecraft.item.ItemGroup;


public class ModItemGroups {
	
	
	private static ItemGroup itemGroup;
	
	public static ItemGroup getItemGroup() {
		
		if( itemGroup == null ) {
			if( ClientConfig.getUseVanillaTab() ) {
				itemGroup = ItemGroup.TAB_REDSTONE;
			} else {
				itemGroup = new BridgeMakerItemGroup();
			}
		}
		return itemGroup;
	}
}
