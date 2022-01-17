package com.example.a220117;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listViewMp3;
    Button btnPlay, btnStop;
    TextView tvMp3;
    ProgressBar pbMp3;

    ArrayList<String> mp3List; //배열 선언
    String selectedMp3;

    // String mp3path = Environment.getExternalStorageDirectory().getPath() + "/";
    // => mp3 파일이 저장되어 있는 SD카드의 절대 경로를 지정할 문자열 변수
    MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("MP3 플레이어");

        //sd 카드에 음악을 집어 넣는다.
        ActivityCompat.requestPermissions(this, new String[]
                {Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);

        mp3List = new ArrayList<String>();
        File[] listFiles = new File(mp3path).listFiles(); //mp3path는 현재 주석 처리 중이다
        String fileName,extName;

        //mp3 파일의 목록을 만들어 주는 for문
        for(File file : listFiles) { //파일의 형식이 저장될 조건이 맞는지 검사하고 맞으면 리스트에게 파일을 추가
            fileName = file.getName();
            extName = fileName.substring(fileName.length());
            if(extName.equals((String) "mp3"))
                mp3List.add(fileName);
        }

        listViewMp3 = (ListView) findViewById(R.id.listViewMP3);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice, mp3List); // 한 개만 선택 가능하게 만들어 주겠다.
        listViewMp3.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listViewMp3.setAdapter(adapter);
        listViewMp3.setItemChecked(0, true);

        listViewMp3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // 배열의 첫 번째 값을 인덱스로 잡는다.
                selectedMp3 = mp3List.get(arg2); //사용자가 뭘 입력했는지 모르니까 임시로 arg2 => 정수형으로 받으니까 arg2가 되어야 됨
            }
        });

        selectedMp3 = mp3List.get(0);

        btnPlay = (Button) findViewById(R.id.btnPlay);
        btnStop = (Button) findViewById(R.id.btnStop);
        tvMp3 = (TextView) findViewById(R.id.tvMP3);
        pbMp3 = (ProgressBar) findViewById(R.id.pbMP3);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //이제 try catch문을 사용하자 => 예외처리 구문 사용
                try {
                    mPlayer = new MediaPlayer();
                    mPlayer.setDataSource(mp3path + selectedMp3); //mp3를 플레이 해줄 파일 이름
                    mPlayer.prepare(); // 여기 오류 있었는데 밑에 catch (IOException e) 들어가니까 사라졌음
                    btnPlay.setClickable(false);
                    btnStop.setClickable(true);
                    tvMp3.setText("플레이 되고 있는 음악: " + selectedMp3);
                    pbMp3.setVisibility(View.VISIBLE);
                } catch (IOException e) { }
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mPlayer.stop();
                mPlayer.reset();
                btnPlay.setClickable(true);
                btnStop.setClickable(false);
                tvMp3.setText("플레이 되고 있는 음악: ");
                pbMp3.setVisibility(View.INVISIBLE);
            }
        });
        btnStop.setClickable(false);

    }
}