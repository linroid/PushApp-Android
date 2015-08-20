package com.linroid.pushapp.ui.auth;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.linroid.pushapp.R;
import com.linroid.pushapp.model.Auth;
import com.linroid.pushapp.ui.base.DataAdapter;
import com.linroid.pushapp.util.AndroidUtil;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by linroid on 8/15/15.
 */
public class AuthAdapter extends DataAdapter<Auth, AuthAdapter.AuthHolder> {
    Picasso picasso;
    OnActionListener listener;

    public AuthAdapter(Picasso picasso) {
        this.picasso = picasso;
    }

    public void setListener(OnActionListener listener) {
        this.listener = listener;
    }

    @Override
    public AuthHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_auth, parent, false);
        return new AuthHolder(view);
    }

    @Override
    public void onBindViewHolder(AuthHolder holder, int position) {
        Auth auth = data.get(position);

        Resources res = holder.nicknameTV.getResources();
        picasso.load(auth.getUser().getAvatar()).into(holder.avatarIV);
        holder.nicknameTV.setText(auth.getUser().getNickname());
        holder.authTimeTV.setText(res.getString(R.string.txt_auth_time,
                AndroidUtil.friendlyTime(auth.getCreatedAt())));
    }

    class AuthHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.avatar_iv)
        ImageView avatarIV;
        @Bind(R.id.nickname_tv)
        TextView nicknameTV;
        @Bind(R.id.auth_time_tv)
        TextView authTimeTV;
        @Bind(R.id.revoke_btn)
        Button revokeBtn;

        public AuthHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.revoke_btn)
        void onRevokeBtnClicked(Button btn) {
            if (listener != null) {
                Auth auth = data.get(getAdapterPosition());
                listener.onRevoke(getAdapterPosition(), auth);

//                new AlertDialog.Builder(btn.getContext())
//                        .setTitle(btn.getResources().getString(R.string.msg_dialog_title_revoke))
//                        .setMessage(btn.getResources().getString(R.string.msg_dialog_content_revoke, auth.getUser().getNickname()))
//                        .setNegativeButton(android.R.string.cancel, null)
//                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                                listener.onRevoke(getAdapterPosition(), auth);
//                            }
//                        }).show();
            }
        }
    }

    public interface OnActionListener {
        void onRevoke(int position, Auth auth);
    }

}
