package com.example.youtubeinterntask

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class MainActivity : AppCompatActivity(),ItemClicked{

    private lateinit var mAdapter: VideoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val search: Button = findViewById(R.id.search)
        search.setOnClickListener(View.OnClickListener {
            recyclerView.layoutManager = LinearLayoutManager(this)
            fetchVideoData()
            mAdapter = VideoAdapter(this)
            recyclerView.adapter = mAdapter
        })
    }

    private fun fetchVideoData() {

        val key = "AIzaSyADAHGbNfdtv4AQQ3iSMNS7fYhvb5R6w_4"
        val userInput: EditText = findViewById(R.id.search_vid)
        val q = userInput.text.toString()
        val token: String = ""
        val url1 = "https://www.googleapis.com/youtube/v3/search?part=snippet&pageToken=$token&maxResults=50&q=$q&type=video&key=$key"

        val jsonObjectRequest1 = JsonObjectRequest(Request.Method.GET, url1, null,
            { response ->
                try {
                    val stringResponse = response.toString()
                    val jsonObj = JSONObject(stringResponse)
                    val videoArray = ArrayList<Video>()

                    if (response.has("nextPageToken") && !response.isNull("prevPageToken") ){
                        jsonObj.getString("nextPageToken")
                        jsonObj.getString("prevPageToken")
                    }
                    val jsonArray: JSONArray = jsonObj.getJSONArray("items")
                    for (i in 0 until jsonArray.length()){
                        val idjsonObject = jsonArray.getJSONObject(i).getString("id")
                        val vididjsonObj = JSONObject(idjsonObject)

                        val sniJsonObject = jsonArray.getJSONObject(i).getString("snippet")
                        val statjsonObj = JSONObject(sniJsonObject)

                        val thumbjsonObj = statjsonObj.getJSONObject("thumbnails")
                        val medium = thumbjsonObj.getJSONObject("high")

                        val data = Video(
                            jsonObj.getString("nextPageToken"),
                            vididjsonObj.getString("videoId"),
                            statjsonObj.getString("title"),
                            statjsonObj.getString("description"),
                            medium.getString("url")
                        )
                        videoArray.add(data)
                    }
                    mAdapter.updateNews(videoArray)
                }catch(e:Exception){
                    Log.d(TAG, "fetchData: Error $e")
                }
            },
            { error ->
                Log.e(TAG, "onCreate: $error ")
            }
        )
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest1)
    }

    override fun onItemClicked(item: Video) {

        val intent = Intent(this, VideoPlayer::class.java)
            intent.putExtra("VideoID",item.ID)
            intent.putExtra("VideoTitle",item.Title)
            intent.putExtra("VideoDescription",item.description)
            startActivity(intent)
    }
}