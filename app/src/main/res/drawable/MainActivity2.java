package com.example.mediapl;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {

    private ListView listViewSongs;

    private String[] songNames;
    private String[] trackArtists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2); // Убедитесь, что это ваш файл разметки

        listViewSongs = findViewById(R.id.listViewSongs);

        // Получаем названия песен и исполнителей из ресурсов
        songNames = new String[]{
                getString(R.string.song_1),
                getString(R.string.song_2),
                getString(R.string.song_3),
                getString(R.string.song_4),
                getString(R.string.song_5),
                getString(R.string.song_6),
                getString(R.string.song_7),
                getString(R.string.song_8),
                getString(R.string.song_9),
                getString(R.string.song_10)
        };

        trackArtists = new String[]{
                getString(R.string.artist_1),
                getString(R.string.artist_2),
                getString(R.string.artist_3),
                getString(R.string.artist_4),
                getString(R.string.artist_5),
                getString(R.string.artist_6),
                getString(R.string.artist_7),
                getString(R.string.artist_8),
                getString(R.string.artist_9),
                getString(R.string.artist_10)
        };

        // Создаем адаптер для ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, songNames);
        listViewSongs.setAdapter(adapter);

        // Обработка кликов на элементы списка
        listViewSongs.setOnItemClickListener((AdapterView<?> parent, android.view.View view, int position, long id) -> {
            // Переходим в MainActivity с выбранным треком
            Intent intent = new Intent(MainActivity2.this, MainActivity.class);
            intent.putExtra("selectedTrackIndex", position); // Передаем индекс выбранного трека
            setResult(RESULT_OK, intent);
            finish(); // Закрыть эту активность
        });
    }
}
