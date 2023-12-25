package com.mahfuz.taskmanager.utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;


public class DateProcessing {
	public Timestamp getCurTimestamp() {
		LocalDateTime dateTime = LocalDateTime.now();
		return java.sql.Timestamp.valueOf(dateTime);
	}
	
	public Timestamp getExpTimestamp(int hrs) {
		LocalDateTime dateTime = LocalDateTime.now();
		return java.sql.Timestamp.valueOf(dateTime.plusHours(hrs));
	}
}