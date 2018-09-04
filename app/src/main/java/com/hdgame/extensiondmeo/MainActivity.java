package com.hdgame.extensiondmeo;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private TextView tvMsg;

    private UnZipFileCallback callback = new UnZipFileCallback() {
        @Override
        public void onUnZipFileSuccess(String outputFilePath) {
            Log.i(TAG,"onUnZipFileSuccess:" + outputFilePath);
            File file = new File(outputFilePath);

            File[] files = file.listFiles();
            if(files == null)
                return;

            for (int i = 0; i < files.length; i++) {
                Log.i(TAG,"fileName:" + files[i].getName());
            }
        }

        @Override
        public void onUnZipFileFail(String msg) {
            Log.i(TAG,"onUnZipFileFail:" + msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvMsg = findViewById(R.id.tv_msg);

        //判断是否复制过拓展资源文件
        String fileDir = getFilesDir().getAbsolutePath() + File.separator + "assets";
        File resFile = new File(fileDir + File.separator + "bgm.mp3");
        if(!resFile.exists()){
            tvMsg.setText("resFile not exists,unzip obb file to internal sdcard...");
            new UnZipFileTask(this,fileDir,callback).execute();
        }else{
            tvMsg.setText("resFile is exists." + resFile.getAbsolutePath());
            //拓展资源文件已经复制过
            Log.i(TAG,"resFile is exists." + resFile.getAbsolutePath());
        }

    }

    public class UnZipFileTask extends AsyncTask<Void,Void,Boolean>{
        private Context context;
        private String outputFilePath;
        private UnZipFileCallback callback;

        public UnZipFileTask(Context context,String outputFilePath,UnZipFileCallback callback){
            this.context = context;
            this.outputFilePath = outputFilePath;
            this.callback = callback;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            String obbFilePath = ExtensionsFileUtil.getObbFilePath(context);
            if (!new File(obbFilePath).exists()) {
                tvMsg.setText("obb file not exists,may be deleted.");
                return false;
            }
            try {
                ExtensionsFileUtil.unZipObbFile(context, outputFilePath);
            } catch (IOException e) {
                e.printStackTrace();
                callback.onUnZipFileFail(e.getMessage());
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean flag) {
            super.onPostExecute(flag);
            if(flag) {
                callback.onUnZipFileSuccess(outputFilePath);
                tvMsg.setText("unzip obb file success," + outputFilePath);
            }
        }


    }

    public interface UnZipFileCallback{
        void onUnZipFileSuccess(String outputFilePath);
        void onUnZipFileFail(String msg);
    }
}
