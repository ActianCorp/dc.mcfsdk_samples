/*
 * TestAll.java
 *
 * Copyright (c) 2003-2022 by Actian Corp.
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


//
// import the standard MCF SDK APIs.
//
import com.pervasive.cosmos.component.*;
import com.pervasive.cosmos.component.util.*;
import com.pervasive.cosmos.messaging.*;
import com.pervasive.cosmos.util.*;
import com.pervasive.cosmos.CosmosException;

//
// Java imports used by this component
//
import java.math.BigDecimal;
import java.net.URL;

/**
 * This class extends the convenience base class, 
 * com.pervasive.cosmos.component.util.QueueComponentBase,
 * which in turn implements all the essential interfaces for a
 * queue component.  Component developers are free to 
 * implement the component interfaces themselves if they need
 * to extend a different base class.
 */
public class TestAll
extends QueueComponentBase
{
    /**
     * components are required to provide a null constructor
     */
    public TestAll()
    {
    }

    /**
     * Populate the Message parameter with the first 1000 characters of the file
     * referenced in the sourceURI step option. The queue parameter is not
     * used by this component.
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
        // get the sourceURI property that was set in the Process Editor
        String sourceURI = env.getOption("sourceURI");

        try
        {
            // incoming message should point to some valid source url
            URLSupport urlSup = env.getURLSupport();
            if (sourceURI != null && sourceURI.length() > 0)
            {
                try
                {
                    URLHandle uh = urlSup.openURL(sourceURI, "r",
                            Encoding.OEM);
                    if (uh == null)
                    {
                        message.setText("Error opening source URI '" +
                                sourceURI +
                                "': " + env.getLastErrorText());
                    }
                    else
                    {
                        // read until a non-blank character is found
                        char ch = ' ';
                        while( (ch=uh.getc()) == ' ' && ch != 0xffff );
                        long pos = uh.tell();
                        message.appendText(
                                "First Nonspace character in source is \"" + ch +
                                "\" at position " + pos + ".\n");
                        
                        if ( uh.seek(pos + 40, 1) > pos )
                        {
                            message.appendText(
                                    "The character at position " + uh.tell() +
                                    " is \"" + uh.getc() + "\".\n");
                        }
                        
                        // go back to the beginning
                        uh.seek(0,0);
                        
                        //
                        // Grab first 1000 bytes of the body
                        char[] buf = new char[1000];
                        int ret = uh.read(buf, 1000);
                        if (ret == -1)
                        {
                            message.setText("Error reading body file '" +
                                    sourceURI +
                                    "': " + env.getLastErrorText());
                        }
                        else
                        {
                            message.appendText(
                                    "The first 1000 characters of the file follows:\n" +
                                    new String(buf, 0, ret));
                        }
                        uh.close();
                    }
                }
                catch( URLHandleException urlEx )
                {
                    code = urlEx.getErrorCode().getValue();
                    message.setText("URLHandleException occurred while " +
                            "reading from source file.  Message was \"" +
                            urlEx.getMessage() + ".\"  " +
                            "Error code was " + code);
                }
            }
            //
            // Attempt to set properties of each type
            message.setProperty("sourceURI", sourceURI);
            message.setProperty("decimalProperty",
                    new BigDecimal(Long.MAX_VALUE));
            message.setProperty("intProperty", Integer.MIN_VALUE);
            message.setProperty("doubleProperty", Double.MAX_VALUE);
            message.setProperty("booleanProperty",true);
            message.setProperty("longProperty",Long.MAX_VALUE);
            message.setProperty("byteProperty",Byte.MAX_VALUE);
            
            //
            // set property to an environment variable
            message.setProperty("systemPath",env.getNativeEnv("PATH"));
            
            // set classpath property
            try
            {
                URL[] urls = ((ComponentClassLoader)
                        TestAll.class.getClassLoader()).getURLs();
                StringBuilder buffer = new StringBuilder();
                for( int j = 0; j < urls.length; j++ ) {
                    buffer.append( j > 0 ? ";" : "" );
                    buffer.append(urls[j].toString());
                }
                
                message.setProperty("componentLocalClassPath",
                        buffer.toString());
            }
            catch( CosmosException exc )
            {
                message.setProperty("FailedComponentLocalClassPathException",
                        exc.toString());
            }

            
        }
        catch( CosmosException cEx )
        {
            code = ErrorCode.ERR_INVALID.getValue();
            env.setError(LogLevel.LT_WARN, ErrorCode.ERR_INVALID,
                    true, cEx.getMessage());
        }
        return code;
    }
    
    /**
     * getMessage and putMessage are abstract in MessageComponentBase,
     * so they must be implemented.
     * @param msg The source DJMessage
     * @param queue queue name (not used in this component)
     * @return error code.  0 if completed successfully.
     */
    @Override
    public int putMessage(Message msg, String queue)
    {
        // Treat message as a TextMessage. Components will have to ensure
        // that this actually is a TextMessage once support is added for
        // BytesMessages.
        TextMessage message = (TextMessage) msg;
        Environment env = this.getEnvironment();
        int code = ErrorCode.ERR_OK.getValue();
        try
        {
            String[] pnames = message.getPropertyNames();
            this.info(" Message has " + (pnames == null ? 0 : pnames.length) +
                    " properties.");
            String pname;
            for (int i = 0; pnames != null && i < pnames.length; ++i)
            {
                int ptype = msg.getPropertyType(pnames[i]);
                pname = pnames[i];
                switch (ptype)
                {
                    case Message.PROPERTY_TYPE_STRING:
                        this.info(" Property " + i + ":");
                        this.info("  Name: " + pname);
                        this.info("  Contents: " + message.getStringProperty(pname));
                        break;
                    case Message.PROPERTY_TYPE_INT:
                        this.info(" Property " + i + ":");
                        this.info("  Name: " + pname);
                        this.info("  Contents: " + msg.getIntProperty(pname));
                        break;
                    case Message.PROPERTY_TYPE_DOUBLE:
                        this.info(" Property " + i + ":");
                        this.info("  Name: " + pname);
                        this.info("  Contents: " + msg.getDoubleProperty(pname));
                        break;
                    case Message.PROPERTY_TYPE_DECIMAL:
                        this.info(" Property " + i + ":");
                        this.info("  Name: " + pname);
                        this.info("  Contents: " + msg.getDecimalProperty(pname));
                        break;
                    case Message.PROPERTY_TYPE_LONG:
                        this.info(" Property " + i + ":");
                        this.info("  Name: " + pname);
                        this.info("  Contents: " + msg.getLongProperty(pname));
                        break;
                    case Message.PROPERTY_TYPE_BYTE:
                        this.info(" Property " + i + ":");
                        this.info("  Name: " + pname);
                        this.info("  Contents: " + msg.getByteProperty(pname));
                        break;
                    case Message.PROPERTY_TYPE_BOOLEAN:
                        this.info(" Property " + i + ":");
                        this.info("  Name: " + pname);
                        this.info("  Contents: " + msg.getBooleanProperty(pname));
                        break;
                    case Message.PROPERTY_TYPE_UNKNOWN:
                    default:
                        this.info("Property " + i + " (" + pname + 
                                ") has unknown type");
                        break;
                }
            }
            String body = message.getText();
            int ilen = body.length();
            if (ilen > 400)
                this.info(" Body is " + ilen + " bytes: " + body.substring(0, 400) +
                        "...");
            else
                this.info(" Body is " + ilen + " bytes: " + body);
        }
        catch( CosmosException cEx )
        {
            code = ErrorCode.ERR_INVALID.getValue();
            env.setError(LogLevel.LT_WARN, ErrorCode.ERR_INVALID,
                    true, cEx.getMessage() );
        }
        catch( Exception ioEx )
        {
            code = ErrorCode.ERR_INVALID.getValue();
            env.setError( LogLevel.LT_WARN, ErrorCode.ERR_WRITERR,
                    true, ioEx.getMessage() );
        }
        return code;
    }

    // log an informative message to DataConnect
    private void info(String infoMsg)
    {
        if ( infoMsg != null)
        {
            this.getEnvironment().logMessage(
                    LogLevel.LT_INFO,
                    ErrorCode.ERR_OK,
                    infoMsg );
        }
    }
}
