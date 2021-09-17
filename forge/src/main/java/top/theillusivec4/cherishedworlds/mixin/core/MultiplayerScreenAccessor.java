package top.theillusivec4.cherishedworlds.mixin.core;

import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.client.gui.screen.ServerSelectionList;
import net.minecraft.client.gui.widget.button.Button;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MultiplayerScreen.class)
public interface MultiplayerScreenAccessor {

  @Accessor(value = "serverListSelector")
  ServerSelectionList getSelectionList();

  @Accessor(value = "btnDeleteServer")
  Button getDeleteButton();
}
