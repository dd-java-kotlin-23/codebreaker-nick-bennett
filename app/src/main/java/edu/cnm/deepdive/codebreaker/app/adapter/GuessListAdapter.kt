package edu.cnm.deepdive.codebreaker.app.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import dagger.hilt.android.qualifiers.ActivityContext
import edu.cnm.deepdive.codebreaker.app.R
import edu.cnm.deepdive.codebreaker.app.databinding.ItemGuessBinding
import edu.cnm.deepdive.codebreaker.model.Guess
import jakarta.inject.Inject

class GuessListAdapter @Inject constructor(@ActivityContext context: Context) :
    ArrayAdapter<Guess>(context, R.layout.item_guess, mutableListOf<Guess>()) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    var showText = true
        set(value) {
            val changed = field != value
            field = value
            if (changed) {
                notifyDataSetChanged()
            }
        }

    override fun getItem(position: Int): Guess? =
        super.getItem(count - 1 - position)

    @SuppressLint("SetTextI18n")
    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        val guess = getItem(position) ?: throw IllegalArgumentException()
        val binding = if (convertView == null) {
            ItemGuessBinding.inflate(inflater, parent, false)
        } else {
            ItemGuessBinding.bind(convertView)
        }
        binding.guessNumber.text = (count - position).toString()
        if (showText) {
            binding.guessText.text = guess.text
        } else {
            binding.guessText.text = ""
            binding.guessText.visibility = View.GONE
        }
        binding.exactMatches.text = guess.exactMatches.toString()
        binding.nearMatches.text = guess.nearMatches.toString()
        return binding.root
    }

}