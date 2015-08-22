package nahamawiki.oef.block;

import java.util.Random;

import nahamawiki.oef.core.OEFBlockCore;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockEELamp extends BlockEEMachineBase
{
    private final boolean flag;
    private static final String __OBFID = "CL_00000297";

    public BlockEELamp(boolean flg)
    {
        super();
        this.setResistance(1000000F);
        this.flag = flg;

        if (flg)
        {
            this.setLightLevel(1.0F);
        }
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World world, int x, int y, int z)
    {
        /*if (!world.isRemote)
        {
            if (this.flag && !(world.getBlock(x, y, z) instanceof BlockEEMachineBase) || (world.getBlock(x, y, z) instanceof BlockEEMachineBase && !((BlockEEMachineBase) world.getBlock(x, y, z)).canProvideEE()))
            {
            	world.setBlock(x, y, z, OEFBlockCore.EELamp, 0, 2);
            }
            else if (!this.flag && (world.getBlock(x, y, z) instanceof BlockEEMachineBase && ((BlockEEMachineBase) world.getBlock(x, y, z)).canProvideEE()))
            {
                world.setBlock(x, y, z, OEFBlockCore.EELamp_on, 0, 2);
            }
        }*/
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor Block
     */
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        if (!world.isRemote)
        {
/*            if (this.flag && !(block instanceof BlockEEMachineBase))
            {
            	world.setBlock(x, y, z, OEFBlockCore.EELamp, 0, 2);
            }
            else */if (block instanceof BlockEEMachineBase && ((BlockEEMachineBase) block).canProvideEE())
            {
            	if(((BlockEEMachineBase) block).providingEE() > 0)
            	{
            		world.setBlock(x, y, z, OEFBlockCore.EELamp_on, 0, 2);
            	}
            	else
            	{
            		world.setBlock(x, y, z, OEFBlockCore.EELamp, 0, 2);
            	}
            }
        }
    }
 
 
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
    {
        return Item.getItemFromBlock(OEFBlockCore.EELamp);
    }

    /**
     * Gets an item for the block being called on. Args: world, x, y, z
     */
    @SideOnly(Side.CLIENT)
    public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_)
    {
        return Item.getItemFromBlock(OEFBlockCore.EELamp);
    }

    /**
     * Returns an item stack containing a single instance of the current block type. 'i' is the block's subtype/damage
     * and is ignored for blocks which do not support subtypes. Blocks which cannot be harvested should return null.
     */
    protected ItemStack createStackedBlock(int p_149644_1_)
    {
        return new ItemStack(OEFBlockCore.EELamp);
    }

	/**EE発生中は光源になるようにする*/
	@Override
	public int getLightValue(IBlockAccess iBlockAccess, int x, int y, int z) {
		if(this.flag)
		{
			return 15;
		}
		return 0;
	}

	@Override
	public boolean canProvideEE() {
		return false;
	}

	@Override
	public boolean canReciveEE() {
		return true;
	}

	@Override
	public int providingEE() {
		return 0;
	}
}