package com.example.utils;

public class Constants {
	public static String HOTEL_INFO = "http://10.9.0.9:8080/ajx/service/data?type=hotelItem";
//	public static String HOTEL_INFO = "http://10.9.0.10:8080/ajx/service/data?type=hotelItem";

	public static String TOKEN = "http://192.168.0.176:8080/qtels-cloud/gateway/access_token?access_key=key&secret=secret";
	public static String HOTEL_DETAIL = "http://192.168.0.176:8080/qtels-cloud/gateway/get_hotel_detail?hotel_id=cnbjbjs8sy&locale=zh_CN&access_token=";
	public static String HOTEL_BG_IMG = "http://192.168.0.176:8080/qtels-cloud/gateway/list_hotel_photos?hotel_id=cnbjbjs8sy&locale=zh_CN&access_token=";
	public static String HOTEL_ATTENDANTS = "http://192.168.0.176:8080/qtels-cloud/gateway/get_attendant_list?locale=zh_CN&hotel_id=cnbjbjs8sy&attendant_type_id=&access_token=";
	public static String HOTEL_ROOM = "http://192.168.0.176:8080/qtels-cloud/gateway/get_inhouse_detail?hotel_id=cnbjbjs8sy&locale=zh_CN&access_token=";
}
