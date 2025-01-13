package com.front.app;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class RegisterController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}