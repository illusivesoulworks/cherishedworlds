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

package top.theillusivec4.cherishedworlds.client.favorites;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.io.FileUtils;
import top.theillusivec4.cherishedworlds.CherishedWorldsMod;

public class FavoritesList {

  private static final Set<String> favorites = new HashSet<>();

  public static void load() {

    try {
      favorites.clear();
      File file = new File(FMLPaths.CONFIGDIR.get().toString(),
          CherishedWorldsMod.MOD_ID + "/favorites.dat");
      CompoundTag compound = NbtIo.read(file);

      if (compound == null) {
        return;
      }
      ListTag list = compound.getList("favorites", Constants.NBT.TAG_STRING);

      for (int i = 0; i < list.size(); ++i) {
        favorites.add(list.getString(i));
      }
    } catch (Exception exception) {
      CherishedWorldsMod.LOGGER.error("Couldn't load favorites list", exception);
    }
  }

  public static void save() {

    try {
      ListTag list = new ListTag();

      for (String s : favorites) {
        list.add(StringTag.valueOf(s));
      }
      CompoundTag compound = new CompoundTag();
      compound.put("favorites", list);
      File file = new File(FMLPaths.CONFIGDIR.get().toString(),
          CherishedWorldsMod.MOD_ID + "/favorites.dat");

      if (!file.exists()) {
        FileUtils.forceMkdirParent(file);
      }
      NbtIo.write(compound, file);
    } catch (Exception exception) {
      CherishedWorldsMod.LOGGER.error("Couldn't save favorites list", exception);
    }
  }

  public static boolean contains(String fileName) {
    return favorites.contains(fileName);
  }

  public static void add(String name) {
    favorites.add(name);
  }

  public static void remove(String name) {
    favorites.remove(name);
  }
}
