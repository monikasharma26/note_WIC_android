package note.wic.FinalProject;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.config.Configuration;
import com.birbit.android.jobqueue.log.CustomLogger;
import com.facebook.stetho.Stetho;
import com.raizlabs.android.dbflow.config.FlowManager;


public class App extends Application {
    public static volatile Context CONTEXT;
    public static JobManager JOB_MANAGER;

    @Override
    public void onCreate()
    {
        super.onCreate();
        CONTEXT = getApplicationContext();
        FlowManager.init(this);
        Stetho.initializeWithDefaults(this);
        //Stetho.initializeWithDefaults(this);
        configureJobManager();
    }

    private void configureJobManager()
    {
        Configuration.Builder builder = new Configuration.Builder(this)
                .customLogger(new CustomLogger() {
                    private static final String TAG = "JOBS";

                    @Override
                    public boolean isDebugEnabled()
                    {
                        return true;
                    }

                    @Override
                    public void d(String text, Object... args)
                    {
                        Log.d(TAG, String.format(text, args));
                    }

                    @Override
                    public void e(Throwable t, String text, Object... args)
                    {
                        Log.e(TAG, String.format(text, args), t);
                    }

                    @Override
                    public void e(String text, Object... args)
                    {
                        Log.e(TAG, String.format(text, args));
                    }

                    @Override
                    public void v(String text, Object... args)
                    {

                    }
                })
                .maxConsumerCount(3)//up to 3 consumers at a time
                .loadFactor(3)//3 jobs per consumer
                .consumerKeepAlive(120);//wait 2 minute

        JOB_MANAGER = new JobManager(builder.build());
    }
}
