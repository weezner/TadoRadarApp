package de.tado.wiesner.tadoradarapp;

import android.annotation.SuppressLint;
import android.test.AndroidTestCase;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import de.tado.wiesner.tadoradarapp.connection.ConnectionParams;
import de.tado.wiesner.tadoradarapp.connection.ConnectionTask;
import de.tado.wiesner.tadoradarapp.json.ResultParser;
import de.tado.wiesner.tadoradarapp.model.AppUser;
import de.tado.wiesner.tadoradarapp.model.ResultJSON;

/**
 * Created by wiesner on 12.03.15.
 */
public class testConnection extends AndroidTestCase {

    @SuppressLint("NewApi")
    public void testCreateDB() throws Throwable {

        String resultsString = new ConnectionTask().execute(ConnectionParams.TADO_SERVER+ConnectionParams.REL_POS_CONNECTOR+"?username=apptesthome&password=test").get();
        assertTrue(resultsString!=null && resultsString!="" && resultsString.startsWith("{\"success\":"));

        ResultParser rp = new ResultParser();
        ResultJSON resultJSON = rp.readJsonStream(new ByteArrayInputStream(resultsString.getBytes(StandardCharsets.UTF_8)));
        List<AppUser> users = resultJSON.getUsers();
        assertTrue(users !=null && users.size()>0);


    }

}
