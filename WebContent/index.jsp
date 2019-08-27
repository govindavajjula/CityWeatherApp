<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
  <head>
    <title>City Weather App</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script>
      $(document).ready(function(){
        $('button').attr("disabled",true);
        $('#cityName').keyup(function(){
          $('button').attr("disabled",$('#cityName').val().length == 0);
        });
        $('#cityName').keydown(function(event){
            if(event.keyCode === 13) {
                if (!$('button').prop('disabled')) {
                    postAjaxRequest();
                }
            }

        });

        $('button').click(function(){
          postAjaxRequest();
        });
      });

      function postAjaxRequest(){
          var cities = $('#cityName').val();
          console.log("Cities Entered ==> " + cities)
          var isGood = validateCities(cities);
          if(isGood) {
              $('#dataTable').find("tr:gt(0)").remove();
              $.post("/WeatherApp/weather/info", {"cities": cities},
                  function (data, status) {
                      if (status == "success") {
                          processResponse(data)
                      } else {
                          $(document.body).append("Service is down. Please try again later");
                      }

                  });
          }else{
              alert('Please check city names');
          }
      }

      function validateCities(cities){
    	  var isValid = true
          var re = /^[a-zA-Z',.\s-]{1,25}$/g;
          var cityArray = cities.split(",");
          for(var city of, cityArray){
        	  if(!re.test(city)) {
            	  isValid = false;
            	  break;
              }
          }
        

          return isValid;
      }

      function processResponse(data){
          $.each(data,function(index,value){
              displayWeatherInfo(index,value)
          });
      }

      function displayWeatherInfo(index,value){
          var tableDataRow;
          if(value.notfound != undefined || value.notfound){
               tableDataRow = '<tr>' +
                   '<td class="datatd">' + value.name + '</td>' +
                  '<td class="datatd">Not Found </td>' +
                  '<td class="datatd">Not Found </td>' +
                  '<td class="datatd">Not Found </td>' +
                  '<td class="datatd">Not Found </td>' +
              '</tr>';
          }else {
               tableDataRow = '<tr>' +
                  '<td class="datatd">' + value.name + '</td>' +
                  '<td class="datatd">' + value.main.temp + '</td>' +
                  '<td class="datatd">' + value.main.humidity + '</td>' +
                  '<td class="datatd">' + new Date(value.sys.sunrise).toLocaleString() + '</td>' +
                  '<td class="datatd">' + new Date(value.sys.sunset).toLocaleString() + '</td>'
              '</tr>';
          }
          $('#dataTable').find('tbody').append(tableDataRow);
      }

    </script>
      <style>
          .center {
              margin: auto;
              width: 50%;
              border: 3px solid #FFFFFF;
              padding: 10px;
          }
          table.paleBlueRows {
              font-family: "Times New Roman", Times, serif;
              border: 1px solid #FFFFFF;
              width: 610px;
              height: 50px;
              text-align: center;
              border-collapse: collapse;
          }
          table.paleBlueRows td, table.paleBlueRows th {
              border: 1px solid #FFFFFF;
              padding: 3px 2px;
          }
          table.paleBlueRows tbody td {
              font-size: 13px;
          }

          table.paleBlueRows tr:nth-child(even) {
              background: #D0E4F5;
          }
          table.paleBlueRows thead {
              background: #0B6FA4;
              border-bottom: 5px solid #FFFFFF;
          }
          table.paleBlueRows thead th {
              font-size: 17px;
              font-weight: bold;
              color: #FFFFFF;
              text-align: center;
              border-left: 2px solid #FFFFFF;
          }
          table.paleBlueRows thead th:first-child {
              border-left: none;
          }


          .cityName{
              font-size: 14px;
              font-weight: bold;
              color: #0B6FA4;
          }

      </style>
  </head>
  <body>
  <div id="content" class="center">
    <div id="serachDiv" style="margin: 40px 0px 0px 20px;">
      <table>
        <tr>
          <td class="cityName">Enter City Name(s)</td>
          <td><input type = "text"  id="cityName" name="cityName" size="63" maxlength="100"/></td>
          <td><button>Find Weather</button></td>
        </tr>
      </table>
    </div>
      <div id="citiesWeather" style="margin: 40px 0px 0px 20px;">
        <table id="dataTable" border="1" class="paleBlueRows">
            <thead>
                <tr>
                    <th>City</th>
                    <th>Temp</th>
                    <th>Humidity</th>
                    <th>Sunrise</th>
                    <th>Sunset</th>
                </tr>
            </thead>
            <tbody>

            </tbody>
        </table>

      </div>
  </div>
  </body>
</html>
