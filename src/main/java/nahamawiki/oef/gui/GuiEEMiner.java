package nahamawiki.oef.gui;

import org.lwjgl.opengl.GL11;

import nahamawiki.oef.OEFCore;
import nahamawiki.oef.inventory.ContainerEEMiner;
import nahamawiki.oef.tileentity.TileEntityEEMiner;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class GuiEEMiner extends GuiContainer {

	private TileEntityEEMiner tileEntity;
	private static final ResourceLocation GUITEXTURE = new ResourceLocation(OEFCore.DOMEINNAME + "textures/gui/EEMiner.png");

	public GuiEEMiner(EntityPlayer player, TileEntityEEMiner tileEntity) {
		super(new ContainerEEMiner(player, tileEntity));
		this.tileEntity = tileEntity;
		this.ySize = 222;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		String s = this.tileEntity.hasCustomInventoryName() ? this.tileEntity.getInventoryName() : StatCollector.translateToLocal(this.tileEntity.getInventoryName());
		this.fontRendererObj.drawString(s, 8, 6, 4210752);
		this.fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		this.mc.getTextureManager().bindTexture(GUITEXTURE);

		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);

		int i = this.tileEntity.getHoldingEEScaled(60);
		this.drawTexturedModalRect(x + 91, y + 128, 176, 0, i, 8);
	}

}
