package shenkar.koruApps.PackChetApp.objects;

import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import shenkar.koruApps.PackChetApp.R;

public class MessageAdapter extends FirebaseListAdapter<ChatMessage> {

    private FragmentActivity activity;



    public MessageAdapter(FragmentActivity activity, Class<ChatMessage> modelClass, int item_in_message, DatabaseReference ref) {
        super(activity, modelClass, item_in_message, ref);
        this.activity = activity;


    }


    @Override
    protected void populateView(View v, ChatMessage model, int position) {

        TextView messageText = (TextView) v.findViewById(R.id.message_text);
        TextView messageUser = (TextView) v.findViewById(R.id.message_user);
        TextView messageTime = (TextView) v.findViewById(R.id.message_time);

        messageText.setText(model.getMessageText());
        messageUser.setText(model.getMessageUser());

        // Format the date before showing it
        messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ChatMessage chatMessage = getItem(position);

        if (chatMessage.getMessageUserId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
            view = activity.getLayoutInflater().inflate(R.layout.item_out_message, viewGroup, false);
        else
            view = activity.getLayoutInflater().inflate(R.layout.item_in_message, viewGroup, false);
        //generating view
        populateView(view, chatMessage, position);
        return view;
    }

    @Override
    public int getViewTypeCount() {
        // return the total number of view types. this value should never change
        // at runtime
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        // return a value between 0 and (getViewTypeCount - 1)
        return position % 2;
    }
}
