package com.prophecysenorlytic.diag.ui.zk.controllers;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

import com.prophecysenorlytic.diag.dto.RpiDetails;

public class Render_RpiDetails implements RowRenderer<RpiDetails>, EventListener<Event> {
	private Controller_View_Rpi_Records partentController;
	private RpiDetails dto;

	public Render_RpiDetails(Controller_View_Rpi_Records partentController) {
		super();
		this.partentController = partentController;
	}

	@Override
	public void render(Row row, RpiDetails data, int index) throws Exception {
		this.dto = data;
		String styleRow = "background-color:%s;color:%s";
		String bg = index % 2 == 0 ? "#cce0ff" : "#b3b3ff";

		row.setStyle(String.format(styleRow, bg,"#000000"));
		row.addEventListener(Events.ON_CLICK, this);
		addCell_LocalTime(row, data, index);
		addCell_Timestamp(row, data, index);
		addCell_externalIP(row, data, index);
		addCell_internalIP(row, data, index);
		addCell_UUID(row, data, index);
		addCell_Load(row, data, index);
		addCell_Memory(row, data, index);
		addCell_Uptime(row, data, index);
		addCell_Wifi(row, data, index);

	}

	private void addCell_MoreInfo(Row row, RpiDetails data, int index) {
		Image img = new Image("/icons/icon_moreinfo.png");
		img.setParent(row);
		img.addEventListener(Events.ON_CLICK, this);

	}

	private void addCell_TextData(Row row, String data, int index) {

		Label label = new Label(data);

		label.setParent(row);
	}

	private void addCell_Wifi(Row row, RpiDetails data, int index) {
		addCell_TextData(row, data.getWifi(), index);

	}

	private void addCell_Uptime(Row row, RpiDetails data, int index) {
		addCell_TextData(row, data.getUptime(), index);

	}

	private void addCell_Memory(Row row, RpiDetails data, int index) {
		addCell_TextData(row, data.getMem(), index);

	}

	private void addCell_Load(Row row, RpiDetails data, int index) {
		addCell_TextData(row, data.getLoad(), index);

	}

	private void addCell_UUID(Row row, RpiDetails data, int index) {
		addCell_TextData(row, data.getUuid(), index);

	}

	private void addCell_internalIP(Row row, RpiDetails data, int index) {
		addCell_TextData(row, data.getInternalIp(), index);

	}

	private void addCell_externalIP(Row row, RpiDetails data, int index) {
		addCell_TextData(row, data.getExternalIp(), index);

	}

	private void addCell_LocalTime(Row row, RpiDetails data, int index) {
		addCell_TextData(row, data.getLocalTime(), index);

	}

	private void addCell_Timestamp(Row row, RpiDetails data, int index) {
		addCell_TextData(row, data.getTimestamp(), index);

	}

	@Override
	public void onEvent(Event event) throws Exception {
		partentController.showJsonPopup(dto.getSelfAsJsonObj().toJSONString(), event.getTarget());
		
		/*Clients.showNotification(dto.getSelfAsJsonObj().toJSONString(), Clients.NOTIFICATION_TYPE_INFO,
				event.getTarget(), "start_before", 6000, true);*/

	}

}
