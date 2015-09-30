package nahamawiki.oef.gui;

import org.lwjgl.opengl.GL11;

import nahamawiki.oef.inventory.ContainerEECharger;
import nahamawiki.oef.tileentity.TileEntityEECharger;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class GuiEECharger extends GuiContainer {

	private TileEntityEECharger tileEntity;
	private static final ResourceLocation GUITEXTURE = new ResourceLocation("textures/gui/container/furnace.png");

	public GuiEECharger(EntityPlayer player, TileEntityEECharger tileEntity) {
		super(new ContainerEECharger(player, tileEntity));
		this.tileEntity = tileEntity;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		String s = this.tileEntity.hasCustomInventoryName() ? this.tileEntity.getInventoryName() : StatCollector.translateToLocal(this.tileEntity.getInventoryName());
		this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
		this.fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		this.mc.getTextureManager().bindTexture(GUITEXTURE);

		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
		int i1;
		//
		// if (this.tileEntity.isBurning()) {
		// i1 = this.tileEntity.getBurnTimeRemainingScaled(12);
		// this.drawTexturedModalRect(k + 56, l + 36 + 12 - i1, 176, 12 - i1, 14, i1 + 2);
		// }
		//
		// i1 = this.tileEntity.getSmeltProgressScaled(24);
		// this.drawTexturedModalRect(k + 79, l + 34, 176, 14, i1 + 1, 16);
	}

}