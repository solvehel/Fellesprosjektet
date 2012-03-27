package no.ntnu.fp.model;

import java.util.*;

/**
 * @author Sebastian Zalewski
 */
public class MeetingRoom {
	/**
	 * name of this room
	 */
	private String name;
	
	/**
	 * max amount of people that can be in this meeting room
	 */
	private int capacity;
	
	/**
	 * Constructor
	 * 
	 * @param name - The name of the meeting room.
	 * @param capacity - Max amount of people that can be in this meeting room.
	 */
	public MeetingRoom(String name, int capacity) {
		this.name = name;
		this.capacity = capacity;
	}
	
	/**
	 * Returns the name of the meeting room.
	 * This name is unique.
	 * 
	 * @return the name of the meeting room
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Checks if a meeting room is available in the given time interval.
	 * 
	 * @param occupiedTimePoints - The occupied time points
	 * @param startTime - The start time of the meeting.
	 * @param endTime - The end time of the meeting.
	 * @return true if the given time interval is available 
	 */
	public static boolean isTimeAvailable(Map<Calendar, Calendar> occupiedTimePoints, Calendar startTime, Calendar endTime) {
		Iterator<Calendar> iterator = occupiedTimePoints.keySet().iterator();
		
		long inputStartTime = startTime.getTimeInMillis();
		long inputEndTime = endTime.getTimeInMillis();
		
		while (iterator.hasNext()) {
			Calendar itElement = iterator.next();
			long itElementStartTime = itElement.getTimeInMillis();
			long itElementEndTime = occupiedTimePoints.get(itElement).getTimeInMillis();
			
			boolean isItElementStartTimeInInterval = inputStartTime > itElementStartTime && inputStartTime < itElementEndTime;
			boolean isItElementEndTimeInInterval = inputEndTime > itElementStartTime && inputEndTime < itElementEndTime;
			if (isItElementStartTimeInInterval || isItElementEndTimeInInterval)
				return false;
			
			boolean isInputStartTimeInInterval = itElementStartTime > inputStartTime && itElementStartTime < inputEndTime;
			boolean isInputEndTimeInInterval = itElementEndTime > inputStartTime && itElementEndTime < inputEndTime;
			if (isInputStartTimeInInterval || isInputEndTimeInInterval)
				return false;
		}
		
		return true;
	}
	
	/**
	 * @return max amount of people that can be in this meeting room
	 */
	public int getCapacity() {
		return capacity;
	}
}
