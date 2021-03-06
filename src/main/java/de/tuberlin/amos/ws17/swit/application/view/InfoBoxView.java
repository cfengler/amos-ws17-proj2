package de.tuberlin.amos.ws17.swit.application.view;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;

import java.util.Set;

public class InfoBoxView extends BorderPane {

    private BorderPane titlePane;
    private BorderPane contentPane;
    private Button     closeButton;
    private Text       title;
    private Label      information;
    private ImageView  image;
    private ScrollPane scrollPane;
    private RingProgressView indicator;


    private static Font fontTitle = new Font(12);
    private static Font fontText  = new Font(12);

    public InfoBoxView() {
        setupViews();
    }

    private void initFonts() {
        Screen screen = Screen.getPrimary();
        Rectangle2D screenVisualBounds = screen.getVisualBounds();

        fontTitle = new Font(screenVisualBounds.getHeight() * 0.03);
        fontText = new Font(screenVisualBounds.getHeight() * 0.015);
    }

    private void setupViews() {
        initFonts();
        title = new Text();
        title.setId("expansionName");

        title.setFont(fontTitle);
        title.setStyle("" +
                "-fx-fill: white;" +
                "-fx-effect: dropshadow( gaussian , black , 8, 0.90 , 0 , 0 );");
        //title.set
        BorderPane.setAlignment(title, Pos.CENTER_LEFT);
        BorderPane.setMargin(title, new Insets(4));

        Text text = new Text();
        text.setText("X");
        text.setFont(fontTitle);
        text.setStyle("" +
                "-fx-fill: white;" +
                "-fx-effect: dropshadow( gaussian , black , 8, 0.90 , 0 , 0 );");

        //Button b = new Button(null, );
        closeButton = new Button(null, text);
        closeButton.setId("expansionButton");
        closeButton.setMinHeight(64);
        closeButton.setMinWidth(64);
        //closeButton.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));

        titlePane = new BorderPane();
        titlePane.setId("expansionTopPane");
        titlePane.setLeft(title);
        titlePane.setRight(closeButton);
        BorderPane.setAlignment(closeButton, Pos.CENTER_LEFT);
        titlePane.setStyle("" +
                "-fx-background-color: rgba(255, 255, 255, 0.25); " +
                "-fx-padding: 0 0 0 8");

        image = new ImageView();
        image.setId("expansionImage");
        BorderPane.setAlignment(image, Pos.CENTER);
        image.setPreserveRatio(true);

        image.minHeight(0);
        image.minWidth(0);

        indicator = new RingProgressView();

        information = new Label();
        information.setId("expansionInformation");
        information.setAlignment(Pos.TOP_CENTER);
        information.setFont(fontText);
        //information.setPrefHeight(300);
        information.setStyle("-fx-fill: white;" +
                "-fx-effect: dropshadow( gaussian , black , 4, 0.90 , 0 , 0 );" +
                "-fx-font-size: " + fontText.getSize() + ";");

        contentPane = new BorderPane();
        contentPane.setId("expansionContentPane");
        contentPane.setTop(image);
        contentPane.setBottom(information);

        scrollPane = new ScrollPane();
        scrollPane.setId("expansionScrollPane");
        scrollPane.setContent(contentPane);
        scrollPane.setFitToWidth(true);

        findScrollBar();

        setId("expansionPane");
        setVisible(false);
        BorderPane.setAlignment(this, Pos.CENTER);
        setTop(titlePane);
        setCenter(image);
        setBottom(scrollPane);
        setRight(indicator);

        Screen screen = Screen.getPrimary();
        Rectangle2D screenVisualBounds = screen.getVisualBounds();
        setMaxWidth(screenVisualBounds.getWidth() * 0.3);
        image.setFitWidth(screenVisualBounds.getWidth() * 0.3 - 150);
        setMaxHeight(screenVisualBounds.getHeight() * 0.6);
        scrollPane.setMaxHeight(screenVisualBounds.getHeight() * 0.3);
    }

    private ScrollBar findScrollBar() {
        Set<Node> nodes = scrollPane.lookupAll(".scroll-bar");
        for (final Node node : nodes) {
            if (node instanceof ScrollBar) {
                return (ScrollBar) node;
            }
        }
        return null;
    }

    public void setOnScrollListener(EventHandler<InputEvent> eventHandler) {
        contentPane.setOnScroll(eventHandler);
        ScrollBar scrollBar = findScrollBar();
        if (scrollBar != null) scrollBar.setOnDragDetected(eventHandler);
    }

    public Button getCloseButton() {
        return closeButton;
    }

    public Text getTitle() {
        return title;
    }

    public Label getInformation() {
        return information;
    }

    public ImageView getImage() {
        return image;
    }

    public ScrollPane getScrollPane() {
        return scrollPane;
    }

    public RingProgressView getIndicator() {
        return indicator;
    }
}
