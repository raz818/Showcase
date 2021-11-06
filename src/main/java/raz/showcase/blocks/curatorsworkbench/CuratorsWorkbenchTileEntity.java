package raz.showcase.blocks.curatorsworkbench;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import raz.showcase.blocks.displaycase.DisplayCaseBlock;
import raz.showcase.blocks.displaycase.DisplayCaseTileEntity;
import raz.showcase.setup.Registration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CuratorsWorkbenchTileEntity extends TileEntity {

    private ItemStackHandler itemHandler = createHandler();

    private LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);

    private final List<BlockPos> storedLocations = new ArrayList<BlockPos>();

    protected CuratorsWorkbenchTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public CuratorsWorkbenchTileEntity() {
        this(Registration.CURATORS_WORKBENCH_TILE_ENTITY.get());
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(27) {
            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
            }

            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }
        };
    }

    public int getNumberOfStoredLocations() {return storedLocations.size();}

    public void storeNewLocation(BlockPos pos)
    {
        storedLocations.add(pos);
    }

    public BlockPos getStoredPositionAt(int index)
    {
        BlockPos sendToPos = getPos();
        if(index<getNumberOfStoredLocations())
        {
            sendToPos = storedLocations.get(index);
        }

        return sendToPos;
    }

    public boolean removeLocation(BlockPos pos)
    {
        boolean returner = false;
        if(getNumberOfStoredLocations() >= 1)
        {
            storedLocations.remove(pos);
            returner=true;
        }
        this.markDirty();
        this.world.notifyBlockUpdate(pos,getBlockState(),getBlockState(),2);

        return returner;
    }

    public boolean isAlreadyLinked(BlockPos pos) {
        return storedLocations.contains(pos);
    }

    public List<BlockPos> getLocationList()
    {
        return storedLocations;
    }

    public boolean canSendItemsToDisplayCase(BlockPos posReceiver, ItemStack itemToSend) {
        boolean returner = false;

        if (world.isAreaLoaded(posReceiver, 1)) {
            if (world.getBlockState(posReceiver).getBlock() instanceof DisplayCaseBlock) {
                if (world.getTileEntity(posReceiver) instanceof DisplayCaseTileEntity) {
                    DisplayCaseTileEntity displayCaseTileEntity = (DisplayCaseTileEntity) world.getTileEntity(posReceiver);
                    //returner = displayCaseTileEntity.canAcceptItem(itemToSend);
                }
            }
            else {
                removeLocation(posReceiver);
            }
        }
        return returner;
    }

    public void transferItemsToDisplayCases() {
        int numLocations = getNumberOfStoredLocations();
        if (numLocations > 0) {
            for (int i=0; i < numLocations; i++) {
                sendItemsToDisplayCase(getStoredPositionAt(i));
            }
        }
    }

    public void sendItemsToDisplayCase(BlockPos displayCasePos) {
        if (!(world.getTileEntity(displayCasePos) instanceof DisplayCaseTileEntity)) {
            return;
        }
        DisplayCaseTileEntity displayCaseTileEntity = (DisplayCaseTileEntity) world.getTileEntity(displayCasePos);
        IItemHandler h = handler.orElse(null);
        for (int i = 0; i < h.getSlots(); i++) {
            if (canSendItemsToDisplayCase(displayCasePos, h.extractItem(i, 1, true))) {
                displayCaseTileEntity.setDisplayItem(h.extractItem(i, 1, false));
            }
        }
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inv"));

        int[] storedIX = nbt.getIntArray("intArrayXPos");
        int[] storedIY = nbt.getIntArray("intArrayYPos");
        int[] storedIZ = nbt.getIntArray("intArrayZPos");

        for(int i=0;i<storedIX.length;i++)
        {
            BlockPos gotPos = new BlockPos(storedIX[i],storedIY[i],storedIZ[i]);
            storedLocations.add(gotPos);
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.put("inv", itemHandler.serializeNBT());

        List<Integer> storedX = new ArrayList<Integer>();
        List<Integer> storedY = new ArrayList<Integer>();
        List<Integer> storedZ = new ArrayList<Integer>();

        for(int i=0;i<getNumberOfStoredLocations();i++)
        {
            storedX.add(storedLocations.get(i).getX());
            storedY.add(storedLocations.get(i).getY());
            storedZ.add(storedLocations.get(i).getZ());
        }

        tag.putIntArray("intArrayXPos",storedX);
        tag.putIntArray("intArrayYPos",storedY);
        tag.putIntArray("intArrayZPos",storedZ);


        return super.write(tag);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT nbtTagCompound = new CompoundNBT();
        write(nbtTagCompound);
        int tileEntityType = 42;  // arbitrary number; only used for vanilla TileEntities.  You can use it, or not, as you want.
        return new SUpdateTileEntityPacket(this.pos, tileEntityType, nbtTagCompound);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return write(new CompoundNBT());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        super.onDataPacket(net, pkt);
        BlockState state = this.world.getBlockState(this.pos);
        this.handleUpdateTag(state,pkt.getNbtCompound());
        this.world.notifyBlockUpdate(this.pos, state, state, 3);
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag)
    {
        this.read(state, tag);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
            return handler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void remove() {
        super.remove();
        handler.invalidate();
    }
}
