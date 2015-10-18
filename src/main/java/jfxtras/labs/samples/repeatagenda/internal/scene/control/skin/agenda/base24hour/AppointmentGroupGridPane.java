package jfxtras.labs.samples.repeatagenda.internal.scene.control.skin.agenda.base24hour;

import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Agenda.Appointment;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Agenda.AppointmentGroup;

//makes a group of colored squares used to select appointment group
public class AppointmentGroupGridPane extends GridPane {

// private LayoutHelp layoutHelp;
 private Pane[] lPane;
 private IntegerProperty appointmentGroupSelected = new SimpleIntegerProperty(-1);
 
 public AppointmentGroupGridPane() {
 }

 public AppointmentGroupGridPane(Appointment appointment, List<AppointmentGroup> appointmentGroups) {
     setupData(appointment, appointmentGroups);
 }
 
// public void setupData(Appointment appointment, LayoutHelp layoutHelp) {
 public void setupData(Appointment appointment, List<AppointmentGroup> appointmentGroups)
 {
     // construct a area of appointment groups
//     this.layoutHelp = layoutHelp;
//     getStylesheets().add(layoutHelp.skinnable.getUserAgentStylesheet());
//     String css = this.getClass().getResource("../Agenda.css").toExternalForm(); 
//     this.getStylesheets().add(css);
     this.getStyleClass().add("AppointmentGroups");
     this.setHgap(2);
     this.setVgap(2);
//     lPane = new Pane[layoutHelp.skinnable.appointmentGroups().size()];
     lPane = new Pane[appointmentGroups.size()];
     
     int lCnt = 0;
//     for (AppointmentGroup lAppointmentGroup : layoutHelp.skinnable.appointmentGroups())
     for (AppointmentGroup lAppointmentGroup : appointmentGroups)
     {
         lPane[lCnt] = lAppointmentGroup.getIcon();
         // create the appointment group
//         lPane[lCnt] = new Pane();
//         lPane[lCnt].setPrefSize(20, 20);
//         lPane[lCnt].getStyleClass().addAll("AppointmentGroup", lAppointmentGroup.getStyleClass());
         this.add(lPane[lCnt], lCnt % 12, lCnt / 12 );

         // tooltip
         updateToolTip(lCnt, appointmentGroups);
         LayoutHelp layoutHelp;
         // mouse 
         setupMouseOverAsBusy(lPane[lCnt]);
         lPane[lCnt].setOnMouseClicked( (mouseEvent) ->
         {
             mouseEvent.consume(); // consume before anything else, in case there is a problem in the handling
             appointmentGroupSelected.set(appointmentGroups.indexOf(lAppointmentGroup));

             // assign appointment group
             AppointmentGroup g = appointmentGroups.get(appointmentGroupSelected.getValue());
             appointment.setAppointmentGroup(g);
//             layoutHelp.skin.setupAppointments();    // updates all appointments - TODO better to update individual appointment - don't update because other changes may make display wrong
         });
         lCnt++;
     }
   //int index = layoutHelp.skinnable.appointmentGroups().indexOf(appointment.getAppointmentGroup());
//     int index = appointment.getAppointmentGroupIndex();
     int index = appointment.getAppointmentGroup().getKey();
     setAppointmentGroupSelected(index);
     setLPane(index);
     
     // change listener - runs when new color is selected
     appointmentGroupSelected.addListener((observable, oldSelection, newSelection) ->  {
       int oldS = (int) oldSelection;
       int newS = (int) newSelection;
       setLPane(newS);
       unsetLPane(oldS);
     });
 }

 // blue border in selection
 private void unsetLPane(int i) {
     lPane[i].setStyle("-fx-border-color: WHITE");
 }
 private void setLPane(int i) {
      final String cssDefault = "-fx-border-color: blue;\n"
             + "-fx-border-insets: -1;\n"
             + "-fx-border-width: 2;\n";
     lPane[i].setStyle(cssDefault);
 }
 
 public void updateToolTip(int i, List<AppointmentGroup> appointmentGroups) {
     AppointmentGroup a = appointmentGroups.get(i);
     if (a.getDescription() != "" && a.getDescription() != null) {
         Tooltip.install(lPane[i], new Tooltip(a.getDescription()));
//         System.out.println("new tooltip " + a.getDescription());
//         setLPane(i);
     } 
 }

 public IntegerProperty appointmentGroupSelectedProperty() {
     return appointmentGroupSelected;
 }

 public void setAppointmentGroupSelected(Integer i) {
     appointmentGroupSelected.set(i);
 }
 
 public Integer getAppointmentGroupSelected() {
     return appointmentGroupSelected.getValue();
 }   

 void setupMouseOverAsBusy(final Node node) {
     // play with the mouse pointer to show something can be done here
     node.setOnMouseEntered( (mouseEvent) -> {
         if (!mouseEvent.isPrimaryButtonDown()) {                        
             node.setCursor(Cursor.HAND);
             mouseEvent.consume();
         }
     });
     node.setOnMouseExited( (mouseEvent) -> {
         if (!mouseEvent.isPrimaryButtonDown()) {
             node.setCursor(Cursor.DEFAULT);
             mouseEvent.consume();
         }
     });
 }

}
