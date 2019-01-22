package main.Interface;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.algorithm.FileReader;
import main.algorithm.RSA;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class RSA_interface {

    //Main
    private VBox mainBox = new VBox();
    private Scene rsaScene = new Scene(mainBox,300,250);
    public Stage mainStage = new Stage();



    //Input
    private FileChooser fileChooser = new FileChooser();
    private Button loadFileButton = new Button("Load message");
    private TextField inputField = new TextField("Type text here!");
    private HBox inputBox = new HBox(loadFileButton, inputField);

    //InputKey
    private FileChooser fileChooserKey = new FileChooser();
    private Button loadFileKeyButton = new Button("Load PrivKey");
    private TextField inputKeyField = new TextField();
    private HBox inputKeyBox = new HBox(loadFileKeyButton, inputKeyField);


    //En/De Buttons
    private Button encypteButton = new Button("Encrypte");
    private Button decrypteButton = new Button("Decrypte");
    private HBox enDeButtonsBox = new HBox(encypteButton,decrypteButton);


    //Output Label
    private Label privateKeyLabel = new Label("Private key:");
    private Label publicKeyLabel = new Label("Public key:");
    private Label cryptogramLabel = new Label("Cryptogram:");
    private VBox outPutBox = new VBox(cryptogramLabel, privateKeyLabel, publicKeyLabel);

    //RSA
    private RSA rsa;

    //File
    File keyFile;

    public RSA_interface()
    {
        //Input Box
        inputField.setPrefSize(170,30);
        inputBox.setPadding(new Insets(20,0,0,10));
        inputBox.setSpacing(10);

        inputKeyBox.setPadding(new Insets(20,0,0,10));
        inputKeyBox.setSpacing(10);

        loadFileButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File file = fileChooser.showOpenDialog(mainStage);
                FileReader fileReader = new FileReader();

                try {
                    String message = fileReader.readFile(file.getPath());
                    inputField.setText(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        loadFileKeyButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    keyFile = fileChooserKey.showOpenDialog(mainStage);
                    FileReader fileReader = new FileReader();
                    inputKeyField.setText("Public key: " + fileReader.readFile(keyFile.getPath()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



        encypteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(inputField.getText().equals(""))
                {
                   inputField.setText("Input can't be empty!");
                }
                else
                {
                    try {
                        rsa = new RSA(inputField.getText(),2048);


                        cryptogramLabel.setText("Cryptogram: ");

                        String privateKeyPemStr = new String(Base64.getEncoder().encode(rsa.getPrivateKey().getEncoded()));
                        privateKeyLabel.setText("Private Key: " + privateKeyPemStr);
                        inputKeyField.setText(privateKeyPemStr);

                        try (PrintWriter out = new PrintWriter("privateKey")) {
                            out.println(new String(Base64.getEncoder().encode(rsa.getPrivateKey().getEncoded())));
                        }

                        String publicKeyPemStr = new String(Base64.getEncoder().encode(rsa.getPublicKey().getEncoded()));
                        publicKeyLabel.setText("Private Key: " + publicKeyPemStr);

                        try (PrintWriter out = new PrintWriter("publicKey")) {
                            out.println(new String(Base64.getEncoder().encode(rsa.getPublicKey().getEncoded())));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        decrypteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {


                try {
                    rsa = new RSA(inputField.getText(),2048);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


        //EnDe Buttons
        enDeButtonsBox.setPadding(new Insets(20,0,0,40));
        enDeButtonsBox.setSpacing(30);
        encypteButton.setPrefSize(100,55);
        decrypteButton.setPrefSize(100,55);


        mainBox.getChildren().addAll(inputBox,inputKeyBox,  enDeButtonsBox, outPutBox);
        mainStage.setTitle("RSA");
        mainStage.setScene(rsaScene);
    }




}
