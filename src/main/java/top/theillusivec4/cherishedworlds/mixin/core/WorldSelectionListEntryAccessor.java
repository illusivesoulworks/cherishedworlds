package top.theillusivec4.cherishedworlds.mixin.core;

import net.minecraft.client.gui.screen.WorldSelectionList;
import net.minecraft.world.storage.WorldSummary;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WorldSelectionList.Entry.class)
public interface WorldSelectionListEntryAccessor {

  @Accessor(value = "field_214451_d")
  WorldSummary getWorldSummary();
}
