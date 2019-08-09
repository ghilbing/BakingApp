package com.hilbing.bakingapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.hilbing.bakingapp.R;
import com.hilbing.bakingapp.model.Recipe;
import com.squareup.picasso.Picasso;

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

    public RecipeAdapter(Context mContext, RecipeClickListener listener) {
        this.mContext = mContext;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_recipe, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final Recipe recipe = recipes.get(i);
        myViewHolder.recipeName.setText(recipe.getName());
        switch (recipe.getId()){
            case 1:
                Picasso.get().load(R.drawable.nutella_pie).placeholder(R.drawable.placeholder).into(myViewHolder.recipeImage);
                break;
            case 2:
                Picasso.get().load(R.drawable.brownies).placeholder(R.drawable.placeholder).into(myViewHolder.recipeImage);
                break;
            case 3:
                Picasso.get().load(R.drawable.yellow_cake).placeholder(R.drawable.placeholder).into(myViewHolder.recipeImage);
                break;
            case 4:
                Picasso.get().load(R.drawable.cheese_cake).placeholder(R.drawable.placeholder).into(myViewHolder.recipeImage);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return recipes == null ? 0 : recipes.size();
    }

    public void setData(List<Recipe> recipeList){
        recipes = recipeList;
        notifyDataSetChanged();
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

                }
        }
    }
}
