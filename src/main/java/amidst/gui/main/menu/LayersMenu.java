package amidst.gui.main.menu;

import java.util.LinkedList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import amidst.AmidstSettings;
import amidst.FeatureToggles;
import amidst.ResourceLoader;
import amidst.documentation.AmidstThread;
import amidst.documentation.CalledOnlyBy;
import amidst.documentation.NotThreadSafe;
import amidst.fragment.layer.LayerIds;
import amidst.gui.main.viewer.ViewerFacade;
import amidst.mojangapi.world.Dimension;
import amidst.settings.Setting;

@NotThreadSafe
public class LayersMenu {
	private final JMenu menu;
	private final AmidstSettings settings;
	private final Setting<Dimension> dimensionSetting;
	private final List<JMenuItem> overworldMenuItems = new LinkedList<>();
	private final List<JMenuItem> netherMenuItems = new LinkedList<>();
	private final List<JMenuItem> endMenuItems = new LinkedList<>();
	private volatile ViewerFacade viewerFacade;

	@CalledOnlyBy(AmidstThread.EDT)
	public LayersMenu(JMenu menu, AmidstSettings settings) {
		this.menu = menu;
		this.settings = settings;
		this.dimensionSetting = settings.dimension
				.withListener((oldValue, newValue) -> this.createMenu(newValue));
	}

	@CalledOnlyBy(AmidstThread.EDT)
	public void init(ViewerFacade viewerFacade) {
		this.viewerFacade = viewerFacade;
		if (viewerFacade != null) {
			createMenu(dimensionSetting.get());
		} else {
			disable();
		}
	}

	@CalledOnlyBy(AmidstThread.EDT)
	private void createMenu(Dimension selectedDimension) {
		menu.removeAll();
		overworldMenuItems.clear();
		endMenuItems.clear();
		netherMenuItems.clear();
		createDimensionLayers(selectedDimension);
		menu.setEnabled(true);
	}

	@CalledOnlyBy(AmidstThread.EDT)
	private void createDimensionLayers(Dimension dimension) {
		ButtonGroup group = new ButtonGroup();
		// All dimension menu
		createAllDimensions();
		menu.addSeparator();
		// Overworld Menu
		Menus.radio(menu, dimensionSetting, group, Dimension.OVERWORLD, MenuShortcuts.DISPLAY_DIMENSION_OVERWORLD);
		createOverworldLayers(dimension);
		// Nether Menu
		if (FeatureToggles.SUPPORT_NETHER) {
			menu.addSeparator();
			Menus.radio(menu, dimensionSetting, group, Dimension.NETHER, MenuShortcuts.DISPLAY_DIMENSION_NETHER);
			createNetherLayers(dimension);
		}
		// End Menu
		if (viewerFacade.hasLayer(LayerIds.END_ISLANDS)) {
			menu.addSeparator();
			Menus.radio(menu, dimensionSetting, group, Dimension.END, MenuShortcuts.DISPLAY_DIMENSION_END);
			createEndLayers(dimension);
		}
	}

	@CalledOnlyBy(AmidstThread.EDT)
	private void createOverworldAndEndLayers(Dimension dimension) {
		// @formatter:off
		ButtonGroup group = new ButtonGroup();
		Menus.radio(   menu, dimensionSetting, group,     Dimension.OVERWORLD,                                      MenuShortcuts.DISPLAY_DIMENSION_OVERWORLD);
		createOverworldLayers(dimension);
		menu.addSeparator();
		Menus.radio(   menu, dimensionSetting, group,     Dimension.END,                                            MenuShortcuts.DISPLAY_DIMENSION_END);
		createEndLayers(dimension);
		// @formatter:on
	}

	@CalledOnlyBy(AmidstThread.EDT)
	private void createOverworldLayers(Dimension dimension) {
		// @formatter:off
		overworldLayer(settings.showSlimeChunks,          "Slime Chunks",           getIcon("slime.png"),           MenuShortcuts.SHOW_SLIME_CHUNKS,      dimension, LayerIds.SLIME);
		overworldLayer(settings.showSpawn,                "Spawn Location Icon",    getIcon("spawn.png"),           MenuShortcuts.SHOW_WORLD_SPAWN,       dimension, LayerIds.SPAWN);
		overworldLayer(settings.showStrongholds,          "Stronghold Icons",       getIcon("stronghold.png"),      MenuShortcuts.SHOW_STRONGHOLDS,       dimension, LayerIds.STRONGHOLD);
		overworldLayer(settings.showVillages,             "Village/Outpost Icons",  getIcon("village.png"),         MenuShortcuts.SHOW_VILLAGES,          dimension, LayerIds.VILLAGE);
		overworldLayer(settings.showTemples,              "Temple/Witch Hut Icons", getIcon("desert.png"),          MenuShortcuts.SHOW_TEMPLES,           dimension, LayerIds.TEMPLE);
		overworldLayer(settings.showMineshafts,           "Mineshaft Icons",        getIcon("mineshaft.png"),       MenuShortcuts.SHOW_MINESHAFTS,        dimension, LayerIds.MINESHAFT);
		overworldLayer(settings.showOceanMonuments,       "Ocean Monument Icons",   getIcon("ocean_monument.png"),  MenuShortcuts.SHOW_OCEAN_MONUMENTS,   dimension, LayerIds.OCEAN_MONUMENT);
		overworldLayer(settings.showWoodlandMansions,     "Woodland Mansion Icons", getIcon("woodland_mansion.png"),MenuShortcuts.SHOW_WOODLAND_MANSIONS, dimension, LayerIds.WOODLAND_MANSION);
		overworldLayer(settings.showOceanFeatures,        "Ocean Features Icons",   getIcon("shipwreck.png"),       MenuShortcuts.SHOW_OCEAN_FEATURES,    dimension, LayerIds.OCEAN_FEATURES);
		// Sharing overworld and nether
		overworldLayer(settings.showNetherFortresses_OW,  "Nether Fortress Icons",  getIcon("nether_fortress.png"), MenuShortcuts.SHOW_NETHER_FORTRESS,   dimension, LayerIds.NETHER_FORTRESS);
		overworldLayer(settings.showBastionRemnant_OW,	  "Bastion Remnant Icons",  getIcon("bastion_remnant.png"), MenuShortcuts.SHOW_BASTION_REMNENT,   dimension, LayerIds.BASTION_REMNANT);
		overworldLayer(settings.showRuinedPortals_OW,     "Ruined Portal Icons",    getIcon("ruined_portal.png"),   MenuShortcuts.SHOW_RUINED_PORTALS,    dimension, LayerIds.RUINED_PORTALS);
		// @formatter:on
	}

