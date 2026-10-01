package de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker;

import de.geheimagentnr1.bridge_maker.BridgeMakerMod;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;


public class BridgeMakerScreen extends AbstractContainerScreen<BridgeMakerMenu> {
	
	
	@NotNull
	private static final Identifier GUI = Identifier.fromNamespaceAndPath(
		BridgeMakerMod.MODID,
		"textures/gui/bridge_maker/bridge_maker_gui.png"
	);
	
	private static final float TEXTURE_SIZE = 256.0F;
	
	public BridgeMakerScreen(
		@NotNull BridgeMakerMenu container,
		@NotNull Inventory inventory,
		@NotNull Component _title ) {
		
		//default size 176 x 166, one pixel higher for the texture
		super( container, inventory, _title, 176, 167 );
	}
	
	@Override
	public void extractBackground( @NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick ) {
		
		super.extractBackground( graphics, mouseX, mouseY, partialTick );
		//Overload without RenderPipeline (uses RenderPipelines.GUI_TEXTURED internally): the RenderPipeline class
		//moved from com.mojang.blaze3d to com.mojang.renderpearl in 26.3, so one jar can cover 26.1 - 26.3
		int x = leftPos;
		int y = ( height - imageHeight ) / 2;
		graphics.blit(
			GUI,
			x,
			y,
			x + imageWidth,
			y + imageHeight,
			0.0F,
			imageWidth / TEXTURE_SIZE,
			0.0F,
			imageHeight / TEXTURE_SIZE
		);
	}
}
