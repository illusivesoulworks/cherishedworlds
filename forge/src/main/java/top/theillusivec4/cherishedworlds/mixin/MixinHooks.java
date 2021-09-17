package top.theillusivec4.cherishedworlds.mixin;

import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import top.theillusivec4.cherishedworlds.client.favorites.FavoritesList;

public class MixinHooks {

  public static boolean isNotValidSwap(ServerList serverList, int pos1, int pos2) {
    ServerData data1 = serverList.get(pos1);
    ServerData data2 = serverList.get(pos2);
    boolean isFavored1 = FavoritesList.contains(data1.name + data1.ip);
    boolean isFavored2 = FavoritesList.contains(data2.name + data2.ip);
    return (isFavored1 && !isFavored2) || (!isFavored1 && isFavored2);
  }
}
