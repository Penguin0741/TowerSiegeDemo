package hk.hellopenguin.projectbattleground.tower;

import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import hk.hellopenguin.projectbattleground.ProjectBattleground;
import hk.hellopenguin.projectbattleground.gameend.VictoryAction;
import hk.hellopenguin.projectbattleground.players.PlayerManager;
import hk.hellopenguin.projectbattleground.utility.CustomLog;
import hk.hellopenguin.projectbattleground.utility.EnumDictionary.FieldTower;
import hk.hellopenguin.projectbattleground.utility.EnumDictionary.TeamColor;

public class SiegeAction {
	
	private CustomLog pbglog = CustomLog.getCustomLogger();	
	private PlayerManager plManager = PlayerManager.getManager();
	private TowerManager twManager = TowerManager.getManager();
	
	private World world = ProjectBattleground.getInstance().getServer().getWorlds().get(0);
	
	private Player sieger;
	private TeamColor siegerTeam;
	private TowerProfile tower;
	
	private String siegerName;
	private String towerName;
	
	public SiegeAction(Player sieger, FieldTower towerName) {
		this.sieger = sieger;
		this.siegerTeam = plManager.getProfile(sieger).getTeamColor();
		this.tower = twManager.getTower(towerName);
		
		this.siegerName = sieger.getName();
		this.towerName = towerName.toString();
	}
	
	public void siege() {
		if (!(isValidAttack())) return;
		
		if (tower.getHealth() > 0) tower.addHealth(-1);
		
		if (tower.getHealth() > 0) {
			resultStanding();
		} else {
			resultDestroyed();
		}
	}
	
	private void resultStanding() {
		world.playSound(tower.getCentre(), Sound.ENTITY_WITHER_BREAK_BLOCK, 0.8F, 1.0F);
		
		messageAll(String.format("%s is sieging %s! HP : %d/%d", siegerName, towerName, tower.getHealth(), tower.getMaxHealth()));
		pbglog.customLog(String.format("%s sieged %s. Remaining HP : %d/%d", siegerName, towerName, tower.getHealth(), tower.getMaxHealth()));
	}
	
	private void resultDestroyed() {
		if (tower.isNexus()) {
			new VictoryAction(siegerTeam).win();			
			return;
		}
		
		new DestroyAction(tower, siegerTeam).destroy();
		
		messageAll(String.format("%s destroyed %s!", siegerName, towerName));
		pbglog.customLog(String.format("%s has destroyed %s!", siegerName, towerName));
	}
	
	private void messageAll(String message) {
		for (Player player : plManager.getAllPlayers()) {
			player.sendMessage(message);
		}
	}
	
	private boolean isValidAttack() {		
		if (isFriendlyFire()) {
			sieger.sendMessage("You cannot damage towers on your team!");
			sieger.playSound(sieger.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F);
			return false;
		}
		
		if (tower.isInvulenarble()) {
			sieger.sendMessage("This tower is invulnerable to damage!");
			sieger.playSound(sieger.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, 0.9F);
			return false;
		}		
		return true;
	}
	
	private boolean isFriendlyFire() {	
		return (siegerTeam == tower.getAlignment());
	}
}
