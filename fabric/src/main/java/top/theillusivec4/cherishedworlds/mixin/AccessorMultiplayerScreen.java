package top.theillusivec4.cherishedworlds.mixin;

import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MultiplayerScreen.class)
public interface AccessorMultiplayerScreen {

  @Accessor
  MultiplayerServerListWidget getServerListWidget();

  @Accessor(value = "buttonDelete")
  ButtonWidget getDeleteButton();
}
