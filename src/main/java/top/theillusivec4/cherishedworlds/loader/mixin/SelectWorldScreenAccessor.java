package top.theillusivec4.cherishedworlds.loader.mixin;

import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SelectWorldScreen.class)
public interface SelectWorldScreenAccessor {

  @Accessor
  WorldListWidget getLevelList();

  @Accessor
  ButtonWidget getDeleteButton();

  @Accessor
  TextFieldWidget getSearchBox();
}
