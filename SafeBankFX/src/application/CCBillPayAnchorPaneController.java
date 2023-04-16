package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.VBox;


public class CCBillPayAnchorPaneController extends Controller implements Initializable {
	
	@FXML
    private ToggleGroup ChangeAmountLimit;

    @FXML
    private Button btnReset;

    @FXML
    private ComboBox<String> cbAccounts;

    @FXML
    private RadioButton radioBtnCustom;

    @FXML
    private RadioButton radioBtnFull;

    @FXML
    private RadioButton radioBtnMin;

    @FXML
    private TextField txtAmount;

    @FXML
    private VBox vBoxDetails;
	

    @FXML
    public void displayAccounts(ActionEvent event) {
    	String selectedValue = cbAccounts.getSelectionModel().getSelectedItem();
    	if(selectedValue != null) {
    		vBoxDetails.setVisible(true);
    		txtAmount.setDisable(true);
    		btnReset.setVisible(true);
    	}
    }

    @FXML
    public void handleChangeAmountLimit(ActionEvent event) {
    	if(radioBtnFull.isSelected()) {
    		txtAmount.setDisable(true);
    		txtAmount.setText("5000.0");
    	}
    	else if(radioBtnMin.isSelected()) {
    		txtAmount.setDisable(true);
    		txtAmount.setText("500.0");
    	}
    	else {
    		txtAmount.setDisable(false);
    		txtAmount.setText("");
    	}
    }

    @FXML
    public void handleReset(ActionEvent event) {
    	System.out.println("working");
    	cbAccounts.getSelectionModel().clearSelection();
    	cbAccounts.setButtonCell(new PromptButtonCell<>(cbAccounts.getPromptText()));
    	vBoxDetails.setVisible(false);
    	btnReset.setVisible(false);
    	radioBtnFull.setSelected(true);
    	txtAmount.setText("5000.0");
    }

    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		ObservableList<String> accountNumbersList = 
				FXCollections.observableArrayList("94467845362", "78546789012");
		cbAccounts.setItems(accountNumbersList);
		vBoxDetails.setVisible(false);
		btnReset.setVisible(false);
		txtAmount.setText("5000.0");
	}
}
