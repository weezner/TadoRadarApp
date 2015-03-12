package de.tado.wiesner.tadoradarapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.tado.wiesner.tadoradarapp.connection.ConnectionParams;
import de.tado.wiesner.tadoradarapp.connection.ConnectionTask;
import de.tado.wiesner.tadoradarapp.json.ResultParser;
import de.tado.wiesner.tadoradarapp.model.AppUser;
import de.tado.wiesner.tadoradarapp.model.GeoMapScale;
import de.tado.wiesner.tadoradarapp.model.ResultJSON;


public class MainActivity extends ActionBarActivity {

    private final static String LOG_TAG = MainActivity.class.getSimpleName();
    private final static int BINS = 10;

    private final static boolean DEBUG = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConnectionTask connectionTask = new ConnectionTask(this);
        connectionTask.execute(ConnectionParams.TADO_SERVER+ConnectionParams.REL_POS_CONNECTOR+ConnectionParams.PARAMS);
    }

    @SuppressLint("NewApi")
    public void displayUsers(String resultJSONString) {

        if (resultJSONString!=null) {

            int displayHeight = getApplicationContext().getResources().getDisplayMetrics().heightPixels - 300; // -250px for Home image, - 50px padding
            int displayWidth = getApplicationContext().getResources().getDisplayMetrics().widthPixels;

            RelativeLayout layout = (RelativeLayout) findViewById(R.id.main_layout);

            ResultParser rp = new ResultParser();
            ResultJSON resultJSON = null;
            try {
                resultJSON = rp.readJsonStream(new ByteArrayInputStream(resultJSONString.getBytes(StandardCharsets.UTF_8)));
            } catch (IOException e) {
                e.printStackTrace();
            }

            GeoMapScale gms;
            if ((gms=resultJSON.getGeoMapScale())!=null) {
                double val0 = gms.getVal0();
                double val100 = gms.getVal100();
                double val50 = (val100+val0)/2;

                ((TextView) findViewById(R.id.degree_val_0)).setText(getString(R.string.degree,val0));
                ((TextView) findViewById(R.id.degree_val_50)).setText(getString(R.string.degree,val50));
                ((TextView) findViewById(R.id.degree_val_100)).setText(getString(R.string.degree,val100));

            }

            List<AppUser> users = resultJSON.getUsers();

            if (DEBUG) {
                // Creates user list for debugging
                users.clear();
                for (int i = 0; i <= 5; i++) {
                    for (int j=0;j<i;j++) {
                        users.add(new AppUser("N" + i+"-"+j, "N" + i+"-"+j, false, false, i * 20));
                    }
                }
            }

            // Divides users into bins of similiar distances
            HashMap<Integer, List<AppUser>> binnedUsers = new HashMap<Integer,List<AppUser>>();
            for (AppUser user : users) {
                int bin = (int) Math.round(user.getRelPos()/BINS);
                List<AppUser> userInBin = binnedUsers.get(bin);
                if (userInBin==null) {
                    userInBin= new LinkedList<AppUser>();

                }
                userInBin.add(user);
                binnedUsers.put(bin,userInBin);
            }

            // Loop through binned users groups
            RelativeLayout.LayoutParams lp;
            Iterator it = binnedUsers.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();

                int userCount =  binnedUsers.get(pair.getKey()).size();
                int binPos = ((int) pair.getKey())*BINS;

                if (userCount>=4) {

                    String username = getString(R.string.multi_user,userCount);
                    View userView = createUserView(R.layout.layout_multi_user_display,binPos,1,0,username,displayHeight,displayWidth);
                    layout.addView(userView);

                }
                else {
                    int currentUser = 0;

                    for (AppUser user : binnedUsers.get(pair.getKey())){

                        String username = user.getNickname();
                        View userView = createUserView(R.layout.layout_user_display,binPos,userCount,currentUser,username,displayHeight,displayWidth);
                        layout.addView(userView);
                        currentUser++;
                    }

                }



            }

        }

    }

    private View createUserView(int layoutId, int binPos, int userCount, int currentUser, String username, int displayHeight, int displayWidth) {

        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View userView = inflater.inflate(layoutId, null);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //TODO: Position hacked
        int top = (int) Math.floor(displayHeight - displayHeight * binPos / 100.0) + 25; // +25px padding
        int spacing = 150;
        int view_width=80;
        int left = ((int) Math.floor(displayWidth / 2.0 -(view_width/2.0)-(spacing/2.0)*(userCount-1)+currentUser*spacing));


        layoutParams.setMargins(left, top, 0, 0);
        userView.setLayoutParams(layoutParams);

        TextView textView = (TextView) userView.findViewById(R.id.user_display_username);
        textView.setText(username);

        return userView;


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
    }
}
