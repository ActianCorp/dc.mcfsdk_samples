/*
 * CurrentTimeQueue.java
 *
 * Copyright (c) 2022 by Actian Corp.
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
import com.pervasive.cosmos.component.Environment;
import com.pervasive.cosmos.component.util.ErrorCode;
import com.pervasive.cosmos.component.util.QueueComponentBase;
import com.pervasive.cosmos.messaging.Message;
import com.pervasive.cosmos.messaging.TextMessage;
import com.pervasive.cosmos.util.LogLevel;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.TimeZone;


/**
 * Simple queue component which returns the current 
 * time in the requested timezone when the getMessage
 * action is called.
 */
public class CurrentTimeQueue extends QueueComponentBase {
    
    /**
     * Populate the Message parameter with the current formatted time in the
     * time zone specified by the hours and minutes offset options.
     * @param msg the Message object to be populated by the
     *              getMessage action
     * @param queue not used by this component
     * @return error code.  0 if completed successfully.
     */
    @Override
    public int getMessage(Message msg, String queue)
    {
        int code = ErrorCode.ERR_OK.getValue();
        Environment env = this.getEnvironment();

        //
        // Treat message as a TextMessage. Components will have to ensure
        // that this actually is a TextMessage once support is added for
        // BytesMessages.
        TextMessage message = (TextMessage) msg;

        //
        // get the hours offset property that was set in the Process Editor
        String hoursOffsetStr = env.getOption("hoursOffset");
        int hoursOffset = hoursOffsetStr != null ? Integer.parseInt(hoursOffsetStr) : 0;
        
        // get the minutes offset from the hours offset above
        String minutesOffsetStr = env.getOption("minutesOffset");
        int minutesOffset = minutesOffsetStr != null ? Integer.parseInt(minutesOffsetStr) : 0;
        
        try
        {
            Date now = new Date(System.currentTimeMillis());
            message.setText(getFormattedTime(now, hoursOffset, minutesOffset));
        }
        catch( Exception cEx )
        {
            code = ErrorCode.ERR_INVALID.getValue();
            env.setError(LogLevel.LT_WARN, ErrorCode.ERR_INVALID,
                    true, cEx.getMessage());
        }
        return code;
    }
    
	/**
	 * Static method to return the GMT time as a string
	 */
	private String getFormattedTime(Date time, int hourOffset, int minuteOffset) {
        if (hourOffset > 12 || hourOffset < -12) {
            throw new IllegalArgumentException("Hours offset must be between -12 and 12");
        }
        if (minuteOffset < 0 || minuteOffset > 59) {
            throw new IllegalArgumentException("Minutes offset must be between 0 and 59");
        }
        String tzString = String.format("GMT%+02d%02d", hourOffset, minuteOffset);
		TimeZone tz = TimeZone.getTimeZone(tzString);
		SimpleDateFormat dfGMT = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		dfGMT.setTimeZone(tz);		
		return dfGMT.format(time);
	} 
}
