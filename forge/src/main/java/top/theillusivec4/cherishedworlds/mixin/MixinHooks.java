package top.theillusivec4.cherishedworlds.mixin;

import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import top.theillusivec4.cherishedworlds.client.favorites.FavoritesList;

public class MixinHooks {

  public static boolean isNotValidSwap(ServerList serverList, int pos1, int pos2) {
    ServerData data1 = serverList.getServerData(pos1);
    ServerData data2 = serverList.getServerData(pos2);
    boolean isFavored1 = FavoritesList.contains(data1.serverName + data1.serverIP);
    boolean isFavored2 = FavoritesList.contains(data2.serverName + data2.serverIP);
    return (isFavored1 && !isFavored2) || (!isFavored1 && isFavored2);
  }
}
