/**
 * 
 */
package com.realtalkserver.util;

import java.util.Collection;

/**
 * Constants that define Response Codes or Messages
 * 
 * @author Colin Kho
 *
 */
public class ResponseParameters {
    // Parameter that indicates an Error Code.
    public static final String PARAMETER_ERROR_CODE = "ERROR_CODE";
    // Parameter that indicates an Error Message.
    public static final String PARAMETER_ERROR_MSG = "ERROR_MESSAGE";
    
    
    // Error Codes
    // Indicates User Info has incurred an error with the server.
    public static final String RESPONSE_ERROR_CODE_USER = "ERROR_USER";
    // Indicates Room Info has incurred an error with the server.
    public static final String RESPONSE_ERROR_CODE_ROOM = "ERROR_ROOM";
    // Indicates Message Info has incurred an error with the server.
    public static final String RESPONSE_ERROR_CODE_MESSAGE = "ERROR_MESSAGE";
    // Generic Error Code
    public static final String RESPONSE_ERROR = "ERROR";
    
    // Error Messages: Format (MESSAGE_<Context>_<Context-Parameter>_<Reason>)
    // Note          : Context Parameter is optional
    
    // USER ERROR MESSAGES
    // Message indicating that User's user name is not found or invalid.
    public static final String RESPONSE_MESSAGE_USER_NAME_INVALID = "MESSAGE_USER_NAME_ERROR";
    // Message indicating that User's password is wrong or is invalid.
    public static final String RESPONSE_MESSAGE_USER_PASSWORD_INVALID = "MESSAGE_USER_PASSWORD_ERROR";
    // Generic Message to indicate that user error.
    public static final String RESPONSE_MESSAGE_USER_ERROR = "MESSAGE_USER_ERROR";
    
    // CHATROOM ERROR MESSAGES
    // Generic Message to indicate that room has encountered an error.
    public static final String RESPONSE_MESSAGE_ROOM_ERROR = "MESSAGE_ROOM_ERROR";
    // Message indicating that the room name is invalid or does not match with given ID
    public static final String RESPONSE_MESSAGE_ROOM_NAME_INVALID = "MESSAGE_ROOM_NAME_ERROR";
    // Message indicating that room id is invalid or does not match with given name.
    public static final String RESPONSE_MESSAGE_ROOM_ROOMID_INVALID = "MESSAGE_ROOM_ID_ERROR";
    
    // MESSAGE ERROR MESSAGES
    // Generic Message to indicate that room has encountered an error.
    public static final String RESPONSE_MESSAGE_MESSAGE_ERROR = "MESSAGE_MESSAGE_ERROR";
}
