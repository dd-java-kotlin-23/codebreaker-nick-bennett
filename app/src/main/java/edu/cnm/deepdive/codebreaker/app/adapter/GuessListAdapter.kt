package edu.cnm.deepdive.codebreaker.app.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import edu.cnm.deepdive.codebreaker.app.R
import edu.cnm.deepdive.codebreaker.app.databinding.ItemGuessBinding
import edu.cnm.deepdive.codebreaker.model.Guess

class GuessListAdapter(context: Context, guesses: List<Guess>) :
    ArrayAdapter<Guess>(context, R.layout.item_guess, guesses) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    @SuppressLint("SetTextI18n")
    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        val binding = if (convertView == null) {
            ItemGuessBinding.inflate(inflater, parent, false)
        } else {
            ItemGuessBinding.bind(convertView)
        }
        binding.guessNumber.text = (position + 1).toString()
    }

}