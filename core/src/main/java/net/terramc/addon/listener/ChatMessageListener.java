package net.terramc.addon.listener;

import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import net.terramc.addon.TerraAddon;
import net.terramc.addon.game.BedWarsGame;
import net.terramc.addon.data.AddonData;
import net.terramc.addon.group.GroupService;

public class ChatMessageListener {

  private final TerraAddon addon;

  public ChatMessageListener(TerraAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onChatReceive(ChatReceiveEvent event) {
    if(!this.addon.configuration().enabled().get()) return;
    if(!this.addon.isConnected()) return;
    //String formatted = event.chatMessage().getOriginalFormattedText();
    String plain = event.chatMessage().getOriginalPlainText();

    // Nick-Section

    String nickPrefix = "▎▏ Nick » ";
    //String nickPrefixColor = "§7§l§o▎§8§l§o▏ §eNick §8» ";

    //if(plain.startsWith(nickPrefix)) {
    if(plain.startsWith(nickPrefix + "Du spielst nun als: ")) {
      //nickName = clean.replace(nickPrefix + "Du spielst nun als: ", "");
      //AddonData.setNickName(formatted.replace(nickPrefixColor + "§eDu spielst nun als§8: ", ""));
      AddonData.setNickName(plain.split(": ")[1]);
    }

    if(plain.startsWith(nickPrefix + "You are now playing as: ")) {
      //nickName = clean.replace(nickPrefix + "You are now playing as: ", "");
      AddonData.setNickName(plain.split(": ")[1]);
    }

    if(plain.startsWith(nickPrefix + "Dein Nickname wurde zurückgesetzt.") ||
        plain.startsWith(nickPrefix + "Your nickname has been reset.")) {
      AddonData.setNickName(null);
    }

    // Spectator-Section

    if(plain.contains("Du bist nun ein Zuschauer.") || plain.contains("You are now an spectator.")) {
      AddonData.setSpectator(true);
    }

    // BedWars-Section

    String bedWarsPrefix = "▎▏ BedWars » ";

    if(plain.equalsIgnoreCase(bedWarsPrefix + "Die Runde startet nun.") || plain.equalsIgnoreCase(bedWarsPrefix + "The round starts now")) {
      if(AddonData.getBedWarsGame() == null) {
        AddonData.setBedWarsGame(new BedWarsGame());
      } else {
        AddonData.getBedWarsGame().setGameStarted(System.currentTimeMillis());
      }
    }

    if(plain.equalsIgnoreCase(bedWarsPrefix + "Das Bett deines Teams wurde abgebaut.") || plain.equalsIgnoreCase(bedWarsPrefix + "The bed of your team has been destroyed.")) {
      if(AddonData.getBedWarsGame() != null) {
        AddonData.getBedWarsGame().setBedAlive(false);
      }
    }

    if(plain.equalsIgnoreCase(bedWarsPrefix + "Das Spiel ist nun beendet.") || plain.equalsIgnoreCase(bedWarsPrefix + "The game has now ended.")) {
      AddonData.setBedWarsGame(null);
      if(this.addon.configuration().enableAutoGG().get() && (GroupService.getPremiumGroups().contains(AddonData.getRank()) || GroupService.getSpecialPremiumGroups().contains(AddonData.getRank()))) {
        if(AddonData.isInRound() & !AddonData.isSpectator() & !AddonData.isGgSent()) {
          AddonData.setGgSent(true);
          if(AddonData.getNickName() != null || AddonData.rankToggled()) {
            this.addon.sendMessage("GG");
          } else {
            if(GroupService.getSpecialPremiumGroups().contains(AddonData.getRank())) {
              this.addon.sendMessage("&8&k|&7&k|&r &eGG &7&k|&8&k|");
            } else {
              this.addon.sendMessage("GG");
            }
          }
        }
      }
    }

    // AutoGG-Section

    if(plain.contains("Die Runde wurde beendet.") || plain.contains("The round ended.")) {
      if(this.addon.configuration().enableAutoGG().get() && (GroupService.getPremiumGroups().contains(AddonData.getRank()) || GroupService.getSpecialPremiumGroups().contains(AddonData.getRank()))) {
        if(AddonData.isInRound() & !AddonData.isSpectator() & !AddonData.isGgSent()) {
          AddonData.setGgSent(true);
          if(AddonData.getNickName() != null || AddonData.rankToggled()) {
            this.addon.sendMessage("GG");
          } else {
            if(GroupService.getSpecialPremiumGroups().contains(AddonData.getRank())) {
              this.addon.sendMessage("&8&k|&7&k|&r &eGG &7&k|&8&k|");
            } else {
              this.addon.sendMessage("GG");
            }
          }
        }
      }
    }

  }

}
