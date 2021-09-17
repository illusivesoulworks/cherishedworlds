package top.theillusivec4.cherishedworlds.mixin.core;

import net.minecraft.client.gui.screens.worldselection.WorldSelectionList;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Button;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SelectWorldScreen.class)
public interface WorldSelectionScreenAccessor {

  @Accessor
  Button getDeleteButton();

  @Accessor
  EditBox getSearchBox();

  @Accessor
  WorldSelectionList getList();
}
