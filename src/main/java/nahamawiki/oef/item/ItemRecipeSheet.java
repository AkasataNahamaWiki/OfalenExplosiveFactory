package nahamawiki.oef.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemRecipeSheet extends ItemOEFBase {

	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if (player.isSneaking() && itemStack.hasTagCompound()) {
			// スニークしていたら、記録していたレシピを無効化。
			NBTTagCompound nbt = itemStack.getTagCompound();
			nbt.setBoolean("IsWrited", false);
			itemStack.setTagCompound(nbt);
			return true;
		}
		return false;
	}

	/** アイテムの説明文を設定する処理。 */
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean flag) {
		if (!itemStack.hasTagCompound())
			return;
		// 有効で、レシピが登録されているなら、完成品の名前を表示。
		NBTTagCompound nbt = itemStack.getTagCompound();
		if (!nbt.getBoolean("IsWrited") || !nbt.hasKey("SampleResult"))
			return;
		NBTTagCompound nbt1 = (NBTTagCompound) nbt.getTag("SampleResult");
		ItemStack result = ItemStack.loadItemStackFromNBT(nbt1);
		list.add(StatCollector.translateToLocal(result.getUnlocalizedName() + ".name"));
	}

}
