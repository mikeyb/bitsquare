/*
 * This file is part of Bitsquare.
 *
 * Bitsquare is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * Bitsquare is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Bitsquare. If not, see <http://www.gnu.org/licenses/>.
 */

package io.bitsquare.gui.main.portfolio.pendingtrades;

import io.bitsquare.gui.components.TitledGroupBg;
import io.bitsquare.gui.main.portfolio.pendingtrades.steps.TradeStepView;
import io.bitsquare.gui.main.portfolio.pendingtrades.steps.TradeWizardItem;
import io.bitsquare.gui.util.Layout;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.bitsquare.gui.util.FormBuilder.*;

public abstract class TradeSubView extends HBox {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    protected final PendingTradesViewModel model;
    protected VBox leftVBox;
    protected AnchorPane contentPane;
    protected TradeStepView tradeStepView;
    private Button openDisputeButton;
    private NotificationGroup notificationGroup;
    protected GridPane leftGridPane;
    protected TitledGroupBg tradeProcessTitledGroupBg;
    protected int leftGridPaneRowIndex = 0;


    ///////////////////////////////////////////////////////////////////////////////////////////
    // Constructor, Initialisation
    ///////////////////////////////////////////////////////////////////////////////////////////

    public TradeSubView(PendingTradesViewModel model) {
        this.model = model;

        setSpacing(Layout.PADDING_WINDOW);
        buildViews();
    }

    protected void activate() {
    }

    protected void deactivate() {
        if (tradeStepView != null)
            tradeStepView.doDeactivate();

        if (openDisputeButton != null)
            leftGridPane.getChildren().remove(openDisputeButton);
        if (notificationGroup != null)
            notificationGroup.removeItselfFrom(leftGridPane);
    }

    private void buildViews() {
        addLeftBox();
        addContentPane();

        leftGridPane = new GridPane();
        leftGridPane.setPrefWidth(340);
        VBox.setMargin(leftGridPane, new Insets(0, 10, 10, 10));
        leftGridPane.setHgap(Layout.GRID_GAP);
        leftGridPane.setVgap(Layout.GRID_GAP);
        leftVBox.getChildren().add(leftGridPane);

        leftGridPaneRowIndex = 0;
        tradeProcessTitledGroupBg = addTitledGroupBg(leftGridPane, leftGridPaneRowIndex, 1, "Trade process");

        addWizards();

        TitledGroupBg noticeTitledGroupBg = addTitledGroupBg(leftGridPane, leftGridPaneRowIndex, 1, "", Layout.GROUP_DISTANCE);
        Label label = addMultilineLabel(leftGridPane, leftGridPaneRowIndex, "", Layout.FIRST_ROW_AND_GROUP_DISTANCE);
        openDisputeButton = addButtonAfterGroup(leftGridPane, ++leftGridPaneRowIndex, "Open Dispute");
        GridPane.setColumnIndex(openDisputeButton, 0);
        openDisputeButton.setId("open-dispute-button");

        notificationGroup = new NotificationGroup(noticeTitledGroupBg, label, openDisputeButton);
        notificationGroup.setLabelAndHeadlineVisible(false);
        notificationGroup.setButtonVisible(false);
    }

    public static class NotificationGroup {
        public final TitledGroupBg titledGroupBg;
        public final Label label;
        public final Button button;

        public NotificationGroup(TitledGroupBg titledGroupBg, Label label, Button button) {
            this.titledGroupBg = titledGroupBg;
            this.label = label;
            this.button = button;
        }

        public void setLabelAndHeadlineVisible(boolean isVisible) {
            titledGroupBg.setVisible(isVisible);
            label.setVisible(isVisible);
            titledGroupBg.setManaged(isVisible);
            label.setManaged(isVisible);
        }

        public void setButtonVisible(boolean isVisible) {
            button.setVisible(isVisible);
            button.setManaged(isVisible);
        }

        public void removeItselfFrom(GridPane leftGridPane) {
            leftGridPane.getChildren().remove(titledGroupBg);
            leftGridPane.getChildren().remove(label);
            leftGridPane.getChildren().remove(button);
        }
    }

    protected void showItem(TradeWizardItem item) {
        item.setActive();
        createAndAddTradeStepView(item.getViewClass());
    }

    abstract protected void addWizards();

    protected void addWizardsToGridPane(TradeWizardItem tradeWizardItem) {
        if (leftGridPaneRowIndex == 0)
            GridPane.setMargin(tradeWizardItem, new Insets(Layout.FIRST_ROW_DISTANCE, 0, 0, 0));

        GridPane.setRowIndex(tradeWizardItem, leftGridPaneRowIndex++);
        leftGridPane.getChildren().add(tradeWizardItem);
        GridPane.setRowSpan(tradeProcessTitledGroupBg, leftGridPaneRowIndex);
        GridPane.setFillWidth(tradeWizardItem, true);
    }

    private void createAndAddTradeStepView(Class<? extends TradeStepView> viewClass) {
        try {
            tradeStepView = viewClass.getDeclaredConstructor(PendingTradesViewModel.class).newInstance(model);
            contentPane.getChildren().setAll(tradeStepView);

            tradeStepView.setNotificationGroup(notificationGroup);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addLeftBox() {
        leftVBox = new VBox();
        leftVBox.setSpacing(Layout.SPACING_VBOX);
        leftVBox.setMinWidth(290);
        getChildren().add(leftVBox);
    }

    private void addContentPane() {
        contentPane = new AnchorPane();
        HBox.setHgrow(contentPane, Priority.SOMETIMES);
        getChildren().add(contentPane);
    }
}



