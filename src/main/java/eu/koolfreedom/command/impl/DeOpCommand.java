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

@CommandParameters(name = "deop", description = "De-OP a player", usage = "/<command> <player>")
public class DeOpCommand extends KoolCommand
{
    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String s, String[] args)
    {
        if (args.length < 1)
        {
            return false;
        }

        final String targetName = args[0].toLowerCase();

        final List<String> matchedPlayerNames = new ArrayList<>();
        for (Player player : server.getOnlinePlayers())
        {
            if (player.getName().toLowerCase().contains(targetName) || player.getDisplayName().toLowerCase().contains(targetName)
                    || player.getName().contains(targetName) || player.getDisplayName().contains(targetName))
            {
                if (player.isOp())
                {
                    matchedPlayerNames.add(player.getName());
                    player.setOp(false);
                    msg(player, YOU_ARE_NOT_OP);
                }
            }
        }

        if (!matchedPlayerNames.isEmpty())
        {
            FUtil.staffAction(sender, "De-Opping <target>",
                    Placeholder.unparsed("target", StringUtils.join(matchedPlayerNames, ", ")));
        }
        else
        {
            msg(sender, "<gray>Either the player is already deopped, or the player could not be found.");
        }

        return true;
    }
}
