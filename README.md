# WorldGuard-Towny
A plugin that hooks into WorldGuard flags, to control certain events.

Current Features:
- Town creation protection via the region `town-creation` flag, see [Prevent Town Creation via Worldguard Flag](https://github.com/TownyAdvanced/WorldGuard-Towny/wiki/Preventing-Town-creation-in-a-region-via-Region-Flag)

- The ability to prevent OutlawTeleportEvents from occuring in a Region. ie: When players would enter a town they're outlawed in they could be teleported away by Towny. When `ignores-outlawing` is true, these teleports are blocked. This same flag also prevents jailing outlawed residents who die in the region.