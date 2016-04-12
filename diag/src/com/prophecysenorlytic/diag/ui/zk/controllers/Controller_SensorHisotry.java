package com.prophecysenorlytic.diag.ui.zk.controllers;

import java.util.List;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

import com.prophecysenorlytic.diag.dao.util.Util_CSV;
import com.prophecysenorlytic.diag.dao.util.Util_Formatters;
import com.prophecysenorlytic.diag.dto.DTO_MachineJson;
import com.prophecysenorlytic.diag.dto.DTO_Sensor;
import com.prophecysenorlytic.diag.dto.DTO_SensorStat;
import com.prophecysenorlytic.diag.resthandlers.RestAPI_CurrentSensorStats;
import com.prophecysenorlytic.diag.resthandlers.RestAPI_HistorySensorStats;

public class Controller_SensorHisotry extends SelectorComposer<Window> {

	private static final long serialVersionUID = -5088230035803213458L;
	private DTO_Sensor sensor;
	private List<DTO_SensorStat> stats = null;
	private SimpleListModel<DTO_SensorStat> model_History;
	private boolean isMVP;
	private DTO_MachineJson machine;
	private String companyName;
	 

	@Wire
	Window win_SensorStatsHistory;

	@Wire
	Combobox cmb_MVP, cmb_Unit;
	@Wire
	Intbox tb_Interval;
	@Wire
	Toolbarbutton btn_Download, btn_DownloadCSV;
	@Wire
	Toolbarbutton btn_Show;

	@Wire
	Label lbl_ServerTime, lbl_LastActivity;

	@Wire
	Grid grd_History;
	private String strLastActivity;

	@Override
	public void doAfterCompose(Window comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		this.sensor = (DTO_Sensor) (Executions.getCurrent().getArg().get("args"));
		this.companyName=(String)(Executions.getCurrent().getArg().get("company"));
		this.machine=(DTO_MachineJson)(Executions.getCurrent().getArg().get("machine"));
		if (Executions.getCurrent().getArg().containsKey("isMVP")) {
			this.isMVP = (Boolean) (Executions.getCurrent().getArg().get("isMVP"));
			
			cmb_MVP.setSelectedIndex(isMVP ? 1 : 0);
			cmb_MVP.setDisabled(true);
		}else{
			cmb_MVP.setSelectedIndex(0);
		}

		this.lbl_ServerTime.setValue(Util_Formatters.formatNow("yyyy-MM-dd HH:mm:ss"));
		showBasicInformation();
	}

	private void showBasicInformation() {
		 String strFormat="Company: %s # Machine Name: %s # Sensor ID: %s";
		 win_SensorStatsHistory.setTitle(String.format(strFormat, this.companyName,this.machine.getName(),Util_Formatters.getFormattedMAC(this.sensor.get_id())));
		
	}

	@Listen("onClick =#btn_Show")
	public void fetchHistory() {
		boolean isOkay = true;
		if (tb_Interval.getText().length() == 0 || tb_Interval.getValue() <= 0) {
			tb_Interval.setErrorMessage("Value must be greater than ZERO");
			isOkay = false;
		}
		if (cmb_MVP.getSelectedIndex() == -1) {
			cmb_MVP.setErrorMessage("MVP or BETA ... where to point?");
			isOkay = false;
		}
		if (cmb_Unit.getSelectedIndex() == -1) {
			cmb_Unit.setErrorMessage("Unit must be chosen");
			isOkay = false;
		}
		System.out.println("Controller_SensorHisotry.fetchHistory() " + isOkay);
		if (isOkay) {
			int duration = tb_Interval.getValue();
			int unit = Integer.parseInt((String) cmb_Unit.getSelectedItem().getValue());
			
			System.out.println("Controller_SensorHisotry.fetchHistory()");
			this.stats = RestAPI_HistorySensorStats.getSensorStatsHistory(sensor, duration, unit, isMVP); 
			if (null == stats || stats.size() == 0) {
				Clients.showNotification("No Records in Hisotry", Clients.NOTIFICATION_TYPE_ERROR, btn_Show,
						"before_start", 5000, true);
			} else {
				this.model_History = new SimpleListModel<>(stats);
				grd_History.setModel(model_History);
				btn_Download.setDisabled(false);
				btn_DownloadCSV.setDisabled(false);
				if (null == strLastActivity) {
					this.strLastActivity = RestAPI_CurrentSensorStats
							.getSensorCurrentStatsBySensorIdAsString(this.sensor.get_id(), this.isMVP);
				}
				this.lbl_LastActivity.setValue(strLastActivity);
			}
		}

	}

	@Listen("onClick =#btn_Download,#btn_DownloadCSV")
	public void downloadReport() {
		Util_CSV.downloadCSV_SensorDiagnosticsLog(this.model_History);
	}

}
