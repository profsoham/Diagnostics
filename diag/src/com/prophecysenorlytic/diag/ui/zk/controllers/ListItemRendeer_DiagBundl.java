package com.prophecysenorlytic.diag.ui.zk.controllers;

import org.zkoss.json.JSONObject;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Html;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import com.prophecysenorlytic.diag.dto.DTO_DiagBundle;

public class ListItemRendeer_DiagBundl implements ListitemRenderer<DTO_DiagBundle>, EventListener<Event> {

	private Controller_BundleViewer parentController = null;

	public ListItemRendeer_DiagBundl(Controller_BundleViewer parentController) {
		super();
		this.parentController = parentController;
	}

	@Override
	public void render(Listitem item, DTO_DiagBundle data, int index) throws Exception {
		item.setValue(data);
		addCell_ReportDate(item, data, index);
		addCell_CollectorType(item, data, index);
		addCell_PIN(item, data, index);
		addCell_DownloadLionk(item, data, index);
		JSONObject jObj=((DTO_DiagBundle)item.getValue()).getJsonString();
		item.setTooltiptext(jObj.toJSONString());

	}

	private void addCell_DownloadLionk(Listitem item, DTO_DiagBundle data, int index) {
		Html html = new Html();
		html.setContent("<a href='" + data.getLink_fileDownload() + "'>Download</a>");
		Listcell cell = new Listcell();
		cell.appendChild(html);
		cell.setParent(item);

	}

	private void addCell_PIN(Listitem item, DTO_DiagBundle data, int index) {

		Listcell cell = new Listcell(data.getPin());
		cell.setParent(item);
	}

	private void addCell_CollectorType(Listitem item, DTO_DiagBundle data, int index) {
		Listcell cell = new Listcell(data.getUser());
		cell.setParent(item);

	}

	private void addCell_ReportDate(Listitem item, DTO_DiagBundle data, int index) {
		Listcell cell = new Listcell(data.getInsert_datetime());
		cell.setParent(item);

	}

	@Override
	public void onEvent(Event event) throws Exception {

		Listitem listItem = (Listitem) event.getTarget();
		JSONObject jObj=((DTO_DiagBundle)listItem.getValue()).getJsonString();
		
	}

}
