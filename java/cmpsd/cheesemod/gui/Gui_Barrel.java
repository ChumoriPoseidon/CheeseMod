package cmpsd.cheesemod.gui;

import java.util.ArrayList;

import cmpsd.cheesemod.Reference;
import cmpsd.cheesemod.container.Container_Barrel;
import cmpsd.cheesemod.tileentity.TileEntity_Barrel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Gui_Barrel extends GuiContainer {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "textures/gui/gui_barrel.png");
	private InventoryPlayer inventory;
	private TileEntity_Barrel te_barrel;

	public Gui_Barrel(InventoryPlayer inventoryPlayer, TileEntity_Barrel tileEntity) {

		super(new Container_Barrel(inventoryPlayer, tileEntity));
		this.inventory = inventoryPlayer;
		this.te_barrel = tileEntity;
		this.xSize = 176;
		this.ySize = 166;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(TEXTURE);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);

		if(TileEntity_Barrel.isBurning(this.te_barrel)) {

			int k = this.getBurnLeftScaled(13);
			this.drawTexturedModalRect(i + 26, j + 36 + 12 - k, 176, 12 - k, 14, k + 1);
		}
		int l = this.getAmountLiquid(48);
		this.renderLiquid(i + 68, j + 19 + 48, l);
	}

	private int getBurnLeftScaled(int pixels) {

		int i = this.te_barrel.getField(1);
		if(i == 0) {

			i = 200;
		}
		return this.te_barrel.getField(0) * pixels / i;
	}

	private int getAmountLiquid(int pixels) {

		return this.te_barrel.tank.getFluidAmount() * pixels / this.te_barrel.maxCapacity;
	}

	private void renderLiquid(int x, int y, int amount) {

		mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		TextureMap textureMap = Minecraft.getMinecraft().getTextureMapBlocks();
		TextureAtlasSprite textureAtlasSprite = textureMap.getTextureExtry("minecraft:blocks/water_still");
		double uMin = textureAtlasSprite.getMinU();
		double uMax = textureAtlasSprite.getMaxU();
		double vMin = textureAtlasSprite.getMinV();
		double vMax = textureAtlasSprite.getMaxV();

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferBuilder.pos(x, y, zLevel).tex(uMin, vMax).endVertex();
		bufferBuilder.pos(x + 40, y, zLevel).tex(uMax, vMax).endVertex();
		bufferBuilder.pos(x + 40, y - amount, zLevel).tex(uMax, vMin).endVertex();
		bufferBuilder.pos(x, y - amount, zLevel).tex(uMin, vMin).endVertex();
		tessellator.draw();
	}

	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

		String name = I18n.format(this.te_barrel.getName(), new Object[0]);
		this.fontRenderer.drawString(name, this.xSize / 2 - this.fontRenderer.getStringWidth(name) / 2, 6, 4210752);
		this.fontRenderer.drawString(this.inventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {

		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		ArrayList<String> list = new ArrayList<String>();
		if (isPointInRegion(68, 19, 40, 48, mouseX, mouseY)) {

			int amountLiquid = this.te_barrel.tank.getFluidAmount();
			float progress = this.te_barrel.getField(2) * 100.0F / this.te_barrel.getField(3);
			list.add("Amount : " + amountLiquid + "mb / " + this.te_barrel.maxCapacity + "mb");
			list.add("Progress : " + progress + "%");
		}
		this.drawHoveringText(list, mouseX, mouseY);
		this.renderHoveredToolTip(mouseX, mouseY);
	}
}
