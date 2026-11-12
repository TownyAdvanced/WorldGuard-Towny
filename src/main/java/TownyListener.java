
import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.event.PreNewTownEvent;
import com.palmergames.bukkit.towny.event.TownPreClaimEvent;
import com.palmergames.bukkit.towny.event.resident.ResidentPreJailEvent;
import com.palmergames.bukkit.towny.event.teleport.OutlawTeleportEvent;
import com.palmergames.bukkit.towny.object.jail.JailReason;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TownyListener implements Listener {
    WorldGuardTowny plugin;

    public TownyListener(WorldGuardTowny plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onTownCreate(PreNewTownEvent event) {
        // Get the player.
        Player player = event.getPlayer();
        validateClaimingAction(player, event);
    }

    @EventHandler
    public void onTownClaim(TownPreClaimEvent event) {
        // Get the player.
        Player player = event.getPlayer();
        validateClaimingAction(player, event);
    }

    public void validateClaimingAction(Player player, Cancellable event) {
    	LocalPlayer wgPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        ApplicableRegionSet set = getApplicableRegionSet(player);
        if (set == null)
        	return;

        // Check the flag.
        if (!set.testState(wgPlayer, WorldGuardTowny.getInstance().getTownCreationAllowedFlag())) {
            // Cancel Town creation
            TownyMessaging.sendErrorMsg(player, plugin.getDenyclaimMsg());
            event.setCancelled(true);
        }
    }

	@EventHandler(ignoreCancelled = true)
	public void onOutlawKill(ResidentPreJailEvent event) {
		if (!event.getReason().equals(JailReason.OUTLAW_DEATH))
			return;

		Player player = event.getResident().getPlayer();
		if (player == null)
			return;
		LocalPlayer wgPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
		ApplicableRegionSet set = getApplicableRegionSet(player);

		if (set == null)
			return;

		if (set.testState(wgPlayer, WorldGuardTowny.getInstance().getIgnoresOutlawingFlag())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onOutlawTeleport(OutlawTeleportEvent event) {
		Player player = event.getOutlaw().getPlayer();
		if (player == null) {
			return;
		}
		LocalPlayer wgPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
		ApplicableRegionSet set = getApplicableRegionSet(player);

		if (set == null) {
			return;
		}

		if (WorldGuardTowny.getInstance().getIgnoresOutlawingFlag() == null) {
			return;
		}

		if (set.testState(wgPlayer, WorldGuardTowny.getInstance().getIgnoresOutlawingFlag())) {
			event.setCancelled(true);
			return;
		}
	}

	private ApplicableRegionSet getApplicableRegionSet(Player player) {
		// Convert properties into WorldGuard compatible objects.
		BlockVector3 loc = BukkitAdapter.asBlockVector(player.getLocation());
		World world = BukkitAdapter.adapt(player.getWorld());

		// Get the regions.
		RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
		RegionManager regions = container.get(world);

		// If there are no regions don't bother.
		if (regions == null) {
			return null;
		}

		// Get the regions the player is in.
		ApplicableRegionSet set = regions.getApplicableRegions(loc);
		return set;
	}
}
