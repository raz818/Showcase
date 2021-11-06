package raz.showcase.datagen;

import net.minecraft.data.DataGenerator;
import raz.showcase.setup.Registration;

public class LootTables extends BaseLootTableProvider {
    public LootTables(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    protected void addTables() {
        lootTables.put(Registration.DISPLAY_CASE.get(), createStandardTable("display_case", Registration.DISPLAY_CASE.get()));
    }
}
