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
import com.google.common.collect.Lists;
import net.minecraft.client.gui.*;
import net.minecraft.world.storage.WorldSummary;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class ReflectionAccessor {

    private static final Field SELECTION_LIST = ReflectionHelper.findField(GuiWorldSelection.class, "selectionList",
            "field_184866_u");
    private static final Field BUTTON_LIST = ReflectionHelper.findField(GuiScreen.class, "buttonList",
            "field_146292_n");
    private static final Field WORLD_SUMMARY = ReflectionHelper.findField(GuiListWorldSelectionEntry.class,
            "worldSummary", "field_186786_g");
    private static final Field ENTRIES = ReflectionHelper.findField(GuiListWorldSelection.class, "entries",
            "field_186799_w");

    private static final Method GET_SIZE = ReflectionHelper.findMethod(GuiListWorldSelection.class, "getSize",
            "func_148127_b");

    @Nullable
    public static GuiListWorldSelection getSelectionList(GuiWorldSelection worldSelection) {

        try {
            return (GuiListWorldSelection)SELECTION_LIST.get(worldSelection);
        } catch (IllegalAccessException e) {
            CherishedWorlds.logger.error("Could not retrieve selection list of worlds!" + e.getLocalizedMessage());
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static List<GuiButton> getButtonList(GuiScreen guiScreen) {

        try {
            return (List<GuiButton>)BUTTON_LIST.get(guiScreen);
        } catch (IllegalAccessException e) {
            CherishedWorlds.logger.error("Could not retrieve button list of gui screen!" + e.getLocalizedMessage());
        }
        return Lists.newArrayList();
    }

    @Nullable
    public static WorldSummary getWorldSummary(GuiListWorldSelectionEntry entry) {

        try {
            return (WorldSummary)WORLD_SUMMARY.get(entry);
        } catch (IllegalAccessException e) {
            CherishedWorlds.logger.error("Could not retrieve world summary from entry!" + e.getLocalizedMessage());
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static List<GuiListWorldSelectionEntry> getWorldSelectionEntries(GuiListWorldSelection listWorld) {

        try {
            return (List<GuiListWorldSelectionEntry>)ENTRIES.get(listWorld);
        } catch (IllegalAccessException e) {
            CherishedWorlds.logger.error("Could not retrieve entries from world list!" + e.getLocalizedMessage());
        }
        return Lists.newArrayList();
    }

    public static int getSize(GuiListWorldSelection listWorld) {

        try {
            return (int)GET_SIZE.invoke(listWorld);
        } catch (IllegalAccessException | InvocationTargetException e) {
            CherishedWorlds.logger.error("Could not retrieve size of world list!" + e.getLocalizedMessage());
        }
        return 0;
    }
}
