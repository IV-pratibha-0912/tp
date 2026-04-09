package fairshare.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import fairshare.model.balance.Balance;
import fairshare.model.group.Group;
import fairshare.ui.exceptions.UiException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * A UI panel that displays the net balance summary by Group,
 * showing who owes whom and how much.
 */
public class BalancePanel {

    private static final String FXML = "/view/BalancePanel.fxml";

    private Region root;

    @FXML
    private Accordion balancesAccordion;

    @FXML
    private Label emptyStateLabel;

    /**
     * Constructs a {@code BalancePanel} with the given map of group balances.
     *
     * @param groupBalances the initial map of group balances to display.
     */
    public BalancePanel(Map<Group, List<Balance>> groupBalances) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    BalancePanel.class.getResource(FXML));
            fxmlLoader.setController(this);
            root = fxmlLoader.load();
        } catch (IOException e) {
            throw new UiException("Failed to load " + FXML, e);
        }

        setBalances(groupBalances);

    }

    /**
     * Returns the root region of this component.
     *
     * @return the root {@code Region}.
     */
    public Region getRoot() {
        return root;
    }

    /**
     * Refreshes the panel with an updated map of group balances.
     *
     * @param groupBalances the updated map of group balances.
     */
    public void refresh(Map<Group, List<Balance>> groupBalances) {
        setBalances(groupBalances);
    }

    private void setBalances(Map<Group, List<Balance>> groupBalances) {
        balancesAccordion.getPanes().clear(); // Clear old UI

        if (groupBalances.isEmpty()) {
            updateEmptyState(true);
            return;
        }

        updateEmptyState(false);

        // Build the Accordion Panes dynamically
        for (Map.Entry<Group, List<Balance>> entry : groupBalances.entrySet()) {
            Group group = entry.getKey();
            List<Balance> debts = entry.getValue();

            VBox cardContainer = new VBox();
            cardContainer.setSpacing(5);
            cardContainer.setStyle("-fx-padding: 10;");

            if (debts.isEmpty()) {
                // The "All Settled" hybrid approach!
                Label settledLabel = new Label("All settled up!");
                settledLabel.setStyle("-fx-text-fill: #2e7d32; -fx-font-weight: bold; -fx-padding: 5;");
                cardContainer.getChildren().add(settledLabel);
            } else {
                // Reuse existing BalanceCard logic by grouping by debtor inside this group
                Map<String, List<Balance>> debtsByPerson = new LinkedHashMap<>();
                for (Balance balance : debts) {
                    String debtorName = balance.getDebtor().getName();
                    debtsByPerson.computeIfAbsent(debtorName, k -> new ArrayList<>()).add(balance);
                }

                for (Map.Entry<String, List<Balance>> personEntry : debtsByPerson.entrySet()) {
                    BalanceCard card = new BalanceCard(personEntry.getKey(), personEntry.getValue());
                    cardContainer.getChildren().add(card.getRoot());
                }
            }

            // Create the Collapsible Header
            TitledPane groupPane = new TitledPane();
            groupPane.setText("Group: " + group.getGroupName().toUpperCase());
            groupPane.setContent(cardContainer);

            balancesAccordion.getPanes().add(groupPane);
        }

        // Auto-expand the first group for better UX
        if (!balancesAccordion.getPanes().isEmpty()) {
            balancesAccordion.setExpandedPane(balancesAccordion.getPanes().get(0));
        }
    }

    /**
     * Shows or hides the empty state label based on whether
     * the balance list is empty.
     *
     * @param isEmpty whether the balance list is empty.
     */
    private void updateEmptyState(boolean isEmpty) {
        emptyStateLabel.setVisible(isEmpty);
        emptyStateLabel.setManaged(isEmpty);
    }

}
