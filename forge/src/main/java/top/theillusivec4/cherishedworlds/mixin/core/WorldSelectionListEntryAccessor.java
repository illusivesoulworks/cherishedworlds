package top.theillusivec4.cherishedworlds.mixin.core;

import net.minecraft.client.gui.screens.worldselection.WorldSelectionList;
import net.minecraft.world.level.storage.LevelSummary;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WorldSelectionList.WorldListEntry.class)
public interface WorldSelectionListEntryAccessor {

  @Accessor(value = "summary")
  LevelSummary getWorldSummary();
}
