package com.realtalkserver.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * LocationLogic contains methods for working with location, such as 
 * calculating distances between GPS coordinates.
 * 
 * @author Jory Rice
 */
public class LocationLogic {
	
	private static final int EARTH_RADIUS_METERS = 6371000;
	
	/**
	 * Sorts the given cri list by proximity to the given coordinates.
	 * Closest rooms are "less than" further rooms, and will therefore
	 * be earlier in the returned list.
	 * 
	 * @param rgcriRooms    The list to sort
	 * @param latitude      Latitude of the target point
	 * @param longitude     Longitude of the target point
	 * @return              A new list with the same rooms, in sorted order
	 */
	public static List<ChatRoomInfo> rgcriSortByProximity(
			List<ChatRoomInfo> rgcriRooms, double latitude, double longitude) {
		List<ChatRoomProximity> rgcrp = new ArrayList<ChatRoomProximity>();
		
		// Add proximity information to chat rooms
		for (ChatRoomInfo cri : rgcriRooms) {
			rgcrp.add(new ChatRoomProximity(cri, latitude, longitude));
		}
		
		// Sort by proximity
		Collections.sort(rgcrp);
		
		// return chat room list in sorted order
		List<ChatRoomInfo> rgcriSorted = new ArrayList<ChatRoomInfo>();
		for (ChatRoomProximity crp : rgcrp) {
			rgcriSorted.add(crp.getChatRoomInfo());
		}
		return rgcriSorted;
	}

	/**
	 * Computes the distance between two points along the earth's surface,
	 * using the Haversine formula. Answer is in meters.
	 * 
	 * @param lat1      latitude for point 1
	 * @param lon1      longitude for point 1
	 * @param lat2      latitude for point 2
	 * @param lon2      longitude for point 2
	 * @return          distance between point 1 and 2
	 */
	public static double distance(double lat1, double lon1, double lat2, double lon2) {
		double dLat = deg2rad(lat2 - lat1);
		double dLon = deg2rad(lon2 - lon1);
		lat1 = deg2rad(lat1);
		lat2 = deg2rad(lat2);

		double a = 
				Math.sin(dLat/2) * Math.sin(dLat/2) +
				Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2); 
		
		double c = 2 * Math.asin(Math.sqrt(a)); 
		return EARTH_RADIUS_METERS * c;
	}

	/**
	 * Converts degrees to radians.
	 * 
	 * @param deg    Number in degrees
	 * @return       Number in radians
	 */
	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}
	
	/**
	 * Stores a chat room object with a distance from the given coordinates
	 * 
	 * @author Jory Rice
	 */
	static class ChatRoomProximity implements Comparable<ChatRoomProximity> {
		
		private ChatRoomInfo cri;
		private double distance;
		
		ChatRoomProximity(ChatRoomInfo cri, double latitude, double longitude) {
			this.cri = cri;
			this.distance = distance(latitude, longitude, cri.getLatitude(), cri.getLongitude());
		}
		
		ChatRoomInfo getChatRoomInfo() {
			return this.cri;
		}

		double getDistance() {
			return this.distance;
		}

		/**
		 * A room that is closer to its point is "less than"
		 * a room that is farther from its point.
		 */
		public int compareTo(ChatRoomProximity o) {
			if (this.getDistance() > o.getDistance()) {
				return 1;
			} else if (this.getDistance() < o.getDistance()) {
				return -1;
			} else {
				return 0;
			}
		}
	}
}
