package de.geheimagentnr1.bridge_maker.elements.blocks.bridge_maker;

import com.mojang.blaze3d.platform.GlStateManager;
import de.geheimagentnr1.bridge_maker.BridgeMakerMod;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

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
		
		++ySize;
	}
	
	@Override
	public void render( int mouseX, int mouseY, float partialTicks ) {
		
		renderBackground();
		super.render( mouseX, mouseY, partialTicks );
		renderHoveredToolTip( mouseX, mouseY );
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer( int mouseX, int mouseY ) {
		
		font.drawString( title.getFormattedText(), 8.0F, 6.0F, 4210752 );
		font.drawString( playerInventory.getDisplayName().getFormattedText(), 8.0F, ySize - 96 + 2, 4210752 );
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer( float partialTicks, int mouseX, int mouseY ) {
		
		GlStateManager.color4f( 1.0F, 1.0F, 1.0F, 1.0F );
		Objects.requireNonNull( minecraft ).getTextureManager().bindTexture( GUI );
		int relX = ( width - xSize ) / 2;
		int relY = ( height - ySize ) / 2;
		blit( relX, relY, 0, 0, xSize, ySize );
	}
}
