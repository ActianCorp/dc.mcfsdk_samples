/*
 * MsgBoxInvoker.java
 *
 * Copyright (c) 2004-2022 by Actian Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.actian.dc.mcfsdk.samples;

import com.pervasive.cosmos.CosmosException;
import com.pervasive.cosmos.component.CustomOptionHandler;
import com.pervasive.cosmos.component.util.ErrorCode;
import com.pervasive.cosmos.component.util.InvokerComponentBase;
import com.pervasive.cosmos.messaging.Message;
import com.pervasive.cosmos.messaging.TextMessage;
import com.pervasive.cosmos.util.LogLevel;
import java.awt.Dimension;
import java.awt.HeadlessException;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JDialog;

/**
 * Sample component that pops up a dialog with content from the source message.
 * Note that this component can only be used in the design studio.
 *
 * @author twaldrep
 */
public class MsgBoxInvoker extends InvokerComponentBase
        implements CustomOptionHandler {

    public static final String CUSTOM_OPTION = "custom";
    
    /**
     * Creates a new instance of MsgBoxInvoker
     */
    public MsgBoxInvoker() {
    }

    /**
     * Displays the body of the source message in a pop-up dialog.
     *
     * @param source Source Message
     * @param target Target Message
     * @return ErrorCode int value. Successful return is 0 (ERR_OK).
     */
    @Override
    public int execute(Message source, Message target) {
        int rc = ErrorCode.ERR_OK.getValue();
        try {
            if (source instanceof TextMessage) {
                StringBuilder displayText = new StringBuilder();
                
                String customOpt = getEnvironment().getOption(CUSTOM_OPTION);
                if (customOpt != null && customOpt.length() > 0) {
                    displayText.append("Custom option value: ");
                    displayText.append(customOpt);
                    displayText.append("\n\n");
                }
                
                //
                // This will need to be modified once DataConnect supports
                // BytesMessage type.
                String srcTxt = ((TextMessage) source).getText();
                if (srcTxt == null) {
                    srcTxt = "(Empty Message)";
                }
                displayText.append("Source message body: ");
                displayText.append(srcTxt);

                JTextArea textArea = new JTextArea(displayText.toString());
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

                JOptionPane optionPane = new JOptionPane(scrollPane, JOptionPane.PLAIN_MESSAGE);
                optionPane.setPreferredSize(new Dimension(500, 350));
                optionPane.setOptionType(JOptionPane.OK_CANCEL_OPTION);
                JDialog dialog = optionPane.createDialog("Message Content");
                dialog.setSize(600, 400);
                dialog.setAutoRequestFocus(true);
                dialog.setModal(true);
                dialog.setVisible(true);
            }
        } catch (CosmosException | HeadlessException exc) {
            this.getEnvironment().setError(
                    LogLevel.LT_WARN,
                    ErrorCode.ERR_INVALID,
                    true,
                    "Exception " + exc.toString() + " occurred while attempting "
                    + "to display message content.");
            rc = ErrorCode.ERR_INVALID.getValue();
        }

        return rc;
    }

    /**
     * A blocking call will be made to this method to obtain the value.
     */
    @Override
    public String getOptionValue(String optName, String action, String oldVal) {
        String response = "";
        if (optName != null && optName.equals(CUSTOM_OPTION)) {
            // Present a dialog to gather the user's response.
            response = JOptionPane.showInputDialog("Provide Custom Option Value");
        }
        return response;
    }
}
