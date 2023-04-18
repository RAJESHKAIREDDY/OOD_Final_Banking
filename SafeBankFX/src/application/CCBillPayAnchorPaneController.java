package application;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import dao.CreditCardsDAO;
import dao.SavingsAccountsDAO;
import enums.PaymentStatus;
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
import models.CreditCard;
import models.SavingsAccount;
import transactions.CCBillPaymentTransaction;
import utils.SavingsAccountUtils;
import validations.TransactionValidations;


public class CCBillPayAnchorPaneController extends Controller implements Initializable {
	
	@FXML
    private ToggleGroup ChangeAmountLimit;

    @FXML
    private Button btnReset;
    
    @FXML
    private Button btnCCBillPay;

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
    
	private Map<String, Long> displayAccountNumbersMapping;
	private Map<String, Long> displayCardNumbersMapping;
	private String selectedDisplayAccountNumber;
	private String selectedDisplayCardNumber;
	private SavingsAccount currentSelectedAccount;
    
	private int incorrectOTPAttempts;
	private int invalidOTPAttemptsCount;
	private int insufficientFundsAttemptsCount;
	
    @FXML
    public void handleCCBillPayAction(ActionEvent event) throws Exception {
    
    	String title = null;
		String headerText = null;
		String contentText = null;
		
		selectedDisplayAccountNumber = SavingsAccountUtils
		.getLastFourDigitsOf(currentSelectedAccount.getAccountNumber());
		
		String userId = user.getUserId().toString();
		String accountId = currentSelectedAccount.getAccountId().toString();
		double accountBalance = currentSelectedAccount.getAccountBalance();
		
		CreditCard userCreditCard = CreditCardsDAO.getCreditCardByUserId(userId);
		String cardId = userCreditCard.getCreditCardId().toString();
		double remainingCreditLimit = userCreditCard.getRemainingCreditLimit();
		double totalCreditLimit = userCreditCard.getTotalCreditLimit();
		
    	String amountTextFieldValue = txtAmount.getText();
		boolean isAmountValid = TransactionValidations
    			.isAmountValidForCCBillPayment(amountTextFieldValue, totalCreditLimit);
		
		if(!isAmountValid) {
			title = "Invalid Amount";
			headerText = "Amount is invalid for transaction";
			contentText = "Enter a valid numerical value";
			AlertController.showError(title, headerText, contentText);
			return;
		}
		else {
			
			double amount = Double.parseDouble(amountTextFieldValue);
			CCBillPaymentTransaction ccBillPaymentTransaction = new CCBillPaymentTransaction();
			ccBillPaymentTransaction.setUserId(userId);
			ccBillPaymentTransaction.setAccountId(accountId);
			ccBillPaymentTransaction.setCardId(cardId);
			ccBillPaymentTransaction.setAccountBalance(accountBalance);
			ccBillPaymentTransaction.setRemainingCreditLimit(remainingCreditLimit);
			ccBillPaymentTransaction.setAmount(amount);
			PaymentStatus paymentStatus = ccBillPaymentTransaction.payCCBillAmount();

			if (paymentStatus == PaymentStatus.SUCCESS) {
				title = "Transaction Successful";
				headerText = "*** Successfully Paid USD " + amount;
				contentText	= "towards card number : "+selectedDisplayCardNumber+"\n"
				+ "from account number : "+selectedDisplayAccountNumber+ " ***";
				AlertController.showSuccess(title, headerText, contentText);
				return;
			} else {
				if (insufficientFundsAttemptsCount == 2 || invalidOTPAttemptsCount == 2
						|| incorrectOTPAttempts == 2) {

					title = "Transaction Blocked";
					headerText = "*** You have reached maximum allowed attempts ***";
					contentText = "For security reasons, you will be signed out";
					if(isSessionActive) {
						isSessionActive = false;
						user = null;
					}
					AlertController.showError(title, headerText, contentText);
					SwitchSceneController.invokeLayout(event, SceneFiles.LOGIN_SCENE_LAYOUT);
				} else {
					if (paymentStatus == PaymentStatus.INSUFFICIENT_FUNDS) {
						title = "Insufficient Funds";
						headerText = "*** Insufficient Funds ***";
						contentText = "Try again with different ammount or account." + " Attempts Remaining : "
								+ (2 - insufficientFundsAttemptsCount) + " ***";
						insufficientFundsAttemptsCount += 1;
					} else if (paymentStatus == PaymentStatus.INCORRECT_OTP) {
						title = "Incorrect OTP";
						headerText = "*** Incorrect OTP. Transaction Declined. " + "Attempts Remaining : "
								+ (2 - incorrectOTPAttempts) + "***";
						incorrectOTPAttempts += 1;
					} else if (paymentStatus == PaymentStatus.INVALID_OTP) {
						title = "Invalid OTP";
						headerText = "*** Invalid OTP. " + "Transaction Declined. Attempts Remaining : "
								+ (2 - invalidOTPAttemptsCount) + " ***";
						invalidOTPAttemptsCount += 1;
					} else if (paymentStatus == PaymentStatus.PAYMENT_EXCEPTION) {
						title = "Transaction Exception";
						headerText = "*** Transaction Failed .Try Again Later ***";
					}
					AlertController.showError(title, headerText, contentText);
					return;
				}
			}
		}
    }

    @FXML
    public void displayAccounts(ActionEvent event) throws IOException {
    	String selectedAccountNumber = cbAccounts.getSelectionModel().getSelectedItem();
    	if(selectedAccountNumber != null) {
    		vBoxDetails.setVisible(true);
    		txtAmount.setDisable(true);
    		btnReset.setVisible(true);
    		
    		Long accountNumber = displayAccountNumbersMapping.get(selectedAccountNumber);
			currentSelectedAccount = SavingsAccountsDAO.getSavingsAccountByAccountNumber(accountNumber);
			System.out.println(accountNumber);
    	}
    }

    @FXML
    public void handleChangeAmountLimit(ActionEvent event) throws IOException {
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
    public void handleReset(ActionEvent event) throws IOException {
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
		refreshState();
		displayAccountNumbersMapping = new HashMap<>();
		displayCardNumbersMapping = new HashMap<>();
		
		List<String> accountNumbers = user.getAccounts().stream().map(account -> {
			long accountNumber = account.getAccountNumber();
			String displayAccountNumber = SavingsAccountUtils.getLastFourDigitsOf(accountNumber);
			displayAccountNumbersMapping.put(displayAccountNumber, accountNumber);
			return displayAccountNumber;
		}).collect(Collectors.toList());

		ObservableList<String> accountNumbersList = FXCollections.observableArrayList(accountNumbers);
		cbAccounts.setItems(accountNumbersList);
		
		CreditCard userCreditCard = 
				CreditCardsDAO.getCreditCardByUserId(user.getUserId().toString());
		
		
		String displayCardNumber = SavingsAccountUtils.getLastFourDigitsOf(userCreditCard.getCardNumber());
		displayCardNumbersMapping.put(displayCardNumber, userCreditCard.getCardNumber());
		
		double totalCreditLimit = userCreditCard.getTotalCreditLimit();
		double remainingCreditLimit = userCreditCard.getRemainingCreditLimit();
		Double totalPayableAmount = totalCreditLimit - remainingCreditLimit;
		
		vBoxDetails.setVisible(false);
		btnReset.setVisible(false);
		txtAmount.setText(totalPayableAmount.toString());
	}
}
