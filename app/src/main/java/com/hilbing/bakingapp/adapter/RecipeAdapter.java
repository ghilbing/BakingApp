package com.hilbing.bakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hilbing.bakingapp.R;
import com.hilbing.bakingapp.activities.RecipeDetailActivity;
import com.hilbing.bakingapp.model.Recipe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.MyViewHolder> {

    private Context mContext;
    private List<Recipe> recipes;
    private RecipeClickListener mListener;

    private static final String RECIPE_KEY = "Recipe";

    public interface RecipeClickListener{
        void onRecipeClick(Recipe recipe);
    }

    public RecipeAdapter(Context mContext, RecipeClickListener listener, List<Recipe> recipes) {
        this.mContext = mContext;
        this.mListener = listener;
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_recipe, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Recipe recipe = recipes.get(i);
        myViewHolder.recipeName.setText(recipe.getName());
        switch (recipe.getId()){
            case 1:
                myViewHolder.recipeImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.nutella_pie));
                break;
            case 2:
                myViewHolder.recipeImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.brownies));
                break;
            case 3:
                myViewHolder.recipeImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.yellow_cake));
                break;
            case 4:
                myViewHolder.recipeImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.cheese_cake));
                break;
        }

    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_recipeImage)
        ImageView recipeImage;
        @BindView(R.id.tv_recipeName)
        TextView recipeName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION){
                Recipe recipeItem = recipes.get(pos);
                mListener.onRecipeClick(recipeItem);

//                Intent intent = new Intent(mContext, RecipeDetailActivity.class);
//                intent.putExtra(RECIPE_KEY, recipeItem);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                mContext.startActivity(intent);


                }
        }
    }
}
