package raz.showcase.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nullable;
import java.util.stream.IntStream;

public abstract class ItemDisplayTile extends LockableLootTileEntity implements ISidedInventory {
    protected NonNullList<ItemStack> stacks;

    public ItemDisplayTile(TileEntityType<?> typeIn) {
        this(typeIn,1);
    }

    public ItemDisplayTile(TileEntityType<?> typeIn, int slots) {
        super(typeIn);
        this.stacks = NonNullList.withSize(slots, ItemStack.EMPTY);
    }

    @Override
    public void markDirty() {
        if (this.world == null) return;
        this.updateOnDirtyBeforePacket();
        super.markDirty();
    }

    public void updateOnDirtyBeforePacket() {
        this.world.notifyBlockUpdate(this.pos, this.getBlockState(), this.getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
    }

    public ItemStack getDisplayItem() {
        return this.getStackInSlot(0);
    }

    public void setDisplayItem(ItemStack itemStack) {
        this.setInventorySlotContents(0, itemStack);
    }

    public void updateClientVisualsOnLoad(){}

    public ActionResultType handleInteract(PlayerEntity player, Hand handIn){
        return this.handleInteract(player, handIn, 0);
    }

    public ActionResultType handleInteract(PlayerEntity player, Hand handIn, int slot){
        if(handIn==Hand.MAIN_HAND) {
            ItemStack handItem = player.getHeldItem(handIn);
            //remove
            if (!this.isEmpty() && handItem.isEmpty()) {
                ItemStack it = this.removeStackFromSlot(slot);

                if (!this.world.isRemote) {
                    player.setHeldItem(handIn, it);
                    this.markDirty();
                }
                else{
                    //also update visuals on client. will get overwritten by packet tho
                    this.updateClientVisualsOnLoad();
                }
                return ActionResultType.func_233537_a_(this.world.isRemote);
            }
            //place
            else if (!handItem.isEmpty() && this.isItemValidForSlot(slot, handItem)) {
                ItemStack it = handItem.copy();
                it.setCount(1);
                this.setInventorySlotContents(slot,it);

                if (!player.isCreative()) {
                    handItem.shrink(1);
                }
                if (!this.world.isRemote) {
                    this.world.playSound(null, this.pos, SoundEvents.ENTITY_ITEM_FRAME_ADD_ITEM, SoundCategory.BLOCKS, 1.0F, this.world.getRandom().nextFloat() * 0.10F + 0.95F);
                }
                else{
                    //also update visuals on client. will get overwritten by packet tho
                    this.updateClientVisualsOnLoad();
                }
                return ActionResultType.func_233537_a_(this.world.isRemote);
            }
        }
        return ActionResultType.PASS;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return IntStream.range(0, this.getSizeInventory()).toArray();
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return false;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return false;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.stacks;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> itemsIn) {
        this.stacks = itemsIn;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return this.isEmpty();
    }

    @Override
    protected ITextComponent getDefaultName() {
        return null;
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return ChestContainer.createGeneric9X3(id, player, this);
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public int getSizeInventory() {
        return stacks.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : this.stacks)
            if (!itemstack.isEmpty())
                return false;
        return true;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        if (!this.checkLootAndRead(nbt)) {
            this.stacks = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        }
        ItemStackHelper.loadAllItems(nbt, this.stacks);
        if (this.world != null && !this.world.isRemote) this.updateClientVisualsOnLoad();
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        if (!this.checkLootAndWrite(compound)) {
            ItemStackHelper.saveAllItems(compound, this.stacks);
        }
        return compound;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, 0, this.getUpdateTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.read(this.getBlockState(), pkt.getNbtCompound());
    }

    private final LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this, Direction.values());
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (!this.removed && side != null && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return handlers[side.ordinal()].cast();
        return super.getCapability(cap, side);
    }

    @Override
    protected void invalidateCaps() {
        super.invalidateCaps();
        for (LazyOptional<? extends IItemHandler> handler : handlers)
            handler.invalidate();
    }
}
