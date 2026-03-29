package eu.koolfreedom.command.impl;

import eu.koolfreedom.command.KoolCommand;
import eu.koolfreedom.command.annotation.CommandParameters;
import eu.koolfreedom.util.FUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import eu.koolfreedom.KoolAnarchyMod;

import java.util.Arrays;
import java.util.List;

@CommandParameters(name = "gtfo", description = "Bans the specified player.", usage = "/<command> <username> [reason] [-nrb | -q]")
public class GTFOCommand extends KoolCommand
{
    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String s, String[] args)
    {
        if (args.length == 0)
        {
            return false;
        }

        if (!KoolAnarchyMod.isAllowed(sender))
        {
            msg(sender, "<red>You do not have access to execute this command");
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if (!target.isOnline() && !target.hasPlayedBefore())
        {
            msg(sender, playerNotFound);
            return true;
        }

        String reason = args.length > 1 ? " (" + String.join(" ", Arrays.copyOfRange(args, 1, args.length)) + ")" : "";
        plugin.getBanSystem().banOfflinePlayer(target, "You've met with a terrible fate, haven't you? " + reason);

        FUtil.staffAction(sender, "Banning <target>", Placeholder.unparsed("target", target.getName() != null ? target.getName() : args[0]));
        if (target instanceof Player online)
        {
            // Now for the fun part...
            for (int i = 0; i < 4; i++)
            {
                online.getWorld().strikeLightning(online.getLocation());
            }
            online.setHealth(0);

            // We had our fun, they're gone
            online.kick(Component.text("You have been banned", NamedTextColor.RED));

            // Just for good measure...
            Bukkit.getOnlinePlayers().stream().filter(player -> FUtil.getIp(player).equalsIgnoreCase(FUtil.getIp(online))).forEach(player ->
            {
                // ZAP, and they're gone
                for (int i = 0; i < 4; i++)
                {
                    player.getWorld().strikeLightning(player.getLocation());
                }
                player.setHealth(0);
                player.kick(Component.text("You have been banned", NamedTextColor.RED));
            });
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, String commandLabel, String[] args)
    {
        return args.length == 1 ? Bukkit.getOnlinePlayers().stream().map(Player::getName)
                .filter(name -> !name.equalsIgnoreCase(sender.getName())).toList() : List.of();
    }
}

