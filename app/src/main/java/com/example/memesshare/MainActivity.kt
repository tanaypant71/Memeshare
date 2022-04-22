package com.example.memesshare

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var currentImageURL: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadMeme()

    }

    private fun loadMeme(){
        progressBar.visibility = View.VISIBLE
        nextButton.isEnabled = false
        shareButton.isEnabled = false

        val url = "https://meme-api.herokuapp.com/gimme"

        // Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url,null,
            { response ->
               currentImageURL = response.getString("url")

                Glide.with(this).load(currentImageURL).listener(object: RequestListener<Drawable> {

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                            progressBar.visibility = View.GONE
                            nextButton.isEnabled = true
                            shareButton.isEnabled = true
                            return false
                    }
                }).into(memeImageView)

            },
            {
             Toast.makeText(this,"Something went wrong",Toast.LENGTH_LONG).show()
             })

        // Add the request to the RequestQueue.
    MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

    }
    fun shareMeme(view: View) {
    val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Hey , Check out this cool meme $currentImageURL")
        val chooser = Intent.createChooser(intent,"share memes using....")
        startActivity(chooser)
    }
    fun nextMeme(view: View) {
    loadMeme()
    }
}