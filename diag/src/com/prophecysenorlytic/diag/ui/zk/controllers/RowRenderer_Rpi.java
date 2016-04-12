package com.prophecysenorlytic.diag.ui.zk.controllers;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Window;

import com.prophecysenorlytic.diag.dao.DaoRpiDetails;
import com.prophecysenorlytic.diag.dto.RpiDetails;

public class RowRenderer_Rpi implements ListitemRenderer<RpiDetails> {

	private Window parent;
	private Controller_View_Rpi_Records paretntController;

	public RowRenderer_Rpi(Window parent, Controller_View_Rpi_Records paretntController) {
		super();
		this.parent = parent;
		this.paretntController = paretntController;
	}

	public RowRenderer_Rpi() {

	}

	@Override
	public void render(final Listitem item, RpiDetails data, int index) throws Exception {
		item.setValue(data);
		addCell_RpiName(item, data, index);
		addCell_RpiMac(item, data, index);
		
		
		addCell_Rpi_L_Index(item,data,index);
		addCell_RpiLastHeartbeat(item,data,index);
		item.setContext(paretntController.buildPopu_Rpi(data));
		item.addEventListener(Events.ON_DOUBLE_CLICK, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				 
				paretntController.showBreifMsg(item);
			}
		});

	}

	private void addCell_Rpi_L_Index(Listitem item, RpiDetails data, int index) {
		 
		Listcell cell = new Listcell(data.get_LI());
		cell.setParent(item);
	}

	private void addCell_RpiLastHeartbeat(Listitem item, RpiDetails data, int index) {
		 
		Listcell cell = new Listcell(data.getLastHeartbeatTime());
		cell.setParent(item);
	}

	private void addCell_RpiMac(Listitem item, RpiDetails data, int index) {
		Listcell cell = new Listcell(data.getMac());
		cell.setParent(item);
	}

	private void addCell_RpiName(Listitem item, RpiDetails data, int index) {
		Listcell cell = new Listcell(data.getName());
		cell.setParent(item);
	}

}
