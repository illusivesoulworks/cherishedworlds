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

package top.theillusivec4.cherishedworlds.loader.impl;

import java.io.File;
import net.fabricmc.loader.api.FabricLoader;
import top.theillusivec4.cherishedworlds.core.Accessor;
import top.theillusivec4.cherishedworlds.core.CherishedWorlds;

public class CherishedWorldsImpl implements CherishedWorlds {

  public static CherishedWorlds INSTANCE = new CherishedWorldsImpl();

  private final Accessor accessor = new AccessorImpl();

  @Override
  public Accessor getAccessor() {
    return accessor;
  }

  @Override
  public File getConfigDir() {
    return FabricLoader.getInstance().getConfigDir().toFile();
  }
}
