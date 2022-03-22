/*
 * ContentTypeInvoker.java
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

import com.pervasive.cosmos.component.util.ErrorCode;
import com.pervasive.cosmos.component.util.InvokerComponentBase;
import com.pervasive.cosmos.messaging.Message;
import com.pervasive.cosmos.util.LogLevel;
import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Sample invoker component that consumes a path to a file 
 * and produces a message with Content-Type property
 * @author  twaldrep
 */
public class ContentTypeInvoker
extends InvokerComponentBase
{
    static final String CONTENT_TYPE_DEFAULT = "application/octet-stream";
	
    /**
     * Creates a new instance of ContentTypeInvoker
     */
    public ContentTypeInvoker()
    {
    }

    /**
     * Attempts to determine the content type of a file provided via
     * either step-level option or message property.  The content type
     * will be reported in the target message's Content-Type property.
     *
     * @param source Source Message
     * @param target Target Message
     * @return ErrorCode int value. Successful return is 0 (ERR_OK).
     */
    public int execute( Message source, Message target )
    {
        int rc = ErrorCode.ERR_OK.getValue();
        try
        {
			// this component ignores the message content
			// so no reason to cast to TextMessage or BytesMessage
            
            // Get the filePath option
            String pathStr = getEnvironment().getOption("filePath");
            
            // Check for override of filePath in the source message
            String fileOverride = source.getStringProperty("filePath");
            if (fileOverride != null && fileOverride.trim().length() > 0) {
                pathStr = fileOverride;
            }
            
            File filePath = new File(pathStr);
            if (filePath.exists()) {
                String contentType = getFileContentType(filePath);
                target.setProperty("File", filePath.getCanonicalPath());
                target.setProperty("Content-Type", contentType);
            } else {
                this.getEnvironment().setError(
                        LogLevel.LT_WARN, ErrorCode.ERR_OPENERR, true,
                        "Unable to find source file " + pathStr +
                        ". Unable to retrieve content type.");
            }
            
        }
        catch( Exception exc )
        {
            this.getEnvironment().setError(
                    LogLevel.LT_ERROR,
                    ErrorCode.ERR_INVALID,
                    true, 
                    "Exception " + exc.toString() + " occurred while attempting " +
                    "obtain file content type.");
            rc = ErrorCode.ERR_INVALID.getValue();
        }
        
        return rc;
    }
	
    String getFileContentType(java.io.File file) {
        String contentType;
        try {
            Path path = file.toPath();
            contentType = Files.probeContentType(path);
        } catch (IOException ex) {
            // log a warning
            String msg = String.format(
                    "Error occurred while obtaining source file content type. Message is '%s'",
                    ex.getMessage());
            this.getEnvironment().logMessage(LogLevel.LT_WARN, ErrorCode.ERR_READERR, msg);
            
            // Try to guess the content type just from the name
            contentType = URLConnection.guessContentTypeFromName(file.getName());
        }
        return contentType != null && contentType.trim().length() > 0 ?
                contentType : CONTENT_TYPE_DEFAULT;
    }
}
