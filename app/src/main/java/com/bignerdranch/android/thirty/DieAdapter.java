package com.bignerdranch.android.thirty;

/**
 * Author: Annika Svedin
 * email: annika.svedin@gmail.com
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import java.util.ArrayList;

class DieAdapter extends RecyclerView.Adapter<DieAdapter.DieHolder> {

    private ArrayList<Die> dice;
    private Context context;

    DieAdapter(ArrayList<Die> dice, Context context) {
        this.dice = dice;
        this.context = context;
    }

    /**
     * Rolls every active die in the collection and update the view to display the new state.
     */
     void rollDice() {
        for (Die die : dice) {
            if (die.getActiveState() == 1) {
                die.rollDie();
                die.toggleActiveState();
                notifyItemChanged(dice.indexOf(die));
            }
        }
    }

    /**
     * Every active dice in the collection sets to disabled, which indicates they are not clickable,
     * updates the view.
     */
    void disableActiveDice() {
        for (Die die : dice) {
            if (die.getActiveState() == 1) {
                die.disableDie();
                notifyItemChanged(dice.indexOf(die));
            }
        }
    }

    /**
     *
     * @return the total avlue of the dice being Active.
     */
    int getTotalValue() {
        int totalValue = 0;

        for (Die die : dice) {
            if (die.getActiveState() == 1) {
                totalValue += die.getValue();
            }
        }
        return totalValue;
    }

    /**
     * Rolls all dice in the collection, sets their state to passive and updates the view.
     */
    void updateDiceView() {
        for (Die die : dice) {
            die.rollDie();
            die.setState(0);
            notifyItemChanged(dice.indexOf(die));
        }
    }

    @Override
    public DieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.die_view, parent, false);

        return new DieHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DieHolder holder, int position) {
        final Die die = dice.get(position);
        holder.die = die;
        holder.updateImage();
    }

    @Override
    public int getItemCount() {
        return dice.size();
    }


    class DieHolder extends RecyclerView.ViewHolder {
        ImageView dieView;
        RelativeLayout relativeLayout;
        Die die;


        private final Integer[] images_red_dice = { R.drawable.red1,
                R.drawable.red2,
                R.drawable.red3,
                R.drawable.red4,
                R.drawable.red5,
                R.drawable.red6 };

        private final Integer[] images_grey_dice = { R.drawable.grey1,
                R.drawable.grey2,
                R.drawable.grey3,
                R.drawable.grey4,
                R.drawable.grey5,
                R.drawable.grey6 };

        private final Integer[] images_white_dice = { R.drawable.white1,
                R.drawable.white2,
                R.drawable.white3,
                R.drawable.white4,
                R.drawable.white5,
                R.drawable.white6 };

        DieHolder(View view) {
            super(view);
            dieView = view.findViewById(R.id.die_view_image);

            relativeLayout = view.findViewById(R.id.relative_layout);
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    die.toggleActiveState();
                    updateImage();
                }
            });
        }

        void updateImage() {
            int state = die.getActiveState();

            switch(state) {
                case -1:
                    dieView.setImageResource(images_grey_dice[die.getValue() - 1]);
                    break;
                case 0:
                    dieView.setImageResource(images_white_dice[die.getValue() - 1]);
                    break;
                case 1:
                    dieView.setImageResource(images_red_dice[die.getValue() - 1]);
                    break;

                default:
                    throw new IllegalStateException("State not defined");

            }
        }
    }
}