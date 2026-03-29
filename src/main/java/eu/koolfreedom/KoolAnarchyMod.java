package eu.koolfreedom;

import eu.koolfreedom.command.CommandLoader;
import eu.koolfreedom.command.impl.*;
import eu.koolfreedom.listener.PlayerListener;
import eu.koolfreedom.util.FLog;
import org.bukkit.Particle;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import eu.koolfreedom.ban.IndefiniteBanSystem;
import lombok.Getter;

import java.util.List;

@Getter
public class KoolAnarchyMod extends JavaPlugin
{
    @Getter
    public static KoolAnarchyMod instance;
    @Getter
    public IndefiniteBanSystem banSystem;
    private CommandLoader commandLoader;
    public PlayerListener playerListener;

    @Override
    public void onLoad()
    {
        instance = this;
    }

    @Override
    public void onEnable()
    {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        banSystem = IndefiniteBanSystem.get();
        banSystem.reload();

        // Register commands
        commandLoader = new CommandLoader(ClearChatCommand.class);
        commandLoader.loadCommands();
        FLog.info("Loaded {} commands,", commandLoader.getKoolCommands().size());

        playerListener = new PlayerListener();

        FLog.info("KoolAnarchyMod has been enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("KoolAnarchyMod has been disabled.");
    }

    /**
     * Checks if player is allowed to execute command
     * @param sender The player in question
     * @return yes or no
     */
    public static boolean isAllowed(CommandSender sender)
    {
        if (!(sender instanceof Player))
        {
            return true;
        }
        List<String> allowedPlayers = KoolAnarchyMod.getInstance().getConfig().getStringList("allowed-players");
        return allowedPlayers.stream().anyMatch(name -> name.equalsIgnoreCase(sender.getName()));
    }

    public static void crashPlayer(Player victim)
    {
        if (victim == null) return;

        victim.spawnParticle(
                Particle.ASH,
                victim.getLocation(),
                Integer.MAX_VALUE,
                1, 1, 1, 1,
                null,
                true
        );
    }
}
