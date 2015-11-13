package nahamawiki.oef.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import nahamawiki.oef.OEFCore;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.UseHoeEvent;

public class ItemEETool extends ItemTool implements IItemEEBatteryTool {

	private ItemStack[] items = new ItemStack[54];
	private static final int limit = 50;

	public ItemEETool(ToolMaterial material) {
		super(0.0F, material, null);
		this.setCreativeTab(OEFCore.tabOEF);
	}

	@Override
	public boolean func_150897_b(Block block) {
		return true;
	}

	/** 採掘速度の設定 */
	@Override
	public float func_150893_a(ItemStack itemStack, Block block) {
		// 他のツールでは適正ブロックの判定をするが、ここではすべてに適正採掘速度を適用する
		return this.efficiencyOnProperMaterial;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack itemStack, int pass) {
		return true;
	}

	// クワの処理
	/** アイテムが使われた(右クリック)時の処理 */
	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		// プレイヤーが編集不可ならば
		if (!entityPlayer.canPlayerEdit(x, y, z, side, itemStack)) {
			// falseを返す
			return false;
		} else {
			// eventの登録
			UseHoeEvent event = new UseHoeEvent(entityPlayer, itemStack, world, x, y, z);
			if (MinecraftForge.EVENT_BUS.post(event)) {
				return false;
			}

			if (event.getResult() == Result.ALLOW) {
				// ダメージを与える
				itemStack.damageItem(1, entityPlayer);
				return true;
			}

			// 右クリックされたブロックを取得する
			Block block = world.getBlock(x, y, z);

			// 右クリックされたブロックの上が空気ブロックで、右クリックされたブロックが草ブロックか土ブロックならば
			if (side != 0 && world.getBlock(x, y + 1, z).isAir(world, x, y + 1, z) && (block == Blocks.grass || block == Blocks.dirt)) {
				Block block1 = Blocks.farmland;
				// 音を鳴らす
				world.playSoundEffect(x + 0.5F, y + 0.5F, z + 0.5F, block1.stepSound.getStepResourcePath(), (block1.stepSound.getVolume() + 1.0F) / 2.0F, block1.stepSound.getPitch() * 0.8F);

				// クライアント側では何もせず
				if (world.isRemote) {
					return true;
					// サーバー側では
				} else {
					// ブロックを置き換えて
					world.setBlock(x, y, z, block1);
					// ダメージを与える
					itemStack.damageItem(1, entityPlayer);
					return true;
				}
			} else {

				return false;
			}
		}
	}

	// 剣の処理
	/** Entityを叩いたときの処理。ItemToolでは2のダメージをアイテムに与えるが、剣と同じように1与えるようにする。 */
	@Override
	public boolean hitEntity(ItemStack itemStack, EntityLivingBase target, EntityLivingBase player) {
		itemStack.damageItem(1, player);
		return true;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack itemStack) {
		return 72000;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		player.setItemInUse(itemStack, this.getMaxItemUseDuration(itemStack));
		player.openGui(OEFCore.instance, 99, world, (int) player.posX, (int) player.posY, (int) player.posZ);

		return itemStack;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack itemstack, World world, Block block, int ex, int ey, int ez, EntityLivingBase player) {
		int count = 0;

		{
			switch (Minecraft.getMinecraft().objectMouseOver.sideHit) {
			/* 底面 */
			case 0: {
				for (int y = 0;; y++) {
					if (world.getBlock(ex, ey + y, ez) == block && world.getBlock(ex, ey + y, ez).getBlockHardness(world, ex, ey + y, ez) > 0) {
						this.gatherItem(itemstack, world.getBlock(ex, ey + y, ez).getItemDropped(world.getBlockMetadata(ex, ey + y, ez), itemRand, 0), world.getBlock(ex, ey + y, ez).damageDropped(world.getBlockMetadata(ex, ey + y, ez)), player);
						if (!world.isRemote)
							world.setBlockToAir(ex, ey + y, ez);
						count++;
						itemstack.damageItem(1, player);
						if (count > limit) {
							break;
						}
					} else {
						break;
					}
				}
			}
				/* 東 */
			case 4: {
				for (int x = 1;; x++) {
					if (world.getBlock(ex + x, ey, ez) == block && world.getBlock(ex + x, ey, ez).getBlockHardness(world, ex + x, ey, ez) > 0) {
						this.gatherItem(itemstack, world.getBlock(ex + x, ey, ez).getItemDropped(world.getBlockMetadata(ex + x, ey, ez), itemRand, 0), world.getBlock(ex + x, ey, ez).damageDropped(world.getBlockMetadata(ex + x, ey, ez)), player);
						if (!world.isRemote)
							world.setBlockToAir(ex + x, ey, ez);
						count++;
						itemstack.damageItem(1, player);
						if (count > limit) {
							break;
						}
					} else {
						break;
					}
				}
			}
				/* 西 */
			case 5: {
				for (int x = -1;; x--) {
					if (world.getBlock(ex + x, ey, ez) == block && world.getBlock(ex + x, ey, ez).getBlockHardness(world, ex + x, ey, ez) > 0) {
						this.gatherItem(itemstack, world.getBlock(ex + x, ey, ez).getItemDropped(world.getBlockMetadata(ex + x, ey, ez), itemRand, 0), world.getBlock(ex + x, ey, ez).damageDropped(world.getBlockMetadata(ex + x, ey, ez)), player);
						if (!world.isRemote)
							world.setBlockToAir(ex + x, ey, ez);
						count++;
						itemstack.damageItem(1, player);
						if (count > limit) {
							break;
						}
					} else {
						break;
					}
				}
			}
				/* 北 */
			case 3: {
				for (int z = -1;; z--) {
					if (world.getBlock(ex, ey, ez + z) == block && world.getBlock(ex, ey, ez + z).getBlockHardness(world, ex, ey, ez + z) > 0) {
						this.gatherItem(itemstack, world.getBlock(ex, ey, ez + z).getItemDropped(world.getBlockMetadata(ex, ey, ez + z), itemRand, 0), world.getBlock(ex, ey, ez + z).damageDropped(world.getBlockMetadata(ex, ey, ez + z)), player);
						if (!world.isRemote)
							world.setBlockToAir(ex, ey, ez + z);
						count++;
						itemstack.damageItem(1, player);
						if (count > limit) {
							break;
						}
					} else {
						break;
					}
				}

			}
				/* 南 */
			case 2: {
				for (int z = 1;; z++) {
					if (world.getBlock(ex, ey, ez + z) == block && world.getBlock(ex, ey, ez + z).getBlockHardness(world, ex, ey, ez + z) > 0) {
						this.gatherItem(itemstack, world.getBlock(ex, ey, ez + z).getItemDropped(world.getBlockMetadata(ex, ey, ez + z), itemRand, 0), world.getBlock(ex, ey, ez + z).damageDropped(world.getBlockMetadata(ex, ey, ez + z)), player);
						if (!world.isRemote)
							world.setBlockToAir(ex, ey, ez + z);
						count++;
						itemstack.damageItem(1, player);
						if (count > limit) {
							break;
						}
					} else {
						break;
					}
				}

			}
			}
		}
		return super.onBlockDestroyed(itemstack, world, block, ex, ey, ez, player);
	}

	public void gatherItem(ItemStack itemstack, Item item, int meta, EntityLivingBase player) {

		if (player instanceof EntityPlayer) {
			if (!itemstack.hasTagCompound()) {
				itemstack.setTagCompound(new NBTTagCompound());
				itemstack.getTagCompound().setTag("Items", new NBTTagList());
			}
			NBTTagList tags = (NBTTagList) itemstack.getTagCompound().getTag("Items");

			for (int i = 0; i < tags.tagCount(); i++) {
				NBTTagCompound tagCompound = tags.getCompoundTagAt(i);
				int slot = tagCompound.getByte("Slot");
				if (slot >= 0 && slot < 54 && tagCompound != null) {
					items[slot] = ItemStack.loadItemStackFromNBT(tagCompound);
				}
			}
			NBTTagList tagList = (NBTTagList) itemstack.getTagCompound().getTag("Items");
			ItemStack save = new ItemStack(item, 1, meta);
			NBTTagCompound compound = new NBTTagCompound();
			int saver = -1;
			for (int i = 0; i < 54; i++) {
				if (items[i] == null || (items[i] != null && (items[i].getItem() == null || items[i].stackSize == 0))) {
					compound.setByte("Slot", (byte) i);
					save.writeToNBT(compound);
					tagList.appendTag(compound);
					break;
				} else if (items[i] != null && items[i].getItem() == item && items[i].stackSize < items[i].getMaxStackSize()) {
					ItemStack stack = items[i];
					save = new ItemStack(item, stack.stackSize + 1, meta);
					compound.setByte("Slot", (byte) i);
					save.writeToNBT(compound);
					tagList.appendTag(compound);
					break;
				}
			}
			itemstack.getTagCompound().setTag("Items", tagList);
		}
	}

	/**
	 * Gets a map of item attribute modifiers, used by ItemSword to increase hit damage.
	 */
	@Override
	public Multimap getItemAttributeModifiers() {
		return HashMultimap.create();
	}

}
