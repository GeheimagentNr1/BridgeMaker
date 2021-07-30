package de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.geheimagentnr1.bridge_maker.BridgeMakerMod;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.Nonnull;


public class BridgeMakerScreen extends AbstractContainerScreen<BridgeMakerMenu> {
	
	
	private static final ResourceLocation GUI = new ResourceLocation(
		BridgeMakerMod.MODID,
		"textures/gui/bridge_maker/bridge_maker_gui.png"
	);
	
	public BridgeMakerScreen( BridgeMakerMenu container, Inventory inventory, Component _title ) {
		
		super( container, inventory, _title );
		initScreen();
	}
	
	private void initScreen() {
		
		imageHeight++;
	}
	
	@Override
	public void render( @Nonnull PoseStack poseStack, int mouseX, int mouseY, float partialTicks ) {
		
		renderBackground( poseStack );
		super.render( poseStack, mouseX, mouseY, partialTicks );
		renderTooltip( poseStack, mouseX, mouseY );
	}
	
	@Override
	protected void renderBg( @Nonnull PoseStack poseStack, float partialTicks, int x, int y ) {
		
		RenderSystem.setShader( GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, GUI);
		int left = ( width - imageWidth ) / 2;
		int top = ( height - imageHeight ) / 2;
		blit( poseStack, left, top, 0, 0, imageWidth, imageHeight );
	}
}
