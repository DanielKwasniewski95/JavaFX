package com.daniel.app.controller;

import com.daniel.app.data.Device;
import com.daniel.app.service.ConfigFile;
import com.daniel.app.service.XmlReadWrite;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javafx.stage.FileChooser;

public class Controller {

    private boolean valid;
    private List<Device> deviceList;
    private Pattern pattern;
    private ObservableList<String> choices;
    private Stage primaryStage;
    private String path;
    private XmlReadWrite xmlReadWrite;
    private ConfigFile configFile;

    @FXML
    private TableView<Device> deviceTableView;
    @FXML
    private TableColumn<Device, String> deviceNameColumn;
    @FXML
    private TableColumn<Device, Double> devicePowerColumn;
    @FXML
    private TableColumn<Device, Double> deviceTimeColumn;
    @FXML
    private TextField deviceNameField;
    @FXML
    private TextField powerField;
    @FXML
    private TextField resultField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField priceKwHField;
    @FXML
    private TextField timeField;
    @FXML
    private Button addButton;
    @FXML
    private Button countButton;
    @FXML
    private Button clearAllButton;
    @FXML
    private ChoiceBox<String> periodChoiceBox;


    public Controller() {
        this.deviceList = new ArrayList<>();
        this.pattern = Pattern.compile("[0-9]+[a-z]+");
        choices = FXCollections.observableArrayList(" ", "week", "month", "year");
        xmlReadWrite = new XmlReadWrite();
        configFile = new ConfigFile();
    }

    //******buttons*******
    @FXML
    private void handleAdd() {

        boolean valid2;
        String time = "";
        String power = "";
        String deviceName = deviceNameField.getText();
        double devicePower = 0;
        double deviceTime = 0;

        try {
            power = powerField.getText();
            if (pattern.matcher(power).matches()) {
                powerField.setText("bad format");
                powerField.setStyle("-fx-text-fill: red");
                valid = false;
            } else {
                devicePower = Double.parseDouble(powerField.getText());
                if (devicePower == 0) {
                    powerField.setText("typed zero");
                    powerField.setStyle("-fx-text-fill: red");
                    valid = false;
                } else if (devicePower < 0) {
                    valid = false;
                    powerField.setText("bad value");
                    powerField.setStyle("-fx-text-fill: red");
                } else {
                    valid = true;
                }

            }
        } catch (Exception e) {
            if (power.equals("typed zero")) {
                powerField.setText("typed zero");
                powerField.setStyle("-fx-text-fill: red");
                valid = false;
            } else if (power.equals("bad value")) {
                powerField.setText("bad value");
                powerField.setStyle("-fx-text-fill: red");
                valid = false;
            } else {
                powerField.setText("bad format");
                powerField.setStyle("-fx-text-fill: red");
                valid = false;
            }
        }

        try {
            time = timeField.getText();
            if (pattern.matcher(time).matches()) {
                timeField.setText("bad format");
                timeField.setStyle("-fx-text-fill: red");
                valid2 = false;
            } else {
                deviceTime = Double.parseDouble(timeField.getText());
                if (deviceTime > 1440) {
                    valid2 = false;
                    timeField.setText("to high");
                    timeField.setStyle("-fx-text-fill: red");
                } else if (deviceTime == 0) {
                    timeField.setText("typed zero");
                    timeField.setStyle("-fx-text-fill: red");
                    valid2 = false;
                } else if (deviceTime < 0) {
                    valid2 = false;
                    timeField.setText("bad value");
                    timeField.setStyle("-fx-text-fill: red");
                } else {
                    valid2 = true;
                }
            }
        } catch (Exception e) {
            if (time.equals("typed zero")) {
                timeField.setText("typed zero");
                timeField.setStyle("-fx-text-fill: red");
                valid2 = false;
            } else if (time.equals("bad value")) {
                timeField.setText("bad value");
                timeField.setStyle("-fx-text-fill: red");
                valid2 = false;
            } else {
                timeField.setText("bad format");
                timeField.setStyle("-fx-text-fill: red");
                valid2 = false;
            }
        }

        if (valid && valid2) {
            Device device = new Device(deviceName, devicePower, deviceTime);
            deviceTableView.getItems().add(device);
            this.deviceList.add(device);

            deviceNameField.clear();
            timeField.clear();
            powerField.clear();
        }
        addButton.setDisable(true);
    }

    @FXML
    private void handleSave() {
        this.deviceList.clear();
        for (int i = 0; i < deviceTableView.getItems().size(); i++) {
            this.deviceList.add(deviceTableView.getItems().get(i));

        }
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML Files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        try {
            File file = fileChooser.showSaveDialog(primaryStage);
            this.path = file.getAbsolutePath();
            xmlReadWrite.saveXmlFile(this.deviceList, this.path);
            configFile.saveConfigFile(this.path);
        }catch(Exception e){}
    }

    @FXML
    private void handleLoad() {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML Files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(primaryStage);
        try {
            this.path = file.getAbsolutePath();

        this.deviceList.clear();
        this.deviceList = xmlReadWrite.readXmlFile(this.path);
        configFile.saveConfigFile(this.path);
        }
        catch (Exception e){}

        handleClear();
        if (!deviceList.isEmpty())
            for (Device device : deviceList) {
                deviceTableView.getItems().add(device);
            }
    }

    @FXML
    private void deleteSelectedRow(ActionEvent actionEvent) {
        deviceTableView.getItems().removeAll(deviceTableView.getSelectionModel().getSelectedItem());
        this.deviceList.clear();
        for (int i = 0; i < deviceTableView.getItems().size(); i++) {
            this.deviceList.add(deviceTableView.getItems().get(i));
        }

    }

    @FXML
    private void handleClear() {
        for (int i = 0; i < deviceTableView.getItems().size(); i++) {
            deviceTableView.getItems().clear();
        }
    }

