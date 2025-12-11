package de.geheimagentnr1.bridge_maker.elements.blocks;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;


public interface BlockItemInterface {
	
	
	@NotNull
	default Item getBlockItem( @NotNull Block block, @NotNull Item.Properties properties ) {
		
		return new BlockItem( block, properties );
	}
}
