package com.example.michael.cal;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import com.example.michael.cal.CalNetwork.PostData;
import com.example.michael.cal.CalSQL.CalSqlAdapter;
import com.example.michael.cal.CalSQL.CalSQLObj;

import org.apache.http.HttpResponse;
import java.util.concurrent.ExecutionException;

public class dataService extends Service {
    private IBinder mBinder = new dataBinder();
    CalSqlAdapter calSqlAdapter;

    public dataService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        calSqlAdapter = MainActivity.getAdapter();
        if(calSqlAdapter==null)
            calSqlAdapter = MainActivity.setAdapter(new CalSqlAdapter(this));
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class dataBinder extends Binder {
        dataService getService() {
            return dataService.this;
        }
    }

    private class backgroundTask extends AsyncTask<Integer,Integer,Integer> {
        long timestamp=System.currentTimeMillis();
        @Override
        protected Integer doInBackground(Integer... params) {
            return submitData();
        }

        @Override
        protected void onPostExecute(Integer result) {
            Log.i("Daniel","Finished AsyncTask in "+(System.currentTimeMillis()-timestamp));
        }

        @Override
        protected void onPreExecute() {
            Log.i("Daniel","Starting AsyncTask");
        }

        @Override
        protected void onProgressUpdate(Integer... values) { }

        private int submitData() {
            long timestamp = System.currentTimeMillis();
            CalSQLObj[] cso = calSqlAdapter.getRangeData(0, System.currentTimeMillis());                //Submits localDatabase to server
            String json = calSqlAdapter.createJSONObjWithEmail(cso).toString();
            try {
                HttpResponse httpr = new PostData.PostDataTask().execute(new PostData.PostDataObj("http://grantuy.com/cal/insert.php", json)).get();
                if (httpr != null) {
                    if (httpr.getStatusLine().getStatusCode() == 200) {                                 //Request successful
                        Log.i("Daniel","Time to push"+(System.currentTimeMillis()-timestamp));
                        return clearDatabase();
                    }
                }
                Log.i("DataBase:", "There was a problem with this HTTP requst.");                       //Request failed

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return -1;
        }
    }

    public void submitData() {
        backgroundTask bt = new backgroundTask();
        bt.execute();
    }

    public int clearDatabase(){
        return calSqlAdapter.delDbData();
    }

}
