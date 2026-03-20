package fairshare;

import fairshare.logic.Logic;
import fairshare.logic.LogicManager;
import fairshare.model.Model;
import fairshare.model.ModelManager;
import fairshare.ui.Ui;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main entry point to FairShare
 */
public class FairShare extends Application {
    private Ui ui;
    private Logic logic;
    private Model model;
    private Storage storage;

    public void init() throws Exception {
        super.init();
        this.model = new ModelManager();
        this.logic = new LogicManager(model, storage);
        this.ui = new UiManager(logic);
    }

    public void start(Stage stage) {
        ui.start(stage);
    }
}
