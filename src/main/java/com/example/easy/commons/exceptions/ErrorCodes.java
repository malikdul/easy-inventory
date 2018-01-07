package com.example.easy.commons.exceptions;

public class ErrorCodes
{

	/**
	 * 
	 * Codes for errors that map to HTTP statuses.
	 * 
	 * Reserved Range: 10000 - 10999.
	 */

	public static final String ERROR_AUTHENTICATION = "10401";
	public static final String RESOURCE_NOT_ACCESSIBLE = "10403";
	public static final String RESOURCE_NOT_FOUND = "10404";
	public static final String BAD_REQUEST = "10400";
	public static final String SERVICE_ERROR = "10500";
	public static final String DATABASE_NOT_AVAILABLE = "10503";
	public static final String RESOURCE_IS_GONE = "10410";

	/**
	 * 
	 * Codes for errors that do not map to HTTP statuses.
	 * 
	 * Reserved Range: 11000 - 11999.
	 */
	public static String UPDATE_AVAILABLE = "11001";
	public static String IMMEDIATE_UPDATE = "11002";

	/**
	 * 
	 * Codes for MySql errors.
	 * 
	 * Reserved Range: 1000 - 1999.
	 */
	public static String ER_LOCK_WAIT_TIMEOUT = "1205";
	public static String ER_LOCK_DEADLOCK = "1213";

	public static long getNextErrorID()
	{
		return System.currentTimeMillis();
	}

}
