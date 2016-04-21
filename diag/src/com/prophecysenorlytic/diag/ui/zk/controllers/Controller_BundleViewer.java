package com.prophecysenorlytic.diag.ui.zk.controllers;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.prophecysenorlytic.diag.dto.DTO_DiagBundle;
import com.prophecysenorlytic.diag.resthandlers.RestAPI_BundleViewer;
import com.prophecysenorlytic.diag.resthandlers.RestAPI_CompanyDetails;

public class Controller_BundleViewer extends SelectorComposer<Window> {

	private static final long serialVersionUID = 254310482363494703L;

	private RestAPI_CompanyDetails details = new RestAPI_CompanyDetails(true);
	@Wire
	Window win_BundleViewer;

	@Wire
	Listbox lst_BundleViewer;

	@Wire
	Intbox tb_DateRangeFigure;
	@Wire
	Textbox tb_Pin;

	@Wire
	Combobox cmb_SrcOne;
	@Wire
	Combobox cmb_Src,cmb_Companies, cmb_DateRangeUnit;
	@Wire
	Button btn_Fetch, btn_Download;

	@Override
	public void doAfterCompose(Window comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
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

	public void disablePageUntilDataSourceChanges() {
		Clients.showBusy("Execute..."); // show a busy message to user
		Events.echoEvent("onLater", win_BundleViewer, null); // echo an event
																// back
	}

	public void reEnablePageAfterDataSourceChanges() {
		Clients.clearBusy();
	}

	private void initCompanyCombo() {
		List<String> comapanyNames = details.getAllCompanyNames();
		cmb_Companies.setModel(new ListModelList<>(comapanyNames));
		cmb_Companies.invalidate();

	}

	@Listen("onClick =#btn_Fetch")
	public void fetchDiagBundleAndShow() {
		String str_CompanyId = cmb_Companies.getValue();

		boolean isMVP = cmb_Src.getSelectedIndex() == 1;
		String str_DateRange = getNumberAsString(tb_DateRangeFigure.getValue()) + cmb_DateRangeUnit.getValue();
		List<DTO_DiagBundle> lstdiagBndl = RestAPI_BundleViewer.getdiagnosticBundleByCompanyAndDateRange(str_CompanyId,
				str_DateRange, isMVP);
		SimpleListModel<DTO_DiagBundle> model = new SimpleListModel<>(lstdiagBndl);
		lst_BundleViewer.setModel(model);
		lst_BundleViewer.setItemRenderer(new ListItemRendeer_DiagBundl(this));

	}

	@Listen("onClick =#btn_Download")
	public void downloadLogByPin() {
		String pin = tb_Pin.getText();
		boolean isMVP = cmb_SrcOne.getSelectedIndex() == 1;
		String url = RestAPI_BundleViewer.getUrl_BundleByPin(pin, isMVP);
		try {
			Filedownload.save(new URL(url), "text/plain");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String getNumberAsString(Integer value) {
		String[] numbers = { "one", "two", "three", "four", "five", "six", "seven", "eight", "nine" };
		return numbers[value-1];
	}
}
