package com.prophecysenorlytic.diag.ui.zk.controllers;

import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import com.prophecysenorlytic.diag.dto.DTO_MachineJson;

public class MachineItemRenderer implements ListitemRenderer<DTO_MachineJson> {
	private Controller_Machine_View_Sensor parentController;

	public MachineItemRenderer(Controller_Machine_View_Sensor parentController) {
		super();
		this.parentController = parentController;
	}

	@Override
	public void render(Listitem item, DTO_MachineJson data, int index) throws Exception {
		item.setValue(data);
		addCell_MachineName(item, data, index);
		addCell_MachineID(item, data, index);

	}

	private void addCell_MachineID(Listitem item, DTO_MachineJson data, int index) {

		Listcell cell = new Listcell(data.get_id());
		cell.setParent(item);
	}

	private void addCell_MachineName(Listitem item, DTO_MachineJson data, int index) {
		Listcell cell = new Listcell(data.getName());
		
		cell.setParent(item);
	}

}
