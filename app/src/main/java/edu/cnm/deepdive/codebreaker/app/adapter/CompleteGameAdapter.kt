package edu.cnm.deepdive.codebreaker.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import edu.cnm.deepdive.codebreaker.app.R
import edu.cnm.deepdive.codebreaker.app.databinding.ItemCompleteGameBinding
import edu.cnm.deepdive.codebreaker.app.model.entity.CompleteGame
import jakarta.inject.Inject
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.math.roundToInt

private const val MILLISECONDS_PER_SECOND = 1000.0
private const val MINUTES_PER_SECOND = 60

class CompleteGameAdapter @Inject constructor(context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dateFormatter: DateTimeFormatter =
        DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
    private val numberFormatter: NumberFormat = NumberFormat.getNumberInstance()
    private val elapsedTimeFormat: String = context.getString(R.string.elapsed_time_format)
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val games: MutableList<CompleteGame> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        Holder(ItemCompleteGameBinding.inflate(inflater, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        (holder as Holder).bind(position)

    override fun getItemCount(): Int =
        games.size

    // TODO: Add functions to clear & populate the list of games.

    private inner class Holder(private val binding: ItemCompleteGameBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val game = games[position]
            binding.rank.text = numberFormatter.format(position + 1)
            binding.updated.text = dateFormatter.format(game.completed)
            binding.guessCount.text = numberFormatter.format(game.guessCount)
            var seconds = (game.elapsedTime.toDouble() / MILLISECONDS_PER_SECOND).roundToInt()
            val minutes = seconds / MINUTES_PER_SECOND
            seconds %= MINUTES_PER_SECOND
            binding.elapsedTime.text = elapsedTimeFormat.format(minutes, seconds)
        }

    }

}