# WorldGuard-Towny
A plugin that hooks into WorldGuard flags, to control certain events.

****Requires 0.95.2.5+****

Current Features:
- Town creation protection via the region `town-creation` flag, see [Prevent Town Creation via Worldguard Flag](https://github.com/TownyAdvanced/WorldGuard-Towny/wiki/Preventing-Town-creation-in-a-region-via-Region-Flag)

## Usage
1) To create a region with WorldGuard use the wand from `//wand`.    
2) Expand the region with `//expand vert` (This makes it go from bedrock to build limit)  
3) Select an area and create a region with `/rg create {regionName}`.   
4) Now use `/rg flag {regionName} town-creation deny`.    
5) This will make people unable to create towns inside this region.  
