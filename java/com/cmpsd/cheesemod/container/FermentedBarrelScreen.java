package com.cmpsd.cheesemod.container;

import java.util.ArrayList;

import com.cmpsd.cheesemod.CheeseMod;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FermentedBarrelScreen extends ContainerScreen<FermentedBarrelContainer> {

	private static final ResourceLocation FERMENTED_BARREL_GUI_TEXTURES = new ResourceLocation(CheeseMod.MODID, "textures/gui/container/block_fermented_barrel.png");

	public FermentedBarrelScreen(FermentedBarrelContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		this.renderBackground();
		super.render(mouseX, mouseY, partialTicks);
		ArrayList<String> list = new ArrayList<String>();
		if (this.isPointInRegion(68, 19, 40, 48, mouseX, mouseY)) {
			list.add(this.container.getTankInfo());
			list.add(this.container.getProgressInfo());
			this.renderTooltip(list, mouseX, mouseY);
		}
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(FERMENTED_BARREL_GUI_TEXTURES);
		int i = this.guiLeft;
		int j = this.guiTop;
		this.blit(i, j, 0, 0, this.xSize, this.ySize);
		if(this.container.isBurning()) {
			int k = this.container.getBurnLeftScaled();
			this.blit(i + 26, j + 36 + 12 - k, 176, 12 - k, 14, k + 1);
		}
		int l = this.container.getFluidAmountScaled();
		this.renderFluid(i + 68, j + 19 + 48, l);
	}

	int time = 0;

	private void renderFluid(int x, int y, int scale) {
		this.minecraft.getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
		AtlasTexture atlasTexture = this.minecraft.getTextureMap();
		TextureAtlasSprite tas = atlasTexture.getSprite(Fluids.WATER.getStillFluid().getAttributes().getStillTexture());
		float uMin = tas.getMinU();
		float uMax = tas.getMaxU();
		float vMin = tas.getMinV();
		float vMax = tas.getMaxV();

		int color = Fluids.WATER.getStillFluid().getAttributes().getColor();
		float redBase = (color >> 16 & 0xFF) / 255.0F;
		float greenBase = (color >> 8 & 0xFF) / 255.0F;
		float blueBase = (color & 0xFF) / 255.0F;
		float alpha = ((color >> 24) & 0xFF) / 255F;

		int progress = this.container.getProgress();
		float red = redBase + (1.0F - redBase) / 100.0F * progress;
		float green = greenBase + (1.0F - greenBase) / 100.0F * progress;
		float blue = blueBase + (1.0F - blueBase) / 100.0F * progress;

		GlStateManager.enableBlend();
		GlStateManager.enableAlphaTest();

		GlStateManager.color4f(red, green, blue, alpha);

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bb = tessellator.getBuffer();
		float z = this.itemRenderer.zLevel;
		bb.begin(7, DefaultVertexFormats.POSITION_TEX);
		bb.pos(x, y, z).tex(uMin, vMax).endVertex();
		bb.pos(x + 40, y, z).tex(uMax, vMax).endVertex();
		bb.pos(x + 40, y - scale, z).tex(uMax, vMin).endVertex();
		bb.pos(x, y - scale, z).tex(uMin, vMin).endVertex();
		tessellator.draw();

		GlStateManager.disableAlphaTest();
		GlStateManager.disableBlend();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String displayTitle = this.title.getFormattedText();
		this.font.drawString(displayTitle, (float)(this.xSize / 2 - this.font.getStringWidth(displayTitle) / 2), 6.0F, 4210752);
		this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F, (float)(this.ySize - 96 + 2), 4210752);
	}
}
