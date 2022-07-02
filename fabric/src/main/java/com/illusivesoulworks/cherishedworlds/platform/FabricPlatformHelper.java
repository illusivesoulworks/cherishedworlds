/*
 * Copyright (C) 2018-2022 Illusive Soulworks
 *
 * Cherished Worlds is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * Cherished Worlds is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Cherished Worlds.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.illusivesoulworks.cherishedworlds.platform;

import com.illusivesoulworks.cherishedworlds.mixin.core.AccessorAbstractSelectionList;
import com.illusivesoulworks.cherishedworlds.platform.services.IPlatformHelper;
import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.components.AbstractSelectionList;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public Path getGamePath() {
        return FabricLoader.getInstance().getGameDir();
    }

    @Override
    public int getTop(AbstractSelectionList<?> abstractSelectionList) {
        return ((AccessorAbstractSelectionList) abstractSelectionList).getTop();
    }

    @Override
    public int getBottom(AbstractSelectionList<?> abstractSelectionList) {
        return ((AccessorAbstractSelectionList) abstractSelectionList).getBottom();
    }
}
