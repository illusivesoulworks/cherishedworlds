package top.theillusivec4.cherishedworlds.mixin.core;

import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.client.gui.screen.ServerSelectionList;
import net.minecraft.client.multiplayer.ServerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ServerSelectionList.NormalEntry.class)
public interface ServerSelectionListNormalEntryAccessor {

  @Invoker("<init>")
  static ServerSelectionList.NormalEntry cherishedworlds$createEntry(
      ServerSelectionList serverSelectionList, MultiplayerScreen multiplayerScreen,
      ServerData serverData) {
    throw new IllegalStateException("Untransformed accessor!");
  }
}
