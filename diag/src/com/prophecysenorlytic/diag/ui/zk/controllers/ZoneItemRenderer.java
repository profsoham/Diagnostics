package com.prophecysenorlytic.diag.ui.zk.controllers;

import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import com.prophecysenorlytic.diag.dto.DTO_Zone;

public class ZoneItemRenderer implements ListitemRenderer<DTO_Zone> {

	private Controller_Machine_View_Sensor parentController;

	public ZoneItemRenderer(Controller_Machine_View_Sensor parentController) {
		super();
		this.parentController = parentController;
	}

	@Override
	public void render(Listitem item, DTO_Zone data, int index) throws Exception {
		item.setValue(data);
		String styleRow = "background-color:%s;color:%s";
		String bg = index % 2 == 0 ? "#cce0ff" : "#b3b3ff";

		item.setStyle(String.format(styleRow, bg,"#000000"));
		addCell_ZoneId(item, data, index);
	}

	private void addCell_ZoneId(Listitem item, DTO_Zone data, int index) {
		Listcell listcell = new Listcell(data.getId());
		listcell.setParent(item);

	}

}
