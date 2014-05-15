package com.example.rememberwhen;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.glass.app.Card;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jgluck on 5/15/14.
 */
public class RememberPhotoBundle extends Activity {


    SharedPreferences prefs;
    private List<Card> mCards;
    private CardScrollView mCardScrollView;


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        prefs = this.getSharedPreferences(
                "com.example.rememberwhen", Context.MODE_PRIVATE);
        createCards();

        mCardScrollView = new CardScrollView(this);
        ExampleCardScrollAdapter adapter = new ExampleCardScrollAdapter();
        mCardScrollView.setAdapter(adapter);
        mCardScrollView.activate();
        setContentView(mCardScrollView);

    }


    private void createCards() {
        mCards = new ArrayList<Card>();

        Card card;

        card = new Card(this);
        card.setText("This card has a footer.");
        card.setFootnote("I'm the footer!");
        mCards.add(card);

        card = new Card(this);
        card.setText("This card has a cute background image.");
        card.setFootnote("How can you resist?");
        card.setImageLayout(Card.ImageLayout.FULL);
        card.addImage(R.drawable.kent1);
        mCards.add(card);

        card = new Card(this);
        card.setText("This card has a mosaic of awesome.");
        card.setFootnote("Aren't they precious?");
        card.setImageLayout(Card.ImageLayout.LEFT);
        card.addImage(R.drawable.kent1);
        card.addImage(R.drawable.kent2);
        card.addImage(R.drawable.kent3);
        mCards.add(card);
    }

    private class ExampleCardScrollAdapter extends CardScrollAdapter {

        @Override
        public int getPosition(Object item) {
            return mCards.indexOf(item);
        }

        @Override
        public int getCount() {
            return mCards.size();
        }

        @Override
        public Object getItem(int position) {
            return mCards.get(position);
        }

        /**
         * Returns the amount of view types.
         */
        @Override
        public int getViewTypeCount() {
            return Card.getViewTypeCount();
        }

        /**
         * Returns the view type of this card so the system can figure out
         * if it can be recycled.
         */
        @Override
        public int getItemViewType(int position){
            return mCards.get(position).getItemViewType();
        }

        @Override
        public View getView(int position, View convertView,
                            ViewGroup parent) {
            return  mCards.get(position).getView(convertView, parent);
        }
    }

}


