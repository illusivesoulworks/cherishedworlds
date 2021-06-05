package top.theillusivec4.cherishedworlds.client.favorites;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.client.gui.screen.ServerSelectionList;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
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
            evt.setCanceled(true);
            return;
          }
        }
      }
    }
  }

  @Override
  public void clicked(MultiplayerScreen screen) {
//    MultiplayerScreenAccessor accessor = (MultiplayerScreenAccessor) screen;
//    ServerSelectionList selectionList = accessor.getSelectionList();
//
//    if (selectionList != null) {
//      ServerSelectionList.Entry entry = selectionList.getSelected();
//
//      if (entry instanceof ServerSelectionList.NormalEntry) {
//        Button deleteButton = accessor.getDeleteButton();
//
//        if (deleteButton != null) {
//          disableDeletion((ServerSelectionList.NormalEntry) entry, deleteButton);
//        }
//      }
//    }
  }

  @Override
  public int getOffset() {
    return 168;
  }

  private static void refreshList(MultiplayerScreen multiplayerScreen,
                                  ServerSelectionList serverSelectionList) {
//    Minecraft mc = Minecraft.getInstance();
//    ServerList serverList = new ServerList(mc);
//    List<ServerSelectionList.Entry> entries = serverSelectionList.getEventListeners();
//    entries.clear();
//    List<ServerData> serverData = new ArrayList<>();
//
//    for (int i = 0; i < serverList.countServers(); i++) {
//      serverData.add(serverList.getServerData(i));
//    }
//    List<ServerData> favorites = new ArrayList<>();
//    List<ServerData> list = new ArrayList<>();
//
//    for (ServerData server : serverData) {
//
//      if (FavoritesList.contains(server.serverName + server.serverIP)) {
//        favorites.add(server);
//      } else {
//        list.add(server);
//      }
//    }
//    int index = 0;
//
//    for (ServerData server : favorites) {
//      serverList.set(index, server);
//      index++;
//    }
//
//    for (ServerData server : list) {
//      serverList.set(index, server);
//      index++;
//    }
//    serverSelectionList.updateOnlineServers(serverList);
//    ServerSelectionList.Entry entry = serverSelectionList.getSelected();
//
//    if (entry instanceof ServerSelectionList.NormalEntry) {
//      Button deleteButton = ((MultiplayerScreenAccessor) multiplayerScreen).getDeleteButton();
//
//      if (deleteButton != null) {
//        disableDeletion((ServerSelectionList.NormalEntry) entry, deleteButton);
//      }
//    }
  }

  private static void disableDeletion(ServerSelectionList.NormalEntry entry, Button deleteButton) {
    ServerData serverData = entry.getServerData();
    deleteButton.active = !FavoritesList.contains(serverData.serverName + serverData.serverIP);
  }
}
