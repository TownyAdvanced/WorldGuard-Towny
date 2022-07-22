import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;

import java.util.Objects;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldGuardTowny extends JavaPlugin {

    private static StateFlag TOWN_CREATION_ALLOWED_FLAG;
    public static WorldGuardTowny instance;
    private String denyClaimMsg;

    public static StateFlag getTownCreationAllowedFlag() {
        return TOWN_CREATION_ALLOWED_FLAG;
    }

    @Override
    public void onEnable() {
        instance = this;
        this.getServer().getPluginManager().registerEvents(new TownyListener(this), this);

        // Config Initialisation
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        setDenyClaimMsg();
    }

    @Override
    public void onLoad() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();

        // Register Flag
        try {
            // Create the town creation flag.
            StateFlag flag = new StateFlag("town-creation", true);
            registry.register(flag);
            TOWN_CREATION_ALLOWED_FLAG = flag;
        } catch (FlagConflictException e) {
            Flag<?> existing = registry.get("town-creation");

            // Happens if a flag with the same name exists.
            if (existing instanceof StateFlag) {
                TOWN_CREATION_ALLOWED_FLAG = (StateFlag) existing;
                getLogger().warning("Found WorldGuard Flag with that matches, overriding it.");
            } else {
                getLogger().warning("Towny WorldGuard Flag could not be created:\n" + e.getMessage());
            }
        }
    }

    public static WorldGuardTowny getInstance() {
        return instance;
    }

	public String getDenyclaimMsg() {
		return denyClaimMsg;
	}

	public void setDenyClaimMsg() {
		this.denyClaimMsg = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(getConfig().getString("language.deny-claim")));
	}
}
