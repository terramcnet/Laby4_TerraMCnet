package net.terramc.addon.chat;

import com.google.gson.JsonObject;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.util.concurrent.task.Task;
import net.terramc.addon.TerraAddon;
import net.terramc.addon.chat.event.ChatServerMessageReceiveEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class ChatClient {

  private final String SERVER_IP = "terramc.net";
  private final int SERVER_PORT = 28943;

  private boolean online = false;
  private Socket socket;
  private PrintWriter serverOut;

  private TerraAddon addon;
  private ChatClientUtil util;

  public ChatClient(TerraAddon addon) {
    this.addon = addon;
    this.util = new ChatClientUtil(addon, this);
  }

  public void connect(boolean reconnect) {
    if(!addon.labyAPI().minecraft().sessionAccessor().isPremium() && !addon.labyAPI().labyModLoader().isAddonDevelopmentEnvironment()) {
      addon.logger().info("[TerraMC - Chat] Not connecting to Chat-Server. Account is cracked account!");
      return;
    }
    try {
      socket = new Socket(SERVER_IP, SERVER_PORT);
      serverOut = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
      online = true;
      if(reconnect) {
        addon.pushNotification(
            Component.translatable("terramc.chat.title", TextColor.color(255, 255, 85)),
            Component.translatable("terramc.chat.notification.connected", TextColor.color(85, 255, 85)));
      }

      new Thread(() -> {
        try {
          if(socket != null && !socket.isClosed()) {
            BufferedReader serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            String serverMessage;
            while ((serverMessage = serverIn.readLine()) != null) {
              JsonObject object = addon.gson().fromJson(serverMessage, JsonObject.class);
              Laby.fireEvent(new ChatServerMessageReceiveEvent(object));
            }
            socket.close();
          }

        } catch (Exception e) {
          online = false;
          this.addon.logger().error("Unable to receive TerraChat Server message", e);
        }
      }).start();
    } catch (IOException e) {
      online = false;
      if(reconnect) {
        addon.pushNotification(Component.translatable("terramc.chat.title", TextColor.color(255, 255, 85)),
            Component.translatable("terramc.chat.notification.no-connection", TextColor.color(255, 85, 85)));
      }
      this.addon.logger().error("Unable to connect to TerraChat Server", e);
      // Handle connection error
    }
  }

  public void connectStartUp() {
    this.connect(false);
    this.checkStatus();
    this.util.sendPlayerStatus(addon.labyAPI().getUniqueId().toString(), addon.labyAPI().getName(), false);
    Task.builder(() -> {
      if(!isConnected()) {
        this.connect(false);
      }
      this.util.sendRetrievePlayerData(addon.labyAPI().getUniqueId().toString());
    }).delay(5, TimeUnit.SECONDS).build().execute();
  }

  private boolean isPortOpen() {
    try (Socket ignored = new Socket(SERVER_IP, SERVER_PORT)) {
      return true;
    } catch (IOException ignored) {
      return false;
    }
  }

  public void checkStatus() {
    Task.builder(() -> {
      boolean status = isPortOpen();
      if(!status && online) {
        addon.pushNotification(Component.translatable("terramc.chat.title", TextColor.color(255, 255, 85)),
            Component.translatable("terramc.chat.notification.timed", TextColor.color(255, 85, 85)));
        if(this.isConnected()) {
          try {
            socket.close();
            serverOut = null;
          } catch(IOException ignored) {}
        }
      }
      if(status & !online) {
        connect(true);
      }
      online = status;
    }).repeat(1, TimeUnit.MINUTES).build().execute();
  }

  public boolean isConnected() {
    return this.socket != null && !this.socket.isClosed();
  }

  public void sendMessage(String channel, JsonObject object) {
    if(serverOut == null || socket.isClosed()) return;
    JsonObject data = new JsonObject();
    data.add(channel, object);
    serverOut.println(data);
  }

  public void closeConnection() {
    if(this.serverOut != null) {
      this.serverOut.close();
      this.serverOut = null;
    }

    try {
      if (socket != null && !socket.isClosed()) {
        socket.close();
        socket = null;
      }
    } catch (IOException ignored) {}

  }

  public boolean connected() {
    return serverOut == null || socket.isClosed();
  }

  public boolean online() {
    return online;
  }

  public ChatClientUtil util() {
    return util;
  }
}
