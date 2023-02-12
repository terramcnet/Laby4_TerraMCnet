package net.terramc.core;

import net.labymod.api.Laby;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.tag.PositionType;
import net.labymod.api.client.gui.hud.binding.category.HudWidgetCategory;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.models.addon.annotation.AddonMain;
import net.labymod.api.notification.Notification;
import net.labymod.api.notification.Notification.Type;
import net.labymod.api.serverapi.LabyProtocolApi;
import net.terramc.core.group.StaffGroupIconTag;
import net.terramc.core.group.StaffGroupTextTag;
import net.terramc.core.group.StaffTabListRenderer;
import net.terramc.core.gui.TerraNavigationElement;
import net.terramc.core.gui.activity.TerraMainActivity;
import net.terramc.core.hudwidget.CoinsHudWidget;
import net.terramc.core.hudwidget.GameRankHudWidget;
import net.terramc.core.hudwidget.NickHudWidget;
import net.terramc.core.hudwidget.PointsHudWidget;
import net.terramc.core.hudwidget.PointsRankHudWidget;
import net.terramc.core.hudwidget.game.GoldTimerHudWidget;
import net.terramc.core.hudwidget.game.IronTimerHudWidget;
import net.terramc.core.hudwidget.staff.RestartTimeHudWidget;
import net.terramc.core.hudwidget.staff.ServerStatusHudWidget;
import net.terramc.core.hudwidget.staff.VanishHudWidget;
import net.terramc.core.listener.ChatMessageListener;
import net.terramc.core.listener.NetworkListener;
import net.terramc.core.listener.ServerMessageListener;
import net.terramc.core.listener.SessionListener;
import net.terramc.core.util.ApiUtil;
import net.terramc.core.util.RankUtil;
import java.util.UUID;

@AddonMain
public class TerraAddon extends LabyAddon<TerraConfiguration> {

  public static final HudWidgetCategory TERRA = new HudWidgetCategory("terramc");
  public static final HudWidgetCategory TERRA_STAFF = new HudWidgetCategory("terramc_staff");

  public TerraMainActivity terraMainActivity;

  private ApiUtil apiUtil;
  private RankUtil rankUtil;

  private static boolean connectedTerra = false;
  public static String doubleLine = "§7§l§o▎§8§l§o▏ ";
  public static String doubleDots = "§7•§8●";

  @Override
  protected void enable() {
    this.registerSettingCategory();

    this.terraMainActivity = new TerraMainActivity(this);

    this.rankUtil = new RankUtil();

    this.apiUtil = new ApiUtil(this);
    if(!Laby.labyAPI().labyModLoader().isAddonDevelopmentEnvironment()) {
      this.apiUtil.loadPlayerStats(Laby.references().gameUserService().clientGameUser().getUniqueId());
      this.apiUtil.loadStaffSettings(Laby.references().gameUserService().clientGameUser().getUniqueId());
      this.apiUtil.loadRankData(Laby.references().gameUserService().clientGameUser().getUniqueId());
    } else {
      UUID uuid = UUID.fromString("966b5d5e-2577-4ab7-987a-89bfa59da74a");
      this.apiUtil.loadPlayerStats(uuid);
      this.apiUtil.loadStaffSettings(uuid);
      this.apiUtil.loadRankData(uuid);
    }

    this.registerListener(new ChatMessageListener(this));
    this.registerListener(new NetworkListener(this));
    this.registerListener(new SessionListener(this));

    LabyProtocolApi protocolApi = Laby.references().labyProtocolApi();
    protocolApi.getProtocolService().registerTranslationListener(new ServerMessageListener(this));

    labyAPI().navigationService().register("terramc_main_ui", new TerraNavigationElement(this));

    labyAPI().hudWidgetRegistry().categoryRegistry().register(TERRA);
    labyAPI().hudWidgetRegistry().categoryRegistry().register(TERRA_STAFF);

    labyAPI().tagRegistry().register("terramc_role", PositionType.ABOVE_NAME, new StaffGroupTextTag(this));
    labyAPI().tagRegistry().register("terramc_role_icon", PositionType.RIGHT_TO_NAME, new StaffGroupIconTag(this));
    Laby.references().badgeRegistry().register("terra_role_tab", net.labymod.api.client.entity.player.badge.PositionType.LEFT_TO_NAME, new StaffTabListRenderer(this));

    labyAPI().hudWidgetRegistry().register(new NickHudWidget(this));
    labyAPI().hudWidgetRegistry().register(new CoinsHudWidget(this));
    labyAPI().hudWidgetRegistry().register(new PointsHudWidget(this));
    labyAPI().hudWidgetRegistry().register(new GameRankHudWidget(this));
    labyAPI().hudWidgetRegistry().register(new PointsRankHudWidget(this));

    labyAPI().hudWidgetRegistry().register(new VanishHudWidget(this));
    labyAPI().hudWidgetRegistry().register(new ServerStatusHudWidget(this));
    labyAPI().hudWidgetRegistry().register(new RestartTimeHudWidget(this));

    labyAPI().hudWidgetRegistry().register(new IronTimerHudWidget(this));
    labyAPI().hudWidgetRegistry().register(new GoldTimerHudWidget(this));

    //labyAPI().permissionRegistry().register("terra_staff", false);

    //labyAPI().chatProvider().prefixRegistry().register("terra_player_head", new TerraPlayerHeadPrefix(this));

    this.logger().info("[TerraMCnet] Addon enabled.");

  }

  public void pushNotification(String title, String text) {
    Notification.Builder builder = Notification.builder()
        .title(Component.text(title))
        .text(Component.text(text))
        .icon(Icon.texture(ResourceLocation.create("terramc", "textures/icon.png")))
        .type(Type.ADVANCEMENT);
    labyAPI().notificationController().push(builder.build());
  }

  public void pushNotificationIcon(String title, String text, Icon icon) {
    Notification.Builder builder = Notification.builder()
        .title(Component.text(title))
        .text(Component.text(text))
        .icon(icon)
        .type(Type.ADVANCEMENT);
    labyAPI().notificationController().push(builder.build());
  }

  @Override
  protected Class<TerraConfiguration> configurationClass() {
    return TerraConfiguration.class;
  }

  public static boolean isConnectedTerra() {
    return connectedTerra;
  }

  public static void setConnectedTerra(boolean connectedTerra) {
    TerraAddon.connectedTerra = connectedTerra;
  }

  public ApiUtil apiUtil() {
    return apiUtil;
  }

  public RankUtil rankUtil() {
    return rankUtil;
  }

}
