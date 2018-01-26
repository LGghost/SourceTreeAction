package cn.order.ordereasy.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.UserInfo;
import cn.order.ordereasy.bean.XGNotification;
import cn.order.ordereasy.view.activity.PushActivity;

public class MessageReceiver extends XGPushBaseReceiver {
    public static final String LogTag = "TPushReceiver";
    private Context mContext;

    private void show(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    // 通知展示
    @Override
    public void onNotifactionShowedResult(Context context,
                                          XGPushShowedResult notifiShowedRlt) {
        if (context == null || notifiShowedRlt == null) {
            return;
        }
        XGNotification notific = new XGNotification();
        notific.setMsg_id(notifiShowedRlt.getMsgId());
        notific.setTitle(notifiShowedRlt.getTitle());
        notific.setContent(notifiShowedRlt.getContent());
        // notificationActionType==1为Activity，2为url，3为intent
        notific.setNotificationActionType(notifiShowedRlt
                .getNotificationActionType());
        // Activity,url,intent都可以通过getActivity()获得
        notific.setActivity(notifiShowedRlt.getActivity());
        notific.setUpdate_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(Calendar.getInstance().getTime()));
    }

    @Override
    public void onUnregisterResult(Context context, int errorCode) {
        if (context == null) {
            return;
        }
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            Log.e(LogTag, "反注册成功" + errorCode);
        } else {
            Log.e(LogTag, "反注册失败" + errorCode);
        }

    }

    @Override
    public void onSetTagResult(Context context, int errorCode, String tagName) {
        if (context == null) {
            return;
        }
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            Log.d(LogTag, "\"" + tagName + "\"设置成功");
        } else {
            Log.d(LogTag, "\"" + tagName + "\"设置失败,错误码：" + errorCode);
        }
    }

    @Override
    public void onDeleteTagResult(Context context, int errorCode, String tagName) {
        if (context == null) {
            return;
        }
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            Log.d(LogTag, "\"" + tagName + "\"删除成功");
        } else {
            Log.d(LogTag, "\"" + tagName + "\"删除失败,错误码：" + errorCode);
        }
    }

    // 通知点击回调 actionType=1为该消息被清除，actionType=0为该消息被点击
    @Override
    public void onNotifactionClickedResult(Context context,
                                           XGPushClickedResult message) {
        if (context == null || message == null) {
            return;
        }
        if (message.getActionType() == XGPushClickedResult.NOTIFACTION_CLICKED_TYPE) {//该消息被点击
            Log.e(LogTag, "msgId:" + message.getMsgId() + "title:" + message.getTitle() +
                    "customContent:" + message.getCustomContent() + "activityName:" + message.getActivityName());
//            try {
//            JSONObject jsonObject = new JSONObject(message.toString());
//            Intent noteList = new Intent(context, PushActivity.class);
//            noteList.putExtra("title", jsonObject.getString("title"));
//            noteList.putExtra("body", jsonObject.getString("body"));
//            noteList.putExtra("key", jsonObject.getString("key"));
//            noteList.putExtra("type", jsonObject.getInt("type"));
//            noteList.putExtra("order_id", jsonObject.getInt("order_id"));
//            context.startActivity(noteList);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        } else if (message.getActionType() == XGPushClickedResult.NOTIFACTION_DELETED_TYPE) {//该消息被清除
        }
    }

    @Override
    public void onRegisterResult(Context context, int errorCode,
                                 XGPushRegisterResult message) {
        // TODO Auto-generated method stub
        if (context == null || message == null) {
            return;
        }
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            // 在这里拿token
            Log.e(LogTag, "注册成功token：" + message.getToken());
        } else {
            Log.e(LogTag, message + "注册失败，错误码：" + errorCode);
        }

    }

    // 消息透传
    @Override
    public void onTextMessage(Context context, XGPushTextMessage message) {
        // TODO Auto-generated method stub
        Log.e(LogTag, "收到消息:" + message.toString());
//        try {
//            JSONObject jsonObject = new JSONObject(message.toString());
//            Intent noteList = new Intent(context, PushActivity.class);
//            noteList.putExtra("title", jsonObject.getString("title"));
//            noteList.putExtra("body", jsonObject.getString("body"));
//            noteList.putExtra("key", jsonObject.getString("key"));
//            noteList.putExtra("type", jsonObject.getInt("type"));
//            noteList.putExtra("order_id", jsonObject.getInt("order_id"));
//            context.startActivity(noteList);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

}
