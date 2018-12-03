/*
 * Copyright (C) 2018  C4
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

package c4.cherishedworlds.core;

import c4.cherishedworlds.CherishedWorlds;
import com.google.common.collect.Sets;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.Loader;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Set;

public class FavoriteWorldsList {

    private static final Set<String> favorites = Sets.newHashSet();

    public static void loadFavoritesList() {

        try {
            favorites.clear();
            File file = new File(Loader.instance().getConfigDir(), CherishedWorlds.MODID + "/favorites.dat");
            NBTTagCompound nbttagcompound = CompressedStreamTools.read(file);

            if (nbttagcompound == null) {
                return;
            }

            NBTTagList nbttaglist = nbttagcompound.getTagList("favorites", Constants.NBT.TAG_STRING);

            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                favorites.add(nbttaglist.getStringTagAt(i));
            }
        } catch (Exception exception) {
            CherishedWorlds.logger.error("Couldn't load favorites list", exception);
        }
    }

    public static void saveFavoritesList() {

        try {
            NBTTagList nbttaglist = new NBTTagList();

            for (String s : favorites) {
                nbttaglist.appendTag(new NBTTagString(s));
            }
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setTag("favorites", nbttaglist);
            File file = new File(Loader.instance().getConfigDir(), CherishedWorlds.MODID + "/favorites.dat");

            if (!file.exists()) {
                FileUtils.forceMkdirParent(file);
            }
            CompressedStreamTools.safeWrite(nbttagcompound, file);
        } catch (Exception exception) {
            CherishedWorlds.logger.error("Couldn't save favorites list", exception);
        }
    }

    public static boolean hasFavorite() {
        return !favorites.isEmpty();
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
