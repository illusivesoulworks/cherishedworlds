/*
 * Copyright (C) 2018-2021  C4
 *
 * This file is part of Cherished Worlds, a mod made for Minecraft.
 *
 * Cherished Worlds is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or any later version.
 *
 * Cherished Worlds is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with Cherished
 * Worlds.  If not, see <https://www.gnu.org/licenses/>.
 */

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
