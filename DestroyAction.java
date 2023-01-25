package hk.hellopenguin.projectbattleground.tower;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import hk.hellopenguin.projectbattleground.ProjectBattleground;
import hk.hellopenguin.projectbattleground.utility.CustomLog;
import hk.hellopenguin.projectbattleground.utility.EnumDictionary.FieldTower;
import hk.hellopenguin.projectbattleground.utility.EnumDictionary.TeamColor;

public class DestroyAction {
	
	private ProjectBattleground pbg = ProjectBattleground.getInstance();
	private CustomLog pbglog = CustomLog.getCustomLogger();	
	
	private World world = pbg.getServer().getWorlds().get(0);
	int buildTicks = 200;
	
	TowerProfile tower;
	FieldTower towerName;
	TeamColor newTeam;
	
	public DestroyAction(TowerProfile tower, TeamColor newTeam) {
		this.tower = tower;
		this.towerName = tower.getTowerName();
		this.newTeam = newTeam;
	}
	
	public void destroy() {
		alignToNeutral();
		destroyEffect();
		buildHalfTower();
		buildCountdown();
	}
	
	private void alignToNeutral() {
		tower.setAlignment(TeamColor.NULL);
	}
	
	private void destroyEffect() {
		Location explodeLoc = tower.getCentre().clone().add(0, 9, 0);
		
		world.spawnParticle(Particle.EXPLOSION_HUGE, explodeLoc, 4);
		world.spawnParticle(Particle.EXPLOSION_LARGE, explodeLoc, 8, 3 ,3, 3);
		world.playSound(explodeLoc, Sound.ENTITY_GENERIC_EXPLODE, 2.0F, 1.0F);
	}
	
	private void buildHalfTower() {	
		new TowerBuilder().build(newTeam.halfTower(), towerName);
		pbglog.customLog(String.format("Constructing %s for %s team...", towerName.toString(), newTeam.toString()));
	}
	
	private void buildCountdown() {
		new BukkitRunnable() {
			public void run() {
				new ConstructAction(tower, newTeam).construct();
			}
		}.runTaskLater(pbg, buildTicks);
	}
}
