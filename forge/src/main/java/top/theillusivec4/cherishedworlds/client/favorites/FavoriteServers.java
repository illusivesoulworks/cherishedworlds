package top.theillusivec4.cherishedworlds.client.favorites;

import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.client.event.GuiScreenEvent;
import top.theillusivec4.cherishedworlds.mixin.core.MultiplayerScreenAccessor;

public class FavoriteServers implements IFavoritesManager<JoinMultiplayerScreen> {

  @Override
  public void init(JoinMultiplayerScreen screen) {
    MultiplayerScreenAccessor accessor = (MultiplayerScreenAccessor) screen;
    ServerSelectionList selectionList = accessor.getSelectionList();

    if (selectionList != null) {
      selectionList.updateOnlineServers(screen.getServers());
    }
  }

  @Override
  public void draw(GuiScreenEvent.DrawScreenEvent.Post evt, JoinMultiplayerScreen screen) {
    MultiplayerScreenAccessor accessor = (MultiplayerScreenAccessor) screen;
    ServerSelectionList selectionList = accessor.getSelectionList();

    if (selectionList != null) {

      for (int i = 0; i < selectionList.children().size(); i++) {
        ServerSelectionList.Entry entry = selectionList.children().get(i);

        if (entry instanceof ServerSelectionList.OnlineServerEntry) {
          ServerData serverData = ((ServerSelectionList.OnlineServerEntry) entry).getServerData();
          boolean isFavorite =
              FavoritesList.contains(serverData.name + serverData.ip);
          drawIcon(evt, screen, i, isFavorite, selectionList.getTop(),
              selectionList.getScrollAmount(), selectionList.getBottom());
        }
      }
    }
  }

  @Override
  public void click(GuiScreenEvent.MouseClickedEvent.Pre evt, JoinMultiplayerScreen screen) {
    MultiplayerScreenAccessor accessor = (MultiplayerScreenAccessor) screen;
    ServerSelectionList selectionList = accessor.getSelectionList();

    if (selectionList != null) {

      for (int i = 0; i < selectionList.children().size(); i++) {
        ServerSelectionList.Entry entry = selectionList.children().get(i);

        if (entry instanceof ServerSelectionList.OnlineServerEntry) {
          ServerData serverData = ((ServerSelectionList.OnlineServerEntry) entry).getServerData();
          boolean isFavorite = FavoritesList.contains(serverData.name + serverData.ip);
          int top = (int) (selectionList.getTop() + 15 + 36 * i - selectionList
              .getScrollAmount());
          int x = evt.getGui().width / 2 - getOffset();
          double mouseX = evt.getMouseX();
          double mouseY = evt.getMouseY();

          if (mouseY >= top && mouseY <= (top + 9) && mouseX >= x && mouseX <= (x + 9)) {
            String s = serverData.name + serverData.ip;

            if (isFavorite) {
              FavoritesList.remove(s);
            } else {
              FavoritesList.add(s);
            }
            FavoritesList.save();
            accessor.getSelectionList().updateOnlineServers(screen.getServers());
            ServerSelectionList.Entry selected = accessor.getSelectionList().getSelected();

            if (selected instanceof ServerSelectionList.OnlineServerEntry) {
              disableDeletion((ServerSelectionList.OnlineServerEntry) selected,
                  accessor.getDeleteButton());
            }
            return;
          }
        }
      }
    }
  }

  @Override
  public void clicked(JoinMultiplayerScreen screen) {
    // NO-OP
  }

  @Override
  public int getOffset() {
    return 168;
  }

  private static void disableDeletion(ServerSelectionList.OnlineServerEntry entry, Button deleteButton) {
    ServerData serverData = entry.getServerData();
    deleteButton.active = !FavoritesList.contains(serverData.name + serverData.ip);
  }
}
