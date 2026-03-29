package eu.koolfreedom.command.impl;

import eu.koolfreedom.command.KoolCommand;
import eu.koolfreedom.command.annotation.CommandParameters;
import eu.koolfreedom.util.FUtil;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(name = "opall", description = "OP everyone on the server.")
public class OpAllCommand extends KoolCommand
{
    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String s, String[] args)
    {
        FUtil.broadcast("<aqua><sender> - Opping everyone on the server",
                Placeholder.unparsed("sender", sender.getName()));

        for (Player player : server.getOnlinePlayers())
        {
            if (!player.isOp())
            {
                player.setOp(true);
                msg(player, YOU_ARE_OP);
            }
            else
            {
                player.recalculatePermissions();
            }
        }

        return true;
    }
}
