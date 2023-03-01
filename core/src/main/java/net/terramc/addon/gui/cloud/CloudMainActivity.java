package net.terramc.addon.gui.cloud;

import java.util.ArrayList;
import java.util.List;
import net.labymod.api.client.gui.mouse.MutableMouse;
import net.labymod.api.client.gui.screen.LabyScreen;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.widget.attributes.bounds.Bounds;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.render.font.text.TextRenderer;
import net.labymod.api.client.render.matrix.Stack;
import net.terramc.addon.TerraAddon;
import net.terramc.addon.util.ActivityUtil;
import org.jetbrains.annotations.Nullable;

@AutoActivity
@Link("cloud.lss")
public class CloudMainActivity extends Activity {

  private TerraAddon addon;

  public static String restart = "§cNEUSTART";
  public static String maintenance = "§4WARTUNG";

  public CloudMainActivity(TerraAddon addon) {
    this.addon = addon;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    if(!this.addon.rankUtil().canControlCloud()) return;

    List<ButtonWidget> buttonList = new ArrayList<>();

    // - - - Right Side - - -

    // Proxy
    ButtonWidget proxyRe = ButtonWidget.text("§aProxy §7- " + restart);
    proxyRe.addId("btn-proxy-re");
    proxyRe.setActionListener(() -> this.addon.apiUtil().sendControlToProxy("restart", "Proxy"));
    buttonList.add(proxyRe);

    // Lobby
    ButtonWidget lobbyRe = ButtonWidget.text("§aLobby §7- " + restart);
    lobbyRe.addId("left-first");
    lobbyRe.addId("top-first-b1");
    lobbyRe.setActionListener(() -> this.addon.apiUtil().sendControlToProxy("restart", "Lobby"));
    buttonList.add(lobbyRe);

    ButtonWidget lobbyMa = ButtonWidget.text("§aLobby §7- " + maintenance);
    lobbyMa.addId("left-first");
    lobbyMa.addId("top-first-b2");
    lobbyMa.setActionListener(() -> this.addon.apiUtil().sendControlToProxy("maintenance", "Lobby"));
    buttonList.add(lobbyMa);

    // PremiumLobby
    ButtonWidget pLobbyRe = ButtonWidget.text("§6P-Lobby §7- " + restart);
    pLobbyRe.addId("left-first");
    pLobbyRe.addId("top-second-b1");
    pLobbyRe.setActionListener(() -> this.addon.apiUtil().sendControlToProxy("restart", "PremiumLobby"));
    buttonList.add(pLobbyRe);

    ButtonWidget pLobbyMa = ButtonWidget.text("§6P-Lobby §7- " + maintenance);
    pLobbyMa.addId("left-first");
    pLobbyMa.addId("top-second-b2");
    pLobbyMa.setActionListener(() -> this.addon.apiUtil().sendControlToProxy("maintenance", "PremiumLobby"));
    buttonList.add(pLobbyMa);

    // SilentLobby
    ButtonWidget sLobbyRe = ButtonWidget.text("§4S-Lobby §7- " + restart);
    sLobbyRe.addId("left-first");
    sLobbyRe.addId("top-third-b1");
    sLobbyRe.setActionListener(() -> this.addon.apiUtil().sendControlToProxy("restart", "SilentLobby"));
    buttonList.add(sLobbyRe);

    ButtonWidget sLobbyMa = ButtonWidget.text("§4S-Lobby §7- " + maintenance);
    sLobbyMa.addId("left-first");
    sLobbyMa.addId("top-third-b2");
    sLobbyMa.setActionListener(() -> this.addon.apiUtil().sendControlToProxy("maintenance", "SilentLobby"));
    buttonList.add(sLobbyMa);

    // - - - Left Side - - -

      // - - - Left Line 1 - - -

    // KnockBackFFA
    ButtonWidget kbffaRe = ButtonWidget.text("§6KBFFA §7- " + restart);
    kbffaRe.addId("right-first");
    kbffaRe.addId("top-first-b1");
    kbffaRe.setActionListener(() -> this.addon.apiUtil().sendControlToProxy("restart", "KBFFA"));
    buttonList.add(kbffaRe);

    ButtonWidget kbffaMa = ButtonWidget.text("§6KBFFA §7- " + maintenance);
    kbffaMa.addId("right-first");
    kbffaMa.addId("top-first-b2");
    kbffaMa.setActionListener(() -> this.addon.apiUtil().sendControlToProxy("maintenance", "KBFFA"));
    buttonList.add(kbffaMa);

    // BuildFFA
    ButtonWidget buildRe = ButtonWidget.text("§eBuildFFA §7- " + restart);
    buildRe.addId("right-first");
    buildRe.addId("top-second-b1");
    buildRe.setActionListener(() -> this.addon.apiUtil().sendControlToProxy("restart", "BuildFFA"));
    buttonList.add(buildRe);

    ButtonWidget buildMa = ButtonWidget.text("§eBuildFFA §7- " + maintenance);
    buildMa.addId("right-first");
    buildMa.addId("top-second-b2");
    buildMa.setActionListener(() -> this.addon.apiUtil().sendControlToProxy("maintenance", "BuildFFA"));
    buttonList.add(buildMa);

    // XP
    ButtonWidget xpRe = ButtonWidget.text("§eXP §7- " + restart);
    xpRe.addId("right-first");
    xpRe.addId("top-third-b1");
    xpRe.setActionListener(() -> this.addon.apiUtil().sendControlToProxy("restart", "XP"));
    buttonList.add(xpRe);

    ButtonWidget xpMa = ButtonWidget.text("§eXP §7- " + maintenance);
    xpMa.addId("right-first");
    xpMa.addId("top-third-b2");
    xpMa.setActionListener(() -> this.addon.apiUtil().sendControlToProxy("maintenance", "XP"));
    buttonList.add(xpMa);

      // - - - Left Line 2 - - -

    // WaterFightFFA
    ButtonWidget waterRe = ButtonWidget.text("§9WaterFFA §7- " + restart);
    waterRe.addId("right-second");
    waterRe.addId("top-first-b1");
    waterRe.setActionListener(() -> this.addon.apiUtil().sendControlToProxy("restart", "WaterFFA"));
    buttonList.add(waterRe);

    ButtonWidget waterMa = ButtonWidget.text("§9WaterFFA §7- " + maintenance);
    waterMa.addId("right-second");
    waterMa.addId("top-first-b2");
    waterMa.setActionListener(() -> this.addon.apiUtil().sendControlToProxy("maintenance", "WaterFFA"));
    buttonList.add(waterMa);

    // FFA
    ButtonWidget ffaRe = ButtonWidget.text("§cFFA §7- " + restart);
    ffaRe.addId("right-second");
    ffaRe.addId("top-second-b1");
    ffaRe.setActionListener(() -> this.addon.apiUtil().sendControlToProxy("restart", "FFA"));
    buttonList.add(ffaRe);

    ButtonWidget ffaMa = ButtonWidget.text("§cFFA §7- " + maintenance);
    ffaMa.addId("right-second");
    ffaMa.addId("top-second-b2");
    ffaMa.setActionListener(() -> this.addon.apiUtil().sendControlToProxy("maintenance", "FFA"));
    buttonList.add(ffaMa);

    // GunGame
    ButtonWidget gunGameRe = ButtonWidget.text("§6GunGame §7- " + restart);
    gunGameRe.addId("right-second");
    gunGameRe.addId("top-third-b1");
    gunGameRe.setActionListener(() -> this.addon.apiUtil().sendControlToProxy("restart", "GunGame"));
    buttonList.add(gunGameRe);

    ButtonWidget gunGameMa = ButtonWidget.text("§6GunGame §7- " + maintenance);
    gunGameMa.addId("right-second");
    gunGameMa.addId("top-third-b2");
    gunGameMa.setActionListener(() -> this.addon.apiUtil().sendControlToProxy("maintenance", "GunGame"));
    buttonList.add(gunGameMa);

      // - - - Left Line 3 - - -

    // TheLab
    ButtonWidget labRe = ButtonWidget.text("§bTheLab §7- " + restart);
    labRe.addId("right-third");
    labRe.addId("top-first-b1");
    labRe.setActionListener(() -> this.addon.apiUtil().sendControlToProxy("restart", "TheLab"));
    buttonList.add(labRe);

    ButtonWidget labMa = ButtonWidget.text("§bTheLab §7- " + maintenance);
    labMa.addId("right-third");
    labMa.addId("top-first-b2");
    labMa.setActionListener(() -> this.addon.apiUtil().sendControlToProxy("maintenance", "TheLab"));
    buttonList.add(labMa);

    // SoupTrainer
    ButtonWidget stRe = ButtonWidget.text("§bST §7- " + restart);
    stRe.addId("right-third");
    stRe.addId("top-second-b1");
    stRe.setActionListener(() -> this.addon.apiUtil().sendControlToProxy("restart", "ST"));
    buttonList.add(stRe);

    ButtonWidget stMa = ButtonWidget.text("§bST §7- " + maintenance);
    stMa.addId("right-third");
    stMa.addId("top-second-b2");
    stMa.setActionListener(() -> this.addon.apiUtil().sendControlToProxy("maintenance", "ST"));
    buttonList.add(stMa);

    // CityBuild
    ButtonWidget cbRe = ButtonWidget.text("§aCB §7- " + restart);
    cbRe.addId("right-third");
    cbRe.addId("top-third-b1");
    cbRe.setActionListener(() -> this.addon.apiUtil().sendControlToProxy("restart", "CB"));
    buttonList.add(cbRe);

    ButtonWidget cbMa = ButtonWidget.text("§aCB §7- " + maintenance);
    cbMa.addId("right-third");
    cbMa.addId("top-third-b2");
    cbMa.setActionListener(() -> this.addon.apiUtil().sendControlToProxy("maintenance", "CB"));
    buttonList.add(cbMa);

      // - - - Left Line 4 - - -

    // CityBuild
    ButtonWidget communityRe = ButtonWidget.text("§aTerraUnity §7- " + restart);
    communityRe.addId("right-fourth");
    communityRe.addId("top-first-b1");
    communityRe.setActionListener(() -> this.addon.apiUtil().sendControlToProxy("restart", "TerraUnity"));
    buttonList.add(communityRe);

    ButtonWidget communityMa = ButtonWidget.text("§aTerraUnity §7- " + maintenance);
    communityMa.addId("right-fourth");
    communityMa.addId("top-first-b2");
    communityMa.setActionListener(() -> this.addon.apiUtil().sendControlToProxy("maintenance", "TerraUnity"));
    buttonList.add(communityMa);

    buttonList.forEach(this.document::addChild);
    buttonList.clear();
  }

  @Override
  public void render(Stack stack, MutableMouse mouse, float partialTicks) {
    super.render(stack, mouse, partialTicks);

    Bounds bounds = this.bounds();
    TextRenderer textRenderer = this.labyAPI.renderPipeline().textRenderer();

    ActivityUtil.drawCredits(textRenderer, bounds, stack);

    /*if(!TerraAddon.isConnectedTerra()) {
      textRenderer.text(I18n.translate("terramc.ui.general.noInfoAvailable"))
          .pos(bounds.getCenterX(), bounds.getCenterY())
          .centered(true)
          .render(stack);

      textRenderer.text(I18n.translate("terramc.ui.general.connectToServer"))
          .pos(bounds.getCenterX(), bounds.getCenterY() + 10)
          .centered(true)
          .render(stack);
    }*/

  }

  @Override
  public <T extends LabyScreen> @Nullable T renew() {
    return null;
  }

}
