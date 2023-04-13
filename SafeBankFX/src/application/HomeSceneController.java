package application;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;

import javafx.scene.control.Label;
import models.User;
import services.UserService;

public class HomeSceneController  implements Initializable {
	@FXML
	private Label lblHome;
	@FXML
	private Button btnLogout;
	@FXML
	private Label lblCurrentUserEmail;
	@FXML
	private Button btnProfileInfo;
	@FXML
	private Button btnPayments;
	@FXML
	private Button btnAccounts;
	@FXML
	private Button btnTransfers;
	@FXML
	private Button btnReports;
	@FXML
	private Button btnAnalytics;

	// Event Listener on Button[#btnLogout].onAction
	@FXML
	public void handleLogoutAction(ActionEvent event) throws IOException {
		// TODO Autogenerated
		SwitchSceneController.invokeLayout(event, SceneFiles.LOGIN_SCENE_LAYOUT);
	}
	// Event Listener on Button[#btnProfileInfo].onAction
	@FXML
	public void invokeProfileInfoScene(ActionEvent event) throws IOException {
		// TODO Autogenerated
		SwitchSceneController.invokeLayout(event, SceneFiles.PROFILE_INFO_SCENE_LAYOUT);
	}
	// Event Listener on Button[#btnDeposits].onAction
	@FXML
	public void invokePaymentsScene(ActionEvent event) throws IOException {
		// TODO Autogenerated
		SwitchSceneController.invokeLayout(event, SceneFiles.PAYMENTS_SCENE_LAYOUT);
	}
	// Event Listener on Button[#btnAccounts].onAction
	@FXML
	public void invokeAccountsScene(ActionEvent event) throws IOException {
		// TODO Autogenerated
		SwitchSceneController.invokeLayout(event, SceneFiles.ACCOUNTS_SCENE_LAYOUT);
	}
	// Event Listener on Button[#btnTransfers].onAction
	@FXML
	public void invokeTransfersScene(ActionEvent event) throws IOException {
		// TODO Autogenerated
		SwitchSceneController.invokeLayout(event, SceneFiles.TRANSFERS_SCENE_LAYOUT);
	}
	// Event Listener on Button[#btnReports].onAction
	@FXML
	public void invokeReportsScene(ActionEvent event) throws IOException {
		// TODO Autogenerated
		SwitchSceneController.invokeLayout(event, SceneFiles.REPORTS_SCENE_LAYOUT);
	}
	// Event Listener on Button[#btnAnalytics].onAction
	@FXML
	public void invokeAnalyticsScene(ActionEvent event) throws IOException {
		// TODO Autogenerated
		SwitchSceneController.invokeLayout(event, SceneFiles.ANALYTICS_SCENE_LAYOUT);
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub		
	}
}
