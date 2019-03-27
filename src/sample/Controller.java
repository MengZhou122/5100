package sample;
import javafx.application.HostServices;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.im4java.core.Info;

import java.awt.*;
import java.io.*;
import javafx.scene.control.TextFormatter.Change;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class Controller {
    @FXML
    private BorderPane borderPane ;


    @FXML
    private ChoiceBox choiceBox;
    @FXML
    private Label chooseSize;
    @FXML
    private Label successfulNotice;
    @FXML
    private TextField textWidth;
    @FXML
    private TextField textHeight;
    @FXML
    private Label errDisplay;
    @FXML
    private GridPane gridpane;
    @FXML
    private CheckBox checkBox;
    @FXML
    private Label taskCompleted;
    @FXML
    private Label content_header;
    @FXML
    private ImageView con;

    private Image image;
    private int width;
    private int height;
    private int convertWidth;
    private int convertHeight;
    FileInputStream input;
    private ImageView imageView;
    private List<File> files;
    private String text;


    @FXML
    public void startUpload(){
        if(getValueSpinner()){
            errDisplay.setVisible(false);
            chooseSize.setVisible(true);
            uploadHandle();
        }else{
            Label oops1 = new Label("Check Your Input Width and Height");
            Label oops2 = new Label("Please input valid numbers");
            oops1.setTextFill(javafx.scene.paint.Paint.valueOf("#ff0000"));
            oops1.setFont(javafx.scene.text.Font.font("Calibri", FontWeight.EXTRA_BOLD,20));
            oops2.setTextFill(javafx.scene.paint.Paint.valueOf("#ff0000"));
            oops2.setFont(javafx.scene.text.Font.font("Calibri", FontWeight.EXTRA_BOLD,20));

            Class<?> clazz = this.getClass();
            InputStream input = clazz.getResourceAsStream("../image/oops.jpg");

            Image imagess = new Image(input);
            ImageView imageView1 = new ImageView(imagess);
            imageView1.setFitWidth(200);
            imageView1.setFitHeight(200);
            gridpane.add(imageView1,0,1);
            gridpane.add(oops1,0,2);
            gridpane.add(oops2,0,3);
        }
    }

    @FXML
    public void convert() throws FileNotFoundException{
        handleConvert();
        content_header.setVisible(false);
        gridpane.getChildren().clear();
        Label con1 = new Label("Congraulation! ");
        Label con2 = new Label("Your images have been converted!!! ");

        con1.setTextFill(javafx.scene.paint.Paint.valueOf("#ff0000"));
        con1.setFont(javafx.scene.text.Font.font("Calibri", FontWeight.EXTRA_BOLD,20));
        con2.setTextFill(javafx.scene.paint.Paint.valueOf("#ff0000"));
        con2.setFont(javafx.scene.text.Font.font("Calibri", FontWeight.EXTRA_BOLD,20));

        Class<?> clazz = this.getClass();
        InputStream input = clazz.getResourceAsStream("../image/con.png");

        Image imagess = new Image(input);
        ImageView imageView1 = new ImageView(imagess);
        imageView1.setFitWidth(200);
        imageView1.setFitHeight(200);

        gridpane.add(imageView1,0,1);
        gridpane.add(con1,0,2);
        gridpane.add(con2,0,3);
    }

    @FXML
    public void uploadHandle() {

        String sourcePath;
        String imageName;
        gridpane.getChildren().clear();
        numbericOnly();
        int count = 0;

        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif"
                ,"*.psd","*.tif","*.pdf","*.jpeg")
        );
