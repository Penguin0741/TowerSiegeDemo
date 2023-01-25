package hk.hellopenguin.projectbattleground.tower;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.World;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;

import hk.hellopenguin.projectbattleground.utility.ConfigLibrary;
import hk.hellopenguin.projectbattleground.utility.CustomLog;
import hk.hellopenguin.projectbattleground.utility.EnumDictionary.FieldTower;
import hk.hellopenguin.projectbattleground.utility.EnumDictionary.TemplateTower;

public class TowerBuilder {
	
	private CustomLog pbglog = CustomLog.getCustomLogger();
	private static ConfigLibrary config = ConfigLibrary.getLibrary();
	private TowerManager twManager = TowerManager.getManager();
	
	private World world = Bukkit.getWorlds().get(0);
	
	private static Map<TemplateTower, CuboidRegion> regionList = config.getTemplateTowerRegion();
	
	public void buildDefault() {
		build(TemplateTower.NEUTRAL, FieldTower.POINT_A_TAIGA);
		build(TemplateTower.NEUTRAL, FieldTower.POINT_B_ISLAND);
		build(TemplateTower.NEUTRAL, FieldTower.POINT_C_SAVANNA);
		build(TemplateTower.RED_BASE, FieldTower.BASE_RED);
		build(TemplateTower.AQUA_BASE, FieldTower.BASE_AQUA);
	}
	
	public void build(TemplateTower templateTower, FieldTower targetTower) {
		CuboidRegion templateRegion = regionList.get(templateTower);
		BlockVector3 targetOrigin = twManager.getTower(targetTower).getOrigin();
		
		BlockArrayClipboard clipboard = copy(templateRegion);
		paste(clipboard, targetOrigin);
		pbglog.customLog("Built " + templateTower.toString() + " Tower at " + targetTower.toString());
	}
	
	private BlockArrayClipboard copy(CuboidRegion region) {
		BlockArrayClipboard clipboard = new BlockArrayClipboard(region);
		
		try (EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(world))) {
			ForwardExtentCopy fec = new ForwardExtentCopy(editSession, region, clipboard, region.getMinimumPoint());
			Operations.complete(fec);
		}
		catch (WorldEditException e) {
			e.printStackTrace();
		}
		return clipboard;
	}
	
	private void paste(BlockArrayClipboard clipboard, BlockVector3 origin) {
		try (EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(world))) {
			Operation op = new ClipboardHolder(clipboard).createPaste(editSession).to(origin).build();
			Operations.complete(op);
		}
		catch (WorldEditException e) {
			e.printStackTrace();
		}
	}	
}
