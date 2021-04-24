package de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import de.geheimagentnr1.bridge_maker.BridgeMakerMod;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import java.util.Objects;


public class BridgeMakerScreen extends ContainerScreen<BridgeMakerContainer> {
	
	
	private static final ResourceLocation GUI = new ResourceLocation(
		BridgeMakerMod.MODID,
		"textures/gui/bridge_maker/bridge_maker_gui.png"
	);
	
	public BridgeMakerScreen( BridgeMakerContainer _container, PlayerInventory inv, ITextComponent titleIn ) {
		
		super( _container, inv, titleIn );
		initScreen();
	}
	
	private void initScreen() {
		
		++imageHeight;
	}
	
	@Override
	public void render( @Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks ) {
		
		renderBackground( matrixStack );
		super.render( matrixStack, mouseX, mouseY, partialTicks );
		renderTooltip( matrixStack, mouseX, mouseY );
	}
	
	@SuppressWarnings( "deprecation" )
	@Override
	protected void renderBg( @Nonnull MatrixStack matrixStack, float partialTicks, int x, int y ) {
		
		RenderSystem.color4f( 1.0F, 1.0F, 1.0F, 1.0F );
		Objects.requireNonNull( minecraft ).getTextureManager().bind( GUI );
		int i = ( width - imageWidth ) / 2;
		int j = ( height - imageHeight ) / 2;
		blit( matrixStack, i, j, 0, 0, imageWidth, imageHeight );
	}
}
