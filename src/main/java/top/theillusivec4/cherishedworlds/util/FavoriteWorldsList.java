/*
 * Copyright (c) 2018-2020 C4
 *
 * This file is part of Cherished Worlds, a mod made for Minecraft.
 *
 * Cherished Worlds is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Cherished Worlds is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Cherished Worlds.  If not, see <https://www.gnu.org/licenses/>.
 */

package top.theillusivec4.cherishedworlds.util;

import com.google.common.collect.Sets;
import java.io.File;
import java.util.Set;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.io.FileUtils;
import top.theillusivec4.cherishedworlds.CherishedWorlds;

public class FavoriteWorldsList {

  private static final Set<String> favorites = Sets.newHashSet();

  public static void loadFavoritesList() {

    try {
      favorites.clear();
      File file = new File(FMLPaths.CONFIGDIR.get().toString(),
          CherishedWorlds.MODID + "/favorites.dat");
      CompoundNBT compound = CompressedStreamTools.read(file);

      if (compound == null) {
        return;
      }
      ListNBT list = compound.getList("favorites", Constants.NBT.TAG_STRING);

      for (int i = 0; i < list.size(); ++i) {
        favorites.add(list.getString(i));
      }
    } catch (Exception exception) {
      CherishedWorlds.LOGGER.error("Couldn't load favorites list", exception);
    }
  }

  public static void saveFavoritesList() {

    try {
      ListNBT list = new ListNBT();

      for (String s : favorites) {
        list.add(StringNBT.valueOf(s));
      }
      CompoundNBT compound = new CompoundNBT();
      compound.put("favorites", list);
      File file = new File(FMLPaths.CONFIGDIR.get().toString(),
          CherishedWorlds.MODID + "/favorites.dat");

      if (!file.exists()) {
        FileUtils.forceMkdirParent(file);
      }
      CompressedStreamTools.write(compound, file);
    } catch (Exception exception) {
      CherishedWorlds.LOGGER.error("Couldn't save favorites list", exception);
    }
  }

  public static boolean isFavorite(String fileName) {
    return favorites.contains(fileName);
  }

  public static void addFavorite(String name) {
    favorites.add(name);
  }

  public static void removeFavorite(String name) {
    favorites.remove(name);
  }
}
