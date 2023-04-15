package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.mail.MessagingException;

import dao.UsersDAO;
import enums.CardCategory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import models.CreditCard;
import notifications.EmailService;
import services.CreditCardsService;
import utils.CreditCardUtils;
import validations.UserValidations;

public class ProfileInfoSceneController extends Controller implements Initializable {
	@FXML
	private Label lblHome;
	@FXML
	private Button btnLogout;
	@FXML
	private Label lblCurrentUserEmail;
	@FXML
	private Button btnGoBack;
	@FXML
	private Label lblName;
	@FXML
	private TextField txtName;
	@FXML
	private Label lblEmail;
	@FXML
	private TextField txtEmail;
	@FXML
	private Label lblPhone;
	@FXML
	private TextField txtPhone;
	@FXML
	private Button btnChangePassword;
	@FXML
	private Button btnChangeNumber;
	@FXML
	private Button btnChangeName;
	@FXML
	private Button btnEditName;
	@FXML
	private Button btnEditNumber;
	@FXML
	private Button btnRequestCreditCardUpgrade;
	@FXML
	private Button btnViewCreditScore;
	@FXML
	private AnchorPane anchorPaneChangePassword;

	@FXML
	public void handleChangeNameAction(ActionEvent event) throws IOException, MessagingException {
		if (txtName.isDisabled()) {
			// change
			txtName.setDisable(false);
			txtName.setEditable(true);
			btnChangeName.setText("Save Changes");
		} else {
			// save change
			String title = null;
			String headerText = null;
			String contentText = null;

			String name = txtName.getText();
			boolean isNameValid = UserValidations.isNameValid(name);
			if (!isNameValid) {
				headerText = "Invalid Name Format";
				AlertController.showError(title, headerText, contentText);
				return;
			} else {
				UsersDAO.updateUserName(user.getUserId().toString(), name);
				txtName.setDisable(true);
				btnChangeName.setText("Change Name");
				headerText = "Updated User Name Successfully";

				String toEmail = user.getEmail();
				String subject = "SafeBank Update Account Details";
				String message = "Your account name has been updated";

				EmailService.sendEmail(toEmail, subject, message);
				AlertController.showSuccess(title, headerText, contentText);
				return;

			}
		}
	}

	@FXML
	public void handleChangeNumberAction(ActionEvent event) throws IOException, MessagingException {
		if (txtPhone.isDisabled()) {
			// change
			txtPhone.setDisable(false);
			txtPhone.setEditable(true);
			btnChangeNumber.setText("Save Changes");
		} else {
			// save change
			String title = null;
			String headerText = null;
			String contentText = null;

			String phone = txtPhone.getText();
			boolean isPhoneValid = UserValidations.isPhoneValid(phone);
			if (!isPhoneValid) {
				headerText = "Invalid Phone Number";
				AlertController.showError(title, headerText, contentText);
				return;
			} else {
				Long phoneNumber = Long.parseLong(phone);
				UsersDAO.updateUserPhone(user.getUserId().toString(), phoneNumber);
				txtName.setDisable(true);
				btnChangeName.setText("Change Number");
				headerText = "Updated User Phone Number Successfully";

				String toEmail = user.getEmail();
				String subject = "SafeBank Update Account Details";
				String message = "Your phone number has been updated";

				EmailService.sendEmail(toEmail, subject, message);
				AlertController.showSuccess(title, headerText, contentText);
				return;
			}
		}
	}

	@FXML
	public void handleChangePasswordAction(ActionEvent event) throws IOException {

		String title = null;
		String headerText = null;
		String contentText = null;
		int iterationCount = 0;
		boolean iterating = true;
		while (iterating) {
			System.out.println("ietration "+iterationCount);
			boolean passwordsMatched = DialogController.enterCurrentPasswordDialog(iterationCount);
			if (passwordsMatched) {
				AnchorPane transferOther = (AnchorPane) FXMLLoader
						.load(getClass().getResource(SceneFiles.RESET_PASSWORD_ANCHOR_PANE));
				anchorPaneChangePassword.getChildren().setAll(transferOther.getChildren());
				iterating = false;
			} else {

				if (iterationCount == 2) {
					iterating = false;
					Controller.user = null;
					Controller.isSessionActive = false;
					headerText = "Too many incorrect attempst. You have been signed out";
					AlertController.showError(title, headerText, contentText);
					SwitchSceneController.invokeLayout(event, SceneFiles.LOGIN_SCENE_LAYOUT);
					return;
				}
			}
			iterationCount++;
		}
	}

	@FXML
	public void handleRequestCreditCardUpgradeAction(ActionEvent event) throws Exception {
		String title = null;
		String headerText = null;
		String contentText = null;
		
		String userId = user.getUserId().toString();
		CreditCard userCreditCard = user.getCreditCard();
		if(userCreditCard == null) {
			int creditScore = user.getCreditScore();
			if(creditScore < 720) {
				title = "SafeBank Credit Card Request";
				headerText = "Poor Credit Score. Boost up your credit score by adding more account transactions";
				AlertController.showError(title, headerText, contentText);
				return;
			}
			else {
				int creditLimit = 0;
				CardCategory cardCategory = CreditCardUtils.getEligibleCreditCard(creditScore);
				if(cardCategory.equals(CardCategory.BURGUNDY))
					creditLimit = 10000;
				else if(cardCategory.equals(CardCategory.GOLD))
					creditLimit = 20000;
				else
					creditLimit = 30000;
				CreditCardsService.createNewCreditCard(userId, user, cardCategory);
				title = "SafeBank Credit Card Request";
				headerText = "Congratulations. We offered you a "+cardCategory+ ", with a credit limit of "+creditLimit;
				AlertController.showError(title, headerText, contentText);
				return;
			}
		}
		
		
	}

	@FXML
	public void handleViewCreditScoreAction(ActionEvent event) throws IOException {

	}

	@FXML
	public void handleLogoutAction(ActionEvent event) throws IOException {

	}

	@FXML
	public void invokeHomeSceneLayout(ActionEvent event) throws IOException {

	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		// TODO Auto-generated method stub
		txtEmail.setDisable(true);
		txtName.setDisable(true);
		txtPhone.setDisable(true);
		txtEmail.setText(user.getEmail());
		txtName.setText(user.getName());
		txtPhone.setText(user.getPhone() + "");
	}
}
