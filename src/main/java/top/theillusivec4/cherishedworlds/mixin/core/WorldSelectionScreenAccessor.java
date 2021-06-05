package top.theillusivec4.cherishedworlds.mixin.core;

import net.minecraft.client.gui.screen.WorldSelectionList;
import net.minecraft.client.gui.screen.WorldSelectionScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WorldSelectionScreen.class)
public interface WorldSelectionScreenAccessor {

  @Accessor
  Button getDeleteButton();

  @Accessor
  TextFieldWidget getSearchField();

  @Accessor
  WorldSelectionList getSelectionList();
}
