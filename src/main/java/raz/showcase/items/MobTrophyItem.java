package raz.showcase.items;

import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nullable;

public class MobTrophyItem extends Item {
    private final EntityType<?> typeIn;

    public MobTrophyItem(EntityType<?> typeIn, Properties properties) {
        super(properties);
        this.typeIn = typeIn;
    }

    public EntityType<?> getType(@Nullable CompoundNBT nbt) {
        if (nbt != null && nbt.contains("EntityTag", 10)) {
            CompoundNBT compoundnbt = nbt.getCompound("EntityTag");
            if (compoundnbt.contains("id", 8)) {
                return EntityType.byKey(compoundnbt.getString("id")).orElse(this.typeIn);
            }
        }

        return this.typeIn;
    }
}
