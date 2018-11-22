package AndroidLabs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.xmlpull.v1.XmlPullParser.START_TAG;

public class WeatherForecast extends Activity {

    protected static final String ACTIVITY_NAME = "WeatherForecast";
    private ImageView weatherPicture;
    private TextView currentTemperature, minimumTemperature, maxTemperature, windSpeed;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        Log.i(ACTIVITY_NAME, "In onCreate()");

        weatherPicture = (ImageView)findViewById(R.id.weatherImage);
        currentTemperature = (TextView)findViewById(R.id.currentTemp);
        minimumTemperature = (TextView)findViewById(R.id.minTemp);
        maxTemperature = (TextView)findViewById(R.id.maxTemp);
        windSpeed = (TextView)findViewById(R.id.windSpeed);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        ForecastQuery runQuery = new ForecastQuery();
        runQuery.execute();
    }

    /* Inner class for WeatherForecast */
    public class ForecastQuery extends AsyncTask<String, Integer, String[]> {
        private Bitmap imageCurrentWeather;

        protected String[] doInBackground(String... strings) {
            try {
                /* The URL for the web browser */
                URL url = new URL
                        ("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                // Given a string representation of a URL, sets up a connection and gets
                // an input stream.
                urlConnection.setReadTimeout(10000); // milliseconds
                urlConnection.setConnectTimeout(15000); // milliseconds
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoInput(true);
                /* The Query is starting */
                urlConnection.connect();

                /* Creating a Pull parser uses the Factory pattern */
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser parser = factory.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(urlConnection.getInputStream(), "UTF-8");
                int eventType = parser.getEventType();
                String option;
                // Create array
                String[] result = new String[5];
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    parser.next();
                    option = parser.getName();
                    if (option == null) {
                        option = "";
                    }
                    eventType = parser.getEventType();
                    if (eventType == START_TAG) {
                        if (option.equalsIgnoreCase("temperature")) {
                            // The progress of retrieving the data for the current temperature.
                            result[0] = parser.getAttributeValue(null, "value");
                            Log.i(ACTIVITY_NAME, "Current temperature is working");
                            publishProgress(25);
                            // The progress of retrieving the data for the minimum temperature.
                            result[1] = parser.getAttributeValue(null, "min");
                            Log.i(ACTIVITY_NAME, "Minimum temperature is working");
                            publishProgress(50);
                            // The progress of retrieving the data for the maximum temperature.
                            result[2] = parser.getAttributeValue(null, "max");
                            Log.i(ACTIVITY_NAME, "Maximum temperature is working");
                            publishProgress(75);
                            // The progress of retrieving the data for the weather image.
                        } else if (option.equalsIgnoreCase("weather")) {
                            result[3] = parser.getAttributeValue(null, "icon");
                            Log.i(ACTIVITY_NAME, "The weather is working");
                            publishProgress(100);
                        } else if (option.equalsIgnoreCase("speed")) {
                            result[4]= parser.getAttributeValue(null, "value");
                        }
                    } // End if
                } // End While loop
                Log.i(ACTIVITY_NAME, "The file is shown");
                urlConnection.disconnect();
                if (fileExistence(result[3] + ".png")) {
                    FileInputStream fis = null;
                    try {
                        fis = openFileInput(result[3] + ".png");
                        imageCurrentWeather = BitmapFactory.decodeStream(fis);
                        Log.i(ACTIVITY_NAME, "The weather image is shown");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } // End if
                else {
                    try {
                        URL ImageURL = new URL("http://openweathermap.org/img/w/" + result[3] + ".png");
                        imageCurrentWeather = HttpUtils.getImage(ImageURL);
                        FileOutputStream outputStream = openFileOutput(result[3] + ".png", Context.MODE_PRIVATE);
                        imageCurrentWeather.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                        outputStream.flush();
                        outputStream.close();
                        Log.i(ACTIVITY_NAME, "The weather image has been downloaded ");
                    }
                    catch (Exception e) {
                    }
                } // End else
                return result;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (XmlPullParserException e) {
                e.printStackTrace();
                return null;
            }
        } // End doInBackground function

        public boolean fileExistence(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

        @Override
        protected void onProgressUpdate(Integer... value) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(value[0]);
            Log.i(ACTIVITY_NAME, "Progress Update");
        }

        protected void onPostExecute(String[] result) {
            currentTemperature.setText("Current Temperature: " +result[0]+"°C");
            minimumTemperature.setText("Minimum Temperature: " +result[1]+"°C");
            maxTemperature.setText("Max Temperature: " +result[2]+"°C");
            windSpeed.setText("Wind Speed: " +result[4]+"km/h");
            weatherPicture.setImageBitmap(imageCurrentWeather);
            progressBar.setVisibility(View.INVISIBLE);
            Log.i(ACTIVITY_NAME, "Post Execution");
        }
    }
}
