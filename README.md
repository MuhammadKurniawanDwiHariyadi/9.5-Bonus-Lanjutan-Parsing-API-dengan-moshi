# 9.5 Lanjutan dari 9. Latihan Scheduler dengan WorkManager. Dimana Parsing REST API menggunakan moshi

Ini adalah proyek latihan dari kursus **Android Fundamental: Background Task & Scheduler** di platform **Dicoding**.



API JSON berbentuk seperti ini
`{
   "coord":{
      "lon":107.6186,
      "lat":-6.9039
   },
   "weather":[
      {
         "id":804,
         "main":"Clouds",
         "description":"overcast clouds",
         "icon":"04d"
      }
   ],
   "base":"stations",
   "main":{
      "temp":299.3,
      "feels_like":301.74,
      "temp_min":299.3,
      "temp_max":299.3,
      "pressure":1012,
      "humidity":62,
      "sea_level":1012,
      "grnd_level":932
   },
   "visibility":10000,
   "wind":{
      "speed":0.68,
      "deg":38,
      "gust":0.87
   },
   "clouds":{
      "all":99
   },
   "dt":1614909285,
   "sys":{
      "country":"ID",
      "sunrise":1614898494,
      "sunset":1614942453
   },
   "timezone":25200,
   "id":1650357,
   "name":"Bandung",
   "cod":200
}`

untuk mengambil nya dengan moshi maka dijadikan sebuah data class seperti ini (hanya data yang penting saja di ambil, tidak semua) 

`data class Response(
        val id: Int,
        val name: String,
        @Json(name = "weather")
        val weatherList: List<Weather>,
        val main: Main,
)`
 
`data class Weather(
        val main: String,
        val description: String
)`
 
`data class Main(
        @Json(name = "temp")
        val temperature: Double
)`
