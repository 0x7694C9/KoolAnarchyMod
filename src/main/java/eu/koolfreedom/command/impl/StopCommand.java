package eu.koolfreedom.command.impl;

import eu.koolfreedom.KoolAnarchyMod;
import eu.koolfreedom.command.KoolCommand;
import eu.koolfreedom.command.annotation.CommandParameters;
import eu.koolfreedom.util.FUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

@CommandParameters(name = "forcestop", description = "Stops the server (authorized personnel only)", usage = "/<command> [reason]")
public class StopCommand extends KoolCommand
{
    private static final Map<CommandSender, String> STOP_CONFIRM = new HashMap<>();

    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args)
    {
        if (!KoolAnarchyMod.isAllowed(sender))
        {
            Bukkit.dispatchCommand(sender, "bcv Stopping the server");
            return true;
        }

        String reason = "Server is offline, be patient while we fuck around.";

        if (args.length != 0)
        {
            reason = StringUtils.join(args, " ");
        }

        if (!(sender instanceof Player))
        {
            shutdown(reason);
            return true;
        }

        if (STOP_CONFIRM.containsKey(sender))
        {
            shutdown(STOP_CONFIRM.get(sender));
            return true;
        }

        msg(sender, "<red>Warning: You're about to stop the server. Type /stop again to confirm you want to do this.");

        STOP_CONFIRM.put(sender, reason);

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (STOP_CONFIRM.remove(sender) != null)
                {
                    msg(sender, "<gray>Stop request expired.");
                }
            }
        }.runTaskLater(plugin, 15 * 20);
        return true;
    }

    public void shutdown(String reason)
    {
        FUtil.broadcast("<light_purple>Server is going offline!");

        for (Player player : server.getOnlinePlayers())
        {
            player.kick(Component.text(reason, NamedTextColor.LIGHT_PURPLE));
        }

        server.shutdown();
    }
}