package com.example.riot.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.riot.R
import com.example.riot.api.RiotApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.lifecycle.Observer
import com.example.riot.data.MatchData
import java.io.Serializable
import com.example.riot.data.Stats

class MainActivity : AppCompatActivity() {

    private lateinit var riotApi: RiotApi
    private lateinit var riotAdapter: RiotAdapter
    private lateinit var matchData: MatchData


    private lateinit var matchListAdapter: MatchListAdapter
    private lateinit var viewModel: MatchListViewModel

    private lateinit var statsAdapter: StatsAdapter

    private var nameMain: String? = null
    private var passedMatches: List<MatchData> = emptyList()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up RecyclerView and adapter
        var recyclerView: RecyclerView = findViewById(R.id.match_list)
        matchListAdapter = MatchListAdapter(this::onMatchClick)
        recyclerView.adapter = matchListAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)


        // Set up ViewModel
        riotApi = RiotApi()
        riotAdapter = RiotAdapter(riotApi)
        viewModel = MatchListViewModel(riotApi)
        viewModel.matchList.observe(this, Observer { matchList ->
            matchListAdapter.updateMatchList(matchList)
            passedMatches = matchList
        })

        val profilePic: ImageView = findViewById(R.id.pfp)
        val statsButton: ImageView = findViewById(R.id.statsButton)
        val line: TextView = findViewById(R.id.line)
        val recentlyPlayed: TextView = findViewById(R.id.recently_played)
        val riotTD: TextView = findViewById(R.id.riot_id)
        val searchView = findViewById<SearchView>(R.id.idSV)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                var searchQuery = query
                Log.d("Search Query", searchQuery)
                if (searchQuery == "a" || searchQuery == "A") {
                    searchQuery = "vasuleronesdevlo#69696"
                }
                riotTD.text = searchQuery
                profilePic.visibility = View.VISIBLE
                statsButton.visibility = View.VISIBLE
                line.visibility = View.VISIBLE
                recentlyPlayed.visibility = View.VISIBLE

                GlobalScope.launch(Dispatchers.Main) {
                    nameMain = searchQuery.split("#")[0]
                    try {
                        val matches = withContext(Dispatchers.IO) {
                            viewModel.updateMatchList(searchQuery.split("#")[0], searchQuery.split("#")[1])
                        }
                        Log.d("MainActivity", "Match history: $matches")
                    } catch (e: Exception) {
                        Log.e("MainActivity", "Error getting match history: ${e.message}", e)
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        statsButton.setOnClickListener {
            val intent = Intent(this, StatsActivity::class.java)
            intent.putExtra("EXTRA_NAME", nameMain)
<<<<<<< HEAD
            intent.putExtra(EXTRA_MATCH, passedMatches as Serializable)
=======
            if (::matchData.isInitialized) {
                intent.putExtra(EXTRA_MATCH, matchData as Serializable)
            }
>>>>>>> 00840c3552c0d71e8aba793ff544df918c411f1b
            startActivity(intent)
        }

        val riotLogoRelink = findViewById<ImageView>(R.id.riotLogoRelink)
        riotLogoRelink.setOnClickListener {
            val uri = Uri.parse("https://www.riotgames.com")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }

    private fun onMatchClick(matchData: MatchData) {
        // Handle click on match item
        Log.d("MainActivity", "Stats clicked: $matchData")

        // Create an intent to launch the MatchDetailsActivity
        val intent = Intent(this, MatchDetailActivity::class.java)
        intent.putExtra(EXTRA_MATCH, matchData as Serializable)
        startActivity(intent)
    }
    private fun onStatsClick(matchData: MatchData, nameMain: String) {
        // Handle click on stats button
        Log.d("MainActivity", "Stats clicked: $matchData, $nameMain")

        // Create an intent to launch the StatsActivity
        val intent = Intent(this, StatsActivity::class.java)
        intent.putExtra(EXTRA_MATCH, matchData as Serializable)
        intent.putExtra("EXTRA_NAME", nameMain)
        startActivity(intent)
    }
}




