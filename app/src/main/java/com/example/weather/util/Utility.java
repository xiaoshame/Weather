package com.example.weather.util;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.example.weather.db.WeatherDB;
import com.example.weather.model.City;
import com.example.weather.model.County;
import com.example.weather.model.Province;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Lijinpu on 2015/1/19.
 */
public class Utility {
    //解析和处理服务器返回的省级数据
    public synchronized static boolean handleProvincesResponse(WeatherDB weatherDB,String response){
        if(!TextUtils.isEmpty(response)){
            String[] allProvinces = response.split(",");                  //依据,分解字符串
            if(allProvinces != null && allProvinces.length > 0){
                for(String p : allProvinces){
                    //一个字符串无法按照指定的字符拆分，则需要使用"\"转义，转义的时候两个\\表示一个\
                    String[] array = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);
                    //将解析出来的数据存储到Province表
                    weatherDB.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }
    //解析和处理服务器返回的市级数据
    public static boolean handleCitiesResponse(WeatherDB weatherDB,String response,int provinceId){
        if(!TextUtils.isEmpty(response)){
            String[] allCities = response.split(",");
            if(allCities != null && allCities.length > 0){
                for(String c : allCities){
                    String[] array = c.split("\\|");
                    City city = new City();
                    city.setProvinceId(provinceId);
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    weatherDB.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }
    //解析和处理服务器返回的县级数据
    public static boolean handleCountiesResponse(WeatherDB weatherDB,String response,int cityId){
        if(!TextUtils.isEmpty(response)){
            String[] allCounties = response.split(",");
            if(allCounties != null && allCounties.length > 0){
                for(String c : allCounties){
                    String[] array = c.split("\\|");
                    County county = new County();
                    county.setCountyCode(array[0]);
                    county.setCountyName(array[1]);
                    county.setCityId(cityId);
                    weatherDB.saveCounty(county);
                }
            }
            return true;
        }
        return false;
    }

    //解析服务器返回的JSON数据，并将解析出的数据存储到本地
    public static void handleWeatherResponse(Context context,String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
            String cityName = weatherInfo.getString("city");
            String weatherCode = weatherInfo.getString("cityid");
            String temp1 = weatherInfo.getString("temp1");
            String temp2 = weatherInfo.getString("temp2");
            String weatherDesp = weatherInfo.getString("weather");
            String publistTime = weatherInfo.getString("ptime");
            saveWeatherInfo(context,cityName,weatherCode,temp1,temp2,weatherDesp,publistTime);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    //将服务器返回的天气信息存储到SharedPreferences文件中
    public static void saveWeatherInfo(Context context,String cityName,String weatherCode,String temp1,String temp2,
                                       String weatherDesp,String publishTime){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected",true);
        editor.putString("city_name", cityName);
        editor.putString("weather_code",weatherCode);
        editor.putString("temp1",temp1);
        editor.putString("temp2",temp2);
        editor.putString("weather_desp",weatherDesp);
        editor.putString("publish_time",publishTime);
        editor.putString("current_date",sdf.format(new Date()));
        editor.commit();
    }
}
