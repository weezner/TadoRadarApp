package de.tado.wiesner.tadoradarapp.connection;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import de.tado.wiesner.tadoradarapp.MainActivity;

/**
 * Created by wiesner on 12.03.15.
 */
public class ConnectionTask extends AsyncTask<String, Void, String>{

    public final static String LOG_TAG = ConnectionTask.class.getSimpleName();

    private MainActivity mainActivity = null;
    private String resultJSON = null;

    public ConnectionTask() {}
    public ConnectionTask(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }



    @Override
    protected String doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;



        try {

            // Constructing the URL
            URL url = new URL(params[0]);
            Log.d(LOG_TAG, "Constructed url: "+url);

            // Create the request and open connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.connect();

            // Read the input stream into a string
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                resultJSON = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty. No point in parsing.
                resultJSON = null;
            }
            resultJSON = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            resultJSON = null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        return resultJSON;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (mainActivity!=null) {
            mainActivity.displayUsers(resultJSON);
        }

    }
}
