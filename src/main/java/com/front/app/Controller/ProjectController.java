package com.front.app.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import javafx.scene.control.Button;
import com.front.app.SessionManager;

import com.front.app.Controller.TaskController;

public class ProjectController {

    @FXML
    private VBox projectList;

    @FXML
    private ScrollPane projectScrollPane;

    @FXML
    private Label statusLabel;

    @FXML
    private void initialize() {
        // Charger les projets via l'API
        loadProjects();
    }

    @FXML
    private void AddProjectButton(ActionEvent event) {
        try {
            // Charge la vue addProjectView.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/front/app/addProjectView.fxml"));
            Parent root = loader.load();

            // Obtient la scène actuelle et remplace son contenu
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Add project");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadProjects() {
        try {
            // URL de l'API
            URL url = new URL("http://localhost:8080/api/projects/me");

            // Connexion HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            // Ajouter le token JWT à l'en-tête Authorization
            String jwtToken = SessionManager.getJwtToken();
            if (jwtToken == null || jwtToken.isEmpty()) {
                statusLabel.setText("Erreur : Token JWT manquant. Veuillez vous reconnecter.");
                return;
            }
            connection.setRequestProperty("Authorization", "Bearer " + jwtToken);

            // Lecture de la réponse
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                // Afficher les projets directement à partir de la réponse brute
                populateProjectsFromRawJson(response.toString());

                statusLabel.setText("Projets chargés avec succès !");
            } else {
                statusLabel.setText("Erreur : Impossible de charger les projets. Code : " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Erreur : Une erreur s'est produite lors du chargement des projets.");
        }
    }

    private void populateProjectsFromRawJson(String jsonResponse) {
        // Extraire manuellement les informations depuis la réponse JSON brute
        projectList.getChildren().clear();

        String[] projects = jsonResponse.split("\\},\\{"); // Séparer les objets JSON individuels
        for (String project : projects) {
            String id = extractValue(project, "id");
            String name = extractValue(project, "name");
            String description = extractValue(project, "description");

            // Conteneur pour le projet (Label + Button)
            VBox projectContainer = new VBox();
            projectContainer.setStyle("-fx-padding: 10; -fx-border-color: lightgray; -fx-background-color: #f9f9f9;");
            projectContainer.setSpacing(5);

            // Label pour afficher les détails du projet
            Label projectLabel = new Label("Nom : " + name + " - Description : " + description);

            // Bouton pour récupérer l'ID du projet
            Button actionButton = new Button("Sélectionner");
            actionButton.setOnAction(event -> {
                redirectToTaskView(id);
            });

            // Ajouter le label et le bouton dans le conteneur
            projectContainer.getChildren().addAll(projectLabel, actionButton);

            // Ajouter le conteneur au VBox principal
            projectList.getChildren().add(projectContainer);
        }
    }
    private void redirectToTaskView(String projectId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/front/app/taskView.fxml"));
            Parent root = loader.load();

            // Obtenir le contrôleur de la vue
            TaskController taskController = loader.getController();
            taskController.setProjectId(projectId);

            Stage stage = (Stage) projectList.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Task View");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String extractValue(String json, String key) {
        String keyPattern = "\"" + key + "\":";
        int startIndex = json.indexOf(keyPattern) + keyPattern.length();

        // Gérer les types de valeurs
        char firstChar = json.charAt(startIndex);
        if (firstChar == '\"') { // Valeur entre guillemets
            startIndex++;
            int endIndex = json.indexOf("\"", startIndex);
            return json.substring(startIndex, endIndex);
        } else { // Valeur non entre guillemets (ex. ID numérique)
            int endIndex = json.indexOf(",", startIndex);
            if (endIndex == -1) { // Dernier élément de l'objet
                endIndex = json.indexOf("}", startIndex);
            }
            return json.substring(startIndex, endIndex).trim();
        }
    }

}