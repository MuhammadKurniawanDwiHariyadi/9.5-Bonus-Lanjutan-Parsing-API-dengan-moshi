# 9.5 Lanjutan dari 9. Latihan Scheduler dengan WorkManager. Dimana Parsing REST API menggunakan moshi

Ini adalah proyek latihan dari kursus **Android Fundamental: Background Task & Scheduler** di platform **Dicoding**.



API JSON berbentuk seperti ini <br>
`{` <br>
&nbsp;&nbsp;&nbsp;&nbsp;`"coord":{`<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"lon":107.6186,`<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"lat":-6.9039`<br>
&nbsp;&nbsp;&nbsp;&nbsp;`},`<br>
&nbsp;&nbsp;&nbsp;&nbsp;`"weather":[`<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`{`<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"id":804,`<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"main":"Clouds",`<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"description":"overcast clouds",`<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"icon":"04d"`<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`}`<br>
&nbsp;&nbsp;&nbsp;&nbsp;`],`<br>
&nbsp;&nbsp;&nbsp;&nbsp;`"base":"stations",`<br>
&nbsp;&nbsp;&nbsp;&nbsp;`"main":{`<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"temp":299.3,`<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"feels_like":301.74,`<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"temp_min":299.3,`<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"temp_max":299.3,`<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"pressure":1012,`<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"humidity":62,`<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"sea_level":1012,`<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"grnd_level":932`<br>
&nbsp;&nbsp;&nbsp;&nbsp;`},`<br>
&nbsp;&nbsp;&nbsp;&nbsp;`"visibility":10000,`<br>
&nbsp;&nbsp;&nbsp;&nbsp;`"wind":{`<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"speed":0.68,`<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"deg":38,`<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"gust":0.87`<br>
&nbsp;&nbsp;&nbsp;&nbsp;`},`<br>
&nbsp;&nbsp;&nbsp;&nbsp;`"clouds":{`<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"all":99`<br>
&nbsp;&nbsp;&nbsp;&nbsp;`},`<br>
&nbsp;&nbsp;&nbsp;&nbsp;`"dt":1614909285,`<br>
&nbsp;&nbsp;&nbsp;&nbsp;`"sys":{`<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"country":"ID",`<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"sunrise":1614898494,`<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;`"sunset":1614942453`<br>
&nbsp;&nbsp;&nbsp;&nbsp;`},`<br>
&nbsp;&nbsp;&nbsp;&nbsp;`"timezone":25200,`<br>
&nbsp;&nbsp;&nbsp;&nbsp;`"id":1650357,`<br>
&nbsp;&nbsp;&nbsp;&nbsp;`"name":"Bandung",`<br>
&nbsp;&nbsp;&nbsp;&nbsp;`"cod":200`<br>
`}`<br>


untuk mengambil nya dengan moshi maka dijadikan sebuah data class seperti ini (hanya data yang penting saja di ambil, tidak semua) 

`data class Response(` <br>
       &nbsp;&nbsp;&nbsp;&nbsp;`val id: Int,` <br>
       &nbsp;&nbsp;&nbsp;&nbsp;`val name: String,` <br>
       &nbsp;&nbsp;&nbsp;&nbsp;`@Json(name = "weather")` <br>
       &nbsp;&nbsp;&nbsp;&nbsp;`val weatherList: List<Weather>,` <br>
       &nbsp;&nbsp;&nbsp;&nbsp;`val main: Main,` <br>
`)` <br>
<br>
`data class Weather(` <br>
       &nbsp;&nbsp;&nbsp;&nbsp;`val main: String,` <br>
       &nbsp;&nbsp;&nbsp;&nbsp;`val description: String` <br>
`)` <br>
<br>
`data class Main(` <br>
       &nbsp;&nbsp;&nbsp;&nbsp;`@Json(name = "temp")` <br>
       &nbsp;&nbsp;&nbsp;&nbsp;`val temperature: Double` <br>
`)`

