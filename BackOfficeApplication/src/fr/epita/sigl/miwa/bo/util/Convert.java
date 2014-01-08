package fr.epita.sigl.miwa.bo.util;

import java.util.Date;

import fr.epita.sigl.miwa.bo.parser.DomParserHelper;

public class Convert
{
	@SuppressWarnings("deprecation")
	public static Date stringToDate(String date, String format)
	{
		 
		switch (format)
		{
			case "AAAA-MM-JJ HH:mm:ss" :
				String[] dateAndtimeSplited1 = date.split(" ");
				String[] dateSplited1 = dateAndtimeSplited1[0].split("-");
				String[] timeSplited1 = dateAndtimeSplited1[1].split(":");
				
				return new Date(Integer.parseInt(dateSplited1[0]) - 1900, Integer.parseInt(dateSplited1[1]) - 1, Integer.parseInt(dateSplited1[2]), Integer.parseInt(timeSplited1[0]), Integer.parseInt(timeSplited1[1]), Integer.parseInt(timeSplited1[2]));
			case "AAAA-MM-JJ" :
				String[] dateSplited2 = date.split("-");

				return new Date(Integer.parseInt(dateSplited2[0]) - 1900, Integer.parseInt(dateSplited2[1]) - 1, Integer.parseInt(dateSplited2[2]));
			case "AAAAMMJJ" :
				return new Date(Integer.parseInt(date.substring(0, 4)) - 1900, Integer.parseInt(date.substring(4, 6)) - 1, Integer.parseInt(date.substring(6, 8)));
			default :
				return null;
		}
	}
}