	@CalledOnlyBy(AmidstThread.EDT)
	private void createNetherLayers(Dimension dimension) {
		netherLayer(settings.showNetherFortresses_Nether, "Nether Fortress Icons", getIcon("nether_fortress.png"), MenuShortcuts.SHOW_NETHER_FORTRESS, dimension, LayerIds.NETHER_FORTRESS);
		netherLayer(settings.showBastionRemnant_Nether, "Bastion Remnant Icons", getIcon("bastion_remnant.png"), MenuShortcuts.SHOW_BASTION_REMNENT, dimension, LayerIds.BASTION_REMNANT);
		netherLayer(settings.showRuinedPortals_Nether, "Ruined Portal Icons",    getIcon("ruined_portal.png"),   MenuShortcuts.SHOW_RUINED_PORTALS,    dimension, LayerIds.RUINED_PORTALS);
	}
	
	@CalledOnlyBy(AmidstThread.EDT)
	private void createEndLayers(Dimension dimension) {
		// @formatter:off
		endLayer(      settings.showEndCities,            "End City Icons",         getIcon("end_city.png"),        MenuShortcuts.SHOW_END_CITIES,   dimension, LayerIds.END_CITY);
		endLayer(      settings.showEndGateways,          "End Gateway Icons",      getIcon("end_gateway.png"),     MenuShortcuts.SHOW_END_GATEWAYS, dimension, LayerIds.END_GATEWAY);
		// @formatter:on
	}

	@CalledOnlyBy(AmidstThread.EDT)
	private void createAllDimensions() {
		// @formatter:off
		Menus.checkbox(menu, settings.showGrid,           "Grid",                   getIcon("grid.png"),            MenuShortcuts.SHOW_GRID);
		Menus.checkbox(menu, settings.showPlayers,        "Player Icons",           getIcon("player.png"),          MenuShortcuts.SHOW_PLAYERS);
		// @formatter:on
	}

	@CalledOnlyBy(AmidstThread.EDT)
	public void overworldLayer(
			Setting<Boolean> setting,
			String text,
			ImageIcon icon,
			MenuShortcut menuShortcut,
			Dimension dimension,
			int layerId) {
		overworldMenuItems.add(createLayer(setting, text, icon, menuShortcut, dimension, layerId));
	}

	@CalledOnlyBy(AmidstThread.EDT)
	public void netherLayer(
			Setting<Boolean> setting,
			String text,
			ImageIcon icon,
			MenuShortcut menuShortcut,
			Dimension dimension,
			int layerId) {
		netherMenuItems.add(createLayer(setting, text, icon, menuShortcut, dimension, layerId));
	}

	@CalledOnlyBy(AmidstThread.EDT)
	public void endLayer(
			Setting<Boolean> setting,
			String text,
			ImageIcon icon,
			MenuShortcut menuShortcut,
			Dimension dimension,
			int layerId) {
		endMenuItems.add(createLayer(setting, text, icon, menuShortcut, dimension, layerId));
	}
	@CalledOnlyBy(AmidstThread.EDT)
	private JCheckBoxMenuItem createLayer(
			Setting<Boolean> setting,
			String text,
			ImageIcon icon,
			MenuShortcut menuShortcut,
			Dimension dimension,
			int layerId) {
		return  Menus.checkbox(menu, setting, text, icon, menuShortcut);
	}

	@CalledOnlyBy(AmidstThread.EDT)
	public void disable() {
		this.viewerFacade = null;
		menu.setEnabled(false);
	}

	@CalledOnlyBy(AmidstThread.EDT)
	private ImageIcon getIcon(String icon) {
		return new ImageIcon(ResourceLoader.getImage("/amidst/gui/main/icon/" + icon));
	}
}
