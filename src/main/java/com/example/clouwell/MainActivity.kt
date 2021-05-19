package com.example.clouwell


import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.shapes.Shape
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes

import org.jetbrains.anko.doAsync
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity() {

    private var user_field: EditText?=null //текстовое поле пользователя, которое может принимать null
    private var main_btn: Button?=null //кнопка, которая может принимать null
    private var rezult_info: TextView?=null //поле резултата, которое может принимать null
    private var mBackground: LinearLayout?=null //фон
    private var logo: TextView?=null //logo
    private var air_pollution: TextView?=null //индекс загрязнения


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //ссылка на объекты дизайна
        user_field = findViewById(R.id.user_field)
        main_btn = findViewById(R.id.main_btn)
        rezult_info = findViewById(R.id.result_info)
        mBackground = findViewById(R.id.mBackground)
        logo = findViewById(R.id.logo)
        air_pollution = findViewById(R.id.air_pollution)







        rezult_info?.setBackgroundColor(0)
        air_pollution?.setBackgroundColor(0)
        //обработка событий
        main_btn?.setOnClickListener{

            //если кнопка нажата, но ничего не введено, то показывать всплывающее окно с вводом города
            if(user_field?.text?.toString()?.trim()?.equals("")!!)
                Toast.makeText(this, "Введите город в поле", Toast.LENGTH_LONG).show()
            else{
                //rezult_info?.setBackgroundColor(Color.parseColor("#4cb9d2"))
                rezult_info?.setBackgroundResource(R.drawable.rectangle)
                air_pollution?.setBackgroundResource(R.drawable.recatangle1)

                var lat: String = " "
                var lon: String = " "
                val city: String = user_field?.text.toString() //город введённый в поле
                val key: String = "a60241ac8cefba44d2428204555b6ccb" //апи ключ

                val url: String = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$key&lang=ru&units=metric" //ссылка с нужным json

                doAsync {
                    val apiResponse = URL(url).readText() //отправка http запроса по заданному url и считывание запроса по данному url адресу


                    //обработка JSON
                    val weather = JSONObject(apiResponse).getJSONArray("weather") //выбор объекта из массива
                    val desc = weather.getJSONObject(0).getString("description") //выбор элемента массива
                    val wind = JSONObject(apiResponse).getJSONObject("wind")
                    val main = JSONObject(apiResponse).getJSONObject("main")
                    var city_name_from_api = JSONObject(apiResponse).getString("name")
                    var coord = JSONObject(apiResponse).getJSONObject("coord")


                    lat = coord.getString("lat")
                    lon = coord.getString("lon")


                    val token: String = "9f7e1cf085a8eaa7164053b1fd9e5e1ce34af16d"
                    val url_air: String = "https://api.waqi.info/feed/geo:$lat;$lon/?token=$token"
                    doAsync {
                        val apiResponse_pollution = URL(url_air).readText()
                        val main_air = JSONObject(apiResponse_pollution).getJSONObject("data")
                        var aqi = main_air.getInt("aqi")

                        //AQI index air pollution
                        var index_pollution_data: String = " "
                        if (aqi <= 50){
                            index_pollution_data = "Низкое"
                        }
                        else if (aqi <=100 && aqi >50){
                            index_pollution_data = "Умеренное"
                        }
                        else if (aqi <=150 && aqi >100){
                            index_pollution_data = "Повышенное"
                        }
                        else if (aqi <=200 && aqi >150){
                            index_pollution_data = "Высокое"
                        }
                        else if (aqi <=300 && aqi >200){
                            index_pollution_data = "Очень высокое"
                        }
                        else if (aqi <=500 && aqi >300){
                            index_pollution_data = "Катастрофическое"
                        }
                        else{
                            index_pollution_data = "Запредельное"
                        }
                        air_pollution?.text = "ИКВ: $aqi, $index_pollution_data"
                        Log.d("INFO_air", apiResponse_pollution)
                    }



                    val temp1 = main.getInt("temp")
                    val humidity = main.getString("humidity") + " %"
                    val feels_like1 = main.getInt("feels_like")
                    val pressure1 = main.getDouble("pressure") * 0.750064
                    val pressure2 = pressure1.toInt()



                    var speed_wind = wind.getString("speed") + " м/с"
                    var speed_wind_double_type = wind.getDouble("speed")

                    var feels_like = feels_like1.toString()
                    var temp = temp1.toString()
                    var pressure = pressure2.toString() + " мм рт.ст"


                    var wind_desc: String
                    wind_desc = " "

                    //добавление + и - к температуре
                    if (temp1 >= 0){
                        temp = "+ " + temp1.toString() + " °C"
                    }
                    else {
                        temp = " " + temp1.toString() + " °C"
                    }

                    if (feels_like1 >= 0){
                        feels_like = "+ " + feels_like1.toString() + " °C"
                    }
                    else {
                        feels_like = " " + feels_like1.toString() + " °C"
                    }

                    //характеристика ветра
                    if (speed_wind_double_type <= 0.2) {
                        wind_desc = "штиль"
                    }

                    else if (speed_wind_double_type <= 1.5 && speed_wind_double_type > 0.2) {
                        wind_desc = "тихий ветер"
                    }
                    else if (speed_wind_double_type <= 3.3 && speed_wind_double_type > 1.5) {
                        wind_desc = "лёгкий ветер"
                    }
                    else if (speed_wind_double_type <= 5.4 && speed_wind_double_type > 3.3) {
                        wind_desc = "слабый ветер"
                    }
                    else if (speed_wind_double_type <= 7.9 && speed_wind_double_type > 5.4) {
                        wind_desc = "умеренный ветер"
                    }
                    else if (speed_wind_double_type <= 10 && speed_wind_double_type > 7.9) {
                        wind_desc = "свежий ветер"
                    }
                    else if (speed_wind_double_type <= 13.8 && speed_wind_double_type > 10) {
                        wind_desc = "сильный ветер"
                    }
                    else if (speed_wind_double_type <= 17.1 && speed_wind_double_type > 13.8) {
                        wind_desc = "крепкий ветер"
                    }
                    else if (speed_wind_double_type <= 20.7 && speed_wind_double_type > 17.1) {
                        wind_desc = "очень крепкий ветер"
                    }
                    else if (speed_wind_double_type <= 24.4 && speed_wind_double_type > 20.7) {
                        wind_desc = "шторм"
                    }
                    else if (speed_wind_double_type <= 28.4 && speed_wind_double_type > 24.4) {
                        wind_desc = "сильный шторм"
                    }
                    else if (speed_wind_double_type <= 32.6 && speed_wind_double_type > 28.4) {
                        wind_desc = "жестокий шторм"
                    }
                    else if (speed_wind_double_type > 32.6) {
                        wind_desc = "ураган"
                    }






                    if (city_name_from_api.equals("Милан")){
                        mBackground?.setBackgroundResource(R.drawable.clear_sky)
                        user_field?.setTextColor(Color.BLACK)
                        logo?.setTextColor(Color.parseColor("#98DBF8"))
                    }
                    else {
                        mBackground?.setBackgroundColor(Color.parseColor("#2FA2BB"))
                        user_field?.setTextColor(Color.WHITE)
                    }

                    rezult_info?.text = "Город: $city_name_from_api\nТемпература: $temp\nОписание: $desc\nОщущается как: $feels_like\nСкорость ветра: $speed_wind\nХарактеристика ветра: $wind_desc\nДавление: $pressure\nВлажность: $humidity"



                    Log.d("INFO", apiResponse)




                }


            }
        }
    }

}