    @FXML
    private void handleCount() {
        DecimalFormat dfPower = new DecimalFormat("####0.000");
        DecimalFormat dfPrice = new DecimalFormat("####0.00");

        String priceText = "";
        String period;
        double kwH = 0;
        double price = 0;

        if (this.periodChoiceBox.getSelectionModel().isEmpty()) {
            for (Device device : this.deviceList) {
                kwH = kwH + ((device.getPower() / 1000) * (device.getTime() / 60));
            }
        } else {
            period = this.periodChoiceBox.getSelectionModel().getSelectedItem();
            if (period.equals(" ")) {
                for (Device device : this.deviceList) {
                    kwH = kwH + ((device.getPower() / 1000) * (device.getTime() / 60));
                }
            }
            if (period.equals("week")) {
                for (Device device : this.deviceList) {
                    kwH = kwH + (((device.getPower() / 1000) * (device.getTime() / 60)) * 7);
                }
            }
            if (period.equals("month")) {
                for (Device device : this.deviceList) {
                    kwH = kwH + (((device.getPower() / 1000) * (device.getTime() / 60)) * 30);
                }
            }
            if (period.equals("year")) {
                for (Device device : this.deviceList) {
                    kwH = kwH + (((device.getPower() / 1000) * (device.getTime() / 60)) * 365);
                }
            }
        }
        try {
            priceText = priceKwHField.getText();
            if (pattern.matcher(priceText).matches()) {
                priceKwHField.setText("bad format");
                priceKwHField.setStyle("-fx-text-fill: red");
                valid = false;
            } else {
                Double priceKWH = Double.parseDouble(priceText);
                if (priceKWH == 0) {
                    priceKwHField.setText("typed zero");
                    priceKwHField.setStyle("-fx-text-fill: red");
                    valid = false;
                } else if (priceKWH < 0) {
                    priceKwHField.setText("bad value");
                    priceKwHField.setStyle("-fx-text-fill: red");
                    valid = false;
                } else {
                    price = price + (kwH * priceKWH);
                    valid = true;
                }
            }
        } catch (Exception e) {
            if (priceText.equals("typed zero")) {
                priceKwHField.setText("typed zero");
                priceKwHField.setStyle("-fx-text-fill: red");
                valid = false;
            } else if (priceText.equals("bad value")) {
                priceKwHField.setText("bad value");
                priceKwHField.setStyle("-fx-text-fill: red");
                valid = false;
            } else {
                priceKwHField.setText("bad format");
                priceKwHField.setStyle("-fx-text-fill: red");
                valid = false;
            }
        }
        if (valid) {
            resultField.setText("" + dfPower.format(kwH));
            priceField.setText("" + dfPrice.format(price));
        }
    }
//******buttons*******

    //******labels*******
    @FXML
    private void keyReleasedPropertyAddButton() {
        powerField.setStyle("-fx-text-fill: black");
        timeField.setStyle("-fx-text-fill: black");

        if ((deviceNameField.getText().trim().isEmpty() || powerField.getText().trim().isEmpty()) || timeField.getText().trim().isEmpty()) {
            addButton.setDisable(true);
        } else {
            addButton.setDisable(false);
        }
    }

    @FXML
    private void keyReleasedPropertyCountButton() {
        priceKwHField.setStyle("-fx-text-fill: black");

        if (priceKwHField.getText().trim().isEmpty() || this.deviceList.isEmpty()) {
            countButton.setDisable(true);
        } else {
            countButton.setDisable(false);
        }
    }

    @FXML
    private void keyReleasedPropertyClearButton() {
        if (this.deviceList.isEmpty()) {
            clearAllButton.setDisable(true);
        } else {
            clearAllButton.setDisable(false);
        }
    }
    //******labels*******

    //******table*******
    @FXML
    private void editNameColumn(TableColumn.CellEditEvent<Device, String> deviceStringCellEditEvent) {
        Device device = deviceTableView.getSelectionModel().getSelectedItem();
        device.setName(deviceStringCellEditEvent.getNewValue());
    }

    @FXML
    private void editPowerColumn(TableColumn.CellEditEvent<Device, Double> deviceDoubleCellEditEvent) {
        Device device = deviceTableView.getSelectionModel().getSelectedItem();
        device.setPower(deviceDoubleCellEditEvent.getNewValue());
    }

    @FXML
    private void editTimeColumn(TableColumn.CellEditEvent<Device, Double> deviceDoubleCellEditEvent) {
        Device device = deviceTableView.getSelectionModel().getSelectedItem();
        device.setTime(deviceDoubleCellEditEvent.getNewValue());
    }
    //******table*******

    //******textFields*******
    @FXML
    private void selectAllPowerField(MouseEvent mouseEvent) {
        powerField.selectAll();
    }

    @FXML
    private void selectAllPriceField(MouseEvent mouseEvent) {
        priceKwHField.selectAll();
    }

    @FXML
    private void selectAllTimeField(MouseEvent mouseEvent) {
        timeField.selectAll();
    }
    //******textFields*******

    @FXML
    private void initialize() {
        deviceNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        devicePowerColumn.setCellValueFactory(new PropertyValueFactory<>("power"));
        deviceTimeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        deviceTableView.setEditable(true);
        deviceNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        devicePowerColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        deviceTimeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

        addButton.setDisable(true);
        countButton.setDisable(true);
        priceField.setEditable(false);
        resultField.setEditable(false);

        this.deviceList = xmlReadWrite.readXmlFile(configFile.readConfigFile());
        if (!deviceList.isEmpty())
            for (Device device : deviceList) {
                deviceTableView.getItems().add(device);
            }

        this.periodChoiceBox.setItems(this.choices);

    }
}
