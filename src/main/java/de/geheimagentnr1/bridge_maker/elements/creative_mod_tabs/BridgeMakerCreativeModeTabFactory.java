package de.geheimagentnr1.bridge_maker.elements.creative_mod_tabs;

import de.geheimagentnr1.bridge_maker.BridgeMakerMod;
import de.geheimagentnr1.bridge_maker.elements.blocks.ModBlocksRegisterFactory;
import de.geheimagentnr1.minecraft_forge_api.elements.creative_mod_tabs.CreativeModeTabFactory;
import de.geheimagentnr1.minecraft_forge_api.registry.RegistryEntry;
import lombok.RequiredArgsConstructor;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.List;


@RequiredArgsConstructor
class BridgeMakerCreativeModeTabFactory implements CreativeModeTabFactory {
	
	
	@NotNull
	private final ModBlocksRegisterFactory modBlocksRegisterFactory;
	
	@NotNull
	@Override
	public String getRegistryName() {
		
		return BridgeMakerMod.MODID;
	}
	
	@NotNull
	@Override
	public ItemLike getIconItem() {
		
		return ModBlocksRegisterFactory.BRIDGE_MAKER;
	}
	
	@NotNull
	@Override
	public List<RegistryEntry<Block>> getDisplayBlocks() {
		
		return modBlocksRegisterFactory.getBlocks();
	}
}
