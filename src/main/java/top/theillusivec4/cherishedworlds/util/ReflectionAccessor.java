/*
 * Copyright (C) 2018-2019  C4
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

import net.minecraft.client.gui.*;
import net.minecraft.world.storage.WorldSummary;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import javax.annotation.Nullable;
import java.util.List;

public class ReflectionAccessor {

    @Nullable
    public static GuiListWorldSelection getSelectionList(GuiWorldSelection worldSelection) {
        return ObfuscationReflectionHelper.getPrivateValue(GuiWorldSelection.class, worldSelection, "field_184866_u");
    }

    @Nullable
    public static WorldSummary getWorldSummary(GuiListWorldSelectionEntry entry) {
        return ObfuscationReflectionHelper.getPrivateValue(GuiListWorldSelectionEntry.class, entry, "field_186786_g");
    }

    public static List<GuiButton> getButtonList(GuiScreen guiScreen) {
        return ObfuscationReflectionHelper.getPrivateValue(GuiScreen.class, guiScreen, "field_146292_n");
    }

    public static List<GuiListWorldSelectionEntry> getWorldSelectionEntries(GuiListWorldSelection listWorld) {
        return ObfuscationReflectionHelper.getPrivateValue(GuiListExtended.class, listWorld, "field_195087_v");
    }

    public static GuiTextField getTextField(GuiWorldSelection worldSelection) {
        return ObfuscationReflectionHelper.getPrivateValue(GuiWorldSelection.class, worldSelection, "field_212352_g");
    }
}
