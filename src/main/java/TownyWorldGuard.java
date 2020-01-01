import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import org.bukkit.plugin.java.JavaPlugin;

public class TownyWorldGuard extends JavaPlugin {

    private static StateFlag TOWN_CREATION_ALLOWED_FLAG;
    public static TownyWorldGuard instance;

    public static StateFlag getTownCreationAllowedFlag() {
        return TOWN_CREATION_ALLOWED_FLAG;
    }

    @Override
    public void onEnable() {
        instance = this;

        this.getServer().getPluginManager().registerEvents(new TownyListener(), this);
    }

    @Override
    public void onLoad() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            // Create the town creation flag.
            StateFlag flag = new StateFlag("town-creation", true);
            registry.register(flag);
            TOWN_CREATION_ALLOWED_FLAG = flag;
        } catch (FlagConflictException e) {
            Flag<?> existing = registry.get("town-creation");
            if (existing instanceof StateFlag) {
                TOWN_CREATION_ALLOWED_FLAG = (StateFlag) existing;
            } else {

            }
        }
    }

    public static TownyWorldGuard getInstance() {
        return instance;
    }
}
