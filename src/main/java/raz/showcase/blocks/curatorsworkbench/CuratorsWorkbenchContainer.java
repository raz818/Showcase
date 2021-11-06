package raz.showcase.blocks.curatorsworkbench;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import raz.showcase.setup.Registration;

import javax.annotation.Nullable;

public class CuratorsWorkbenchContainer extends Container {

    private CuratorsWorkbenchTileEntity tileEntity;
    private PlayerEntity playerEntity;

    public CuratorsWorkbenchContainer(final int windowId, World world, BlockPos pos, PlayerInventory playerInv, PlayerEntity player) {
        super(Registration.CURATORS_WORKBENCH_CONTAINER.get(), windowId);
        if (world.getTileEntity(pos) instanceof CuratorsWorkbenchTileEntity) {
            tileEntity = (CuratorsWorkbenchTileEntity) world.getTileEntity(pos);
        }
        this.playerEntity = player;

        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                for(int j = 0; j < 3; ++j) {
                    for(int k = 0; k < 9; ++k) {
                        addSlot(new SlotItemHandler(h, k + j * 9, 8 + k * 18, 18 + j * 18));
                    }
                }
            });
        }

        // Main Player Inventory
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 166 - (4 - row) * 18 - 10));
            }
        }

        // Player Hotbar
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInv, col, 8 + col * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos()), playerEntity, Registration.CURATORS_WORKBENCH.get());
    }

    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        tileEntity.transferItemsToDisplayCases();
    }
}
