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
    private Scene rsaScene = new Scene(mainBox,320,250);
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
    private Label messageLabel = new Label("Message: ");

    //Mess to decrypt
    private Button messToDecryptButton = new Button("Load cryptogram");
    private TextField messToDecryptField = new TextField();
    private HBox decryptBox = new HBox(messToDecryptButton, messToDecryptField);
    private VBox outPutBox = new VBox(decryptBox, cryptogramLabel, privateKeyLabel, publicKeyLabel, messageLabel);



    //RSA
    private RSA rsa;

    //File
    File keyFile;

    public RSA_interface()
    {

        decryptBox.setSpacing(15);
        decryptBox.setPadding(new Insets(10,0,10,10));
        messToDecryptField.setPrefSize(170,30);
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
                    inputKeyField.setText(fileReader.readFile(keyFile.getPath()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        messToDecryptButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    keyFile = fileChooserKey.showOpenDialog(mainStage);
                    FileReader fileReader = new FileReader();
                    messToDecryptField.setText(fileReader.readFile(keyFile.getPath()));
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
                        messageLabel.setText("Message: ");
                        String messageToCrypt = inputField.getText();
                        rsa = new RSA(messageToCrypt,2048);
                        rsa.setCryptogram(rsa.encrypt());
                        cryptogramLabel.setText("Cryptogram: " + rsa.getCryptogram());
                        messToDecryptField.setText("");
                        messToDecryptField.setText(rsa.getCryptogram());

                        try (PrintWriter out = new PrintWriter("cryptogram")) {
                            out.println(rsa.getCryptogram());
                        }

                        String privateKeyPemStr = rsa.savePrivateKey();
                        privateKeyLabel.setText("Private Key: " + privateKeyPemStr);
                        inputKeyField.setText(privateKeyPemStr);

                        try (PrintWriter out = new PrintWriter("privateKey")) {
                            out.println(privateKeyPemStr);
                        }

                        String publicKeyPemStr = rsa.savePublicKey();
                        publicKeyLabel.setText("Private Key: " + publicKeyPemStr);

                        try (PrintWriter out = new PrintWriter("publicKey")) {
                            out.println(publicKeyPemStr);
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

                    rsa.loadPrivateKey(inputKeyField.getText());
                    rsa.setCryptogram(messToDecryptField.getText());
                    messageLabel.setText("Message: " + rsa.decrypt());
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


        mainBox.getChildren().addAll(inputBox,enDeButtonsBox, inputKeyBox, outPutBox);
        mainStage.setTitle("RSA");
        mainStage.setScene(rsaScene);
    }




}
