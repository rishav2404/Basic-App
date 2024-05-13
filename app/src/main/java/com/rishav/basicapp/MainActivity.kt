package com.rishav.basicapp


import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.search.SearchView
import com.rishav.basicapp.databinding.ActivityMainBinding
import com.rishav.basicapp.media.MediaActivity
import io.github.jan.supabase.BuildConfig
import io.github.jan.supabase.annotations.SupabaseInternal
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


class MainActivity() : AppCompatActivity(), OnQueryTextListener {

    val supabase = createSupabaseClient(
        supabaseUrl = "https://syzaktxwpunuiimbyyaa.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InN5emFrdHh3cHVudWlpbWJ5eWFhIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MTUzNDA5NDMsImV4cCI6MjAzMDkxNjk0M30.HdN6hORI0vHdDPdfPSO7rUpQIz9-aBO63uOtXQrwtxo"
    ) {
        install(Postgrest)
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var videoList: List<VideoItem>
    private lateinit var recyclerView:RecyclerView

    private var filteredVideoList: List<VideoItem> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        lifecycleScope.launch {
            videoList = withContext(Dispatchers.Default) {
                supabase.from("videoData").select().decodeList<VideoItem>()
            }
            recyclerView = binding.rvVideoItem
            val adapter = VideoAdapter(this@MainActivity, videoList)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)


            // search functionality
            binding.searchView.setOnQueryTextListener(this@MainActivity)
            binding.searchView.setOnCloseListener {
                filterList(null)
                true

            }


            adapter.setOnItemClickListener( object : VideoAdapter.onItemClickListener {
                override fun onItemClicking(position: Int) {
                    val i = Intent(this@MainActivity, MediaActivity::class.java)
                    i.putExtra("videoUrl", videoList[position].videoUrl)
                    startActivity(i)
                }
            })

        }


    }

    // search filtering
    private fun filterList(query: String?) {
        if (query.isNullOrEmpty()) {
            filteredVideoList = videoList
        } else {
            filteredVideoList = videoList.filter {
                it.videoTitle.contains(query, ignoreCase = true) ||
                        it.channelName.contains(query, ignoreCase = true)
            }
        }
        (recyclerView.adapter as VideoAdapter).updateList(filteredVideoList)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        filterList(query)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        filterList(newText)
        return true
    }


}

