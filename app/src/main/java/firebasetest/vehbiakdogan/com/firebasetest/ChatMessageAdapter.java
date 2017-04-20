package firebasetest.vehbiakdogan.com.firebasetest;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vehbiakdogan on 19.04.2017.
 */


class ChatMessageAdapter extends ArrayAdapter<ChatMessage> {

    private TextView chatText;
    private List<ChatMessage> chatMessageList = new ArrayList<ChatMessage>();
    private Context context;
    private ImageView mesajImg;

    @Override
    public void add(ChatMessage object) {
        chatMessageList.add(object);
        super.add(object);
    }
    @Override
    public void clear() {
        chatMessageList.clear();
        super.clear();
    }

    public ChatMessageAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;
    }

    public int getCount() {
        return this.chatMessageList.size();
    }

    public ChatMessage getItem(int index) {
        return this.chatMessageList.get(index);
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage chatMessageObj = getItem(position);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        View row = convertView;
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // mesaja ait UID bizim UID değil ise right xml kullanılacak.
        if (user.getUid() != chatMessageObj.getTokenID()) {
            row = inflater.inflate(R.layout.message_item_right, parent, false);
        }else{
            row = inflater.inflate(R.layout.mesaage_item_left, parent, false);
        }
        chatText = (TextView) row.findViewById(R.id.msgr);
        mesajImg = (ImageView) row.findViewById(R.id.msg_resim);
        chatText.setText(chatMessageObj.getMesaj());
        // mesaj eki varmı yokm kontrolü
        if(!chatMessageObj.getGetMessageImageUrl().equals("")) {
            Glide.with(getContext()).load(chatMessageObj.getGetMessageImageUrl()).into(mesajImg);
            mesajImg.setVisibility(View.VISIBLE);
        }

        return row;
    }
}