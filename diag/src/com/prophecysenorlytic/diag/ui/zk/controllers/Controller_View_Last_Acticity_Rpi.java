package com.prophecysenorlytic.diag.ui.zk.controllers;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

import com.prophecysenorlytic.diag.dao.DaoRpiDetails;
import com.prophecysenorlytic.diag.dto.RpiDetails;

public class Controller_View_Last_Acticity_Rpi extends SelectorComposer<Window> {

	private static final long serialVersionUID = -5600668852085973061L;

	@Wire
	Window win_LastActivity_Info;
	@Wire
	Label lbl_LastSeen, lbl_LastTimestamp, lbl_Load, lbl_Memory, lbl_Uptime, lbl_Wifi, lbl_Name, lbl_MAC,
			lbl_internalIP, lbl_ExternalIP;

	@Wire
	Grid grd_Details;

	public Controller_View_Last_Acticity_Rpi() {
		super();
	}

	@Override
	public void doAfterCompose(Window comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		
		String mac = (String) Executions.getCurrent().getArg().get("mac");
		RpiDetails dto = DaoRpiDetails.getLastActivityDetails(mac);
		win_LastActivity_Info.setTitle("Heartbeat of "+dto.getName());
		lbl_ExternalIP.setValue(dto.getExternalIp());
		lbl_internalIP.setValue(dto.getInternalIp());
		lbl_LastSeen.setValue(dto.getLocalTime());
		lbl_LastTimestamp.setValue(dto.getTimestamp());
		lbl_Load.setValue(dto.getLoad());
		lbl_Memory.setValue(dto.getMem());
		lbl_Uptime.setValue(dto.getUptime());
		lbl_Wifi.setValue(dto.getWifi());
		lbl_MAC.setValue(dto.getMac());
		lbl_Name.setValue(dto.getName());
	}

}
