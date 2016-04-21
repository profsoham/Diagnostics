package com.prophecysensorlytic.daig.auth.zk.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.zkoss.json.JSONObject;
import org.zkoss.json.parser.JSONParser;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.google.zxing.WriterException;
import com.prophecysenorlytic.diag.resthandlers.HttpUtils;
import com.prophecysensorlytic.daig.auth.UtilCrypto_Web;
import com.prophecysensorlytic.daig.auth.UtilQR;
import com.prophecysensorlytic.daig.auth.web.Filter_Firewall;

public class Conmtroller_AuthQR extends SelectorComposer<Window> {

	private static final long serialVersionUID = -6134941988311935425L;

	private static final String _CHALLENGE = "CHALLENGE";
	private String uid;
	private static final String _REST_URL_C_BY_UID = "http://crystalball.prophecysensorlytics.com:80/diag/sdbun?uid=%s";

	private String C = null;
	@Wire
	Image imgQR;
	@Wire
	Hbox hb_AuthPane;

	@Wire
	Window win_QR;
	@Wire
	Textbox tb_Challenge;
	@Wire
	Button btn_Login, btnAuth;

	@Wire
	Textbox tb_UserName;

	private long R;

	@Override
	public void doAfterCompose(Window comp) throws Exception {

		super.doAfterCompose(comp);

	}

	@Listen("onClick= #btn_Login")
	public void chekcLogin() {
		String strPassword = tb_Challenge.getText().trim();
		String str_R = String.valueOf(R);

		String response = UtilCrypto_Web.getUserResponseAsGiven(str_R, C);

		String shortResponse = response.substring(response.length() - 5);
		System.out.println(shortResponse);
		if (strPassword.equals(shortResponse)) {
			Sessions.getCurrent().setAttribute(Filter_Firewall._DIAG_USER_ID, uid);
			Messagebox.show("You will be redirected to Diagnostics Portal", "Successfully Logged in", Messagebox.OK,
					Messagebox.INFORMATION);
			Executions.sendRedirect("diag_home.zul");
		} else {
			Clients.showNotification("Error in Login", Clients.NOTIFICATION_TYPE_ERROR, btn_Login, "before_start",
					3000);
		}

	}

	@Listen(Events.ON_OK + " = #tb_UserName")
	public void onOK_tb_UserName() {
		generateQR();

	}

	@Listen(Events.ON_CLICK + " =#btnAuth")
	public void onClick_btnAuth() {
		generateQR();
	}

	public void generateQR() {
		uid = tb_UserName.getText().trim();

		try {
			String responseJson = HttpUtils.readGetResponse(String.format(_REST_URL_C_BY_UID, uid), "application/json",
					"application/json");
			
			JSONParser parser = new JSONParser();
			JSONObject rootObj = (JSONObject) parser.parse(responseJson);
			if ((boolean) rootObj.get("success")) {
				C = (String) rootObj.get("data");
				Object[] arr = UtilQR.createRandomQR(300, 300);
				R = (long) arr[0];
				System.out.println("R = " + R);
				BufferedImage qrImage = (BufferedImage) arr[1];
				imgQR.setContent(qrImage);
				Sessions.getCurrent().setAttribute(_CHALLENGE, R);
				hb_AuthPane.setVisible(true);
			} else {
				tb_UserName.setErrorMessage((String) rootObj.get("msg"));
				hb_AuthPane.setVisible(false);
				return;
			}
		} catch (ClientProtocolException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (WriterException e) {

			e.printStackTrace();
		}

	}

}
