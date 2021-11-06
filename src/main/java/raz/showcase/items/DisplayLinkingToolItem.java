package raz.showcase.items;

import com.google.common.collect.Maps;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import raz.showcase.blocks.curatorsworkbench.CuratorsWorkbenchBlock;
import raz.showcase.blocks.curatorsworkbench.CuratorsWorkbenchTileEntity;
import raz.showcase.blocks.displaycase.DisplayCaseBlock;
import raz.showcase.blocks.displaycase.DisplayCaseTileEntity;
import raz.showcase.setup.ModSetup;
import raz.showcase.setup.Registration;

import java.util.Map;

public class DisplayLinkingToolItem extends Item {

    public static final BlockPos defaultPos = new BlockPos(0,-2000,0);
    public BlockPos storedWorkbenchPosition = defaultPos;

    public DisplayLinkingToolItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        return new ItemStack(this.getItem());
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();
        BlockPos pos = context.getPos();

        if (!world.isRemote) {
            BlockState blockState = world.getBlockState(pos);

            if (player.isSneaking()) {
                if (blockState.getBlock() instanceof CuratorsWorkbenchBlock) {
                    if (!player.getHeldItemMainhand().isEnchanted()) {
                        this.storedWorkbenchPosition = pos;

                        writePosToNBT(player.getHeldItemMainhand());

                        if (player.getHeldItemMainhand().getItem() instanceof DisplayLinkingToolItem) {
                            player.getHeldItemMainhand().addEnchantment(Enchantments.UNBREAKING,-1);
                        }
                        player.sendMessage(new StringTextComponent("Linked workbench at " + pos), Util.DUMMY_UUID);

                    }
                }
                else if (blockState.getBlock() instanceof DisplayCaseBlock) {
                    TileEntity tileEntity = world.getTileEntity(pos);
                    if (tileEntity instanceof DisplayCaseTileEntity) {
                        // If this tool is bound to a CuratorsWorkbench
                        if (player.getHeldItemMainhand().hasTag() && player.getHeldItemMainhand().isEnchanted()) {
                            tileEntity = world.getTileEntity(getStoredPosition(player.getHeldItemMainhand()));
                            if (tileEntity instanceof CuratorsWorkbenchTileEntity) {
                                CuratorsWorkbenchTileEntity workbenchTileEntity = (CuratorsWorkbenchTileEntity) tileEntity;
                                // Has the DisplayCase already been linked to this workbench?
                                if (!workbenchTileEntity.isAlreadyLinked(pos)) {
                                    workbenchTileEntity.storeNewLocation(pos);
                                    world.notifyBlockUpdate(pos,world.getBlockState(pos),world.getBlockState(pos),2);
                                    player.sendMessage(new StringTextComponent("Linked display case at " + pos), Util.DUMMY_UUID);
                                }
                                else {
                                    workbenchTileEntity.removeLocation(pos);
                                    player.sendMessage(new StringTextComponent("Removed display case at " + pos), Util.DUMMY_UUID);

                                }
                            }
                            else {
                                storedWorkbenchPosition = defaultPos;
                                writePosToNBT(player.getHeldItemMainhand());

                                if(player.getHeldItemMainhand().getItem() instanceof DisplayLinkingToolItem)
                                {
                                    if(player.getHeldItemMainhand().isEnchanted())
                                    {
                                        Map<Enchantment, Integer> enchantsNone = Maps.<Enchantment, Integer>newLinkedHashMap();
                                        EnchantmentHelper.setEnchantments(enchantsNone,player.getHeldItemMainhand());
                                    }
                                }
                                player.sendMessage(new StringTextComponent("Removed workbench link at " + pos), Util.DUMMY_UUID);
                            }
                        }
                    }
                }
            }
        }
        return super.onItemUse(context);
    }

    public BlockPos getStoredPosition(ItemStack getWrenchItem)
    {
        getPosFromNBT(getWrenchItem);
        return storedWorkbenchPosition;
    }

    public void writePosToNBT(ItemStack stack)
    {
        CompoundNBT compound = new CompoundNBT();
        if(stack.hasTag())
        {
            compound = stack.getTag();
        }
        compound.putInt("stored_x",this.storedWorkbenchPosition.getX());
        compound.putInt("stored_y",this.storedWorkbenchPosition.getY());
        compound.putInt("stored_z",this.storedWorkbenchPosition.getZ());
        stack.setTag(compound);
    }

    public void getPosFromNBT(ItemStack stack)
    {
        if(stack.hasTag())
        {
            CompoundNBT getCompound = stack.getTag();
            int x = getCompound.getInt("stored_x");
            int y = getCompound.getInt("stored_y");
            int z = getCompound.getInt("stored_z");
            this.storedWorkbenchPosition = new BlockPos(x,y,z);
        }
    }
}
