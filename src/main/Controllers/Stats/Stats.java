package main.Controllers.Stats;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import main.App;
import main.Controllers.Loader.Loader;
import main.Controllers.PrototypeController;
import main.Models.DBModels.ArchiveDBModel;
import main.Models.DBModels.WriteToDBModel;
import main.Models.DateTimeModel;
import main.Models.Graphics.SceneNavigationModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class Stats extends PrototypeController {

    @FXML
    Button gotoCreatorButton;


    public static final ArrayList<String> categorySequence = new ArrayList<>();

    public static final ArrayList<String> colorSequence = new ArrayList<>();

    public static final HashMap<String, String> colorMap = new HashMap<>();

    public static Scene timeLine;

    public static boolean currentTimelineLayoutInitialized = false;


    public static Comparator<Table.chartDataUnit> chartDataUnitComparator = new Comparator<Table.chartDataUnit>() {
        @Override
        public int compare(Table.chartDataUnit u1, Table.chartDataUnit u2) {
            return ((Integer)u2.getDurationSecs()).compareTo((Integer) u1.getDurationSecs());
        }
    };



    public void initialize(){

        //set prototype property 'gotoCreatorButton'
        setGoToCreatorButton(gotoCreatorButton);

        makeCategorySequence();
        makeColorSequence();
        makeColorMap();
    }

    private void makeCategorySequence(){
        categorySequence.add("Study");
        categorySequence.add("Entertainment");
        categorySequence.add("Spirituality");
        categorySequence.add("Exercise");
        categorySequence.add("Rest");
        categorySequence.add("Reading");
        categorySequence.add("Writing");
        categorySequence.add("Arts");
        categorySequence.add("Social");
        categorySequence.add("Media");
        categorySequence.add("Service");
        categorySequence.add("Miscellaneous");
    }

    private void makeColorSequence(){
        colorSequence.add("aqua");
        colorSequence.add("fuchsia");
        colorSequence.add("gold");
        colorSequence.add("chartreuse");
        colorSequence.add("bisque");
        colorSequence.add("chocolate");
        colorSequence.add("deepskyblue");
        colorSequence.add("crimson");
        colorSequence.add("olive");
        colorSequence.add("green");
        colorSequence.add("coral");
        colorSequence.add("blueviolet");
    }

    public void makeColorMap(){
        colorMap.put("Study", "aqua");
        colorMap.put("Entertainment","fuchsia");
        colorMap.put("Spirituality","gold");
        colorMap.put("Exercise","chartreuse");
        colorMap.put("Rest","bisque");
        colorMap.put("Reading","chocolate");
        colorMap.put("Writing","deepskyblue");
        colorMap.put("Arts","crimson");
        colorMap.put("Social","olive");
        colorMap.put("Media","green");
        colorMap.put("Service","coral");
        colorMap.put("Miscellaneous","blueviolet");
        colorMap.put("NoData","grey");
    }



    @FXML
    public void goToCreator(){
        App.sceneNavigationModel.gotoScene(SceneNavigationModel.scheduleCreator, SceneNavigationModel.stats);
    }


    @FXML
    public void goToBarChart(){
        BarDisplay.updateBarChart();
        App.sceneNavigationModel.gotoScene(SceneNavigationModel.bars, SceneNavigationModel.stats);
    }


    @FXML
    public void goToTable(){
        App.sceneNavigationModel.gotoScene(SceneNavigationModel.table, SceneNavigationModel.stats);
    }


    @FXML
    public void goToPiChart(){
        if (!ArchiveDBModel.archive.isEmpty()){
            PiChart.updatePi();
        }

        App.sceneNavigationModel.gotoScene(SceneNavigationModel.piChart, SceneNavigationModel.stats);
    }

    @FXML
    public void goToLoader() throws IOException {

        System.out.println(SceneNavigationModel.stats);


        App.sceneNavigationModel.loadNewScene("../resources/FXML/Loader/loader.fxml", SceneNavigationModel.stats);


        if (Loader.resumeMode || Loader.loadMode) {

            //set selectedDay temporaryly to changing day
            String temp = DateTimeModel.selectedDay;
            DateTimeModel.selectedDay = DateTimeModel.selectedDay.replaceAll("-", "_");;

            WriteToDBModel.saveDataSynchronously();

            //reset selectedDay
            DateTimeModel.selectedDay = temp;

        }
    }

    @FXML
    public void goToMacro(){
        App.sceneNavigationModel.gotoScene(SceneNavigationModel.macro, SceneNavigationModel.stats);
    }

    @FXML
    public void goToBackups(){
        App.sceneNavigationModel.gotoScene(SceneNavigationModel.backups, SceneNavigationModel.stats);
    }


    @FXML
    public void goToTimeLine(){
        App.sceneNavigationModel.loadNewScene("../resources/FXML/Timeline/timeLine.fxml", SceneNavigationModel.stats);
    }


    static double calculatePercentage(int total, int value){
        return (double) value / (double)total * 100;
    }


}