//        File file = chooser.showOpenDialog(borderPane.getScene().getWindow());
        files = chooser.showOpenMultipleDialog(borderPane.getScene().getWindow());

        if (files != null) {
            for (int i=0;i<files.size();i++){
                try{
                    Label textArea = new Label();
                    text = "";
                    File file = files.get(i);
                    int indexX = i%5;
                    int indexY = i/5;
                    count++;

                    sourcePath = files.get(i).getPath();
                    javaxt.io.Image imageInfo = new javaxt.io.Image(sourcePath);
                    java.util.HashMap<Integer, Object> exif = imageInfo.getExifTags();
                    double[] coord = imageInfo.getGPSCoordinate();

                    System.out.println();
                    imageName = file.getName();
                    System.out.println("imageName: "+imageName);
                    image = new Image(file.toURI().toString());

                    // dispaly images
                    input = new FileInputStream(file);
                    image = new Image(input);
                    imageView = new ImageView(image);
                    if(files.size()==1){
                        imageView.setFitHeight(200);
                        imageView.setFitWidth(200);
                    }else if(files.size()>1&&files.size()<5){
                        imageView.setFitHeight(130);
                        imageView.setFitWidth(130);
                    }else if(files.size()>=5 ) {
                        imageView.setFitHeight(100);
                        imageView.setFitWidth(100);
//                        gridpane.setVgap(100);
                    }

                    if(coord!=null){
                        text = "Name: "+imageName +"\n"+"Height: "+image.getHeight()+"\n"+ "Width: "+image.getWidth()+"\n"+
                                "Date: " + (exif.get(0x0132).toString().substring(0,10))+"\n"+"Camera: " + exif.get(0x0110)+"\n"+
                                "Latitude: "+coord[0]+ "\n"+"Longitude: "+coord[1]+"\n"+
                                "Manufacturer: " + exif.get(0x010F);
                    }else{
                        text = "name: "+imageName +"\n"+"height: "+image.getHeight()+"\n"+
                                "width: "+image.getWidth();
                    }

                    textArea.setText(text);
                    System.out.println(text);

                    if(count>5){
                        indexY+=4;
                    }
                    gridpane.add(imageView,indexX,indexY);
                    gridpane.add(textArea,indexX,indexY+1);
//                    handleConvert();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("Chooser was cancelled");
        }
    }

    @FXML
    public void handleConvert() {
        String imageName;
        String filePath;

        String sourcePath ;

        Info imageInfo;


        FileChooser chooser = new FileChooser();

        File file = chooser.showSaveDialog(borderPane.getScene().getWindow());



        for (int i=0;i<files.size();i++) {
            chooser.setInitialFileName(file.getName());
            chooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*." + choiceBox.getValue())
            );
            String destinationPath = "";
            File imageFile = files.get(i);
            imageName = imageFile.getName();
            sourcePath = files.get(i).getPath();
            if (file != null) {
                try {
                    filePath = file.getPath();

                    System.out.println("filePath: "+filePath);
                    if(files.size()>1){
                        destinationPath = filePath.substring(0,filePath.lastIndexOf("/")+1);
                        System.out.println("destinationPath "+destinationPath);
                        destinationPath +=imageFile.getName().substring(0,imageFile.getName().length()-4);
                        System.out.println("destinationPath "+destinationPath);
                        destinationPath += ("." + choiceBox.getValue());
                        System.out.println("destinationPath "+destinationPath);
                    }else {
                        destinationPath = filePath;
                        destinationPath += ("." + choiceBox.getValue());
                    }
                    width = convertWidth;
                    height = convertHeight;
                    System.out.println(width);
                    System.out.println(height);
                } catch (Exception e) {
                    System.out.println(e.toString());
                }}


                ConvertCmd cmd = new ConvertCmd();
                IMOperation op = new IMOperation();
                op.addImage(sourcePath);
                System.out.println("sourcePath: "+sourcePath);
                System.out.println("destinationPath: "+destinationPath);
                op.resize(width, height);
                if (checkBox.isSelected()) {
                    op.blur(0.0, 8.0);
                }
                op.addImage(destinationPath);
                try {
                    cmd.run(op);
                    successfulNotice.setText("Conversion was successful!");
                    System.out.println("Conversion was successful!!!!");
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
        }
    }


    public void numbericOnly(){
        // force the field to be numeric only
        textWidth.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    textWidth.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        // force the field to be numeric only
        textHeight.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    textHeight.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    public boolean getValueSpinner(){
        if(textWidth.getText().equals("")){
            System.out.println("nullll");
            errDisplay.setVisible(true);
            chooseSize.setVisible(false);
            return false;
        }
        try{
            System.out.println("textWidth.getText(): "+textWidth.getText());
            convertWidth = Integer.parseInt(textWidth.getText());
            System.out.println("textHeight.getText()"+textHeight.getText());
            convertHeight = Integer.parseInt(textHeight.getText());
            if(convertHeight==0||convertWidth==0){
                chooseSize.setText("illegal number");
            }
            System.out.println("convertWidth: "+convertWidth);
            System.out.println("convertHeight: "+convertHeight);
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

}
