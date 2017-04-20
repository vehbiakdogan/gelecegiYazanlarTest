package firebasetest.vehbiakdogan.com.firebasetest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by vehbiakdogan on 20.04.2017.
 */

public class TimelineAdapter extends ArrayAdapter<Timeline> {

    private TextView  userName, postMessage, likeMessage ;
    private Button likeButton;
    private List<Timeline> timelineList = new ArrayList<Timeline>();
    private Context context;
    private ImageView postImg;
    private CircleImageView userProfileImg;

    @Override
    public void add(Timeline object) {
        timelineList.add(object);
        super.add(object);
    }
    @Override
    public void clear() {
        timelineList.clear();
        super.clear();
    }

    public TimelineAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;
    }

    public int getCount() {
        return this.timelineList.size();
    }

    public Timeline getItem(int index) {
        return this.timelineList.get(index);
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        Timeline tmObj = getItem(position);
        View row = convertView;
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = inflater.inflate(R.layout.item_post,parent,false);

        userName = (TextView) row.findViewById(R.id.user_name);
        userProfileImg = (CircleImageView) row.findViewById(R.id.user_image);
        postMessage = (TextView) row.findViewById(R.id.post_message);
        postImg = (ImageView) row.findViewById(R.id.post_message_image);
        likeMessage = (TextView) row.findViewById(R.id.like_count);
        likeButton = (Button) row.findViewById(R.id.like_btn);




        userName.setText(tmObj.getUserName());
        Glide.with(getContext()).load(tmObj.getUserImg()).into(userProfileImg);
        likeMessage.setText(tmObj.getLikeCount()+" Beğenme");

        if(tmObj.getPostName().equals("")) {
            postMessage.setVisibility(View.GONE);
        }else {
            postMessage.setText(tmObj.getPostName());
            postMessage.setVisibility(View.VISIBLE);
        }
        if(tmObj.getPostImgPath().equals("")) {
            postImg.setVisibility(View.GONE);
        }else {
            postImg.setVisibility(View.VISIBLE);
            Glide.with(getContext()).load(tmObj.getPostImgPath()).into(postImg);
        }


        return row;
    }
}