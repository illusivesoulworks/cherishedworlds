package top.theillusivec4.cherishedworlds.loader.impl;

import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.world.level.storage.LevelSummary;
import top.theillusivec4.cherishedworlds.core.Accessor;
import top.theillusivec4.cherishedworlds.loader.mixin.SelectWorldScreenAccessor;
import top.theillusivec4.cherishedworlds.loader.mixin.WorldListEntryAccessor;
import top.theillusivec4.cherishedworlds.loader.mixin.EntryListWidgetAccessor;

public class AccessorImpl implements Accessor {

  @Override
  public WorldListWidget getWorldList(SelectWorldScreen screen) {
    return ((SelectWorldScreenAccessor) screen).getLevelList();
  }

  @SuppressWarnings("ConstantConditions")
  @Override
  public LevelSummary getWorldSummary(WorldListWidget.Entry entry) {
    return ((WorldListEntryAccessor) (Object) entry).getLevel();
  }

  @Override
  public ButtonWidget getDeleteButton(SelectWorldScreen screen) {
    return ((SelectWorldScreenAccessor) screen).getDeleteButton();
  }

  @Override
  public TextFieldWidget getTextField(SelectWorldScreen screen) {
    return ((SelectWorldScreenAccessor) screen).getSearchBox();
  }

  @Override
  public int getTop(WorldListWidget worldList) {
    return ((EntryListWidgetAccessor) worldList).getTop();
  }

  @Override
  public int getBottom(WorldListWidget worldList) {
    return ((EntryListWidgetAccessor) worldList).getBottom();
  }
}
