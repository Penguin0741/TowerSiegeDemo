package hk.hellopenguin.projectbattleground.tower;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import hk.hellopenguin.projectbattleground.utility.CustomLog;
import hk.hellopenguin.projectbattleground.utility.EnumDictionary.FieldTower;

public class SiegeCheck implements Listener {
	
	private CustomLog pbglog = CustomLog.getCustomLogger();
	private TowerManager twManager = TowerManager.getManager();
	
	@EventHandler
	public void onTargetBlockBreak(BlockBreakEvent event) {
		event.setCancelled(true);
		if (event.getBlock().getType() != Material.GILDED_BLACKSTONE) return;
		
		Location brokenBlock = event.getBlock().getLocation();
		FieldTower siegedTower = towerInRange(brokenBlock, 4);
		
		if (siegedTower == null) {
			pbglog.customLog(String.format("A non-tower gilded blackstone block has been destroyed at %f, %f, %f.", brokenBlock.getX(), brokenBlock.getY(), brokenBlock.getZ()));
			return;
		}
		new SiegeAction(event.getPlayer(), siegedTower).siege();
	}
	
	private FieldTower towerInRange(Location brokenBlock, int range) {
		FieldTower sieged = null;
		double rangeSquared = range * range;
		
		for (FieldTower tower : twManager.getTowerList()) {
			Location towerCentre = twManager.getTower(tower).getCentre();			
			if (brokenBlock.distanceSquared(towerCentre) <= rangeSquared) {
				sieged = tower;
				break;
			}
		}
		return sieged;
	}
}
