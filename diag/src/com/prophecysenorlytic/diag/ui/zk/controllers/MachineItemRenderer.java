package com.prophecysenorlytic.diag.ui.zk.controllers;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import com.prophecysenorlytic.diag.dto.DTO_MachineJson;

public class MachineItemRenderer implements ListitemRenderer<DTO_MachineJson>,EventListener<Event> {
	private Controller_Machine_View_Sensor parentController;

	public MachineItemRenderer(Controller_Machine_View_Sensor parentController) {
		super();
		this.parentController = parentController;
	}

	@Override
	public void render(Listitem item, DTO_MachineJson data, int index) throws Exception {
		item.setValue(data);
		String styleRow = "background-color:%s;color:%s";
		String bg = index % 2 == 0 ? "#cce0ff" : "#b3b3ff";

		item.setStyle(String.format(styleRow, bg,"#000000"));
		item.addEventListener(Events.ON_CLICK, this);
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

	@Override
	public void onEvent(Event event) throws Exception {
		
		Listitem li=(Listitem)event.getTarget();
		DTO_MachineJson data=li.getValue();
		String machineInfo = String.format("Model: %s   Assembly: %s", data.getModel(), data.getAssembly());
		Clients.showNotification(machineInfo,li);
	}

}
