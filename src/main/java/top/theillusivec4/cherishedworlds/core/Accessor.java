package top.theillusivec4.cherishedworlds.core;

import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.world.level.storage.LevelSummary;

public interface Accessor {

  WorldListWidget getWorldList(SelectWorldScreen screen);

  LevelSummary getWorldSummary(WorldListWidget.Entry entry);

  ButtonWidget getDeleteButton(SelectWorldScreen screen);

  TextFieldWidget getTextField(SelectWorldScreen screen);

  int getTop(WorldListWidget worldList);

  int getBottom(WorldListWidget worldList);
}
