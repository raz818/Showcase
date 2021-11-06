package raz.showcase.blocks.displaycase;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.*;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import raz.showcase.Showcase;
import raz.showcase.blocks.ItemDisplayTile;
import raz.showcase.items.MobTrophyItem;
import raz.showcase.setup.Registration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Function;

public class DisplayCaseTileEntity extends ItemDisplayTile {
    private Entity cachedEntity;


    public DisplayCaseTileEntity() {
        super(Registration.DISPLAY_CASE_TILE_ENTITY.get(), 2);
    }

    public boolean displayItemIsMobTrophy() {
        return !this.isEmpty() && this.getDisplayItem().getItem() instanceof MobTrophyItem;
    }

    @Override
    public ActionResultType handleInteract(PlayerEntity player, Hand handIn) {
        ItemStack heldItem = player.getHeldItem(handIn);
        if (heldItem.isEmpty() && player.isSneaking()) {
            if (player instanceof ServerPlayerEntity) {
                player.openContainer(this);
            }
            return ActionResultType.func_233537_a_(this.world.isRemote);
        }
        return super.handleInteract(player, handIn);
    }

    public boolean hasFilterItem() {
        return !this.getFilterItem().isEmpty();
    }

    public ItemStack getFilterItem() {
        return this.getStackInSlot(1);
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("screen.showcase.display_case");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new DisplayCaseContainer(id, player, this);
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (index == 0 && hasFilterItem()) {
            return getFilterItem().getItem().equals(stack.getItem());
        }
        return super.isItemValidForSlot(index, stack);
    }

    public Entity getCachedEntity() {
        if (this.cachedEntity == null) {
            if (displayItemIsMobTrophy()) {
                MobTrophyItem mobTrophy = (MobTrophyItem)getDisplayItem().getItem();
                this.cachedEntity = mobTrophy.getType(null).create(this.getWorld());

            }
        }

        return this.cachedEntity;
    }
}
