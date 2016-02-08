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

package io.bitsquare.gui.main.portfolio.pendingtrades.steps.seller;

import io.bitsquare.gui.main.portfolio.pendingtrades.PendingTradesViewModel;
import io.bitsquare.gui.main.portfolio.pendingtrades.steps.TradeStepView;
import io.bitsquare.locale.BSResources;

public class SellerStep2View extends TradeStepView {

    ///////////////////////////////////////////////////////////////////////////////////////////
    // Constructor, Initialisation
    ///////////////////////////////////////////////////////////////////////////////////////////

    public SellerStep2View(PendingTradesViewModel model) {
        super(model);
    }


    ///////////////////////////////////////////////////////////////////////////////////////////
    // Info
    ///////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected String getInfoBlockTitle() {
        return "Wait for blockchain confirmation";
    }

    @Override
    protected String getInfoText() {
        return BSResources.get(
                "The deposit transaction has at least one blockchain confirmation.\n" +
                        "You need to wait until that the bitcoin buyer starts the {0} payment.",
                model.getCurrencyCode());
    }


    ///////////////////////////////////////////////////////////////////////////////////////////
    // Warning
    ///////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected String getWarningText() {
        setInformationHeadline();
        return "The buyer still has not done the " + model.getCurrencyCode() + " payment.\n" +
                "You need to wait until he starts the payment.\n" +
                "If the trade has not been completed on " +
                model.getOpenDisputeTimeAsFormattedDate() +
                " the arbitrator will investigate.";
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    // Dispute
    ///////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected String getOpenForDisputeText() {
        return "The buyer has not started his payment!\n" +
                "The max. allowed period for the trade has elapsed.\n" +
                "Please contact the arbitrator for opening a dispute.";
    }

    @Override
    protected void applyOnDisputeOpened() {
    }

}


