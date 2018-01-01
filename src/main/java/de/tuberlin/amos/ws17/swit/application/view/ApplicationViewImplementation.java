package de.tuberlin.amos.ws17.swit.application.view;

import de.tuberlin.amos.ws17.swit.application.viewmodel.ApplicationViewModelImplementation;
import de.tuberlin.amos.ws17.swit.application.viewmodel.ModuleStatusViewModel;
import de.tuberlin.amos.ws17.swit.application.viewmodel.PoiViewModel;
import de.tuberlin.amos.ws17.swit.application.viewmodel.UserExpressionViewModel;
import de.tuberlin.amos.ws17.swit.common.Module;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.bridj.cpp.com.IDispatch;

import java.nio.charset.Charset;
import java.util.regex.Pattern;

public class ApplicationViewImplementation extends Application implements ApplicationView {

    private BorderPane pnFoundation;
    private ListView<PoiViewModel> listPOIcamera;
    private ListView<PoiViewModel> listPOImaps;
    private ListView<String> listDebugLog;
    private ListView<ModuleStatusViewModel> listModuleStatus;
    private ListView<UserExpressionViewModel> listExpressionStatus;

    private BorderPane expansionPane;
    private BorderPane expansionTopPane;
    private Button expansionButton;
    private Label expansionName;
    private Label expansionInformation;
    private ImageView expansionImage;
    private ScrollPane expansionScrollPane;
    private HBox statusPane;

    private WebEngine webEngine;
    private WebView browser;

    private static final String FONTNAME = "Helvetica Neue";
    public static ApplicationViewImplementation app;
    private static ApplicationViewModelImplementation controller;
    private Stage primaryStage;

