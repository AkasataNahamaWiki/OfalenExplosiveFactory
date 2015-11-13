package nahamawiki.oef.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahamawiki.oef.tileentity.ITileEntityEEMachine;
import nahamawiki.oef.util.EEUtil;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemEEBattery extends ItemOEFBase {

	private IIcon[][] iicon = new IIcon[2][4];

	public ItemEEBattery() {
		super();
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int slot, boolean isHeld) {
		if (!itemStack.hasTagCompound()) {
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setInteger("holdingEE", 0);
			itemStack.setTagCompound(nbt);
		}
		NBTTagCompound nbt = itemStack.getTagCompound();
		if (nbt.getInteger("holdingEE") > EEUtil.getBaseCapacity(itemStack.getItemDamage()))
			nbt.setInteger("holdingEE", EEUtil.getBaseCapacity(itemStack.getItemDamage()));
		if (!nbt.hasKey("canChargeEE"))
			nbt.setBoolean("canChargeEE", true);
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (stack.hasTagCompound() && tileEntity instanceof ITileEntityEEMachine) {
			// NBTを持っていて、右クリックしたのがEE機械なら、
			if (world.isRemote)
				// クライアントは終了。
				return true;
			ITileEntityEEMachine machine = (ITileEntityEEMachine) tileEntity;
			int holdingEE = stack.getTagCompound().getInteger("holdingEE");
			if (player.capabilities.isCreativeMode) {
				// クリエイティブなら、EEを渡すだけ。
				machine.recieveEE(holdingEE, side);
				return true;
			}
			// サバイバル
			holdingEE = machine.recieveEE(holdingEE, side);
			if (stack.stackSize > 1) {
				// スタックされていたら、そこから一個減らす。
				stack.stackSize--;
				// あまりのEEを保持したバッテリーを一つ生成する。
				ItemStack stack1 = new ItemStack(stack.getItem(), 1, stack.getItemDamage());
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setInteger("holdingEE", holdingEE);
				stack1.setTagCompound(nbt);
				world.spawnEntityInWorld(new EntityItem(world, player.posX, player.posY, player.posZ, stack1));
				return true;
			}
			// スタックされていないなら、あまりのEEを保持する。
			stack.getTagCompound().setInteger("holdingEE", holdingEE);
			return true;
		}
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister register) {
		for (int i = 0; i < 4; i++) {
			this.iicon[0][i] = register.registerIcon(this.getIconString() + "-" + i);
			this.iicon[1][i] = register.registerIcon(this.getIconString() + "_Full-" + i);
		}
	}

	@Override
	public IIcon getIconFromDamage(int meta) {
		return this.iicon[1][meta];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconIndex(ItemStack itemStack) {
		if (itemStack.hasTagCompound()) {
			NBTTagCompound nbt = itemStack.getTagCompound();
			if (nbt.getInteger("holdingEE") == 0)
				return this.iicon[0][itemStack.getItemDamage()];
			return this.iicon[1][itemStack.getItemDamage()];
		}
		return this.iicon[0][itemStack.getItemDamage()];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs creativeTab, List list) {
		ItemStack itemStack;
		NBTTagCompound nbt = new NBTTagCompound();

		for (int i = 0; i < 4; i++) {
			itemStack = new ItemStack(item, 1, i);
			nbt = new NBTTagCompound();
			nbt.setInteger("holdingEE", 0);
			itemStack.setTagCompound(nbt);
			list.add(itemStack);

			itemStack = new ItemStack(item, 1, i);
			nbt = new NBTTagCompound();
			nbt.setInteger("holdingEE", EEUtil.getBaseCapacity(i));
			itemStack.setTagCompound(nbt);
			list.add(itemStack);
		}
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean flag) {
		if (itemStack.hasTagCompound())
			list.add(StatCollector.translateToLocal("info.EEMachineState.holding")
					+ itemStack.getTagCompound().getInteger("holdingEE") + " EE / "
					+ EEUtil.getBaseCapacity(itemStack.getItemDamage() & 3) + " EE");
	}

	@Override
	public int getMetadata(int meta) {
		return meta;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		return this.getUnlocalizedName() + "." + itemStack.getItemDamage();
	}

}
