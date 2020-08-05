package dev.weary.limitedcommands;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandPreprocessHandler implements Listener {

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (event.getPlayer().hasPermission(LimitedCommandsSettings.LIMITED_COMMANDS_PERMISSION.asString())) {
            return;
        }

        String fullCommandText = event.getMessage();
        String baseCommand = LimitedCommandsModule.getBaseCommandPart(fullCommandText);
        if (LimitedCommandsModule.isCommandHidden(baseCommand)) {
            event.setCancelled(true);

            String unknownCommandMessage = LimitedCommandsSettings.LIMITED_COMMANDS_MESSAGE.asColoredString();
            if (!unknownCommandMessage.equals("none")) {
                event.getPlayer().sendMessage(unknownCommandMessage);
            }
        }
    }
}
