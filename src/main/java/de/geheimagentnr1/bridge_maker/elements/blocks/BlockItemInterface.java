package de.geheimagentnr1.bridge_maker.elements.blocks;

import net.minecraft.world.item.Item;


@SuppressWarnings( "InterfaceWithOnlyOneDirectInheritor" )
@FunctionalInterface
public interface BlockItemInterface {
	
	
	Item getBlockItem( Item.Properties properties );
}
