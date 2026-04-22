package com.illusivesoulworks.cherishedworlds.client.favorites.impl;

import com.illusivesoulworks.cherishedworlds.CherishedWorldsConstants;
import com.illusivesoulworks.cherishedworlds.client.favorites.AbstractFavoritesWidget;
import com.illusivesoulworks.cherishedworlds.client.favorites.FavoritesList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class FavoriteCreateWorldWidget extends AbstractFavoritesWidget {

  private final CreateWorldScreen createWorldScreen;

  private static boolean isFavorited;

  public FavoriteCreateWorldWidget(CreateWorldScreen createWorldScreen) {
    super(createWorldScreen, TEXTURE_SIZE, TEXTURE_SIZE);
    this.createWorldScreen = createWorldScreen;
    isFavorited = false;

    for (GuiEventListener child : this.createWorldScreen.children()) {

      if (child instanceof AbstractWidget childWidget) {
        if (childWidget.getMessage() instanceof MutableComponent mutableComponent) {

          if (mutableComponent.getContents() instanceof TranslatableContents translatableContents) {

            if (translatableContents.getKey().equals("selectWorld.create")) {
              this.setX(childWidget.getX() - 16);
              this.setY(childWidget.getY() + (childWidget.getHeight() / 2) - 4);
            }
          }
        }
      }
    }
  }

  @Override
  protected void extractWidgetRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX,
                                          int mouseY, float a) {
    this.isHovered = false;
    boolean isHovered =
        graphics.containsPointInScissor(mouseX, mouseY)
            && this.areCoordinatesInIcon(this.getX(), this.getY(), TEXTURE_SIZE, mouseX, mouseY);

    if (isHovered) {
      this.isHovered = true;
      this.setTooltip(
          Tooltip.create(
              Component.translatable(
                  "selectWorld." + CherishedWorldsConstants.MOD_ID + "."
                      + (isFavorited ? "unfavorite" : "favorite")), null));
    }
    Identifier icon;

    if (isFavorited) {

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
    graphics.blit(RenderPipelines.GUI_TEXTURED, icon, this.getX(), this.getY(), 0, 0, TEXTURE_SIZE,
                  TEXTURE_SIZE, TEXTURE_SIZE, TEXTURE_SIZE);

    if (this.isFocused()) {
      graphics.blit(RenderPipelines.GUI_TEXTURED, FOCUSED_OUTLINE, this.getX(), this.getY(), 0, 0,
                    TEXTURE_SIZE, TEXTURE_SIZE, TEXTURE_SIZE, TEXTURE_SIZE);

      if (!this.isHovered) {
        this.setTooltip(
            Tooltip.create(
                Component.translatable(
                    "selectWorld." + CherishedWorldsConstants.MOD_ID + "."
                        + (isFavorited ? "unfavorite" : "favorite")), null));
      }
    }
    this.handleCursor(graphics);
  }

  @Override
  public void onClick(@NonNull MouseButtonEvent event, boolean doubleClick) {
    this.onClick();
  }

  protected void onClick() {
    isFavorited = !isFavorited;
  }

  @Override
  public boolean keyPressed(@NonNull KeyEvent evt) {

    if (!this.isActive()) {
      return false;
    } else if (evt.isSelection()) {
      this.playDownSound(Minecraft.getInstance().getSoundManager());
      this.onClick();
      return true;
    } else {
      return false;
    }
  }

  @Override
  protected void updateWidgetNarration(@Nullable NarrationElementOutput output) {

    if (output != null) {
      String key = "selectWorld.cherishedworlds.";

      if (isFavorited) {
        key += "unfavorite";
      } else {
        key += "favorite";
      }
      output.add(NarratedElementType.TITLE, Component.translatable(key));

      if (this.isFocused()) {
        output.add(NarratedElementType.USAGE,
                   Component.translatable("narration.button.usage.focused"));
      } else {
        output.add(NarratedElementType.USAGE,
                   Component.translatable("narration.button.usage.hovered"));
      }
    }
  }

  public static void saveFavorite(String levelId) {

    if (isFavorited) {
      FavoritesList.add(levelId);
      FavoritesList.save();
    }
    isFavorited = false;
  }
}
