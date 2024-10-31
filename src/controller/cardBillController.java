package controller;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;


import DAO.DAO_Bill;
import application.BillReport;
import application.Support;
import database.ConnectionToDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import model.Bill;

public class cardBillController implements Initializable {
	
	@FXML
    private AnchorPane anchorpane;

    @FXML
    private Label billPriceLabel;

    @FXML
    private Label HomeTownIDLabel;

    @FXML
    private GridPane formGridPane;

    @FXML
    private DatePicker invoiceDatePicker;

    @FXML
    private Label RoomIDLabel;
    
    private Map<String, Object> map;
    private ObservableList<Bill> StateBill;
    Connection connection;
    
    public void setData(Bill tmp) {
    	HomeTownIDLabel.setText(tmp.getHomeTownID());
    	RoomIDLabel.setText(tmp.getRoomID());
		invoiceDatePicker.setValue(tmp.getInvoiceDate());
		
		BigDecimal num = new BigDecimal(tmp.getBillPrice());
		billPriceLabel.setText(NumberFormat.getNumberInstance(Locale.US).format(num));	
	}
	// hàm ấn nút xóa
	public void deleteBillForm(ActionEvent event) {
		StateBill.addAll(DAO_Bill.getInstance().
				selectByCondition(HomeTownIDLabel.getText(), RoomIDLabel.getText(), null));
		
		DAO_Bill.getInstance().deleteData(StateBill.get(0), null);
		
		BillController.lst.remove(this.anchorpane);
		Support.billList.clear();
		Support.billList.addAll(DAO_Bill.getInstance().selectALL());
	}
	
	// Hàm ấn nút in
	public void PrintPDF(ActionEvent event) throws ClassNotFoundException {
		DAO_Bill.getInstance().deleteDataFromTempTable(null);
		Bill bill = DAO_Bill.getInstance().selectObject(HomeTownIDLabel.getText(), RoomIDLabel.getText());
		DAO_Bill.getInstance().insertDataToTempTable(bill);
		connection = ConnectionToDatabase.getConnectionDB();
		map = new HashMap<String, Object>();
		BillReport.createReport(connection, map, DAO_Bill.getInstance().getReport("report_jasper","bill_report"));
		BillReport.showReport();
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		StateBill = FXCollections.observableArrayList();
		
	}
}
