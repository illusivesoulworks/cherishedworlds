package com.illusivesoulworks.cherishedworlds.client.favorites;

import com.illusivesoulworks.cherishedworlds.CherishedWorldsConstants;
import java.time.Duration;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetTooltipHolder;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public abstract class AbstractFavoritesListWidget<T extends ObjectSelectionList<E>, E extends ObjectSelectionList.Entry<E>>
    extends AbstractFavoritesWidget {

  private final WidgetTooltipHolder tooltip = new WidgetTooltipHolder();

  @Nullable
  protected final T list;
  private E hovered;
  private int focusIndex = -1;
  private boolean isFocused;

  public AbstractFavoritesListWidget(Screen parentScreen) {
    super(parentScreen);
    this.list = this.getList();
  }

  @Nullable
  protected E getHoveredEntry() {
    return this.hovered;
  }

  @Nullable
  protected E getFocusedEntry() {

    if (this.isFocused() && this.focusIndex != -1) {

      if (this.list != null) {
        List<E> children = this.list.children();

        if (this.focusIndex < children.size()) {
          return children.get(this.focusIndex);
        }
      }
    }
    return null;
  }

  @Nullable
  protected abstract T getList();

  protected abstract String getKey(E entry);

  @Override
  public boolean shouldTakeFocusAfterInteraction() {
    return false;
  }

  @Override
  public void setTooltip(@Nullable Tooltip tooltip) {
    this.tooltip.set(tooltip);
  }

  @Override
  public void setTooltipDelay(@NonNull Duration delay) {
    this.tooltip.setDelay(delay);
  }

  @Override
  public boolean isMouseOver(double mouseX, double mouseY) {
    return this.isActive() && this.isHovered();
  }

  @Override
  protected void extractWidgetRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX,
                                          int mouseY, float a) {

    if (this.list == null) {
      return;
    }
    this.isHovered = false;
    boolean hasFocus = false;
    this.setTooltip(null);
    Tooltip tooltip = null;
    ScreenRectangle rectangle = ScreenRectangle.empty();
    List<E> children = this.list.children();

    for (int i = 0; i < children.size(); i++) {
      E child = children.get(i);
      String key = this.getKey(child);

      if (key.isBlank()) {
        continue;
      }
      int minY = this.list.getY();
      int maxY = this.list.getBottom();
      int topOffset = 0;
      int bottomOffset = 0;
      int iconY = child.getContentYMiddle() - 4;

      if (minY > iconY) {
        topOffset = minY - iconY;

        if (topOffset >= TEXTURE_SIZE) {
          continue;
        }
      } else if ((iconY + TEXTURE_SIZE) > maxY) {
        bottomOffset = (iconY + TEXTURE_SIZE) - maxY;

        if (bottomOffset >= TEXTURE_SIZE) {
          continue;
        }
      }
      iconY = iconY + topOffset;
      int startV = topOffset;
      int endV = TEXTURE_SIZE - bottomOffset;
      int height = endV - startV;
      Identifier icon;
      int iconX = child.getX() - 16;
      boolean isHovered =
          graphics.containsPointInScissor(mouseX, mouseY)
              && this.areCoordinatesInIcon(iconX, iconY, height, mouseX, mouseY);
      boolean isFavorite = FavoritesList.contains(key);

      if (isHovered) {
        this.hovered = child;
        this.isHovered = true;
        tooltip = Tooltip.create(
            Component.translatable(
                "selectWorld." + CherishedWorldsConstants.MOD_ID + "."
                    + (isFavorite ? "unfavorite" : "favorite")), null);
        rectangle = new ScreenRectangle(iconX, iconY, TEXTURE_SIZE, height);
      }

      if (isFavorite) {

        if (isHovered) {
          icon = HOVERED_FAVORITED_ICON;
        } else {
          icon = FAVORITED_ICON;
        }
      } else {

        if (isHovered) {
          icon = HOVERED_EMPTY_ICON;
        } else {
          icon = EMPTY_ICON;
        }
      }
      graphics.blit(RenderPipelines.GUI_TEXTURED, icon, iconX, iconY, 0, startV, TEXTURE_SIZE,
                    endV - startV, TEXTURE_SIZE, TEXTURE_SIZE);
      boolean isFocused = this.isFocused() && this.focusIndex == i;

      if (isFocused) {
        hasFocus = true;
        graphics.blit(RenderPipelines.GUI_TEXTURED, FOCUSED_OUTLINE, iconX, iconY, 0, startV,
                      TEXTURE_SIZE, endV - startV, TEXTURE_SIZE, TEXTURE_SIZE);

        if (!this.isHovered) {
          tooltip = Tooltip.create(
              Component.translatable(
                  "selectWorld." + CherishedWorldsConstants.MOD_ID + "."
                      + (isFavorite ? "unfavorite" : "favorite")), null);
          rectangle = new ScreenRectangle(iconX, iconY, TEXTURE_SIZE, height);
        }
      }
    }
    this.setTooltip(tooltip);
    this.tooltip.refreshTooltipForNextRenderPass(graphics, mouseX, mouseY, this.isHovered, hasFocus,
                                                 rectangle);
    this.handleCursor(graphics);
  }

  @Override
  public void onClick(@NonNull MouseButtonEvent event, boolean doubleClick) {
    String key = this.onClick(this.getHoveredEntry());
    this.scrollToLevelId(key);
  }

  public String onClick(E entry) {

    if (entry != null) {
      String key = this.getKey(entry);
      boolean isFavorite = FavoritesList.contains(key);

      if (isFavorite) {
        FavoritesList.remove(key);
      } else {
        FavoritesList.add(key);
      }
      FavoritesList.save();
      this.updateList();
      return key;
    }
    return "";
  }

  protected abstract void updateList();

  protected void scrollToLevelId(String key) {

    if (!key.isBlank() && this.list != null) {
      List<E> children = this.list.children();

      for (int i = 0; i < children.size(); i++) {
        E entry = children.get(i);
        String id = this.getKey(entry);

        if (key.equals(id)) {
          this.setFocusIndex(i);
          break;
        }
      }
    }
  }

  @Override
  public boolean keyPressed(@NonNull KeyEvent evt) {

    if (!this.isActive()) {
      return false;
    } else if (evt.isSelection()) {
      this.playDownSound(Minecraft.getInstance().getSoundManager());
      String key = this.onClick(this.getFocusedEntry());
      this.scrollToLevelId(key);
      return true;
    } else {
      return false;
    }
  }

  @Override
  protected void updateWidgetNarration(@Nullable NarrationElementOutput output) {

    if (output != null) {
      E selectedEntry = this.getHoveredEntry();
      boolean isFocused = false;

      if (selectedEntry == null) {
        selectedEntry = this.getFocusedEntry();

        if (selectedEntry != null) {
          isFocused = true;
        }
      }

      if (selectedEntry != null) {
        String id = this.getKey(selectedEntry);

        if (!id.isBlank()) {
          String key = "gui.narrate.cherishedworlds.";

          if (FavoritesList.contains(id)) {
            key += "unfavorite";
          } else {
            key += "favorite";
          }
          output.add(NarratedElementType.TITLE,
                     Component.translatable(key, this.getNarratableId(selectedEntry)));

          if (isFocused) {
            output.add(NarratedElementType.USAGE,
                       Component.translatable("narration.button.usage.focused"));
          } else {
            output.add(NarratedElementType.USAGE,
                       Component.translatable("narration.button.usage.hovered"));
          }
        }
      }
    }
  }

  protected String getNarratableId(E entry) {
    return this.getKey(entry);
  }

  @Override
  public void setFocused(boolean focused) {
    this.isFocused = focused;

    if (focused && this.list != null) {

      if (this.focusIndex == -1) {
        this.focusIndex = 0;
        List<E> children = this.list.children();

        for (int i = 0; i < children.size(); i++) {
          E entry = children.get(i);

          if (entry == this.list.getSelected()) {
            this.setFocusIndex(i);
            break;
          }
        }
      } else {
        this.applyFocus();
      }
    }
  }

  protected void applyFocus() {

    if (this.list != null) {
      List<E> children = this.list.children();
      int index = this.focusIndex;

      if (index >= children.size() || index == -1) {
        index = 0;
      }
      E entry = children.get(index);
      boolean topClipped = entry.getContentY() < this.list.getY();
      boolean bottomClipped = entry.getContentBottom() > this.list.getBottom();

      if (topClipped || bottomClipped) {
        int topDelta = entry.getY() - this.list.getY() - 2;

        if (topDelta < 0) {
          this.list.setScrollAmount(this.list.scrollAmount() + topDelta);
        }
        int bottomDelta = this.list.getBottom() - entry.getY() - entry.getHeight() - 2;

        if (bottomDelta < 0) {
          this.list.setScrollAmount(this.list.scrollAmount() - bottomDelta);
        }
      }
    }
  }

  protected void setFocusIndex(int focusIndex) {
    this.focusIndex = focusIndex;
    this.applyFocus();
  }

  @Override
  public boolean isFocused() {
    return this.isFocused;
  }

  @Nullable
  @Override
  public ComponentPath nextFocusPath(@NonNull FocusNavigationEvent navigationEvent) {

    if (this.list == null || this.list.children().isEmpty()) {
      return null;
    } else if (navigationEvent instanceof FocusNavigationEvent.ArrowNavigation arrowNavigation) {
      int delta = switch (arrowNavigation.direction()) {
        case UP -> -1;
        case DOWN -> 1;
        case LEFT, RIGHT -> 0;
      };

      if (delta == 0) {
        return null;
      }

      if (delta + this.focusIndex < 0 || delta + this.focusIndex >= this.list.children().size()) {
        return null;
      }
      this.setFocusIndex(this.focusIndex + delta);
      this.parentScreen.triggerImmediateNarration(true);
      return ComponentPath.leaf(this);
    } else if (navigationEvent instanceof FocusNavigationEvent.TabNavigation) {
      this.focusIndex = -1;
    }
    return super.nextFocusPath(navigationEvent);
  }

  @NonNull
  @Override
  public ScreenRectangle getRectangle() {

    if (this.list != null) {
      ScreenRectangle rect = this.list.getRectangle();
      return new ScreenRectangle(rect.left() - 16, rect.top(), 16, rect.height());
    }
    return super.getRectangle();
  }
}
