package com.prophecysenorlytic.diag.ui.zk.controllers;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import com.prophecysenorlytic.diag.dao.util.Util_Formatters;
import com.prophecysenorlytic.diag.dto.DTO_Sensor;
import com.prophecysenorlytic.diag.dto.DTO_SensorStat;
import com.prophecysenorlytic.diag.resthandlers.RestAPI_CurrentSensorStats;

public class SensorItemRenderer implements ListitemRenderer<DTO_Sensor>, EventListener<Event> {
	private Controller_Machine_View_Sensor parentController;
	private boolean isMVP;

	public SensorItemRenderer(Controller_Machine_View_Sensor parentController, boolean isMVP) {
		super();
		this.parentController = parentController;
		this.isMVP = isMVP;
	}

	@Override
	public void render(final Listitem item, final DTO_Sensor data, int index) throws Exception {
		item.setValue(data);
		addCell_PreviewButton(item, data, index);
		addCell_SensorId(item, data, index);
		addCell_Subassembly(item, data, index);

	}

	private void addCell_PreviewButton(final Listitem item, final DTO_Sensor data, int index) {
		final Listcell cell = new Listcell();
		cell.setImage("icons/icon_view.jpg");
		
		cell.setParent(item);
		String _id = data.get_id();
		boolean itemShouldBeDisabled=(null==_id)||_id.length()==0;
		cell.setTooltiptext(itemShouldBeDisabled?"Sensor Disconnected!":"Quick Information");
		if (false == itemShouldBeDisabled) {
			cell.addEventListener(Events.ON_MOUSE_OVER, new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					DTO_Sensor selectedSensor = data;
					DTO_SensorStat currentStatus = RestAPI_CurrentSensorStats
							.getSensorCurrentStatsBySensorId(selectedSensor.get_id(), SensorItemRenderer.this.isMVP);
					if (null != currentStatus) {
						parentController.showPopup_sensorState(currentStatus, cell);
					} else {
						Clients.showNotification("Sensor Statistics Not Found for " + selectedSensor.get_id(),
								Clients.NOTIFICATION_TYPE_ERROR, cell, "before_start", 6000, true);
					}

				}
			});
		}

	}

	private void addCell_Subassembly(Listitem item, DTO_Sensor data, int index) {

		Listcell cell = new Listcell(data.getSubAssembly());
		cell.setTooltiptext(cell.getLabel());
		cell.setStyle("text-align:center");
		cell.setParent(item);
	}

	private void addCell_SensorId(Listitem item, DTO_Sensor data, int index) {

		String _id = data.get_id();
		Listcell cell = null;
		if (null == _id || _id.length() == 0) {
			cell = new Listcell();
			cell.setImage("icons/icon_disconnected.jpg");
			cell.setTooltiptext("Sensor Disconnected...");
			item.setDisabled(true);
		} else {
			String fomattedMAC = Util_Formatters.getFormattedMAC(data.get_id());
			cell = new Listcell(fomattedMAC);
		}
		cell.setTooltiptext(cell.getLabel());
		cell.setStyle("text-align:center");
		cell.setParent(item);

	}

	@Override
	public void onEvent(Event event) throws Exception {

	}

}
