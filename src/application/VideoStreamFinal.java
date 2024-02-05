package application;

import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.effect.Glow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;
import javafx.scene.text.Text;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Priority;
import javafx.application.HostServices;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.ContextMenu;

public class VideoStreamFinal extends Application {

    private Stage primaryStage;
    private BorderPane borderPane;
    private ListView<String> searchResultsListView;
    private List<String> allMovieNames = Arrays.asList(
            "Attack on Titan", "Demon Slayer", "My Hero Academia", "Naruto", "One Piece",
            "Black Clover", "Steins Gate", "Bleach", "Hunter x Hunter","Iron Man 1", "Iron Man 2", "Iron Man 3", "Thor 1", "Thor 2", "Thor 3", "Thor 4", "Hulk 1", "Eternals"
            ,"Harry Potter 1", "Harry Potter 2", "Harry Potter 3", "Harry Potter 4", "Harry Potter 5", "Harry Potter 6", "Harry Potter 7 Part 1", "Harry Potter 7 Part 2"
            ,"Star Wars 1", "Star Wars 2", "Star Wars 3", "Star Wars 4", "Star Wars 5", "Star Wars 6", "Star Wars 7", "Star Wars 8", "Star Wars 9"
            ,"Business Proposal", "Crash Landing on You", "Friends", "Itaewon Class", "Squid Games", "Start Up", "The Glory", "The Office", "The Big Bang Theory"
    );

    //trailer necessities
    private MediaPlayer mediaPlayer;
    private HostServices hostServices;

    //Set the borders of the scene
    private final int width = 1500;
    private final int height = 800;


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setStyle("-fx-padding: 10;");

        MenuBar menuBar = createMenuBar(primaryStage);
        ComboBox<String> searchComboBox = createSearchComboBox();

        // Use BorderPane to position the MenuBar on the left and searchComboBox on the right
        BorderPane topContainer = new BorderPane();
        topContainer.setLeft(menuBar);
        topContainer.setRight(searchComboBox);
        topContainer.setStyle("-fx-background-color: darkgrey;");

        vBox.getChildren().addAll(topContainer, createLabelBox());

        searchResultsListView = new ListView<>();
        searchResultsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        searchResultsListView.setPlaceholder(new Label("No results"));

        borderPane = new BorderPane();
        borderPane.setTop(topContainer);
        setDefaultCenter();

        Scene scene = new Scene(borderPane, width, height);
        scene.getStylesheets().add("file:src/styles.css");
        primaryStage.setTitle("Video Streaming");
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    //Sets the default center on startup and whenever you go back to home
    ////////////////////////////////////////////////////////////////
    private void setDefaultCenter() {
        VBox animeContent = createContentVBox("Attack on Titan", "Demon Slayer", "My Hero Academia", "Naruto", "One Piece", "Black Clover", "Steins Gate", "Bleach", "Hunter x Hunter");
        VBox harryPotterContent = createContentVBox("Harry Potter 1", "Harry Potter 2", "Harry Potter 3", "Harry Potter 4", "Harry Potter 5", "Harry Potter 6", "Harry Potter 7 Part 1", "Harry Potter 7 Part 2");
        VBox starWarsContent = createContentVBox("Star Wars 1", "Star Wars 2", "Star Wars 3", "Star Wars 4", "Star Wars 5", "Star Wars 6", "Star Wars 7", "Star Wars 8", "Star Wars 9");
        VBox marvelContent = createContentVBox("Iron Man 1", "Iron Man 2", "Iron Man 3", "Thor 1", "Thor 2", "Thor 3", "Thor 4", "Hulk 1", "Eternals");

        VBox animeRow = createSlidableRow("Anime", animeContent);
        VBox harryPotterRow = createSlidableRow("Harry Potter", harryPotterContent);
        VBox starWarsRow = createSlidableRow("Star Wars", starWarsContent);
        VBox marvelRow = createSlidableRow("Marvel", marvelContent);

        VBox mainContentVBox = new VBox(createTrailerVid("/narutoshowtrailer.mp4", "/Naruto Title.png"), animeRow, harryPotterRow, starWarsRow, marvelRow, createContactUs());
        mainContentVBox.setId("mainContentVBox");

        ScrollPane mainScrollPane = new ScrollPane(mainContentVBox);
        mainScrollPane.setFitToWidth(true);
        mainScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        mainScrollPane.setMinHeight(500);

        borderPane.setCenter(mainScrollPane);
    }
    //////////////////////////////////////////////////////////

    //Sets up the search button
    //////////////////////////////////////////////


