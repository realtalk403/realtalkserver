package com.realtalkserver.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

/**
 * Utility class used for sending multicast messages to GCM
 * 
 * @author Colin Kho
 *
 */
public class GCMSendMessages {
    
    // Max Size for GCM
    private static final int MULTICAST_SIZE = 1000;
    
    // Retry Limit for GCM
    private static final int GCM_SEND_RETRY = 5;
    
    // Logger for logging process
    private static final Logger logger = Logger.getLogger(GCMSendMessages.class.getName());
    
    /**
     * This method pushes new messages to GCM and then to the devices in interest. It converts the 
     * message into a suitable format for GCM and pushes it to the server. It sends the messages
     * asynchronously in batches of 1000 messages each.
     * 
     * @param rgUserinfo   List of UserInfos to send to. Should have a valid username, password and reg id.
     * @param messageinfo  Object containing the message details to send.
     * @param chatroominfo Information about the chatroom that message should be sent to.
     */
    public static void sendMulticastMessage(List<UserInfo> rgUserinfo, 
            MessageInfo messageinfo, ChatRoomInfo chatroominfo) {
        logger.info("GCM: Beginning to send GCM Multicast message(s)");
        if (!rgUserinfo.isEmpty()) {
            logger.info("GCM: Creating Multicast Message");
            Message messagePost = createGCMMessage(messageinfo, chatroominfo);
            // Send a multicast message using JSON
            // Must split in chunks of 1000 devices (GCM limit)
            List<String> rgRegId = rgRegIdExtractIdFromU(rgUserinfo);
            List<String> rgPartialRegIds = new ArrayList<String>(rgRegId.size());
            List<UserInfo> rgPartialUserinfo = new ArrayList<UserInfo>(rgUserinfo.size());
            int iTasks = 0;
            for (int i = 0; i < rgRegId.size(); i++) {
                rgPartialRegIds.add(rgRegId.get(i));
                rgPartialUserinfo.add(rgUserinfo.get(i));
                if (MULTICAST_SIZE == rgPartialRegIds.size() || i == rgRegId.size() - 1) {
                    asyncSend(new ArrayList<String>(rgPartialRegIds), 
                            new ArrayList<UserInfo>(rgPartialUserinfo), messagePost);
                    rgPartialRegIds.clear();
                    rgPartialUserinfo.clear();
                    iTasks++;
                }
            }
            logger.info("Asynchronously sending " + iTasks + 
                    " multicast messages to " + rgRegId.size() + "devices.");
        } else {
            logger.info("GCM: No registration Ids to send to");
        }
    }
    
    /**
     * Helper method to send Multicast messages asynchronously.
     * 
     * @param rgRegId
     * @param rgUserinfo 
     * @param messagePost
     */
    private static void asyncSend(final List<String> rgRegId,
            final List<UserInfo> rgUserinfo, final Message messagePost) {
        (new Thread() {
            
            /**
             * Method used to run the thread.
             */
            @Override
            public void run() {
                Sender sender = new Sender(APIKeyInitializer.getApiKey());
                MulticastResult multicastResult;
                // Post Messages
                try {
                    multicastResult = sender.send(messagePost, rgRegId, GCM_SEND_RETRY);
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "GCM: Error Posting Messages");
                    return;
                }
                
                // Process results
                List<Result> rgResult = multicastResult.getResults();
                
                // Analyze the results
                for (int i = 0; i < rgRegId.size(); i++) {
                    // Reg Ids and Results are indexed the same to correspond to each other
                    String stRegId = rgRegId.get(i);
                    Result result = rgResult.get(i);
                    String stMessageId = result.getMessageId();
                    if (stMessageId != null) {
                        logger.info("GCM: Successfully send message to device: " + stRegId);
                        
                        // Check for canonical id
                        String stCanonicalId = result.getCanonicalRegistrationId();
                        if (stCanonicalId != null) {
                            UserInfo userinfo = rgUserinfo.get(i);
                            logger.info("GCM: Updating Canonical id on device from " + stRegId + " to " + stCanonicalId);
                            UserManager.fChangeId(userinfo.getUserName(), userinfo.getUserName(), stCanonicalId);
                        }
                    } else {
                        String stError = result.getErrorCodeName();
                        UserInfo userinfo = rgUserinfo.get(i);
                        if (stError.equals(Constants.ERROR_NOT_REGISTERED)) {
                            logger.info("GCM: Error sending to GCM server; Reason: " + stRegId + "not registered");
                            logger.info("GCM: Unregistering user: " + userinfo.getUserName() + " with reg id: " + stRegId);
                            UserManager.fRemoveUser(userinfo.getUserName(), userinfo.getPassword(), userinfo.getRegistrationId());
                        } else {
                            logger.severe("GCM: Error sending to GCM server with reg id: " + stRegId);
                        }
                    }
                }
            }
            
        }).start();
        
    }
    
    /**
     * Helper method that creates a list of registration ids given a list of
     * userinfo objects that have the same order with that as the user info list.
     * 
     * @param rgU List of UserInfos to extract from.
     * @return    List of Registration Ids that are arranged in the same manner as the user info list.
     */
    private static List<String> rgRegIdExtractIdFromU(final List<UserInfo> rgU) {
        List<String> rgRegId = new ArrayList<String>();
        for (UserInfo userinfo : rgU) {
            rgRegId.add(userinfo.getRegistrationId());
        }
        return new ArrayList<String>(rgRegId);
    }

    /**
     * This method creates a message suitable for GCM to broadcast to its devices
     * 
     * @param  messageinfo MessageInfo object that has the message information embedded in it.
     * @param chatroominfo 
     * @return Message used for GCM
     */
    public static Message createGCMMessage(MessageInfo messageinfo, ChatRoomInfo chatroominfo) {
        String stTimestamp = String.valueOf(messageinfo.getTimeStamp().getTime());
        Message message = new Message.Builder()
            .addData(RequestParameters.PARAMETER_MESSAGE_SENDER, messageinfo.getSender())
            .addData(RequestParameters.PARAMETER_MESSAGE_TIMESTAMP, stTimestamp)
            .addData(RequestParameters.PARAMETER_MESSAGE_BODY, messageinfo.getBody())
            .addData(RequestParameters.PARAMETER_ROOM_NAME, chatroominfo.getName())
            .addData(RequestParameters.PARAMETER_ROOM_ID, chatroominfo.getId())
            .timeToLive(600)
            .build();
        return message;
    }
}
