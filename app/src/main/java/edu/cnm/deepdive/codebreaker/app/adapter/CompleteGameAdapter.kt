package edu.cnm.deepdive.codebreaker.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import edu.cnm.deepdive.codebreaker.app.model.entity.CompleteGame
import jakarta.inject.Inject
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class CompleteGameAdapter @Inject constructor(context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dateFormatter: DateTimeFormatter =
        DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
    private val numberFormatter: NumberFormat = NumberFormat.getNumberInstance()
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val games: MutableList<CompleteGame> = ArrayList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

}