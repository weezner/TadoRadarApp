package de.tado.wiesner.tadoradarapp.json;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import de.tado.wiesner.tadoradarapp.model.AppUser;
import de.tado.wiesner.tadoradarapp.model.GeoMapScale;
import de.tado.wiesner.tadoradarapp.model.ResultJSON;

/**
 * Created by wiesner on 12.03.15.
 */
public class ResultParser {

           public ResultJSON readJsonStream(InputStream in) throws IOException {
            JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
            try {
                return readMessage(reader);
            }
                finally{
                    reader.close();
                }
            }

        public ResultJSON readMessage(JsonReader reader) throws IOException {
            List<AppUser> users = null;
            GeoMapScale geoMapScale = null;

            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("appUsers")) {
                    users = readUserList(reader);
                }
                else if (name.equals("geoMapScale")) {
                    geoMapScale = readMapScale(reader);
                }
                else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            return new ResultJSON(users,geoMapScale);
      }



    private List<AppUser> readUserList(JsonReader reader) throws IOException{

        List users = new ArrayList();
        reader.beginArray();
        while (reader.hasNext()) {
            users.add(readUser(reader));
        }
        reader.endArray();
        return users;

    }

    public List readDoublesArray(JsonReader reader) throws IOException {
            List doubles = new ArrayList();

            reader.beginArray();
            while (reader.hasNext()) {
                doubles.add(reader.nextDouble());
            }
            reader.endArray();
            return doubles;
        }

        public AppUser readUser(JsonReader reader) throws IOException {
            String username = null;
            String nickname = null;
            boolean geoTrackingEnabled = false;
            boolean geoLocationStale = false;
            double relPos = -1;


            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("nickname")) {
                    nickname = reader.nextString();
                } else if (name.equals("username")) {
                    username = reader.nextString();
                }
                else if (name.equals("geoTrackingEnabled")) {
                    try {
                        geoTrackingEnabled = reader.nextBoolean();
                    }
                    catch(Exception e){
                        // Nothing to do
                    }

                }
                else if (name.equals("geoLocationStale")) {
                    try {
                        geoLocationStale = reader.nextBoolean();
                    }
                    catch(Exception e){
                        // Nothing to do
                    }
                }
                else if (name.equals("relativePosition")) {
                    try {

                        relPos = reader.nextDouble();



                    }
                    catch(Exception e){
                        // Nothing to do
                    }
                }
                else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            return new AppUser(nickname,username,geoTrackingEnabled,geoLocationStale,relPos);
        }

    public GeoMapScale readMapScale(JsonReader reader) throws IOException {
        double val0 = -1;
        double val100 = -1;



        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("0")) {
                val0 = reader.nextDouble();
            } else if (name.equals("100")) {
                val100 = reader.nextDouble();
            }
            else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new GeoMapScale(val0,val100);
    }


}