    public void init() {
        app = this;

        initElements();
        initExpansion();

        controller = new ApplicationViewModelImplementation(this);

        initBindings();
        initCellFactories();
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        primaryStage.setTitle("Side Window Infotainment");

        //primaryStage.initStyle(StageStyle.TRANSPARENT);
        //primaryStage.setMaximized(true);

        Scene scene = new Scene(pnFoundation, 800, 600, Color.WHITE);
        //scene.getStylesheets().add("/stylesheets/ApplicationViewStylesheet.css");
        scene.getStylesheets().add("/stylesheets/TransparentApplicationViewStylesheet.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initElements() {
        pnFoundation = new BorderPane();
        pnFoundation.setId("pnFoundation");

        listPOIcamera = new ListView<>();
        listPOIcamera.setId("listPOIcamera");
        pnFoundation.setTop(listPOIcamera);
        listPOImaps = new ListView<>();
        listPOImaps.setId("listPOImaps");
        pnFoundation.setBottom(listPOImaps);

        statusPane = new HBox();
        statusPane.setId("statusPane");
        pnFoundation.setLeft(statusPane);

        listModuleStatus = new ListView<>();
        listModuleStatus.setId("listModuleStatus");
        statusPane.getChildren().add(listModuleStatus);

        listExpressionStatus = new ListView<>();
        listExpressionStatus.setId("listExpressionStatus");
        statusPane.getChildren().add(listExpressionStatus);

        listDebugLog = new ListView<>();
        listDebugLog.setId("listDebugLog");
        pnFoundation.setRight(listDebugLog);

        expansionPane = new BorderPane();
        expansionPane.setId("expansionPane");
        expansionTopPane = new BorderPane();
        expansionTopPane.setId("expansionTopPane");
        expansionButton = new Button("X");
        expansionButton.setId("expansionButton");
        expansionName = new Label();
        expansionName.setId("expansionName");
        expansionInformation = new Label();
        expansionInformation.setId("expansionInformation");
        expansionImage = new ImageView();
        expansionImage.setId("expansionImage");
        expansionScrollPane = new ScrollPane();
        expansionScrollPane.setId("expansionScrollPane");
    }

    private void initExpansion() {
        expansionName.setAlignment(Pos.TOP_LEFT);
        expansionInformation.setAlignment(Pos.TOP_CENTER);
        expansionScrollPane.setContent(expansionInformation);
        expansionImage.setPreserveRatio(true);
        expansionImage.setFitHeight(300);
        expansionImage.setFitWidth(150);
        expansionTopPane.setLeft(expansionName);
        expansionTopPane.setRight(expansionButton);
        expansionPane.setTop(expansionTopPane);
        expansionPane.setLeft(expansionImage);
        expansionPane.setCenter(expansionScrollPane);
        pnFoundation.setCenter(expansionPane);
        BorderPane.setAlignment(expansionPane, Pos.CENTER_RIGHT);
        BorderPane.setAlignment(expansionName, Pos.CENTER_LEFT);
        BorderPane.setAlignment(expansionImage, Pos.CENTER_LEFT);
        expansionScrollPane.setFitToWidth(true);
    }

    private void initBindings() {
        ImageView cameraImage = new ImageView();
        //pnFoundation.setRight(cameraImage);
        cameraImage.imageProperty().bindBidirectional(controller.propertyCameraImageProperty());

        try {
            expansionButton.visibleProperty().bind(Bindings.equal(controller.getExpandedPOI().nameProperty(), "").not());
            expansionButton.onActionProperty().bindBidirectional(controller.propertyCloseButtonProperty());
            expansionName.textProperty().bindBidirectional(controller.getExpandedPOI().nameProperty());
            expansionImage.imageProperty().bindBidirectional(controller.getExpandedPOI().imageProperty());
            expansionInformation.textProperty().bindBidirectional(controller.getExpandedPOI().informationAbstractProperty());
            listDebugLog.itemsProperty().bindBidirectional(controller.propertyDebugLogProperty());
            listModuleStatus.itemsProperty().bindBidirectional(controller.listModuleStatusProperty());
            listExpressionStatus.itemsProperty().bindBidirectional(controller.listExpressionStatusProperty());
            listPOIcamera.itemsProperty().bindBidirectional(controller.propertyPOIcameraProperty());
            listPOImaps.itemsProperty().bindBidirectional(controller.propertyPOImapsProperty());

            pnFoundation.backgroundProperty().bindBidirectional(controller.backgroundProperty);
        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void initCellFactories() {
        Callback<ListView<PoiViewModel>, ListCell<PoiViewModel>> callback = new Callback<ListView<PoiViewModel>, ListCell<PoiViewModel>>() {
            @Override
            public ListCell<PoiViewModel> call(ListView<PoiViewModel> param) {
                return new ListCell<PoiViewModel>() {
                    @Override
                    public void updateItem(PoiViewModel item, boolean empty) {
                        super.updateItem(item, empty);
                        if(empty || item == null) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            ImageView imageView = new ImageView(item.getImage());
                            imageView.setPreserveRatio(true);
                            imageView.setFitHeight(100);
                            Label lblName = new Label(item.getName());
                            lblName.setFont(new Font(FONTNAME, 13));
                            BorderPane pane = new BorderPane();
                            pane.setTop(lblName);
                            pane.setCenter(imageView);
                            pane.setOnMouseClicked(event -> controller.expandPOI(item.getId()));
                            setGraphic(pane);
                            listPOIcamera.refresh();
                            listPOImaps.refresh();
                        }
                    }
                };
            }
        };
        listPOIcamera.setCellFactory(callback);
        listPOImaps.setCellFactory(callback);

        listDebugLog.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new ListCell<String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if(empty || item == null) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            Label debugText = new Label(item);
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    setGraphic(debugText);
                                    listDebugLog.refresh();
                                }
                            });
                        }
                    }
                };
            }
        });

        listModuleStatus.setCellFactory(new Callback<ListView<ModuleStatusViewModel>, ListCell<ModuleStatusViewModel>>() {
            @Override
            public ListCell<ModuleStatusViewModel> call(ListView<ModuleStatusViewModel> param) {
                return new ListCell<ModuleStatusViewModel>() {
                    @Override
                    public void updateItem(ModuleStatusViewModel item, boolean empty) {
                        super.updateItem(item, empty);
                        if(empty || item == null) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            StackPane pane = new StackPane();
                            ImageView image = new ImageView(item.getErrorType().getImage());
                            image.setPreserveRatio(true);
                            image.setFitWidth(50);
                            image.setFitHeight(50);
                            Label lbl = new Label();
                            item.workingProperty().addListener(new ChangeListener<Boolean>() {
                                @Override
                                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                                    statusIcon(lbl, newValue);
                                }
                            });
                            statusIcon(lbl, item.isWorking());
                            pane.getChildren().add(image);
                            pane.getChildren().add(lbl);
                            StackPane.setAlignment(lbl, Pos.BOTTOM_RIGHT);
                            StackPane.setAlignment(image, Pos.CENTER_LEFT);
                            setGraphic(pane);
                            listModuleStatus.refresh();
                        }
                    }
                };
            }
        });

        listExpressionStatus.setCellFactory(new Callback<ListView<UserExpressionViewModel>, ListCell<UserExpressionViewModel>>() {
            @Override
            public ListCell<UserExpressionViewModel> call(ListView<UserExpressionViewModel> param) {
                return new ListCell<UserExpressionViewModel>() {
                    @Override
                    public void updateItem(UserExpressionViewModel item, boolean empty) {
                        super.updateItem(item, empty);
                        if(empty || item == null) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            StackPane pane = new StackPane();
                            ImageView image = new ImageView(item.getType().getImage());
                            image.setPreserveRatio(true);
                            image.setFitWidth(50);
                            image.setFitHeight(50);
                            Label lbl = new Label();
                            item.activeProperty().addListener(new ChangeListener<Boolean>() {
                                @Override
                                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            statusIcon(lbl, newValue);
                                        }
                                    });
                                }
                            });
                            statusIcon(lbl, item.isActive());
                            pane.getChildren().add(image);
                            pane.getChildren().add(lbl);
                            StackPane.setAlignment(lbl, Pos.BOTTOM_RIGHT);
                            StackPane.setAlignment(image, Pos.CENTER_RIGHT);
                            setGraphic(pane);
                            listExpressionStatus.refresh();
                        }
                    }
                };
            }
        });
    }

    private void statusIcon(Label lbl, boolean status) {
        if(status) {
            String text = new String(Character.toChars(10003));
            lbl.setText(text);
            lbl.setStyle("-fx-text-fill: green; -fx-font-size: 20px; -fx-font-weight: bold; " +
                    "-fx-background-color: transparent; -fx-effect: dropshadow( gaussian , white , 3, 1.0 , 0 , 0 );");
        } else {
            String text = new String(Character.toChars(10005));
            lbl.setText(text);
            lbl.setStyle("-fx-text-fill: red; -fx-font-size: 20px; -fx-font-weight: bold; " +
                    "-fx-background-color: transparent; -fx-effect: dropshadow( gaussian , white , 3, 1.0 , 0 , 0 );");
        }
    }

    @Override
    public void stop(){
        controller.run = false;
        for(Module m: controller.getModuleList()) {
            m.stopModule();
        }
    }
}