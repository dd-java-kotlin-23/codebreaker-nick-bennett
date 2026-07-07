package edu.cnm.deepdive.codebreaker.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import dagger.hilt.android.qualifiers.ActivityContext;
import edu.cnm.deepdive.codebreaker.app.databinding.ItemIncompleteGameBinding;
import edu.cnm.deepdive.codebreaker.app.model.entity.IncompleteGame;
import jakarta.inject.Inject;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class IncompleteGameAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private final DateTimeFormatter dateFormatter;
  private final NumberFormat numberFormatter;
  private final LayoutInflater inflater;
  private final List<IncompleteGame> games;

  @Inject
  IncompleteGameAdapter(@ActivityContext Context context) {
    dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
    numberFormatter = NumberFormat.getNumberInstance();
    inflater = LayoutInflater.from(context);
    games = new ArrayList<>();
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new Holder(ItemIncompleteGameBinding.inflate(inflater, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    ((Holder) holder).bind(position);
  }

  @Override
  public int getItemCount() {
    return games.size();
  }

  public void clear() {
    games.clear();
  }

  public void addAll(Collection<IncompleteGame> games) {
    this.games.addAll(games);
  }

  private class Holder extends RecyclerView.ViewHolder {

    private final ItemIncompleteGameBinding binding;

    private Holder(ItemIncompleteGameBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    private void bind(int position) {
      IncompleteGame game = games.get(position);
      binding.updated.setText(dateFormatter.format(game.getUpdated()));
      binding.codeLength.setText(numberFormatter.format(game.getCodeLength()));
      binding.poolSize.setText(numberFormatter.format(game.getPoolSize()));
      binding.guessCount.setText(numberFormatter.format(game.getGuessCount()));
      binding.exactMatches.setText(numberFormatter.format(game.getExactMatches()));
      binding.nearMatches.setText(numberFormatter.format(game.getNearMatches()));
      // TODO: 7/7/26 Set listeners for long-press and/or icon clicks.
    }

  }

}
