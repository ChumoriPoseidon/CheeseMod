package cmpsd.cheesemod;

import java.util.Random;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class ModTab {

	public static CreativeTabs tabCheeseMod;

	public static void init() {

		tabCheeseMod = new CreativeTabs("CheeseMod") {

			@Override
			public ItemStack getTabIconItem() {

				Random random = new Random();
				return new ItemStack(ModItem.ITEMS.get(random.nextInt(ModItem.ITEMS.size())));
			}
		};
	}
}
