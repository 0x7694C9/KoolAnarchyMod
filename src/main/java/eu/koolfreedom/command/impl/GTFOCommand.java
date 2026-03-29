package eu.koolfreedom.command.impl;

import eu.koolfreedom.ban.IndefiniteBanSystem;
import eu.koolfreedom.command.KoolCommand;
import eu.koolfreedom.command.annotation.CommandParameters;
import eu.koolfreedom.util.FUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import eu.koolfreedom.KoolAnarchyMod;

import java.util.Arrays;
import java.util.List;

@CommandParameters(name = "gtfo", description = "Bans the specified player.", usage = "/<command> <username> [reason] [-nrb | -q]", aliases = "ban")
public class GTFOCommand extends KoolCommand
{
    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String s, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(Component.text("Only players can execute this command", NamedTextColor.RED));
            return true;
        }

        Player player = Bukkit.getPlayer(args[0]);

        assert player != null;
        if (!KoolAnarchyMod.isAllowed(player))
        {
            player.sendMessage(Component.text("You do not have access to execute this command", NamedTextColor.RED));
            return true;
        }

        for (int i = 0; i < 30; i++)
        {
            player.getWorld().strikeLightningEffect(player.getLocation());
        }

        player.setFireTicks(200);
        player.setGameMode(GameMode.ADVENTURE);

        broadcast("<red><sender> is swinging the Russian Hammer over <target>!",
                Placeholder.unparsed("sender", sender.getName()),
                Placeholder.unparsed("target", player.getName()));

        Bukkit.getScheduler().runTaskLater(plugin, () -> broadcast("<red><target> will be completely eviscerated!",
                Placeholder.unparsed("target", player.getName())), 2);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (player.isOp()) player.setOp(false);
            if (player.isWhitelisted()) player.setWhitelisted(false);
        }, 2);

        Bukkit.getScheduler().runTaskLater(plugin, () -> player.setHealth(0), 10);

        Bukkit.getScheduler().runTaskLater(plugin, () -> broadcast("<red><target> has been eradicated from existence!",
                Placeholder.unparsed("target", player.getName())), 30);

        KoolAnarchyMod.crashPlayer(player);

        String reason = args.length > 1 ? " (" + String.join(" ", Arrays.copyOfRange(args, 1, args.length)) + ")" : "";
        Bukkit.getScheduler().runTaskLater(plugin, () ->
                IndefiniteBanSystem.get().banPlayer(player, "You've met with a terrible fate, haven't you? " + reason), 38);
        Bukkit.getScheduler().runTaskLater(plugin, () ->
                FUtil.staffAction(sender, "Banning <target>",
                        Placeholder.unparsed("target", player.getName())), 38);
        player.kick(Component.text("FUCKOFF, and get your shit together", NamedTextColor.RED));
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, Command command, String commandLabel, String[] args)
    {
        return args.length == 1 ? Bukkit.getOnlinePlayers().stream().map(Player::getName).toList() : List.of();
    }
}