    private ComboBox<String> createSearchComboBox() {
        ComboBox<String> searchComboBox = new ComboBox<>();
        ObservableList<String> movieNames = FXCollections.observableArrayList(allMovieNames);
        FilteredList<String> filteredMovieNames = new FilteredList<>(movieNames);

        searchComboBox.setEditable(true);
        searchComboBox.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                }
            }
        });
        searchComboBox.setButtonCell(new TextFieldListCell<>());

        searchComboBox.setItems(filteredMovieNames);

        TextField editor = searchComboBox.getEditor();
        editor.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredMovieNames.setPredicate(movie -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                return movie.toLowerCase().contains(newValue.toLowerCase());
            });
        });

        // Add listener to handle item selection
        searchComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (searchComboBox.isFocused() && newValue != null) {
                // A new item is selected, call setMoviePlayScreen
                setMoviePlayScreen(newValue);
            }
        });

        return searchComboBox;
    }

    private static class TextFieldListCell<T> extends ListCell<T> {
        @Override
        protected void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
            } else {
                setText(item.toString());
            }
        }
    }

    /////////////////////////////////////////////////////

    private HBox createLabelBox() {
        Label freeAnimeLabel = new Label("Movie Site");
        freeAnimeLabel.setStyle("-fx-underline: true;");
        HBox labelBox = new HBox(freeAnimeLabel);
        labelBox.setAlignment(Pos.CENTER);
        return labelBox;
    }

    private MenuBar createMenuBar(Stage primaryStage) {
        MenuBar menuBar = new MenuBar();

        // Create a custom Node (ImageView) for the first option
        ImageView logoImageView = new ImageView(new Image("file:src/images/GogoAnimeLogo.jpg"));
        logoImageView.setFitWidth(100); // Set the width as needed
        logoImageView.setFitHeight(60); // Set the height as needed

        // Create a custom MenuButton to hold the logo
        Label logoLabel = new Label(null, logoImageView);
        logoLabel.getStyleClass().add("logo-label"); // You can define a CSS class for styling
        logoLabel.setOnMouseClicked(e -> setDefaultCenter());

        MenuItem homeItem = new MenuItem("Home");
        homeItem.setOnAction(e -> setDefaultCenter());

        MenuItem moviesItem = new MenuItem("Movies");
        moviesItem.setOnAction(e -> showMoviesContent(primaryStage));

        MenuItem animeItem = new MenuItem("Anime");
        animeItem.setOnAction(e -> showAnimeContent(primaryStage));

        MenuItem tvShowsItem = new MenuItem("TV Shows");
        tvShowsItem.setOnAction(e -> showtvShowsContent(primaryStage));

        MenuItem recommendedItem = new MenuItem("Recommended");
        recommendedItem.setOnAction(e -> showRecommendedContent(primaryStage));


        menuBar.getMenus().addAll(new Menu(null, logoLabel),
                new Menu("Home", null, homeItem),
                new Menu("Movies", null, moviesItem),
                new Menu("TV Shows", null, tvShowsItem),
                new Menu("Anime", null, animeItem),
                new Menu("Recommended", null, recommendedItem));


        return menuBar;
    }

    private void showMoviesContent(Stage primaryStage) {
        VBox mainContentVBox = new VBox(
                createTrailerVid("/HarryPotterTrailer.mp4", "/HarryPottertitle.png"),
                createSlidableRow("Marvel", createContentVBox("Iron Man 1", "Iron Man 2", "Iron Man 3", "Thor 1", "Thor 2", "Thor 3", "Thor 4", "Hulk 1", "Eternals")),
                createSlidableRow("Harry Potter", createContentVBox("Harry Potter 1", "Harry Potter 2", "Harry Potter 3", "Harry Potter 4", "Harry Potter 5", "Harry Potter 6", "Harry Potter 7 Part 1", "Harry Potter 7 Part 2")),
                createSlidableRow("Star Wars", createContentVBox("Star Wars 1", "Star Wars 2", "Star Wars 3", "Star Wars 4", "Star Wars 5", "Star Wars 6", "Star Wars 7", "Star Wars 8", "Star Wars 9"))
        );

        mainContentVBox.getChildren().forEach(contentVBox -> contentVBox.getStyleClass().add("anime-content"));
        mainContentVBox.getChildren().add(createContactUs());
        mainContentVBox.setStyle("-fx-background-color: black;");

        ScrollPane mainScrollPane = new ScrollPane(mainContentVBox);
        mainScrollPane.setFitToWidth(true);
        mainScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        mainScrollPane.setMinHeight(240);


        borderPane.setCenter(mainScrollPane);
    }


    private void showtvShowsContent(Stage primaryStage) {
        VBox mainContentVBox = new VBox(
                createTrailerVid("/FriendsTrailer.mp4", "/friendstitle.png"),
                createSlidableRow("TV Shows", createContentVBox("Business Proposal", "Crash Landing on You", "Friends", "Itaewon Class", "Squid Games", "Start Up", "The Glory", "The Office", "The Big Bang Theory"))
        );

        mainContentVBox.getChildren().forEach(contentVBox -> contentVBox.getStyleClass().add("anime-content"));
        mainContentVBox.getChildren().add(createContactUs());
        mainContentVBox.setStyle("-fx-background-color: black;");

        ScrollPane mainScrollPane = new ScrollPane(mainContentVBox);
        mainScrollPane.setFitToWidth(true);
        mainScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        mainScrollPane.setMinHeight(240);

        borderPane.setCenter(mainScrollPane);
    }

    private void showRecommendedContent(Stage primaryStage) {
        VBox mainContentVBox = new VBox(
                createTrailerVid("/SquidGamesTrailer.mp4", "/SquidTitle.png"),
                createSlidableRow("Recommended", createContentVBox("Naruto", "Star Wars 7", "Friends", "Iron Man 1", "Squid Games", "Black Clover", "Thor 3", "One Piece", "Business Proposal"))
        );

        mainContentVBox.getChildren().forEach(contentVBox -> contentVBox.getStyleClass().add("anime-content"));
        mainContentVBox.getChildren().add(createContactUs());
        mainContentVBox.setStyle("-fx-background-color: black;");

        ScrollPane mainScrollPane = new ScrollPane(mainContentVBox);
        mainScrollPane.setFitToWidth(true);
        mainScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        mainScrollPane.setMinHeight(240);

        borderPane.setCenter(mainScrollPane);
    }

    private void showAnimeContent(Stage primaryStage) {
        VBox mainContentVBox = new VBox(
                createTrailerVid("/OnePieceTrailer.mp4", "/OnePieceTitle.png"),
                createSlidableRow("Anime", createContentVBox("Attack on Titan", "Demon Slayer", "My Hero Academia", "Naruto", "One Piece", "Black Clover", "Steins Gate", "Fullmetal Alchemist", "Hunter x Hunter"))
        );

        mainContentVBox.getChildren().forEach(contentVBox -> contentVBox.getStyleClass().add("anime-content"));
        mainContentVBox.getChildren().add(createContactUs());
        mainContentVBox.setStyle("-fx-background-color: black;");

        ScrollPane mainScrollPane = new ScrollPane(mainContentVBox);
        mainScrollPane.setFitToWidth(true);
        mainScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        mainScrollPane.setMinHeight(240);

        borderPane.setCenter(mainScrollPane);
    }


    private VBox createContentVBox(String... movieNames) {
        HBox hbox = createRow(movieNames);
        VBox contentVBox = new VBox(hbox);
        contentVBox.setAlignment(Pos.CENTER);
        contentVBox.setMinHeight(240);
        return contentVBox;
    }


    private HBox createRow(String... movieNames) {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(30);
        Insets margin = new Insets(200, 0, 0, 0);
        HBox.setMargin(hbox, margin);

        double imageWidth = 100;
        double imageHeight = 130;

        for (String movieName : movieNames) {
            Image image = new Image("file:src/images/" + movieName + ".jpg");
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(imageWidth);
            imageView.setFitHeight(imageHeight);

            addScaleTransition(imageView);

            VBox imageAndLabelBox = new VBox();
            imageAndLabelBox.getChildren().addAll(imageView, new Label(movieName));
            imageAndLabelBox.setAlignment(Pos.CENTER_LEFT);

            hbox.getChildren().add(imageAndLabelBox);

            // Make sure to call the addClickEvent method
            addClickEvent(imageView, image, movieName);
        }

        return hbox;
    }


    private VBox createSlidableRow(String title, VBox content) {
        VBox vbox = new VBox();
        vbox.getStyleClass().add("slidable-row");
        vbox.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-underline: true;");

        HBox headerBox = new HBox(titleLabel);
        headerBox.setAlignment(Pos.CENTER_LEFT); // Adjusted alignment for the title
        headerBox.setMargin(titleLabel, new Insets(-60, 0, 0, 120)); // Adjusted margin for the title

        // Adjust the margin for the content
        VBox.setMargin(content, new Insets(-60, 0, 0, 0));  // Adjust the values as needed

        vbox.getChildren().addAll(headerBox, content);
        return vbox;
    }

    private void addClickEvent(ImageView imageView, Image image, String itemName) {
        imageView.setOnMouseClicked(event -> {
            setMoviePlayScreen(itemName);
        });

        imageView.setOnMouseEntered(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), imageView);
            scaleTransition.setToX(1.2);
            scaleTransition.setToY(1.2);
            scaleTransition.play();
        });

        imageView.setOnMouseExited(event -> {
            ScaleTransition scaleTransition1 = new ScaleTransition(Duration.millis(200), imageView);
            scaleTransition1.setToX(1.0);
            scaleTransition1.setToY(1.0);
            scaleTransition1.play();
        });
    }

    private void setMoviePlayScreen(String itemName) {
        System.out.println("Selected Movie: " + itemName);
        // Create a StackPane to layer components
        StackPane stackPane = new StackPane();
        HostServices hostServices = getHostServices();

        // Set the original image
        Image originalImage = new Image("file:src/images/" + itemName + ".jpg");
        ImageView originalImageView = new ImageView(originalImage);
        double fixedImageWidth = 300; // Adjust width as needed
        double fixedImageHeight = 700; // Adjust height as needed
        originalImageView.setFitWidth(fixedImageWidth);
        originalImageView.setFitHeight(fixedImageHeight);
        originalImageView.setPreserveRatio(true);

        // Set the faded background image
        ImageView backgroundImageView = new ImageView(originalImage);
        backgroundImageView.setFitWidth(width);
        backgroundImageView.setFitHeight(height);
        backgroundImageView.setOpacity(0.1);

        // Add VBox for the description
        VBox descriptionBox = new VBox();
        descriptionBox.setAlignment(Pos.CENTER); // Adjust alignment as needed
        descriptionBox.setSpacing(10);


        // Add message about the movie with word wrapping
        String movieMessage = getMessageForItem(itemName);
        Text messageText = new Text(movieMessage);
        messageText.setWrappingWidth(500); // Adjust the width as needed
        messageText.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: white;");

        // Create an HBox for the additional information
        HBox additionalInfoBox = new HBox();
        additionalInfoBox.setAlignment(Pos.CENTER);
        additionalInfoBox.setSpacing(20);

        Label scoreLabel = new Label("Score: 7.8");
        scoreLabel.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: black;");
        scoreLabel.getStyleClass().add("score-label");
        additionalInfoBox.getChildren().addAll(scoreLabel);

        // Create buttons box
        HBox buttonsBox = createButtonsBox(itemName, hostServices);

        // Add buttonsBox to the descriptionBox
        descriptionBox.getChildren().addAll(messageText, additionalInfoBox, buttonsBox);

        // Add components to the StackPane
        stackPane.getChildren().addAll(backgroundImageView, descriptionBox, originalImageView);

        // Add title label at the top center of the entire scene
        Label titleLabel = new Label(itemName);
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: black;");
        StackPane.setAlignment(titleLabel, Pos.TOP_CENTER);
        stackPane.getChildren().add(titleLabel);

        // Set padding for the originalImageView to adjust the left alignment
        StackPane.setMargin(originalImageView, new Insets(0, 0, 0, -1000));

        borderPane.setStyle("-fx-background-color: transparent;");

        // Set the StackPane as the center of the BorderPane
        borderPane.setCenter(stackPane);
    }


    private HBox createButtonsBox(String itemName, HostServices hostServices) {
        // Create buttons
        Button playButton = new Button("Play");
        Button downloadButton = new Button("Download");

        // Set actions for the buttons
        playButton.setOnAction(e -> {
            openYouTubePage(itemName, hostServices);
        });

        downloadButton.setOnAction(e -> openYouTubePage(itemName,hostServices));

        // Create an HBox for the buttons
        HBox buttonsBox = new HBox(playButton, downloadButton);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.setSpacing(10);

        return buttonsBox;
    }

    //Opens youtube
    private void openYouTubePage(String itemName, HostServices hostServices) {
        String searchQuery = getSearchQueryForItem(itemName);
        String url = "https://www.youtube.com/" + searchQuery;

        hostServices.showDocument(url);
    }

    //If want a specific video
    private String getSearchQueryForItem(String itemName) {
        return (itemName + "youtube");
    }




    private String getMessageForItem(String itemName) {
        // Add specific messages for each item
        switch (itemName) {
            case "Attack on Titan":
                return "Centuries ago, mankind was slaughtered to near extinction by monstrous humanoid creatures called Titans, "
                        + "forcing humans to hide in fear behind enormous concentric walls. What makes these giants truly terrifying is that their "
                        + "taste for human flesh is not born out of hunger but what appears to be out of pleasure. To ensure their survival, the remnants "
                        + "of humanity began living within defensive barriers, resulting in one hundred years without a single titan encounter. However, "
                        + "that fragile calm is soon shattered when a colossal Titan manages to breach the supposedly impregnable outer wall, reigniting "
                        + "the fight for survival against the man-eating abominations.\n\nAfter witnessing a horrific personal loss at the hands of the "
                        + "invading creatures, Eren Yeager dedicates his life to their eradication by enlisting into the Survey Corps, an elite military "
                        + "unit that combats the merciless humanoids outside the protection of the walls. Eren, his adopted sister Mikasa Ackerman, and "
                        + "his childhood friend Armin Arlert join the brutal war against the Titans and race to discover a way of defeating them before "
                        + "the last walls are breached.";
            case "Demon Slayer":
                return "Ever since the death of his father, the burden of supporting the family has fallen upon Tanjirou Kamado's shoulders. Though living impoverished on a remote mountain, "
                        + "the Kamado family are able to enjoy a relatively peaceful and happy life. One day, "
                        + "Tanjirou decides to go down to the local village to make a little money selling charcoal. On his way back, "
                        + "night falls, forcing Tanjirou to take shelter in the house of a strange man, who warns him of the existence of flesh-eating demons that lurk in the woods at night.\r\n"
                        + "\r\n"
                        + "When he finally arrives back home the next day, he is met with a horrifying sight—his whole family has been slaughtered. Worse still, the sole survivor is his sister Nezuko, "
                        + "who has been turned into a bloodthirsty demon. Consumed by rage and hatred, Tanjirou swears to avenge his family and stay by his only remaining sibling. Alongside the "
                        + "mysterious group calling themselves the Demon Slayer Corps, Tanjirou will do whatever it takes to slay the demons and protect the remnants of his beloved sister's humanity.";
            case "My Hero Academia":
                return "The appearance of \"quirks,\" newly discovered super powers, has been steadily increasing over the years, with 80 percent of humanity possessing various abilities from manipulation of elements to shapeshifting. This leaves the remainder of the world completely powerless, and Izuku Midoriya is one such individual.\r\n"
                        + "\r\n"
                        + "Since he was a child, the ambitious middle schooler has wanted nothing more than to be a hero. Izuku's unfair fate leaves him admiring heroes and taking notes on them whenever he can. But it seems that his persistence has borne some fruit: Izuku meets the number one hero and his personal idol, All Might. All Might's quirk is a unique ability that can be inherited, and he has chosen Izuku to be his successor!\r\n"
                        + "\r\n"
                        + "Enduring many months of grueling training, Izuku enrolls in UA High, a prestigious high school famous for its excellent hero training program, and this year's freshmen look especially promising. With his bizarre but talented classmates and the looming threat of a villainous organization, Izuku will soon learn what it really means to be a hero.";
            case "Naruto":
                return "Moments prior to Naruto Uzumaki's birth, a huge demon known as the Kyuubi, the Nine-Tailed Fox, attacked Konohagakure, the Hidden Leaf Village, and wreaked havoc. In order to put an end to the Kyuubi's rampage, the leader of the village, the Fourth Hokage, sacrificed his life and sealed the monstrous beast inside the newborn Naruto.\r\n"
                        + "\r\n"
                        + "Now, Naruto is a hyperactive and knuckle-headed ninja still living in Konohagakure. Shunned because of the Kyuubi inside him, Naruto struggles to find his place in the village, while his burning desire to become the Hokage of Konohagakure leads him not only to some great new friends, but also some deadly foes.";
            case "One Piece":
                return "Barely surviving in a barrel after passing through a terrible whirlpool at sea, carefree Monkey D. Luffy ends up aboard a ship under attack by fearsome pirates. Despite being a naive-looking teenager, he is not to be underestimated. Unmatched in battle, Luffy is a pirate himself who resolutely pursues the coveted One Piece treasure and the King of the Pirates title that comes with it.\r\n"
                        + "\r\n"
                        + "The late King of the Pirates, Gol D. Roger, stirred up the world before his death by disclosing the whereabouts of his hoard of riches and daring everyone to obtain it. Ever since then, countless powerful pirates have sailed dangerous seas for the prized One Piece only to never return. Although Luffy lacks a crew and a proper ship, he is endowed with a superhuman ability and an unbreakable spirit that make him not only a formidable adversary but also an inspiration to many.\r\n"
                        + "\r\n"
                        + "As he faces numerous challenges with a big smile on his face, Luffy gathers one-of-a-kind companions to join him in his ambitious endeavor, together embracing perils and wonders on their once-in-a-lifetime adventure.";
            case "Black Clover":
                return "Asta and Yuno were abandoned at the same church on the same day. Raised together as children, they came to know of the \"Wizard King\"—a title given to the strongest mage in the kingdom—and promised that they would compete against each other for the position of the next Wizard King. However, as they grew up, the stark difference between them became evident. While Yuno is able to wield magic with amazing power and control, Asta cannot use magic at all and desperately tries to awaken his powers by training physically.\r\n"
                        + "\r\n"
                        + "When they reach the age of 15, Yuno is bestowed a spectacular Grimoire with a four-leaf clover, while Asta receives nothing. However, soon after, Yuno is attacked by a person named Lebuty, whose main purpose is to obtain Yuno's Grimoire. Asta tries to fight Lebuty, but he is outmatched. Though without hope and on the brink of defeat, he finds the strength to continue when he hears Yuno's voice. Unleashing his inner emotions in a rage, Asta receives a five-leaf clover Grimoire, a \"Black Clover\" giving him enough power to defeat Lebuty. A few days later, the two friends head out into the world, both seeking the same goal—to become the Wizard King!";
            case "Steins Gate":
                return "Eccentric scientist Rintarou Okabe has a never-ending thirst for scientific exploration. Together with his ditzy but well-meaning friend Mayuri Shiina and his roommate Itaru Hashida, Rintarou founds the Future Gadget Laboratory in the hopes of creating technological innovations that baffle the human psyche. Despite claims of grandeur, the only notable \"gadget\" the trio have created is a microwave that has the mystifying power to turn bananas into green goo.\r\n"
                        + "\r\n"
                        + "However, when Rintarou decides to attend neuroscientist Kurisu Makise's conference on time travel, he experiences a series of strange events that lead him to believe that there is more to the \"Phone Microwave\" gadget than meets the eye. Apparently able to send text messages into the past using the microwave, Rintarou dabbles further with the \"time machine,\" attracting the ire and attention of the mysterious organization SERN.\r\n"
                        + "\r\n"
                        + "Due to the novel discovery, Rintarou and his friends find themselves in an ever-present danger. As he works to mitigate the damage his invention has caused to the timeline, he is not only fighting a battle to save his loved ones, but also one against his degrading sanity.\r\n";


            case "Bleach":
                return "Substitute Soul Reaper Ichigo Kurosaki spends his days fighting against Hollows, dangerous evil spirits that threaten Karakura Town. Ichigo carries out his quest with his closest allies: Orihime Inoue, his childhood friend with a talent for healing; Yasutora Sado, his high school classmate with superhuman strength; and Uryuu Ishida, Ichigo's Quincy rival.\r\n"
                        + "\r\n"
                        + "Ichigo's vigilante routine is disrupted by the sudden appearance of Asguiaro Ebern, a dangerous Arrancar who heralds the return of Yhwach, an ancient Quincy king. Yhwach seeks to reignite the historic blood feud between Soul Reaper and Quincy, and he sets his sights on erasing both the human world and the Soul Society for good.\r\n"
                        + "\r\n"
                        + "Yhwach launches a two-pronged invasion into both the Soul Society and Hueco Mundo, the home of Hollows and Arrancar. In retaliation, Ichigo and his friends must fight alongside old allies and enemies alike to end Yhwach's campaign of carnage before the world itself comes to an end.";
            case "Hunter x Hunter":
                return "Hunters devote themselves to accomplishing hazardous tasks, all from traversing the world's uncharted territories to locating rare items and monsters. Before becoming a Hunter, one must pass the Hunter Examination—a high-risk selection process in which most applicants end up handicapped or worse, deceased.\r\n"
                        + "\r\n"
                        + "Ambitious participants who challenge the notorious exam carry their own reason. What drives 12-year-old Gon Freecss is finding Ging, his father and a Hunter himself. Believing that he will meet his father by becoming a Hunter, Gon takes the first step to walk the same path.\r\n"
                        + "\r\n"
                        + "During the Hunter Examination, Gon befriends the medical student Leorio Paladiknight, the vindictive Kurapika, and ex-assassin Killua Zoldyck. While their motives vastly differ from each other, they band together for a common goal and begin to venture into a perilous world.\r\n"
                        + "";
            case "Harry Potter 1":
                return "The story follows a young boy named Harry Potter, played by Daniel Radcliffe, who learns on his eleventh birthday that he is a wizard and has been accepted to Hogwarts School of Witchcraft and Wizardry. Harry discovers that he is famous in the wizarding world for surviving an attack by the dark wizard Lord Voldemort as a baby, and he also inherits a fortune from his deceased parents.\r\n"
                        + "\r\n"
                        + "As Harry embarks on his magical education at Hogwarts, he befriends Ron Weasley (Rupert Grint) and Hermione Granger (Emma Watson). Together, the trio uncovers the mystery of the Philosopher's Stone, a magical object with the power to grant immortality. They must prevent it from falling into the wrong hands, particularly those of the dark and sinister Voldemort.\r\n"
                        + "\r\n"
                        + "The film features various magical creatures, spells, and enchanting settings within the wizarding world. It explores themes of friendship, courage, and the battle between good and evil. With a mix of fantasy, adventure, and humor, \"Harry Potter and the Philosopher's Stone\" sets the stage for the epic journey that Harry and his friends will undertake in the subsequent films of the series.\r\n";
            case "Harry Potter 2":
                return "Harry Potter (Daniel Radcliffe) returning to Hogwarts for his second year. However, mysterious events unfold as students are found mysteriously petrified. Harry discovers the existence of the Chamber of Secrets, and, with the help of his friends Ron Weasley (Rupert Grint) and Hermione Granger (Emma Watson), he must uncover the truth behind the dark history of the school. The film introduces new characters and magical creatures, including Dobby the house-elf and the phoenix Fawkes. As the trio faces challenges and confronts the heir of Slytherin, the movie sets the stage for the escalating dangers in the wizarding world.";
            case "Harry Potter 3":
                return "Harry (Daniel Radcliffe) learns that Sirius Black (Gary Oldman), a dangerous prisoner, has escaped Azkaban and may be after him. As Harry and his friends face new challenges, the film explores themes of friendship and betrayal. The introduction of the Marauder's Map and the time-turner adds layers to the story, while the revelation of Sirius Black's true intentions reshapes the narrative. Cuarón's directorial style brings a fresh perspective to the series, capturing the magical and mysterious elements of the wizarding world.";
            case "Harry Potter 4":
                return " Harry (Daniel Radcliffe) into the Triwizard Tournament, a dangerous magical competition. The film explores the complexities of growing up, as the characters navigate not only the challenges of the tournament but also the emergence of Lord Voldemort (Ralph Fiennes). The dark forces at play culminate in Voldemort's return to power, setting the stage for the impending battle between good and evil. With stunning visuals and intense action sequences, \"Goblet of Fire\" marks a turning point in the series, laying the groundwork for the darker themes that will follow.";
            case "Harry Potter 5":
                return "Harry (Daniel Radcliffe) as he faces skepticism and resistance from the wizarding world regarding Voldemort's return. The formation of Dumbledore's Army and the exploration of the Department of Mysteries add depth to the story. The film introduces new characters, such as Luna Lovegood (Evanna Lynch), and explores the bonds of friendship amidst internal conflicts. As the threat of Voldemort looms larger, \"Order of the Phoenix\" sets the stage for the inevitable confrontation between Harry and the dark forces.";
            case "Harry Potter 6":
                return "Into the past of Lord Voldemort (Ralph Fiennes) as Harry (Daniel Radcliffe) learns about Horcruxes and their significance. The film explores the complexities of relationships, especially as Ron (Rupert Grint) and Hermione (Emma Watson) navigate their feelings. Dumbledore (Michael Gambon) and Harry embark on a quest to uncover Voldemort's secrets, leading to a series of revelations that reshape the understanding of the wizarding world. With a mix of intrigue, romance, and impending danger, \"Half-Blood Prince\" sets the stage for the epic conclusion..";
            case "Harry Potter 7 Part 1":
                return " Harry (Daniel Radcliffe), Ron (Rupert Grint), and Hermione (Emma Watson) as they go on a perilous mission to find and destroy the Horcruxes—objects containing parts of Voldemort's soul. The dark wizard's forces have taken control of the Ministry of Magic and Hogwarts, making their journey fraught with danger. The film explores the characters' vulnerabilities, their deepening friendship, and the sacrifices they make in the face of overwhelming odds. As the trio faces challenges in a world teetering on the brink of darkness, \"Deathly Hallows – Part 1\" builds the tension towards the ultimate showdown.";
            case "Harry Potter 7 Part 2":
                return "The epic conclusion begins with the Battle of Hogwarts raging as Harry (Daniel Radcliffe), Ron (Rupert Grint), and Hermione (Emma Watson) race against time to destroy the remaining Horcruxes and defeat Voldemort (Ralph Fiennes). The film delivers a thrilling and emotionally charged climax, bringing closure to the characters and storylines that have captivated audiences for a decade. The final battle between good and evil unfolds, leading to the ultimate sacrifice, revelations, and the resolution of the prophecy. \"Deathly Hallows – Part 2\" is a fitting conclusion to the Harry Potter saga, leaving a lasting impact on fans worldwide.";
            case "Thor 1":
                return "Against his father Odin's will, The Mighty Thor - a warrior god - recklessly re-ignites an ancient warfare. Thor forced to live among humans since punishment and is throw right down to Earth. Here, Thor finds what is needed to be considered a protagonist as his world's villain sends the mysterious powers of Asgard to invade Earth.";
            case "Thor 2":
                return "Thor fights to restore order across the cosmos… but an ancient race led by the vengeful Malekith returns to plunge the universe back into darkness. Faced with an enemy that even Odin and Asgard cannot withstand, Thor must embark on his most perilous and personal journey yet, one that will reunite him with Jane Foster and force him to sacrifice everything to save us all.";
            case "Thor 3":
                return "Thor is imprisoned about the opposing side of this universe and finds himself in a race against time to get straight back to Asgard to prevent Ragnarok, the destruction of his Home World and the conclusion of Asgardian civilization, as a result of an all-powerful brand new threat, the callous Hela.";
            case "Thor 4":
                return "Thor embarks on an adventure unlike any he has ever taken before: a search for personal fulfillment. A murderer known only as Gorr the God Butcher appears out of nowhere and puts an end to his retirement plans by killing every god in the galaxy. Thor recruits King Valkyrie, Korg, and his ex-girlfriend Jane Foster to help him defeat the enemy; Jane, to Thor's consternation, ends up wielding his magical hammer, Mjolnir, as the Mighty Thor. They set forth on a perilous intergalactic journey together to figure out the source of the God Butcher's wrath and put a stop to him before it's too late.";
            case "Iron Man 1":
                return " Tony Stark, portrayed by Robert Downey Jr., a brilliant and wealthy industrialist who is also a genius inventor. While demonstrating a new weapon in Afghanistan, Stark is kidnapped by terrorists who force him to build a weapon of mass destruction. Instead, Stark constructs a powered suit of armor to escape. Back in the United States, he refines the suit and uses it to fight against those who misuse his technology. This marks the birth of the iconic superhero, Iron Man. The film explores themes of redemption, responsibility, and the consequences of advanced technology.";
            case "Iron Man 2":
                return "\"Iron Man 2\" continues Tony Stark's story as he grapples with the consequences of revealing his identity as Iron Man. The U.S. government demands that Stark share his technology, and he faces challenges from both a rival industrialist, Justin Hammer (Sam Rockwell), and a new formidable foe, Ivan Vanko (Mickey Rourke), who has his own suit of powered armor. As Stark battles personal demons and health issues, he seeks a successor for his Iron Man legacy. The film further explores the impact of technology on society and the burden of being a superhero.";
            case "Iron Man 3":
                return "\"Iron Man 3\" finds Tony Stark dealing with the aftermath of the events in \"The Avengers.\" Suffering from anxiety and insomnia, Stark faces a new threat in the form of the mysterious terrorist known as the Mandarin (Ben Kingsley). The film explores Stark's vulnerability as he confronts a powerful enemy who seems to know everything about him. \"Iron Man 3\" delves into themes of identity, resilience, and the true strength of the man behind the armor. It also addresses the consequences of Stark's past actions and sets the stage for his evolving role within the MCU. The film showcases a more personal and introspective side of Tony Stark's character.";
            case "Hulk 1":
                return "Description for The Incredible Hulk 1.";
            case "Eternals":
                return "\"Eternals\" follows the story of the Eternals, a group of ancient aliens who were sent to Earth by the Celestials to protect humanity from their counterparts, the Deviants. The film explores their history, relationships, and the reasons for their non-interference in major human events until now.\r\n"
                        + "\r\n"
                        + "The ensemble cast includes Gemma Chan as Sersi, Richard Madden as Ikaris, Angelina Jolie as Thena, Salma Hayek as Ajak, Kit Harington as Dane Whitman, and others. The Eternals reunite to face an ancient enemy and grapple with their existence and purpose.";
            case "Star Wars 1":
                return "\"The Phantom Menace\" is the first film in the prequel trilogy. It introduces young Anakin Skywalker, a pod-racing slave on Tatooine, and the discovery of a Sith threat. Jedi Knights Qui-Gon Jinn and Obi-Wan Kenobi are central characters as they try to protect Queen Amidala and unravel the mysteries surrounding Anakin.";
            case "Star Wars 2":
                return "\"Attack of the Clones\" explores the growing tension in the galaxy. Anakin Skywalker, now a Jedi apprentice, and Padmé Amidala form a romantic connection. The film delves into the creation of the Clone Army and the machinations of the Sith.";
            case "Star Wars 3":
                return "Concluding the prequel trilogy, \"Revenge of the Sith\" directed by George Lucas depicts the fall of the Jedi and the rise of Darth Vader. Anakin succumbs to the dark side of the Force, and the Galactic Republic transforms into the Galactic Empire. The film bridges the prequel and original trilogies.";
            case "Star Wars 4":
                return "\"A New Hope\" is the original film that introduced audiences to Luke Skywalker, Princess Leia, and Han Solo. The Rebel Alliance battles the tyrannical Galactic Empire, and Luke discovers his destiny as a Jedi. The film marked the beginning of the Star Wars franchise.";
            case "Star Wars 5":
                return "Directed by Irvin Kershner, \"The Empire Strikes Back\" is considered by many as one of the best in the saga. The Rebel Alliance faces setbacks, and Luke undergoes Jedi training with Yoda. Darth Vader reveals a shocking connection, and the film ends on a dark note.";
            case "Star Wars 6":
                return "Directed by Richard Marquand, \"Return of the Jedi\" concludes the original trilogy. Luke confronts Darth Vader and Emperor Palpatine, while the Rebel Alliance plans to destroy the second Death Star. The redemption of Darth Vader and the defeat of the Empire bring closure to the saga.";
            case "Star Wars 7":
                return "\"The Force Awakens\" is the beginning of the sequel trilogy. Set years after \"Return of the Jedi,\" the Resistance opposes the First Order. New characters Rey, Finn, and Poe Dameron emerge, and the search for the missing Luke Skywalker becomes central.";
            case "Star Wars 8":
                return " \"The Last Jedi\" explores the training of Rey by Luke Skywalker, the Resistance's struggle against the First Order, and the complexities of the Force. The film challenges traditional Star Wars themes and introduces unexpected developments.";
            case "Star Wars 9":
                return " \"The Rise of Skywalker\" concludes the Skywalker saga. Rey faces her heritage, and the Resistance confronts the Final Order. The film pays homage to the original trilogy and brings closure to the overarching narrative.";
            case "Business Proposal":
                return "Shin Ha-Ri (Kim Se-Jeong) is a single woman and works for a company. She has a male friend and she has held a crush on him for a long time, but she learns that her friend has a girlfriend. Shin Ha-Ri feels sad and decides to meet her friend Jin Young-Seo, who is a daughter of a chaebol family. Jin Young-Seo then asks Shin Ha-Ri to take her place in a blind date and even offers some money for her time. Shin Ha-Ri accepts her friend's offer. She goes out on the blind date as Jin Young-Seo, while having the intention to get rejected by her date. When she sees her blind date, Shin Ha-Ri is dumbfounded. Her blind date is Kang Tae-Moo (Ahn Hyo-Seop). He is the CEO of the company where she works.\r\n"
                        + "\r\n"
                        + "Kang Tae-Moo is the CEO of a company that his grandfather founded. One day, his grandfather informed him of an upcoming blind date that he set up for him. Kang Tae-Moo is a workaholic and he is annoyed that his grandfather sets up blind dates for him. He decides to marry the next woman whom he meets at a blind date, so he won't be disturbed from his work anymore. That woman is Shin Ha-Ri, but pretending to be Jin Young-Seo.\r\n"
                        + "\r\n"
                        + "On the following day, Shin Ha-Ri receives a phone call from Kang Tae-Moo. He asks her to marry him.";
            case "Crash Landing on You":
                return "Yoon Se-Ri (Son Ye-Jin) is an heiress to a conglomerate in South Korea. One day, while paragliding, an accident caused by strong winds leads Yoon Se-Ri to make an emergency landing in North Korea. There, she meets Ri Jeong-Hyeok (Hyun-Bin), who is a North Korean army officer. He tries to protect her and hide her. Soon, Ri Jeong-Hyeok falls in love with Yoon Se-Ri.";
            case "Friends":
                return "Six young men and women live in the same apartment complex and face life and love together in Manhattan, New York City. As they're constantly sticking their noses into each another's businesses, as well as sometimes swapping romantic partners, the group always get into the kind of comic situations that most other people never experience, especially during breakups.";
            case "Itaewon Class":
                return "On the first day of attending his new high school, Park Sae-Ro-Yi (Park Seo-Joon) punches his classmate Jang Geun-Won, who was bullying another classmate. The bully is the son of CEO Jang Dae-Hee (Yoo Jae-Myung). The bully's father runs restaurant business Jagga where Park Sae-Ro-Yi’s own father works. CEO Jang Dae-Hee demands to Park Sae-Ro-Yi that he apologizes to his son, but Park Sae-Ro-Yi refuses. Because of his refusal, Park Sae-Ro-Yi gets expelled from school and his father gets fired from his job. Soon, an accident takes place. Park Sae-Ro-Yi’s father dies in a motorcycle accident caused by his ex-classmate Jang Geun-Won. Burning with anger, Park Sae-Ro-Yi viciously beats Jang Geun-Won. He is soon arrested and receives prison time for the violent assault. Park Sae-Ro-Yi decides to destroy the Jagga company and take revenge upon CEO Jang Dae-Hee and his son Jang Geun-Won. Once Park Sae-Ro-Yi is released from prison, he opens a restaurant in Itaewon, Seoul. Jo Yi-Seo (Kim Da-Mi), who is popular on social media, joins Park Sae-Ro-Yi’s restaurant and works there as a manger. She has feelings for Park Sae-Ro-Yi.";
            case "Squid Games":
                return "Hundreds of cash-strapped contestants accept an invitation to compete in children's games for a tempting prize, but the stakes are deadly.";
            case "Start Up":
                return "Needing to make $90k to open her own business, Seo Dal-Mi drops out of a university and takes up part-time work. She dreams of becoming someone like Steve Jobs.\r\n"
                        + "\r\n"
                        + "Nam Do-San is the founder of Samsan Tech. He is excellent with mathematics. He started Samsan Tech two years ago, but the company is not doing well. Somehow, Nam Do-San becomes Seo Dal-Mi’s first love. They cheer each others start and growth.";
            case "The Glory":
                return "A high school student dreamed of one day working as an architect. She became a victim of high school violence perpetrated by her fellow students. She dropped out of high school because of the bullying. She then planned revenge on her tormentors and also the bystanders who did nothing.\r\n"
                        + "\r\n"
                        + "That student is now an adult. She has waited for the leader of her tormentors to get married and have a child. That child is now an elementary school student. The women who was once a victim of school violence, is now the homeroom teacher of her tormentor's child. Her cruel revenge plot begins in earnest.";
            case "The Office":
                return "This US adaptation, set at a paper company in Scranton, Pa., has a similar documentary style to that of the Ricky Gervais-led British original. It features the staff of Dunder-Mifflin, a staff that includes characters based on characters from the British show (and, quite possibly, people you work with in your office). There's Jim, the likable employee who's a bit of an everyman. Jim has a thing for receptionist-turned-sales rep Pam (because office romances are always a good idea). There's also Dwight, the successful co-worker who lacks social skills and common sense. And there's Ryan, who has held many jobs at the company.";
            case "The Big Bang Theory":
                return "Mensa-fied best friends and roommates Leonard and Sheldon, physicists who work at the California Institute of Technology, may be able to tell everybody more than they want to know about quantum physics, but getting through most basic social situations, especially ones involving women, totally baffles them. How lucky, then, that babe-alicious waitress/aspiring actress Penny moves in next door. Frequently seen hanging out with Leonard and Sheldon are friends and fellow Caltech scientists Wolowitz and Koothrappali. Will worlds collide? Does Einstein theorize in the woods?";
            case "/narutoshowtrailer.mp4":
                return "https://www.youtube.com/watch?v=QczGoCmX-pI&ab_channel=vizmedia";
            case "/FriendsTrailer.mp4":
                return "https://www.youtube.com/watch?v=xckVgRWC8GY&ab_channel=TBS";
            case "/HarryPotterTrailer.mp4":
                return "https://www.youtube.com/watch?v=XXvlqsSeJ7Q&list=PLe-suHc2qwnQPMcjrkwRXWxRLfRX19vZm&index=3&ab_channel=WizardingWorld";
            case "/OnePieceTrailer.mp4":
                return "https://www.youtube.com/watch?v=gr8reTtElqc&ab_channel=CrunchyrollCollection";
            case "/SquidGamesTrailer.mp4":
                return "https://www.youtube.com/watch?v=oqxAJKy0ii4&ab_channel=Netflix";
            default:
                return "";
        }
    }

    private VBox createContactUs() {
        // Create "Contact Us" label
        Label contactUsLabel = new Label("Contact Us");
        contactUsLabel.setStyle("-fx-underline: true; -fx-font-size: 16px; -fx-text-fill: black;");

        // Create image
        Image contactUsImage = new Image("file:src/images/contactimage.jpg"); // Replace with your image path
        ImageView imageView = new ImageView(contactUsImage);
        imageView.setFitWidth(200); // Set the width as needed
        imageView.setFitHeight(60); // Set the height as needed

        contactUsLabel.setOnMouseClicked(event -> {
            getHostServices().showDocument("https://gogoanimeapp.com/contact-us.html");
        });

        // Change font color when the cursor enters
        contactUsLabel.setOnMouseEntered(event -> {
            contactUsLabel.setStyle("-fx-underline: true; -fx-font-size: 16px; -fx-text-fill: orange;");
        });

        // Change font color back when the cursor exits
        contactUsLabel.setOnMouseExited(event -> {
            contactUsLabel.setStyle("-fx-underline: true; -fx-font-size: 16px; -fx-text-fill: black;");
        });

        imageView.setOnMouseClicked(event -> {
            getHostServices().showDocument("https://gogoanimeapp.com/contact-us.html");
        });

        // Create VBox to hold label and image
        VBox contactUsBox = new VBox(contactUsLabel, imageView);
        contactUsBox.setAlignment(Pos.CENTER);
        contactUsBox.setSpacing(10);
        contactUsBox.setPadding(new Insets(10));
        contactUsBox.setStyle("-fx-background-color: darkgrey;");

        return contactUsBox;
    }

    //Create the Trailers
    ////////////////////////////////////////////////////////////////
    private VBox createTrailerVid(String trailerPath, String titlePath) {
        // Create media player
        String mediaPath = trailerPath;
        URL mediaUrl = getClass().getResource(mediaPath);

        if (mediaUrl == null) {
            System.err.println("Could not find the media file: " + mediaPath);
            return new VBox(); // or handle the error accordingly
        }

        Media media = new Media(mediaUrl.toExternalForm());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);

        // Set up looping for the media player
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.seek(Duration.ZERO);
            mediaPlayer.play();
        });

        // Create media view
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setFitWidth(800);
        mediaView.setFitHeight(600);
        mediaView.setStyle("-fx-background-color: transparent;");

        // Create buttons
        Button playPauseButton = new Button("Pause");
        playPauseButton.setStyle("-fx-background-color: #ffffff; -fx-font-family: 'Arial'; -fx-font-weight: bold; -fx-text-fill: black; -fx-font-size: 14;");
        playPauseButton.setOnAction(e -> {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.play();
            }
        });

        Button moreInfoButton = new Button("More Info");
        moreInfoButton.setStyle("-fx-background-color: #4f4e51; -fx-font-family: 'Arial'; -fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 14;");
        moreInfoButton.setOnAction(e -> showMoreInfo(trailerPath));

        Button muteButton = new Button("Mute");
        muteButton.setStyle("-fx-background-color: #ffffff;");
        muteButton.setOnMouseClicked(e -> {
            mediaPlayer.setMute(!mediaPlayer.isMute());
            updateMuteButtonImage(muteButton, mediaPlayer.isMute());
        });
        updateMuteButtonImage(muteButton, mediaPlayer.isMute());

        // Create control box
        HBox controlBox = createControlBox(playPauseButton, moreInfoButton, muteButton, titlePath, trailerPath);
        this.hostServices = getHostServices();

        // Create VBox for video and controls
        VBox videoVBox = new VBox();
        videoVBox.getChildren().addAll(mediaView, controlBox);
        videoVBox.setAlignment(Pos.TOP_CENTER);
        VBox.setMargin(videoVBox, new Insets(50, 0, 0, 0));

        return videoVBox;
    }

    private HBox createControlBox(Button playPauseButton, Button moreInfoButton, Button muteButton, String titlePath, String trailerPath) {
        HBox controlBox = new HBox(10);
        controlBox.setStyle("-fx-background-color: transparent;");

        //Load Image
        ImageView imageView = new ImageView(new Image(getClass().getResource(titlePath).toExternalForm()));
        // keep the dimensions the same
        imageView.setPreserveRatio(true);
        // Adjust image dimensions if needed
        imageView.setFitWidth(230);
        imageView.setFitHeight(90);

        // Load play and pause images
        Image playImage = new Image(getClass().getResource("/play.png").toExternalForm());
        Image pauseImage = new Image(getClass().getResource("/pause.png").toExternalForm());

        // Set the initial image to pause Image
        ImageView playPauseImageView = new ImageView(pauseImage);
        playPauseImageView.setFitWidth(20);
        playPauseImageView.setFitHeight(20);

        // Set the initial image to the playPauseButton
        playPauseButton.setGraphic(playPauseImageView);

        playPauseButton.setOnAction(e -> {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause();
                playPauseImageView.setImage(playImage);
                playPauseButton.setText("Play");
                playPauseButton.setStyle("-fx-background-color: #ffffff; -fx-font-family: 'Arial'; -fx-font-weight: bold; -fx-text-fill: black; -fx-font-size: 14;");
            } else {
                mediaPlayer.play();
                playPauseImageView.setImage(pauseImage);
                playPauseButton.setText("Pause");
                playPauseButton.setStyle("-fx-background-color: #ffffff; -fx-font-family: 'Arial'; -fx-font-weight: bold; -fx-text-fill: black; -fx-font-size: 14;");
            }
        });

        // Load more info image
        Image moreInfoImage = new Image(getClass().getResource("/moreinfo.png").toExternalForm());
        ImageView moreInfoImageView = new ImageView(moreInfoImage);
        moreInfoImageView.setFitWidth(20);
        moreInfoImageView.setFitHeight(20);

        // Set the image to the moreInfoButton
        moreInfoButton.setGraphic(moreInfoImageView);

        // Load mute and volume images
        Image muteImage = new Image(getClass().getResource("/mute.png").toExternalForm());
        Image volumeImage = new Image(getClass().getResource("/volume.png").toExternalForm());

        // Set the initial image to volumeImage
        ImageView volumeImageView = new ImageView(volumeImage);
        volumeImageView.setFitWidth(20);
        volumeImageView.setFitHeight(20);

        // Set the initial image to volumeImage
        ImageView muteImageView = new ImageView(muteImage);
        muteImageView.setFitWidth(20);
        muteImageView.setFitHeight(20);

        // Set the initial image to the muteButton
        muteButton.setGraphic(volumeImageView);
        muteButton.setText(""); // Remove button text
        muteButton.setStyle("-fx-background-color: transparent;"); // Set transparent background

        muteButton.setOnAction(e -> {
            muteButton.setGraphic(muteImageView);
        });

        // Apply circular shape to the mute button
        muteButton.setStyle("-fx-background-radius: 50em; -fx-min-width: 30px; -fx-min-height: 30px; -fx-background-color: #ffffff;");

        moreInfoButton.setOnAction(e -> showMoreInfo(trailerPath));
        muteButton.setOnAction(e -> mediaPlayer.setMute(!mediaPlayer.isMute()));

        // Shift the imageView to the left by setting a margin
        controlBox.setAlignment(Pos.BOTTOM_LEFT);
        // Set the translation directly on the controlBox
        controlBox.setTranslateX(330); // Adjust as needed
        controlBox.setTranslateY(-150); // Adjust as needed

        HBox.setMargin(imageView, new Insets(0, 0, 50, -225)); // Adjust these values as needed

        controlBox.getChildren().addAll(playPauseButton, moreInfoButton, muteButton, imageView);
        return controlBox;
    }

    private void showMoreInfo(String trailerPath) {
        // Create a new stage for more information
        hostServices.showDocument(getMessageForItem(trailerPath));
    }

    private void updateMuteButtonImage(Button muteButton, boolean isMuted) {
        Image muteImage = new Image(getClass().getResource("/mute.png").toExternalForm());
        Image volumeImage = new Image(getClass().getResource("/volume.png").toExternalForm());

        ImageView muteImageView = new ImageView(isMuted ? muteImage : volumeImage);
        muteImageView.setFitWidth(20);
        muteImageView.setFitHeight(20);

        muteButton.setGraphic(muteImageView);
    }

    @Override
    public void stop() {
        mediaPlayer.stop();
    }


    //end trailer section
    ///////////////////////////////////////////////////////////

    private void addScaleTransition(ImageView imageView) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), imageView);
        scaleTransition.setToX(1.0);
        scaleTransition.setToY(1.0);

        imageView.setOnMouseEntered(event -> scaleTransition.setToX(1.2));
        imageView.setOnMouseExited(event -> scaleTransition.setToX(1.0));

        imageView.setOnMouseClicked(event -> {
            scaleTransition.setToX(1.0);
            scaleTransition.setToY(1.0);
        });
    }
}