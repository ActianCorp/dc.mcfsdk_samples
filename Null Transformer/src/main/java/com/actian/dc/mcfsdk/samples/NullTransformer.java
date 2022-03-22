/*
 * NullTransformer.java
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

import com.pervasive.cosmos.component.util.*;
import com.pervasive.cosmos.messaging.*;
import com.pervasive.cosmos.util.*;
import com.pervasive.cosmos.CosmosException;

/**
 * Sample transformer which performs a "null" transformation,
 * i.e. copies the properties and body of the source message
 * to the target message
 */
public class NullTransformer extends TransformerComponentBase
{
    /**
     * components are required to provide a null constructor
     */
    public NullTransformer()
    {
    }

    /**
     * Copies the contents (properties and body) of the
     * source message to the target message
     * @param source Source message
     * @param target Target message
     * @return Completion status code.  Success == 0.
     */
    @Override
    public int execute(Message source, Message target)
    {
        int code = ErrorCode.ERR_OK.getValue();
        try
        {
            // copy properties
            String[] propNames = source.getPropertyNames();
            for (String propName : propNames) {
                switch (source.getPropertyType(propName)) {
                    case Message.PROPERTY_TYPE_STRING:
                        target.setProperty(propName, source.getStringProperty(propName));
                        break;
                    case Message.PROPERTY_TYPE_INT:
                        target.setProperty(propName, source.getIntProperty(propName));
                        break;
                    case Message.PROPERTY_TYPE_LONG:
                        target.setProperty(propName, source.getLongProperty(propName));
                        break;
                    case Message.PROPERTY_TYPE_BOOLEAN:
                        target.setProperty(propName, source.getBooleanProperty(propName));
                        break;
                    case Message.PROPERTY_TYPE_DOUBLE:
                        target.setProperty(propName, source.getDoubleProperty(propName));
                        break;
                    case Message.PROPERTY_TYPE_DECIMAL:
                        target.setProperty(propName, source.getDecimalProperty(propName));
                        break;
                    case Message.PROPERTY_TYPE_BYTE:
                        target.setProperty(propName, source.getByteProperty(propName));
                        break;
                    default:
                        getEnvironment().logMessage(LogLevel.LT_WARN, ErrorCode.ERR_BADTYPE,
                                "Source message property "
                                + propName
                                + " has unsupported type "
                                + typeString(source.getPropertyType(propName)));
                        
                }
            }
            
            //
            // TODO:  make this transformer support BytesMessage instances as
            //        well as TextMessage instances.
            ((TextMessage)target).setText( ((TextMessage)source).getText() );
        }
        catch( CosmosException cEx )
        {
            code = ErrorCode.ERR_INVALID.getValue();
            this.getEnvironment().setError(
            LogLevel.LT_WARN,
            ErrorCode.ERR_INVALID,
            true,
            cEx.getMessage() );
        }

        return code;
    }
    
    private String typeString(int type)
    {
        switch( type )
        {
            case Message.PROPERTY_TYPE_BOOLEAN:
                return "boolean";
            case Message.PROPERTY_TYPE_BYTE:
                return "byte";
            case Message.PROPERTY_TYPE_DECIMAL:
                return "decimal";
            case Message.PROPERTY_TYPE_DOUBLE:
                return "double";
            case Message.PROPERTY_TYPE_INT:
                return "int";
            case Message.PROPERTY_TYPE_LONG:
                return "long";
            case Message.PROPERTY_TYPE_STRING:
                return "string";
            default:
                return "unknown";
        }
    }

}
