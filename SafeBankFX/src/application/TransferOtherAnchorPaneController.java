package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleGroup;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;

import javafx.scene.layout.AnchorPane;

import javafx.scene.control.RadioButton;

public class TransferOtherAnchorPaneController implements Initializable {
	@FXML
	private RadioButton radioBtnBenifExist;
	@FXML
	private ToggleGroup beneficiaryTransferOptions;
	@FXML
	private RadioButton radioBtnBenifNew;
	@FXML
	private AnchorPane anchorPane;

	// Event Listener on RadioButton.onAction
	@FXML
	public void handleBeneficiaryTransferOptions(ActionEvent event) throws IOException {
		// TODO Autogenerated
		if(radioBtnBenifExist.isSelected()) {
			System.out.println("EXISTING");
			AnchorPane transferExisting = (AnchorPane)FXMLLoader.load(getClass().getResource(SceneFiles.TRANSFER_EXISTING));
			anchorPane.getChildren().setAll(transferExisting.getChildren());
		}
		else if(radioBtnBenifNew.isSelected()) {
			System.out.println("ADD_NEW");
			AnchorPane adNewBen = (AnchorPane)FXMLLoader.load(getClass().getResource(SceneFiles.ADD_NEW_BENEFICIARY));
			anchorPane.getChildren().setAll(adNewBen.getChildren());
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		try {
			AnchorPane transferOther = (AnchorPane)FXMLLoader.load(getClass().getResource(SceneFiles.TRANSFER_EXISTING));
			anchorPane.getChildren().setAll(transferOther.getChildren());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
