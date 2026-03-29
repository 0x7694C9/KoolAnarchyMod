package eu.koolfreedom.listener;

import eu.koolfreedom.KoolAnarchyMod;
import eu.koolfreedom.ban.IndefiniteBanSystem;
import eu.koolfreedom.util.FUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import java.net.InetAddress;

@SuppressWarnings({"ConstantConditions", "deprecation"})
public class PlayerListener implements Listener
{
    public PlayerListener()
    {
        KoolAnarchyMod.getInstance().getServer().getPluginManager().registerEvents(this, KoolAnarchyMod.getInstance());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPermBannedPlayerLogin(PlayerLoginEvent event)
    {
        IndefiniteBanSystem idot = IndefiniteBanSystem.get();

        String currentName = event.getPlayer().getName().toLowerCase();
        String uuid = event.getPlayer().getUniqueId().toString();

        InetAddress addr = event.getAddress();
        String ip = addr != null ? addr.getHostAddress() : null;

        boolean nameBanned = idot.isNameBanned(currentName);
        boolean uuidBanned = idot.isUuidBanned(uuid);
        boolean ipBanned = ip != null && idot.isIpBanned(ip);

        if (!(nameBanned || uuidBanned || ipBanned)) {
            return; // not banned
        }

        String entryName;
        if (nameBanned)
        {
            entryName = currentName;
        }
        else if (uuidBanned)
        {
            entryName = idot.findBannedNameByUuid(uuid).orElse(currentName);
        }
        else
        {
            entryName = idot.findBannedNameByIp(ip).orElse(currentName);
        }

        String banType = nameBanned ? "username" : uuidBanned ? "UUID" : "IP address";
        Component reason = idot.getReasonComponent(entryName);

        Component kickMessage = FUtil.miniMessage("""
            <red>Your <ban_type> is indefinitely banned from this server!
            <red>Reason:</red> <gold><reason>
            <red>Appeal at:</red> <gold><ban_url>
            """,
                Placeholder.component("reason", reason),
                Placeholder.unparsed("ban_type", banType),
                Placeholder.unparsed("ban_url", KoolAnarchyMod.getInstance().getConfig().getString("ban-url"))
        );

        // disallow login
        event.disallow(PlayerLoginEvent.Result.KICK_BANNED, kickMessage);

        // Broadcasts to admins
        FUtil.broadcast("kfc.admin",
                "<#ffb373><player><gray> tried joining, but they're indefinitely banned!",
                Placeholder.unparsed("player", event.getPlayer().getName())
        );
    }
}
