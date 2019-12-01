package de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker;

import net.minecraft.entity.player.PlayerEntity;


//package-private
@FunctionalInterface
interface IsUsableByPlayerListener {
	
	
	boolean reactToEvent( PlayerEntity player );
}
