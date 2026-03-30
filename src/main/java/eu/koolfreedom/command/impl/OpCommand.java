package eu.koolfreedom.command.impl;

import eu.koolfreedom.command.KoolCommand;
import eu.koolfreedom.command.annotation.CommandParameters;
import eu.koolfreedom.util.FUtil;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@CommandParameters(name = "op", description = "OP a player", usage = "/<command> <player>")
public class OpCommand extends KoolCommand
{
    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args)
    {
        if (args.length < 1)
        {
            return false;
        }

        final String targetName = args[0].toLowerCase();

        List<String> matchedPlayerNames = new ArrayList<>();
        for (final Player player : server.getOnlinePlayers())
        {
            if (player.getName().toLowerCase().contains(targetName) || player.getDisplayName().toLowerCase().contains(targetName)
                    || player.getName().contains(targetName) || player.getDisplayName().contains(targetName))
            {
                if (!player.isOp())
                {
                    matchedPlayerNames.add(player.getName());
                    player.setOp(true);
                    msg(player, YOU_ARE_OP);
                }
            }
        }

        if (!matchedPlayerNames.isEmpty())
        {
            FUtil.broadcast("<<aqua><sender> - Opping <target>",
                    Placeholder.unparsed("sender", sender.getName()),
                    Placeholder.unparsed("target", StringUtils.join(matchedPlayerNames, ", ")));
        }
        else
        {
            msg(sender,"<gray>Either the player is already opped, or the player could not be found.");
        }

        return true;
    }
}
