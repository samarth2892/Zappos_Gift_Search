package ZapposGiftSearch.Controller;

import ZapposGiftSearch.Model.APIRequest;
import ZapposGiftSearch.Model.Product;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * ScreenOne is used to generate the components of the main screen
 * @version 1.0
 * @author Samarth Patel
 */
public class GiftSearchApp extends Application{
    private Scene firstScreen;
    private StackPane stack = new StackPane();
    private BorderPane border = new BorderPane();

    /**
     * Creates the top part of the scene
     * @return GridPane
     */
    private GridPane createTop() {
        final GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setId("grid");
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text sceneTitle = new Text("Welcome to Zappos gift search app");
        sceneTitle.setId("sceneTitle");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 5, 1);

        Label noOfProLabel = new Label("How many products?");
        grid.add(noOfProLabel, 0, 1);

        final TextField noOfProductFiled = new TextField();
        noOfProductFiled.setMaxSize(40, 10);
        grid.add(noOfProductFiled, 1, 1);

        Label priceRangeLabel = new Label("  Price Range:");
        grid.add(priceRangeLabel, 2, 1);

        final TextField priceRange = new TextField();
        priceRange.setMaxSize(40, 10);
        grid.add(priceRange, 3, 1);

        Button go = new Button();

        go.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                createResults(noOfProductFiled.getText(), priceRange.getText());
            }
        });
        go.setText("Go!");
        grid.add(go, 4, 1);
        return grid;
    }

    /**
     * Instantiates APIRequest, gets results and puts them in a gird pane
     * which is then added to main borderpane.
     * @param noOfProducts
     * @param priceRange
     */
    private void createResults(String noOfProducts, String priceRange) {

        if(verifyNumber(noOfProducts) == -1)
            showPopup("Please enter valid \nnumber of products!");

        if(verifyNumber(priceRange) == -1)
            showPopup("Please enter valid \nprice range!");

        if(verifyNumber(noOfProducts) != -1 && verifyNumber(priceRange) != -1) {

            APIRequest request = new APIRequest();
            ArrayList<Product> results =
            request.search(Double.parseDouble(noOfProducts), "gift", Double.parseDouble(priceRange));

            if(results == null || results.isEmpty()) {
                showPopup("Sorry, we could not process your\n" +
                        "request please make sure are connected to internet\n" +
                        "and you have provided valid inputs.");
            } else {
                final ScrollPane scrollPane = new ScrollPane();
                final GridPane grid = new GridPane();

                border.setCenter(new Text("Please wait..."));

                scrollPane.setStyle("-fx-background-color: transparent");
                grid.setStyle("-fx-background-color:transparent;");
                grid.setAlignment(Pos.CENTER);
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(15, 15, 15, 15));

                Text noOfResults = new Text(Integer.toString(results.size()) + " results found");
                noOfResults.setStyle("-fx-fill: darkred");
                grid.add(noOfResults, 0, 0 , 3, 1);
                grid.add(new Text("Thumbnail"), 0, 1);
                grid.add(new Text("ProductInfo"), 1, 1);
                grid.add(new Text("Price"), 2, 1);

                int i = 1;

                for (Product p : results) {
                    grid.add(new ImageView(p.getThumbnailURL()), 0, i + 1);
                    grid.add(new Text(p.getName() + "\nby " + p.getBrandName()), 1, i + 1);
                    grid.add(new Text(p.getPrice()), 2, i + 1);
                    i++;
                }

                scrollPane.setContent(grid);
                border.setCenter(scrollPane);
            }
        }
    }

    /**
     * verifies if a string can be converted to double
     * @param number
     * @return
     */
    private double verifyNumber(String number) {
        double num;
        try {
            num = Double.parseDouble(number);
            if(num == 0) return -1;
        } catch (NumberFormatException e) {
            return -1;
        }
        return num;
    }

    /**
     * This method is used to make any error popups
     * @param error
     */
    private void showPopup(String error) {
        final VBox errorPopup = new VBox(20);
        final StackPane coverForScreen = new StackPane();
        final Stage popupStage = new Stage();

        errorPopup.setPadding(new Insets(30, 30, 30, 30));
        errorPopup.setStyle("-fx-background-color: whitesmoke");
        errorPopup.setAlignment(Pos.CENTER);

        Text message = new Text(error);
        message.setStyle("-fx-font-size: 18px; " +
                "fx-font-family: \"Arial Black\";" +
                "-fx-fill: firebrick;" +
                "-fx-font-weight: bold;" +
                "-fx-effect: dropshadow( gaussian , rgba(255,255,255,0.5) , 0,0,0,1 );");

        message.setTextAlignment(TextAlignment.CENTER);

        Button ok = new Button("OK");
        ok.setAlignment(Pos.CENTER);
        ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                popupStage.close();
                coverForScreen.setVisible(false);
            }
        });
        stack.getChildren().add(coverForScreen);
        errorPopup.getChildren().addAll(message, ok);

        popupStage.setScene(new Scene(errorPopup));
        popupStage.setTitle("Error!");
        popupStage.setAlwaysOnTop(true);
        popupStage.show();
    }

    /**
     * This method puts together all the Nodes and return a scene object
     * @return
     */
    public Scene createScene() {
        stack.getChildren().add(border);
        border.setId("border");
        border.setTop(createTop());
        firstScreen = new Scene(stack, 600, 500);
        firstScreen.getStylesheets().add
                ("styleSheet.css");
        return firstScreen;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Zappos Gift Search App");
        primaryStage.setScene(createScene());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}