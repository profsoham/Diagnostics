package com.prophecysenorlytic.diag.ui.zk.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Panel;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Window;

import com.prophecysenorlytic.diag.dto.DTO_MachineJson;
import com.prophecysenorlytic.diag.dto.DTO_Sensor;
import com.prophecysenorlytic.diag.dto.DTO_SensorStat;
import com.prophecysenorlytic.diag.dto.DTO_Zone;
import com.prophecysenorlytic.diag.resthandlers.RestAPI_CompanyDetails;
import com.prophecysenorlytic.diag.resthandlers.RestAPI_FetchCompanies;

public class Controller_Machine_View_Sensor extends SelectorComposer<Window> {

	private static final long serialVersionUID = -7933190209751436944L;

	private String company;
	private RestAPI_CompanyDetails details=null;
	private boolean isMVP;
	

	@Wire
	Window win_MachineSensorView;

	@Wire

	Combobox cmb_Companies,cmb_Src;

	@Wire
	Button btn_Serach;

	@Wire
	Panel pnl_Zones, pnl_Machines, pnl_Sensors;

	@Wire
	Listbox lst_Zones, lst_Machine, lst_Sensors;
	private void initCompanyCombo() {
		List<String> comapanyNames = details.getAllCompanyNames();
		cmb_Companies.setModel(new ListModelList<>(comapanyNames));
		cmb_Companies.invalidate();

	}

	public Controller_Machine_View_Sensor() {
		details=new RestAPI_CompanyDetails(true);
	}

	@Override
	public void doAfterCompose(Window comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		populateComboWithCompanies();
	}

	private void populateComboWithCompanies() {
		List<String> comapanyNames = RestAPI_FetchCompanies.getCompanyNames();
		cmb_Companies.setModel(new ListModelList<>(comapanyNames));
		cmb_Companies.invalidate();

	}

	@Listen("onClick= #btn_Serach")
	public void populateZoneOfACompany() {
		this.company = cmb_Companies.getValue();
		pnl_Zones.setTitle("Zones under " + this.company);
		List<DTO_Zone> zones = this.details.getZonesForCompany(cmb_Companies.getSelectedIndex());

		SimpleListModel<DTO_Zone> zonesModel = new SimpleListModel<>(zones);
		lst_Zones.setModel(zonesModel);
		lst_Zones.setItemRenderer(new ZoneItemRenderer(this));
		lst_Zones.invalidate();
	}

	@Listen("onSelect =#lst_Zones")
	public void onSelect_ZoneShowMachines() {
		DTO_Zone selectedZone = lst_Zones.getSelectedItem().getValue();
		pnl_Machines.setTitle("Machines under Zone # " + selectedZone.getId());
		 List<DTO_MachineJson> machines=details.getMachinesUnderZoneOfComapny(lst_Zones.getSelectedIndex());
		
		SimpleListModel<DTO_MachineJson> machinesModel = new SimpleListModel<DTO_MachineJson>(machines);
		lst_Machine.setModel(machinesModel);
		lst_Machine.setItemRenderer(new MachineItemRenderer(this));
		lst_Machine.invalidate();

	}

	@Listen("onSelect =#lst_Machine")
	public void onSelect_MachineShowZones() {
		DTO_MachineJson selectedMachine = lst_Machine.getSelectedItem().getValue();
		pnl_Sensors.setTitle("Sensors for Machine # " + selectedMachine.get_id());
		 List<DTO_Sensor> sensors =details.getSensorsForByMachineId(lst_Machine.getSelectedIndex());
		
		
		SimpleListModel<DTO_Sensor> sensorsModel = new SimpleListModel<DTO_Sensor>(sensors);
		lst_Sensors.setModel(sensorsModel);
		lst_Sensors.setItemRenderer(new SensorItemRenderer(this,this.isMVP));
		lst_Sensors.invalidate();

	}

	@Listen("onSelect =#lst_Sensors")
	public void onClickSensorList() {
		DTO_Sensor selectedSensor = lst_Sensors.getSelectedItem().getValue();
		showPopup_HistorySensorStats(selectedSensor);

	}
	
	@Listen("onChange= #cmb_Src")
	public void onDataSourceChanged() {
		System.out.println("Controller_View_Rpi_Records.onDataSourceChanged()");
		reloadCompanies();
	}
	public void disablePageUntilDataSourceChanges() {
		Clients.showBusy("Execute..."); // show a busy message to user
		Events.echoEvent("onLater", win_MachineSensorView, null); // echo an event back
	}

	public void reEnablePageAfterDataSourceChanges() {
		Clients.clearBusy();
	}
	public void reloadCompanies() {
		disablePageUntilDataSourceChanges();
		this.isMVP = cmb_Src.getSelectedIndex() == 1;
		details.setMVP(isMVP);
		initCompanyCombo();
		reEnablePageAfterDataSourceChanges();
	}

	private void showPopup_HistorySensorStats(DTO_Sensor selectedSensor) {
		Map<String, Object> mapArgs = new HashMap<>();
		mapArgs.put("args", selectedSensor);
		mapArgs.put("company", details.getCurrentCompanyName());
		mapArgs.put("machine", details.getCurrentMachine());
		mapArgs.put("isMVP", cmb_Src.getSelectedIndex()==1);
		
		Window winModal_HistorySensorStats = (Window) Executions.createComponents("sensor_hisotry.zul",
				win_MachineSensorView, mapArgs);
		winModal_HistorySensorStats.doModal();

	}

	void showPopup_sensorState(DTO_SensorStat stat, Component parentComponent) {
		String msg = String.format("Last Heartbeat: %s at %d packets/second", stat.getDate(), stat.getRate());
		Clients.showNotification(msg, Clients.NOTIFICATION_TYPE_INFO, parentComponent, "before_start", 6000, true);
	}

}
