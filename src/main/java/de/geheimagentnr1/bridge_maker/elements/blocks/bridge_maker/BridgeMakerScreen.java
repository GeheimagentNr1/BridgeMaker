package de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker;

import de.geheimagentnr1.bridge_maker.BridgeMakerMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
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
	
	public BridgeMakerScreen(
		@NotNull BridgeMakerMenu container,
		@NotNull Inventory inventory,
		@NotNull Component _title ) {
		
		super( container, inventory, _title );
		initScreen();
	}
	
	private void initScreen() {
		
		imageHeight++;
	}
	
	@Override
	public void render( @NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick ) {
		
		super.render( guiGraphics, mouseX, mouseY, partialTick );
		renderTooltip( guiGraphics, mouseX, mouseY );
	}
	
	@Override
	protected void renderBg( @NotNull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY ) {
		
		guiGraphics.blit(
			RenderPipelines.GUI_TEXTURED,
			GUI,
			leftPos,
			( height - imageHeight ) / 2,
			0.0F,
			0.0F,
			imageWidth,
			imageHeight,
			256,
			256
		);
	}
}
