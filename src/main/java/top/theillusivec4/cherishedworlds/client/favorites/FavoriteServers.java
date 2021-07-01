package top.theillusivec4.cherishedworlds.client.favorites;

import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.client.gui.screen.ServerSelectionList;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.client.event.GuiScreenEvent;
import top.theillusivec4.cherishedworlds.mixin.core.MultiplayerScreenAccessor;

public class FavoriteServers implements IFavoritesManager<MultiplayerScreen> {

  @Override
  public void init(MultiplayerScreen screen) {
    MultiplayerScreenAccessor accessor = (MultiplayerScreenAccessor) screen;
    ServerSelectionList selectionList = accessor.getSelectionList();

    if (selectionList != null) {
      selectionList.updateOnlineServers(screen.getServerList());
    }
  }

  @Override
  public void draw(GuiScreenEvent.DrawScreenEvent.Post evt, MultiplayerScreen screen) {
    MultiplayerScreenAccessor accessor = (MultiplayerScreenAccessor) screen;
    ServerSelectionList selectionList = accessor.getSelectionList();

    if (selectionList != null) {

      for (int i = 0; i < selectionList.getEventListeners().size(); i++) {
        ServerSelectionList.Entry entry = selectionList.getEventListeners().get(i);

        if (entry instanceof ServerSelectionList.NormalEntry) {
          ServerData serverData = ((ServerSelectionList.NormalEntry) entry).getServerData();
          boolean isFavorite =
              FavoritesList.contains(serverData.serverName + serverData.serverIP);
          drawIcon(evt, screen, i, isFavorite, selectionList.getTop(),
              selectionList.getScrollAmount(), selectionList.getBottom());
        }
      }
    }
  }

  @Override
  public void click(GuiScreenEvent.MouseClickedEvent.Pre evt, MultiplayerScreen screen) {
    MultiplayerScreenAccessor accessor = (MultiplayerScreenAccessor) screen;
    ServerSelectionList selectionList = accessor.getSelectionList();

    if (selectionList != null) {

      for (int i = 0; i < selectionList.getEventListeners().size(); i++) {
        ServerSelectionList.Entry entry = selectionList.getEventListeners().get(i);

        if (entry instanceof ServerSelectionList.NormalEntry) {
          ServerData serverData = ((ServerSelectionList.NormalEntry) entry).getServerData();
          boolean isFavorite = FavoritesList.contains(serverData.serverName + serverData.serverIP);
          int top = (int) (selectionList.getTop() + 15 + 36 * i - selectionList
              .getScrollAmount());
          int x = evt.getGui().width / 2 - getOffset();
          double mouseX = evt.getMouseX();
          double mouseY = evt.getMouseY();

          if (mouseY >= top && mouseY <= (top + 9) && mouseX >= x && mouseX <= (x + 9)) {
            String s = serverData.serverName + serverData.serverIP;

            if (isFavorite) {
              FavoritesList.remove(s);
            } else {
              FavoritesList.add(s);
            }
            FavoritesList.save();
            accessor.getSelectionList().updateOnlineServers(screen.getServerList());
            ServerSelectionList.Entry selected = accessor.getSelectionList().getSelected();

            if (selected instanceof ServerSelectionList.NormalEntry) {
              disableDeletion((ServerSelectionList.NormalEntry) selected,
                  accessor.getDeleteButton());
            }
            return;
          }
        }
      }
    }
  }

  @Override
  public void clicked(MultiplayerScreen screen) {
    // NO-OP
  }

  @Override
  public int getOffset() {
    return 168;
  }

  private static void disableDeletion(ServerSelectionList.NormalEntry entry, Button deleteButton) {
    ServerData serverData = entry.getServerData();
    deleteButton.active = !FavoritesList.contains(serverData.serverName + serverData.serverIP);
  }
}
