package cmpsd.cheesemod.handler;

import cmpsd.cheesemod.container.Container_Barrel;
import cmpsd.cheesemod.gui.Gui_Barrel;
import cmpsd.cheesemod.tileentity.TileEntity_Barrel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	public static final int guiID_Barrel = 0;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
		if(tileEntity != null) {

			switch(ID) {
			case guiID_Barrel:

				return new Container_Barrel(player.inventory, (TileEntity_Barrel)tileEntity);
			}
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
		if(tileEntity != null) {

			switch(ID) {
			case guiID_Barrel:

				return new Gui_Barrel(player.inventory, (TileEntity_Barrel)tileEntity);
			}
		}
		return null;
	}
}
