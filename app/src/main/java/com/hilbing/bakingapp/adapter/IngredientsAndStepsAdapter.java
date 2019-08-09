package com.hilbing.bakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hilbing.bakingapp.R;
import com.hilbing.bakingapp.activities.RecipeStepActivity;
import com.hilbing.bakingapp.fragment.RecipeStepFragment;
import com.hilbing.bakingapp.model.Ingredient;
import com.hilbing.bakingapp.model.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsAndStepsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static String TAG = "Ingredients and Steps";

    private List<Object> data;
    private static final int INGREDIENT = 0;
    private static final int STEP = 1;
    public boolean isTwoPane;
    private Context mContext;
    private StepClickListener mListener;

    private int selected_position = 0;

    public interface StepClickListener{
        void onStepClick(Step step);
    }

    public IngredientsAndStepsAdapter(Context context,  List<Object> data, boolean isTwoPane, StepClickListener listener) {
        this.isTwoPane = isTwoPane;
        this.mContext = context;
        this.mListener = listener;
        this.data = data;
    }

    @Override
    public int getItemViewType(int position) {
        if (data.get(position) instanceof Ingredient) {
            return INGREDIENT;
        } else if (data.get(position) instanceof Step) {
            return STEP;
        }

        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        final RecyclerView.ViewHolder viewHolder;
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case INGREDIENT:
                View ingredientView = layoutInflater.inflate(R.layout.item_ingredient, viewGroup, false);
                viewHolder = new IngredientViewHolder(ingredientView);
                break;
            case STEP:
                View stepView = layoutInflater.inflate(R.layout.item_step, viewGroup, false);
                viewHolder = new StepViewHolder(stepView);
                break;
            default:
                View view = layoutInflater.inflate(R.layout.item_step, viewGroup, false);
                viewHolder = new StepViewHolder(view);
                break;
        }

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case INGREDIENT:
                IngredientViewHolder ingredientViewHolder = (IngredientViewHolder) holder;
                setIngredientViewHolder(ingredientViewHolder, position);
                break;
            case STEP:
                StepViewHolder stepViewHolder = (StepViewHolder) holder;
                setStepViewHolder(stepViewHolder, position);
                holder.itemView.setBackgroundColor(selected_position == position ? Color.GRAY : Color.TRANSPARENT);
                break;
            default:
                StepViewHolder defaultViewHolder = (StepViewHolder) holder;
                setStepViewHolder(defaultViewHolder, position);
                break;
        }

    }

    private void setIngredientViewHolder(IngredientViewHolder ingredientViewHolder, int position) {
        Ingredient ingredient = (Ingredient) data.get(position);
        if (ingredient != null) {
            ingredientViewHolder.ingredient.setText(ingredient.getIngredient());
            ingredientViewHolder.quantity.setText(String.valueOf(ingredient.getQuantity()));
            ingredientViewHolder.measure.setText(ingredient.getMeasure());
        }
    }

    private void setStepViewHolder(StepViewHolder stepViewHolder, int position) {
        Step step = (Step) data.get(position);
        if (step != null) {
            stepViewHolder.shortDescription.setText(step.getShortDescription());

        }

    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_ingredient)
        TextView ingredient;
        @BindView(R.id.tv_quantity)
        TextView quantity;
        @BindView(R.id.tv_measure)
        TextView measure;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_short_description)
        TextView shortDescription;
        @BindView(R.id.iv_play)
        ImageView play;

        public StepViewHolder(@NonNull final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                Step clickedDataItem = (Step) data.get(pos);
                mListener.onStepClick(clickedDataItem);
              //  shortDescription.setBackgroundResource(R.color.colorAccent);
                notifyItemChanged(selected_position);
                selected_position = getAdapterPosition();
                notifyItemChanged(selected_position);
            }

        }


    }

}
