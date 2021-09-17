package top.theillusivec4.cherishedworlds.mixin;

import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.network.ServerInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MultiplayerServerListWidget.ServerEntry.class)
public interface AccessorMultiplayerServerListWidgetEntry {

  @Invoker("<init>")
  static MultiplayerServerListWidget.ServerEntry cherishedworlds$createEntry(
      MultiplayerServerListWidget multiplayerServerListWidget, MultiplayerScreen multiplayerScreen,
      ServerInfo serverInfo) {
    throw new IllegalStateException("Untransformed accessor!");
  }
}
