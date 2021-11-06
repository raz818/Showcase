package raz.showcase.blocks.wallmount;

import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.tags.ItemTags;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.ToolType;
import raz.showcase.blocks.ItemDisplayTile;
import raz.showcase.setup.Registration;

import java.util.Arrays;
import java.util.Set;

public class WallMountTileEntity extends ItemDisplayTile {
    public WallMountTileEntity() {
        super(Registration.WALL_MOUNT_TILE_ENTITY.get());
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        boolean isCorrectToolType = stack.getItem() instanceof ToolItem || stack.getItem() instanceof SwordItem;
        if (stack.getItem() instanceof ToolItem || stack.getItem() instanceof SwordItem)
            return super.isItemValidForSlot(index, stack);
        else
            return false;
    }
}
