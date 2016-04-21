package com.prophecysenorlytic.diag.ui.zk.controllers;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.zkoss.zhtml.Head;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Html;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Popup;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.prophecysenorlytic.diag.dao.DaoRpiDetails;
import com.prophecysenorlytic.diag.dao.DaoRpiDetails_OldTable;
import com.prophecysenorlytic.diag.dao.util.Util_CSV;
import com.prophecysenorlytic.diag.dto.RpiDetails;
import com.prophecysenorlytic.diag.resthandlers.RestAPI_CompanyDetails;
import com.prophecysenorlytic.diag.resthandlers.RestAPI_FetchCompanies;

/**
 * This Controller is bound to a ZUL page which displays heart-beat from
 * Raspberry-PI
 *
 * @author Soham Sengupta
 * @version 1.0
 * @since 2016-03-31
 */
public class Controller_View_Rpi_Records extends SelectorComposer<Window> {

	private RpiDetails currentDto;
	private static final long serialVersionUID = -7005689666335640984L;
	private static final int _MAX_NUMBER_OF_RECORDS = 50;

	/* This is a provisional one for Hellerman */
	boolean isHellerman = false;

	private int offset = 0;

	@Wire
	Popup popup_Json;
	@Wire
	Combobox cmb_Companies;
	@Wire
	Window win_Status_rpi;
	@Wire
	Label lbl_Name, lbl_MAC;
	@Wire
	Hbox hb_LastdayPicker;
	@Wire
	Hbox hb_DatePicker;
	@Wire
	Checkbox cb_PickInterval;
	@Wire
	Combobox cmb_NoOfDays, cmb_Src;

	@Wire
	Datebox dt_Start;
	@Wire
	Datebox dt_End;

	@Wire
	Textbox tb_MAC;
	@Wire
	Intbox tb_LastNoOfDays;
	@Wire
	Button btn_Fetch, btn_Download, btn_QuickInfo;
	@Wire
	Listbox lst_Rpi;
	@Wire
	Menupopup popupMenu_Rpi;
	@Wire
	Popup popup_LI;
	@Wire
	Label lbl_JSON;

	@Wire
	Html result_html;

	@Wire
	Grid grd_Information;
	private Menupopup menu;
	private Date startDate;
	private Date endDate;
	private String mac;
	private List<RpiDetails> rpisList;
	private RestAPI_CompanyDetails details = new RestAPI_CompanyDetails(true);

	@Override
	public void doAfterCompose(Window comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		initCompanyCombo();

		initCombo_dayChooser();
	}

	@Listen("onSelect =#cmb_Companies")
	public void onCompanySelected() {
		String companyNameSelected = cmb_Companies.getValue();
		isHellerman = companyNameSelected.equalsIgnoreCase("HellermannTyton");
		if (!isHellerman) {
			this.rpisList = DaoRpiDetails.getRpi_ByCompanyName(companyNameSelected);
		} else {
			this.rpisList = DaoRpiDetails_OldTable.getRpi_ByCompanyName("hellerman"); // paramter
																						// is
																						// not
																						// used
																						// now
		}
		initListbox_Rpi();
	}

	private void initCompanyCombo() {
		List<String> comapanyNames = details.getAllCompanyNames();
		cmb_Companies.setModel(new ListModelList<>(comapanyNames));
		cmb_Companies.invalidate();

	}

	private void initCombo_dayChooser() {
		Integer[] items = { 1, 3, 7, 14, 30 };
		cmb_NoOfDays.setModel(new ListModelList<Integer>(items));
		cmb_NoOfDays.invalidate();
	}

	private void initListbox_Rpi() {

		SimpleListModel<RpiDetails> model = new SimpleListModel<RpiDetails>(rpisList);
		model.setMultiple(true);
		lst_Rpi.setModel(model);

		lst_Rpi.invalidate();
		lst_Rpi.setItemRenderer(new RowRenderer_Rpi(win_Status_rpi, this));
		// rpisList = null;
		// System.gc();
	}

