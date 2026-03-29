package eu.koolfreedom.command.impl;

import eu.koolfreedom.command.KoolCommand;
import eu.koolfreedom.command.annotation.CommandParameters;
import eu.koolfreedom.util.FUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandParameters(name = "deopall", description = "De-OP everyone on the server")
public class DeOpAllCommand extends KoolCommand
{
    @Override
    public boolean run(CommandSender sender, Player playerSender, Command cmd, String commandLabel, String[] args)
    {
        FUtil.staffAction(sender, "De-opping all players on the server");

        for (Player player : server.getOnlinePlayers())
        {
            player.setOp(false);
            msg(player, YOU_ARE_NOT_OP);
        }

        return true;
    }
}
