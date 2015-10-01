package nahamawiki.oef.gui;

import org.lwjgl.opengl.GL11;

import nahamawiki.oef.OEFCore;
import nahamawiki.oef.inventory.ContainerEECharger;
import nahamawiki.oef.tileentity.TileEntityEECharger;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class GuiEECharger extends GuiContainer {

	private TileEntityEECharger tileEntity;
	private static final ResourceLocation GUITEXTURE = new ResourceLocation(OEFCore.DOMEINNAME + "textures/gui/EECharger.png");

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

		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);

		if (this.tileEntity.isCharging()) {
			this.drawTexturedModalRect(x + 78, y + 20, 176, 0, 16, 16);
		} else {
			this.drawTexturedModalRect(x + 78, y + 20, 192, 0, 16, 16);
		}

		int i = this.tileEntity.getHoldingEEScaled(60);
		this.drawTexturedModalRect(x + 136, y + 12 + 60 - i, 176, 16, 15, i);
	}

}