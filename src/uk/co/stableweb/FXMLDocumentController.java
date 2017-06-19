/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.stableweb;

import java.io.File;
import java.io.IOException;
import javafx.scene.input.KeyEvent;
import java.net.URL;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author isuru
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private TextField md5ValueTextField;

    @FXML
    private TextField selectedFileTextField;

    @FXML
    private Button selectFileButton;

    @FXML
    private Button calculateButton;

    @FXML
    private Label statusLabel;

    @FXML
    private HBox fileMD5ValueHBox;

    @FXML
    private TextField fileMD5ValueTextField;

    private File file;

    @FXML
    private void handleCalculateButtonAction(ActionEvent event) {

        //check whether user enters correct md5 value
        if (!isValidMD5(md5ValueTextField.getText())) {
            // If the value is not valid change the status to inform the user.
            statusLabel.setText("Please enter a valid MD5 value.");
        } else {
            // If the value is a valid string show this message.
            statusLabel.setText("Entered MD5 value is valid.");

            if (file.exists() && file != null) {
                // File exists and file is not null, lets compare the values.
                String fileChecksumValue = generateChecksum(file);
                if (fileChecksumValue != null) {
                    if (fileChecksumValue.equals(md5ValueTextField.getText())) {
                        showAlertBox(AlertType.INFORMATION, "Checksum check!", "Checksum Successful!", "User entered MD5 value is same as the file MD5.");
                        statusLabel.setText("MD5 values are same.");
                    } else {                        
                        showAlertBox(AlertType.INFORMATION, "Checksum check!", "Checksum Failed!", "User entered MD5 value is different from the file MD5.");
                        statusLabel.setText("MD5 values are different.");
                    }
                } else {
                    statusLabel.setText("generateChecksum method returned null value");
                }
            } else {
                // If the file is not available or null show the following message.
                statusLabel.setText("Select a file to compare values.");
            }

        }

    }

    @FXML
    private void handleMD5ValueEntered(KeyEvent ke) {
        //check whether user enters correct md5 value
        if (!isValidMD5(md5ValueTextField.getText())) {
            // If the value is not valid change the status to inform the user.
            statusLabel.setText("Please enter a valid MD5 value to compare.");
        } else {
            // If the value is a valid string show this message.
            statusLabel.setText("Entered MD5 value is valid.");
        }
    }

    @FXML
    private void handleSelectFileButtonAction(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select the file...");
        file = fileChooser.showOpenDialog(null);

        if (file.exists() && file != null) {
            selectedFileTextField.setDisable(false);
            selectedFileTextField.setText(file.getAbsolutePath());
            fileMD5ValueHBox.setVisible(true);
            fileMD5ValueTextField.setText(generateChecksum(file));
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Disable the file path TextField
        selectedFileTextField.setDisable(true);
        // Hide the HBox for MD5 (file) value
        fileMD5ValueHBox.setVisible(false);
    }

    /**
     * Checks passed value is a valid MD5 value. The value returned is a
     * boolean:
     *
     * @param s the string value to check.
     * @return the boolean value.
     */
    private boolean isValidMD5(String s) {
        return s.matches("[a-fA-F0-9]{32}");
    }

    private String generateChecksum(File file) {

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(Files.readAllBytes(file.toPath()));
            byte[] hash = messageDigest.digest();

            return DatatypeConverter.printHexBinary(hash).toUpperCase();
        } catch (IOException | NoSuchAlgorithmException e) {
            statusLabel.setText(e.getMessage());
        }
        // Returns null value
        return null;

    }

    private void showAlertBox(AlertType e, String title, String message, String contentText) {
        Alert alert = new Alert(e);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.setContentText(contentText);

        alert.showAndWait();
    }

}
