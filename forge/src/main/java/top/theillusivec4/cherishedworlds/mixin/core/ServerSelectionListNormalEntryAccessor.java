package top.theillusivec4.cherishedworlds.mixin.core;

import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import net.minecraft.client.multiplayer.ServerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ServerSelectionList.OnlineServerEntry.class)
public interface ServerSelectionListNormalEntryAccessor {

  @Invoker("<init>")
  static ServerSelectionList.OnlineServerEntry cherishedworlds$createEntry(
      ServerSelectionList serverSelectionList, JoinMultiplayerScreen multiplayerScreen,
      ServerData serverData) {
    throw new IllegalStateException("Untransformed accessor!");
  }
}
