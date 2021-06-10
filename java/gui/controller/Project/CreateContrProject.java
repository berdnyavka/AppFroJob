package gui.controller.Project;


import gui.model.Project;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateContrProject {

    private Stage dialogStage;
    private Project project;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setProject(Project project) {
        this.project = project;
    }
    @FXML
    private TextField name;
    @FXML
    private void ok(){
        if(project.getCompanyId() != null){
            project.setName(name.getText());
            dialogStage.close();
        }
    }
    @FXML
    private void cancel(){
        dialogStage.close();
    }

}
