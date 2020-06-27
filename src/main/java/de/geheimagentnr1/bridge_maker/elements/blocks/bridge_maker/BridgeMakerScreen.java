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
	
	
	private static final ResourceLocation GUI = new ResourceLocation( BridgeMakerMod.MODID,
		"textures/gui/bridge_maker/bridge_maker_gui.png" );
	
	public BridgeMakerScreen( BridgeMakerContainer _container, PlayerInventory inventory, ITextComponent titleIn ) {
		
		super( _container, inventory, titleIn );
		//noinspection AssignmentToSuperclassField
		++ySize;
	}
	
	@Override
	public void func_230430_a_( @Nonnull MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_,
		float p_230430_4_ ) {
		
		func_230446_a_( p_230430_1_ );
		super.func_230430_a_( p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_ );
		func_230459_a_( p_230430_1_, p_230430_2_, p_230430_3_ );
	}
	
	@SuppressWarnings( "deprecation" )
	@Override
	protected void func_230450_a_( @Nonnull MatrixStack p_230450_1_, float p_230450_2_, int p_230450_3_,
		int p_230450_4_ ) {
		
		RenderSystem.color4f( 1.0F, 1.0F, 1.0F, 1.0F );
		Objects.requireNonNull( field_230706_i_ ).getTextureManager().bindTexture( GUI );
		int i = ( field_230708_k_ - xSize ) / 2;
		int j = ( field_230709_l_ - ySize ) / 2;
		func_238474_b_( p_230450_1_, i, j, 0, 0, xSize, ySize );
	}
}