	@Listen("onSelect =#lst_Rpi")
	public void populateMac() {
		try {
			tb_MAC.setText(((RpiDetails) lst_Rpi.getSelectedItem().getValue()).getMac());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Listen("onClick =#cb_PickInterval")
	public void toggleIntervalPickerOption() {
		boolean boolDatePickerOption = cb_PickInterval.isChecked();
		hb_DatePicker.setVisible(boolDatePickerOption);
		hb_LastdayPicker.setVisible(!boolDatePickerOption);
		cb_PickInterval.setLabel(boolDatePickerOption ? "Hide Calendar" : "Open Calendar");

	}

	@Listen("onClick=#btn_Fetch")
	public void showGrid_Heartbeats() {
		mac = tb_MAC.getText();
		// String name = ((RpiDetails)
		// lst_Rpi.getSelectedItem().getValue()).getName();
		lbl_MAC.setValue(mac);
		// lbl_Name.setValue(name);
		if (cb_PickInterval.isChecked()) {
			startDate = dt_Start.getValue();
			endDate = dt_End.getValue();

		} else {
			int no_of_Days = tb_LastNoOfDays.getValue();
			endDate = new Date();
			Calendar c = Calendar.getInstance();
			c.setTime(endDate);
			c.add(Calendar.DATE, -no_of_Days);
			startDate = c.getTime();

		}
		List<RpiDetails> details =isHellerman?DaoRpiDetails_OldTable.getRpiRecordForInterval(mac, startDate, endDate, offset,
				_MAX_NUMBER_OF_RECORDS) : DaoRpiDetails.getRpiRecordForInterval(mac, startDate, endDate, offset,
				_MAX_NUMBER_OF_RECORDS);
		grd_Information.setModel(new SimpleListModel<RpiDetails>(details));
		grd_Information.setRowRenderer(new Render_RpiDetails(this));
		btn_Download.setDisabled(false);

	}

	public void showJsonPopup(String jsonString, Component cmp) {
		lbl_JSON.setValue(jsonString);
		popup_Json.open(cmp);
	}

	@Listen("onClick =#btn_Download")
	public void downloadCSV_File() {

		ListModel<RpiDetails> model = grd_Information.getListModel();
		byte[] data = Util_CSV.exportToCSV_RpiLogs(model);
		Filedownload.save(data, "text/csv", Util_CSV.getCSV_FileName(startDate, endDate, mac));

	}

	public Menupopup getPopup() {
		return popupMenu_Rpi;
	}

	public Menupopup buildPopu_Rpi(final RpiDetails dto) {
		if (null == menu) {
			menu = new Menupopup();
			menu.setParent(win_Status_rpi);
			Menuitem mnuI_LivelinessIndex = new Menuitem("Liveliness Index");
			mnuI_LivelinessIndex.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					startDate = dt_Start.getValue();
					endDate = dt_End.getValue();
					String livelinessIndexAsHtmlString = DaoRpiDetails.getLivelinessIndexAsString(dto.getMac(),
							startDate, endDate);
					result_html.setContent(livelinessIndexAsHtmlString);
					popup_LI.open(lst_Rpi.getSelectedItem(), "center");

				}
			});

			mnuI_LivelinessIndex.setParent(menu);
			Menuitem mnuI_Heartbeat = new Menuitem("Last Activity");
			mnuI_Heartbeat.setParent(menu);
			mnuI_Heartbeat.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					openDetailsInfo_HB(dto);

				}

			});
			Menuitem mnuI_viewLog = new Menuitem("Search Log");
			mnuI_viewLog.setParent(menu);
			mnuI_viewLog.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					tb_MAC.setValue(dto.getMac());
					tb_LastNoOfDays.focus();

				}
			});

			System.out.println("RowRenderer_Rpi.buildPopu_Rpi()");
		}
		return menu;

	}

	public void openDetailsInfo_HB() {
		Map<String, String> mapArgs = new HashMap<>();
		mapArgs.put("mac", currentDto.getMac());
		Window win_LastActivity = (Window) Executions.createComponents("lastbeat.zul", win_Status_rpi, mapArgs);
		win_Status_rpi.doModal();
	}

	public void openDetailsInfo_HB(final RpiDetails dto) {
		Map<String, String> mapArgs = new HashMap<>();
		mapArgs.put("mac", dto.getMac());
		Window win_LastActivity = (Window) Executions.createComponents("lastbeat.zul", win_Status_rpi, mapArgs);
		win_Status_rpi.doModal();
	}

	public void showBreifMsg(Listitem comp) {
		int numberOfDays = 3;
		try {
			numberOfDays = Integer.parseInt(cmb_NoOfDays.getValue());
		} catch (WrongValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			numberOfDays = cmb_NoOfDays.getSelectedItem().getValue();
			e.printStackTrace();
		}

		Date today = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(today);
		c.add(Calendar.DATE, -numberOfDays);
		Date dayBeforeYesteday = c.getTime();
		this.currentDto = (RpiDetails) comp.getValue();
		String htmlString = isHellerman
				? DaoRpiDetails_OldTable.getSummaryAsHtmlString(currentDto.getMac(), dayBeforeYesteday, today)
				: DaoRpiDetails.getSummaryAsHtmlString(currentDto.getMac(), dayBeforeYesteday, today);
		result_html.setContent(htmlString);
		popup_LI.open(comp);
	}

	@Listen("onClick =#btn_QuickInfo")
	public void showQuickinformation() {
		int numberOfDays = 3;
		try {
			numberOfDays = Integer.parseInt(cmb_NoOfDays.getValue());
		} catch (WrongValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			numberOfDays = cmb_NoOfDays.getSelectedItem().getValue();
			e.printStackTrace();
		}
		Set<Listitem> selectedItems = lst_Rpi.getSelectedItems();
		for (Listitem item : selectedItems) {
			RpiDetails dto = item.getValue();
			if (!isHellerman) {
				DaoRpiDetails.populateQuickInfo(dto, numberOfDays);
			} else {
				DaoRpiDetails_OldTable.populateQuickInfo(dto, numberOfDays);
			}

		}
		initListbox_Rpi();
	}

	public void disablePageUntilDataSourceChanges() {
		Clients.showBusy("Execute..."); // show a busy message to user
		Events.echoEvent("onLater", win_Status_rpi, null); // echo an event back
	}

	public void reEnablePageAfterDataSourceChanges() {
		Clients.clearBusy();
	}

	@Listen("onChange= #cmb_Src")
	public void onDataSourceChanged() {
		System.out.println("Controller_View_Rpi_Records.onDataSourceChanged()");
		reloadCompanies();
	}

	public void reloadCompanies() {
		disablePageUntilDataSourceChanges();
		boolean isMVP = cmb_Src.getSelectedIndex() == 1;
		details.setMVP(isMVP);
		initCompanyCombo();
		reEnablePageAfterDataSourceChanges();
	}

	@Listen("onClick= #grd_Information > rows > row > label")
	public void showAllInfoAsJson() {
		System.out.println("Controller_View_Rpi_Records.showAllInfoAsJson()");
	}

}
