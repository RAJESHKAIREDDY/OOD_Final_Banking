package application;


import java.util.Map;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AlertController extends Controller {
	
	public static boolean showConfirmation(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
        return alert.getResult() == ButtonType.OK;
    }

    public static void showError(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

	public static boolean showConfirmation(ActionEvent event, Map<String, String> alertData) {
		// TODO Auto-generated method stub
		return false;
	}
}
