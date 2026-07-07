package edu.cnm.deepdive.codebreaker.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import edu.cnm.deepdive.codebreaker.app.databinding.ItemIncompleteGameBinding;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class IncompleteGameAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private final DateTimeFormatter dateFormatter;
  private final NumberFormat numberFormatter;
  private final LayoutInflater inflater;

  public IncompleteGameAdapter(Context context) {
    dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
    numberFormatter = NumberFormat.getNumberInstance();
    inflater = LayoutInflater.from(context);
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new Holder(ItemIncompleteGameBinding.inflate(inflater, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

  }

  @Override
  public int getItemCount() {
    return 0;
  }

  private class Holder extends RecyclerView.ViewHolder {

    private final ItemIncompleteGameBinding binding;

    private Holder(ItemIncompleteGameBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

  }

}
