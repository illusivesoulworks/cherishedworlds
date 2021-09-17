package top.theillusivec4.cherishedworlds.mixin;

import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.cherishedworlds.client.FavoriteServers;
import top.theillusivec4.cherishedworlds.client.FavoritesList;

@Mixin(MultiplayerScreen.class)
public class MixinMultiplayerScreen {

  @Shadow
  protected MultiplayerServerListWidget serverListWidget;

  @Shadow
  private ButtonWidget buttonDelete;

  @SuppressWarnings("ConstantConditions")
  @Inject(at = @At("TAIL"), method = "init")
  public void cherishedworlds$init(CallbackInfo cb) {
    FavoriteServers.INSTANCE.init((MultiplayerScreen) (Object) this);
  }

  @SuppressWarnings("ConstantConditions")
  @Inject(at = @At("TAIL"), method = "render")
  public void cherishedworlds$render(MatrixStack matrices, int mouseX, int mouseY, float delta,
                                     CallbackInfo cb) {
    FavoriteServers.INSTANCE.render((MultiplayerScreen) (Object) this, matrices, mouseX, mouseY);
  }

  @Inject(at = @At("TAIL"), method = "updateButtonActivationStates")
  private void cherishedworlds$updateButtonActivationStates(CallbackInfo ci) {
    MultiplayerServerListWidget.Entry entry = this.serverListWidget.getSelectedOrNull();

    if (entry instanceof MultiplayerServerListWidget.ServerEntry) {
      ServerInfo serverInfo = ((MultiplayerServerListWidget.ServerEntry) entry).getServer();
      this.buttonDelete.active = !FavoritesList.contains(serverInfo.name + serverInfo.address);
    }
  }
}
