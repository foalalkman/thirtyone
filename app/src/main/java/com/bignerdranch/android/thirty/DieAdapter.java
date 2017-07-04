package com.bignerdranch.android.thirty;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import java.util.ArrayList;


public class DieAdapter extends RecyclerView.Adapter<DieAdapter.DieHolder> {

    private ArrayList<Die> dice;
    private Context context;

    public DieAdapter(ArrayList<Die> dice, Context context) {
        this.dice = dice;
        this.context = context;
    }

    public void rollDice() {
        for (Die die : dice) {
            if (die.getActiveState() == 1) {
                die.rollDie();
                die.toggleActiveState();
                notifyItemChanged(dice.indexOf(die));
            }
        }
    }
    public void disableActiveDice() {
        for (Die die : dice) {
            if (die.getActiveState() == 1) {
                die.disableDie();
                notifyItemChanged(dice.indexOf(die));
            }
        }
    }

    public int getTotalValue() { // borde ligga i game
        int totalValue = 0;

        for (Die die : dice) {
            if (die.getActiveState() == 1) {
                totalValue += die.getValue();
            }
        }
        return totalValue; // gl;m inte cleara sen, passande st'lle
    }

    public void updateDiceView() {
        for (Die die : dice) {
            die.rollDie();
            die.setState(0); // set state to passive
            notifyItemChanged(dice.indexOf(die));
        }
    }

    @Override // ska skapa instans av DieHolder
    public DieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.die_view, parent, false);

        return new DieHolder(itemView);
    }

    @Override // ska binda data till DieHolder
    public void onBindViewHolder(DieHolder holder, int position) {
        final Die die = dice.get(position);
        holder.die = die;
        holder.updateImage();
    }

    @Override
    public int getItemCount() {
        return dice.size();
    }


    public class DieHolder extends RecyclerView.ViewHolder {
        public ImageView dieView;
        public RelativeLayout relativeLayout;
        public Die die;


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

        public DieHolder(View view) {
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

        public void updateImage() {
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