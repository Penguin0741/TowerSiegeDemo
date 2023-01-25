package hk.hellopenguin.projectbattleground.tower;

import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import hk.hellopenguin.projectbattleground.ProjectBattleground;
import hk.hellopenguin.projectbattleground.players.PlayerManager;
import hk.hellopenguin.projectbattleground.utility.CustomLog;
import hk.hellopenguin.projectbattleground.utility.EnumDictionary.FieldTower;
import hk.hellopenguin.projectbattleground.utility.EnumDictionary.TeamColor;

public class ConstructAction {
	
	private CustomLog pbglog = CustomLog.getCustomLogger();
	private PlayerManager plManager = PlayerManager.getManager();
	
	private World world = ProjectBattleground.getInstance().getServer().getWorlds().get(0);
	
	TowerProfile tower;
	FieldTower towerName;
	TeamColor newTeam;
	
	public ConstructAction(TowerProfile tower, TeamColor newTeam) {
		this.tower = tower;
		this.towerName = tower.getTowerName();
		this.newTeam = newTeam;
	}
	
	public void construct() {
		alignToNew();
		constructEffect();
		buildFullTower();
		announcement();
	}
	
	private void alignToNew() {
		tower.toMaxHealth();
		tower.setAlignment(newTeam);
	}
	
	private void constructEffect() {
		world.playSound(tower.getCentre(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 2.0F, 1.0F);
	}
	
	private void buildFullTower() {		
		new TowerBuilder().build(newTeam.fullTower(), towerName);
		pbglog.customLog(String.format("Constructed %s for %s team!", towerName.toString(), newTeam.toString()));
	}
	
	private void announcement() {
		for (Player player : plManager.getAllPlayers()) {
			player.sendMessage(String.format("%s Team has captured %s!", newTeam.toString(), towerName.toString()));
		}
	}
}
