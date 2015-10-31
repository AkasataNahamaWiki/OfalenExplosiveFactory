package nahamawiki.oef.gui;

import org.lwjgl.opengl.GL11;

import nahamawiki.oef.OEFCore;
import nahamawiki.oef.inventory.ContainerEECapacitor;
import nahamawiki.oef.tileentity.TileEntityEECapacitor;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class GuiEECapacitor extends GuiContainer {

	private TileEntityEECapacitor tileEntity;
	private static final ResourceLocation GUITEXTURE = new ResourceLocation(OEFCore.DOMEINNAME + "textures/gui/EECapacitor.png");

	public GuiEECapacitor(EntityPlayer player, TileEntityEECapacitor tileEntity) {
		super(new ContainerEECapacitor(player, tileEntity));
		this.tileEntity = tileEntity;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		this.fontRendererObj.drawString(StatCollector.translateToLocal("container.EECapacitor"), 8, 6, 4210752);
		this.fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
		this.fontRendererObj.drawString(tileEntity.getHoldingEE() + " / " + tileEntity.getCapacity(), 8, 29, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		this.mc.getTextureManager().bindTexture(GUITEXTURE);

		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);

		int i = this.tileEntity.getHoldingEEScaled(160);
		this.drawTexturedModalRect(x + 8, y + 39, 0, 166, i, 20);
	}

}
