package top.theillusivec4.cherishedworlds.client;

import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.util.math.MatrixStack;
import top.theillusivec4.cherishedworlds.mixin.AccessorEntryListWidget;
import top.theillusivec4.cherishedworlds.mixin.AccessorMultiplayerScreen;

public class FavoriteServers implements FavoritesManager<MultiplayerScreen> {

  public static FavoriteServers INSTANCE = new FavoriteServers();

  @Override
  public void init(MultiplayerScreen screen) {
    MultiplayerServerListWidget serverListWidget =
        ((AccessorMultiplayerScreen) screen).getServerListWidget();

    if (serverListWidget != null) {
      FavoritesList.load();
      serverListWidget.setServers(screen.getServerList());
    }
  }

  @Override
  public void render(MultiplayerScreen screen, MatrixStack matrices, int mouseX, int mouseY) {
    AccessorMultiplayerScreen accessor = ((AccessorMultiplayerScreen) screen);
    MultiplayerServerListWidget serverListWidget = accessor.getServerListWidget();

    if (serverListWidget != null) {

      for (int i = 0; i < serverListWidget.children().size(); i++) {
        MultiplayerServerListWidget.Entry entry = serverListWidget.children().get(i);

        if (entry instanceof MultiplayerServerListWidget.ServerEntry) {
          ServerInfo serverInfo = ((MultiplayerServerListWidget.ServerEntry) entry).getServer();
          boolean isFavorite = FavoritesList.contains(serverInfo.name + serverInfo.address);
          AccessorEntryListWidget accessorList = ((AccessorEntryListWidget) serverListWidget);
          renderIcon(screen, matrices, mouseX, mouseY, serverListWidget.getScrollAmount(), i,
              accessorList.getTop(), accessorList.getBottom(), isFavorite);
        }
      }
    }
  }

  @Override
  public void click(MultiplayerScreen screen, double mouseX, double mouseY) {
    AccessorMultiplayerScreen accessor = (AccessorMultiplayerScreen) screen;
    MultiplayerServerListWidget serverListWidget = accessor.getServerListWidget();

    if (serverListWidget != null) {

      for (int i = 0; i < serverListWidget.children().size(); i++) {
        MultiplayerServerListWidget.Entry entry = serverListWidget.children().get(i);

        if (entry instanceof MultiplayerServerListWidget.ServerEntry) {
          ServerInfo serverInfo = ((MultiplayerServerListWidget.ServerEntry) entry).getServer();
          boolean isFavorite = FavoritesList.contains(serverInfo.name + serverInfo.address);
          int top = (int) (((AccessorEntryListWidget) serverListWidget).getTop() + 15 + 36 * i -
              serverListWidget.getScrollAmount());
          int x = screen.width / 2 - getOffset();

          if (mouseY >= top && mouseY <= (top + 9) && mouseX >= x && mouseX <= (x + 9)) {
            String s = serverInfo.name + serverInfo.address;

            if (isFavorite) {
              FavoritesList.remove(s);
            } else {
              FavoritesList.add(s);
            }
            FavoritesList.save();
            serverListWidget.setServers(screen.getServerList());
            MultiplayerServerListWidget.Entry selected = serverListWidget.getSelectedOrNull();

            if (selected instanceof MultiplayerServerListWidget.ServerEntry) {
              disableDeletion((MultiplayerServerListWidget.ServerEntry) selected,
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

  private static void disableDeletion(MultiplayerServerListWidget.ServerEntry entry,
                                      ButtonWidget button) {
    ServerInfo serverInfo = entry.getServer();
    button.active = !FavoritesList.contains(serverInfo.name + serverInfo.address);
  }
}
