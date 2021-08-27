package com.example.youtubeinterntask

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import kotlinx.android.synthetic.main.activity_video_player.*
import org.json.JSONArray
import org.json.JSONObject

class VideoPlayer : YouTubeBaseActivity() {

    val api_key =  "AIzaSyADAHGbNfdtv4AQQ3iSMNS7fYhvb5R6w_4"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        val obj = intent.getStringExtra("VideoID")
        videoTitle.text = intent.getStringExtra("VideoTitle")
        video_Desc.text = intent.getStringExtra("VideoDescription")

        val ytPlayer = findViewById<YouTubePlayerView>(R.id.ytPlayer)
        ytPlayer.initialize(api_key,object :YouTubePlayer.OnInitializedListener{
            override fun onInitializationSuccess(
                p0: YouTubePlayer.Provider?,
                p1: YouTubePlayer?,
                p2: Boolean,
            ) {
                p1?.loadVideo(obj)
                p1?.play()
            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?,
            ) {
                Toast.makeText(this@VideoPlayer, "Video player Failed", Toast.LENGTH_SHORT).show()
            }
        })

        val url = "https://www.googleapis.com/youtube/v3/videos?part=statistics&id=$obj&key=AIzaSyADAHGbNfdtv4AQQ3iSMNS7fYhvb5R6w_4"
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->

                val stringResponse = response.toString()
                val jsonObj = JSONObject(stringResponse)
                val jsonArray: JSONArray = jsonObj.getJSONArray("items")

                for (i in 0 until jsonArray.length()) {
                    val statJsonObject = jsonArray.getJSONObject(i).getString("statistics")
                    val statjsonObj = JSONObject(statJsonObject)
                    val like = statjsonObj.getString("likeCount")
                    val Views = statjsonObj.getString("viewCount")
                    video_views.setText(Views).toString()
                    Video_likes.setText(like).toString()
                }
            },
            { error ->
                //Log.e(ContentValues.TAG, "onCreate: $error ")
                Toast.makeText(this, ""+error, Toast.LENGTH_SHORT).show()
            }
        )
        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    fun shareVideo(view: View) {
        val i = Intent(Intent.ACTION_SEND)
        i.type = "text/plain"
        val obj = intent.getStringExtra("VideoID")
        val currentUrl = "https://www.youtube.com/watch?v=$obj"
        i.putExtra(Intent.EXTRA_TEXT, "Hi, checkout this Video $currentUrl")
        startActivity(Intent.createChooser(i, "Share this Video with"))
    }
}