
import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.event.PreNewTownEvent;
import com.palmergames.bukkit.towny.event.TownPreClaimEvent;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TownyListener implements Listener {

    @EventHandler
    public void onTownCreate(PreNewTownEvent event) {
        // Get the player.
        Player player = event.getPlayer();
        validateAction(player, event);
    }

    @EventHandler
    public void onTownClaim(TownPreClaimEvent event) {
        // Get the player.
        Player player = event.getPlayer();
        validateAction(player, event);
    }

    public void validateAction(Player player, Cancellable event) {
        // Convert properties into WorldGuard compatible objects.
        BlockVector3 loc = BukkitAdapter.asBlockVector(player.getLocation());
        World world = BukkitAdapter.adapt(player.getWorld());
        LocalPlayer wgPlayer = WorldGuardPlugin.inst().wrapPlayer(player);

        // Get the regions.
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(world);

        // If there are no regions don't bother.
        if (regions == null) {
            return;
        }

        // Get the regions the player is in.
        ApplicableRegionSet set = regions.getApplicableRegions(loc);

        // Check the flag.
        if (!set.testState(wgPlayer, WorldGuardTowny.getTownCreationAllowedFlag())) {
            // Cancel Town creation
            TownyMessaging.sendErrorMsg(player, "You're not allowed to claim in this region.");
            event.setCancelled(true);
        }
    }
}
