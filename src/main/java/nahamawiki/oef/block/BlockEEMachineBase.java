package nahamawiki.oef.block;

import nahamawiki.oef.tileentity.TileEntityEEMachineBase;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import takumicraft.Takumi.TakumiCraftCore;

public abstract class BlockEEMachineBase extends BlockOEFBase implements ITileEntityProvider {

	public BlockEEMachineBase() {
		super();
		this.setHardness(5.0F);
		this.setResistance(6000000.0F);
		this.setHarvestLevel("pickaxe", 3);
		this.setStepSound(soundTypeMetal);
	}

	@Override
	public abstract TileEntity createNewTileEntity(World world, int meta);

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float posX, float posY, float posZ) {
		if(world.getTileEntity(x, y, z) != null && world.getTileEntity(x, y, z) instanceof TileEntityEEMachineBase)
		{
			TileEntityEEMachineBase tile = (TileEntityEEMachineBase) world.getTileEntity(x, y, z);
			if(player.getHeldItem() != null && player.getHeldItem().getItem() == TakumiCraftCore.CreeperRod)
			{
				tile.setCreeper(true);
			}
		}
		return true;
	}

	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	/**
     * Returns the block hardness at a location. Args: world, x, y, z
     */
    public float getBlockHardness(World world, int x, int y, int z)
    {
    	if(world.getTileEntity(x, y, z) != null && world.getTileEntity(x, y, z) instanceof TileEntityEEMachineBase)
		{
			TileEntityEEMachineBase tile = (TileEntityEEMachineBase) world.getTileEntity(x, y, z);
			if(tile.getCreeper())
			{
				return -1;
			}
		}
    	return this.blockHardness;
    }
    
    public void onBlockExploded(World world, int x, int y, int z, Explosion explosion)
    {
    	if(world.getTileEntity(x, y, z) != null && world.getTileEntity(x, y, z) instanceof TileEntityEEMachineBase)
		{
			TileEntityEEMachineBase tile = (TileEntityEEMachineBase) world.getTileEntity(x, y, z);
			if(!tile.getCreeper())
			{
				super.onBlockExploded(world, x, y, z, explosion);
			}
		}
    	else
    	{
    		super.onBlockExploded(world, x, y, z, explosion);
    	}
    }
}
