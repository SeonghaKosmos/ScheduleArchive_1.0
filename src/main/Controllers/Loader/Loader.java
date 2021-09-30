package main.Controllers.Loader;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import main.Controllers.PrototypeController;
import main.Controllers.Stats.Table;
import main.Controllers.Timeline.TimeLine;
import main.Models.DBModels.ArchiveDBModel;
import main.Models.DBModels.ReadFromDBModel;
import main.Models.DateTimeModel;
import main.Models.Graphics.SceneNavigationModel;
import main.Utility.Activity;
import main.App;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;

public class Loader extends PrototypeController {




    @FXML
    ListView backups;
    ObservableList<String> backupData = FXCollections.observableArrayList();


    @FXML
    Button loadButton;
    @FXML
    Button resumeButton;



    public static boolean resumeMode = false;

    public static boolean loadMode = false;

    public static boolean disAbleCreator = false;

    public void initialize(){
        new Thread(setUpBackups).start();
    }


    public void loadData() {
        DateTimeModel.selectedDay = (String)backups.getSelectionModel().getSelectedItem();
        // reading contents of selectedDay on the database
        ArrayList<Activity> loadedDay = ReadFromDBModel.readDay(DateTimeModel.selectedDay);
        // overwriting archive to the contents of the selectedDay
        ArchiveDBModel.archive = loadedDay;
    }

    public void setupBackups() {
        backups.setItems(backupData);

        for (String date : ReadFromDBModel.getAllDates()) {
            backupData.add(date);
        }

        if (!backupData.isEmpty()){
            System.out.println("scrolling to last date");
            System.out.println(backupData.size());
            backups.scrollTo(backupData.size() - 1);
        }

    }

    Runnable setUpBackups = () -> {

        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Platform.runLater(() -> setupBackups());
    };



    @FXML
    public void resumeCreation() throws IOException {
        loadData();

        addNoData(DateTimeModel.getCurrentTimeInSeconds());

        resumeMode = true;
        loadMode = false;

        // exiting
        DateTimeModel.currentDay = ArchiveDBModel.archive.get(0).getDate();
        Table.updateData(ArchiveDBModel.archive);
        wrapUpAndGoToTimeLine();
    }


    public static void addNoData(int gapEndTime) {
        Activity lastEventEntered =  ArchiveDBModel.archive.get(ArchiveDBModel.archive.size() -1);
        int gapStartTime;

        if (lastEventEntered.getName().equals("no data") && lastEventEntered.getCategory().equals("NoData")) {
            ArchiveDBModel.archive.remove(lastEventEntered);
            gapStartTime = lastEventEntered.getStartTimeSecs();
        } else {
            gapStartTime = lastEventEntered.getEndTimeSecs();
        }

        if (gapEndTime < gapStartTime){
            gapEndTime += TimeLine.SECONDS_IN_A_DAY;;
        }

        // adding NoData to archive

        ArchiveDBModel.archive.add(new Activity("no data", "NoData", gapEndTime - gapStartTime, gapStartTime, gapEndTime, LocalDate.now().toString()));
    }


    public void loadDaysOfOld() throws IOException {
        loadData();
        Table.updateData(ArchiveDBModel.archive);
        disableAndGotoStats();
    }

    @FXML
    public void goToStats(){
        App.sceneNavigationModel.gotoScene(SceneNavigationModel.stats, backups.getScene());
    }


    private void disableAndGotoStats() throws IOException {

        //disable creator button
        disAbleCreator = true;
        loadMode = true;
        App.editLog.wipe();

        SceneNavigationModel.stats = App.sceneNavigationModel.createNewScene("../resources/FXML/Stats/stats.fxml");
        App.sceneNavigationModel.gotoScene(SceneNavigationModel.stats, backups.getScene());

    }


    private void wrapUpAndGoToTimeLine() {
        SceneNavigationModel.scheduleCreator = App.sceneNavigationModel.createNewScene("../resources/FXML/Creator/scheduleCreator.fxml");

        App.sceneNavigationModel.loadNewScene("../resources/FXML/Timeline/timeLine.fxml", backups.getScene());

                //reenable creator
        disAbleCreator = false;

        App.editLog.wipe();
        DateTimeModel.selectedDay = (String)backups.getSelectionModel().getSelectedItem();


    }



    private boolean canResume(){
        // getting the selectedDay
        String currentSelectedDate = (String)backups.getSelectionModel().getSelectedItem();
        String currentDay = DateTimeModel.currentDay;

        // checking if the selected date is equal to the currentDate or day before the currentDate
        if (DateTimeModel.addDayToDate(currentSelectedDate, 1).equals(currentDay) || currentSelectedDate.equals(DateTimeModel.currentDay)){
            // getting the dayStartTime of the selectedDay
            ArrayList<Activity> day = ReadFromDBModel.readDay(currentSelectedDate);
            int dayStartTime = day.get(0).getStartTimeSecs();
            int currentTimeInSec = DateTimeModel.getCurrentTimeInSeconds();

            // adding DateTimeModel.SECONDS_IN_A_DAY to currentTimeInSec if the date has changed
            if (currentDay.equals(DateTimeModel.addDayToDate(currentSelectedDate, 1))) {
                currentTimeInSec += DateTimeModel.SECONDS_IN_A_DAY;
            }

            System.out.println("(from canResume) currentTime in seconds: " + currentTimeInSec);
            System.out.println("(from canResume) dayStartTime of the selectedDay: " + dayStartTime);

            // returning True if the time pass since the dayStartTime of the selected day is less than DateTimeModel.SECONDS_IN_A_DAY
            if (currentTimeInSec - dayStartTime < DateTimeModel.SECONDS_IN_A_DAY) {
                return true;
            }
        }
        return false;
    }


    public void assessSelectedDay() throws ParseException {

        String selectedDayTemp = (String)backups.getSelectionModel().getSelectedItem();

        if (!(selectedDayTemp == null)){


            loadButton.setDisable(false);

            if (selectedDayTemp.equals(DateTimeModel.getLastDay()) && canResume() && (selectedDayTemp.equals(DateTimeModel.currentDay) || selectedDayTemp.equals(DateTimeModel.subDayFromDate(DateTimeModel.currentDay, 1)))){
                resumeButton.setDisable(false);
            }
            else {
                resumeButton.setDisable(true);
            }

        }
        else {
            loadButton.setDisable(true);
        }


    }


}
