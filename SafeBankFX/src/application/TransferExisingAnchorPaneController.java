package application;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;

import javafx.scene.control.ComboBox;

import javafx.scene.input.InputMethodEvent;

public class TransferExisingAnchorPaneController extends Controller implements Initializable {
	@FXML
	private ComboBox<String> cbBenName;
	@FXML
	private ComboBox<String> cbBenAccNums;
	@FXML
	private TextField txtAmount;
	@FXML
	private Button btnTransferFunds;

	// Event Listener on ComboBox[#cbBenName].onAction
	@FXML
	public void displayAddedBeneficiaryNames(ActionEvent event) {
		// TODO Autogenerated
	}
	// Event Listener on ComboBox[#cbBenAccNums].onAction
	@FXML
	public void displayBeneficiaryAccNumbers(ActionEvent event) {
		// TODO Autogenerated
	}

	// Event Listener on Button[#btnTransferFunds].onAction
	@FXML
	public void handleTransferFundsAction(ActionEvent event) {
		// TODO Autogenerated
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		refreshState();
		cbBenAccNums.setVisible(false);
		btnTransferFunds.setVisible(false);
	}
}
