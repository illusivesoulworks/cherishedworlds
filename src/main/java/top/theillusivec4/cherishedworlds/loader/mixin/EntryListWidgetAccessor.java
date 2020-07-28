package top.theillusivec4.cherishedworlds.loader.mixin;

import net.minecraft.client.gui.widget.EntryListWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntryListWidget.class)
public interface EntryListWidgetAccessor {

  @Accessor
  int getTop();

  @Accessor
  int getBottom();
}
