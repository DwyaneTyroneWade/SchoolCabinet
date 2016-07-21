package com.xiye.schoolcabinet.delegates;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.xiye.schoolcabinet.MainActivity;
import com.xiye.schoolcabinet.R;
import com.xiye.schoolcabinet.base.BaseActivity;
import com.xiye.schoolcabinet.beans.Box;
import com.xiye.schoolcabinet.beans.CardInfo;
import com.xiye.schoolcabinet.beans.CardInfoBean;
import com.xiye.schoolcabinet.beans.RemoteBean;
import com.xiye.schoolcabinet.dispatcher.ActivityDispatcher;
import com.xiye.schoolcabinet.manager.ConfigManager;
import com.xiye.schoolcabinet.manager.MainBackgroundImageManager;
import com.xiye.schoolcabinet.manager.serialport.BoxActionManager;
import com.xiye.schoolcabinet.manager.serialport.BoxLogicManager;
import com.xiye.schoolcabinet.utils.ActivityStack;
import com.xiye.schoolcabinet.utils.SCConstants;
import com.xiye.schoolcabinet.utils.net.RequestFactory;
import com.xiye.sclibrary.base.L;
import com.xiye.sclibrary.utils.StringUtils;
import com.xiye.sclibrary.utils.ToastHelper;
import com.xiye.sclibrary.utils.Tools;

import java.io.OutputStream;
import java.util.List;

/**
 * Created by wushuang on 6/6/16.
 */
public class MainActivityDelegate {
    public static final String TAG = MainActivityDelegate.class.getSimpleName();
    private final long REMOTE_TIME = 1000;
    //    private OperationType type;
    private BaseActivity activity;
    private OutputStream mOutputStreamLock;
    private boolean hasGoVerifyId = false;//TODO 保证只有在从系统运行app（关闭-->开启）的时候直接进入管理员界面

    private RemoteFromBackstageCallback remoteFromBackstageCallback;

    //    public OperationType getType() {
//        return type;
//    }
//
//    public void setType(OperationType type) {
//        this.type = type;
//    }
    private boolean pauseRemote = false;
    private Handler mHandler = new Handler();
    private Runnable remoteRunnable = new Runnable() {
        @Override
        public void run() {
            if (!pauseRemote) {
                getRemoteFromBackstage();
            }
            mHandler.postDelayed(remoteRunnable, REMOTE_TIME);
        }
    };

    private MainActivityDelegate() {

    }

    /**
     * @param activity         不能为空
     * @param outputStreamLock 不能为空
     */
    public MainActivityDelegate(BaseActivity activity, OutputStream outputStreamLock, RemoteFromBackstageCallback remoteFromBackstageCallback) {
        if (activity == null) {
            throw new NullPointerException("activity null forbidden");
        }
        if (outputStreamLock == null) {
            //TODO dialog Tip
//            throw new NullPointerException("outputstream null forbidden");
        }
        this.activity = activity;
        this.mOutputStreamLock = outputStreamLock;
        this.remoteFromBackstageCallback = remoteFromBackstageCallback;
        BoxActionManager.getInstance().setOutputStreamLock(this.mOutputStreamLock);
    }

    /**
     * 如果没有验证过柜子的编号，进入管理员登录界面 ;如果验证过，那么去刷新一次所有数据
     *
     * @param callBack
     */
    public void getBasicData(ConfigManager.GetAllCardInfoCallBack callBack) {
        String cabinetId = ConfigManager.getCabinetId();
        if (Tools.isStringEmpty(cabinetId)) {
            if (!hasGoVerifyId) {
                Bundle extras = new Bundle();
                extras.putSerializable(SCConstants.BUNDLE_KEY_LOGIN_TYPE, SCConstants.LoginType.ADMIN);
                ActivityDispatcher.goLogin(activity, extras);
                hasGoVerifyId = true;
            }
        } else {
            //TODO 每次onResume都会去去一次数据，理论上来说是不对的，应该只在验证ID的地方取一次，其他情况需要服务器通知
            ConfigManager.getAllCardInfo(ConfigManager.getCabinetId(), activity, callBack);
        }
    }

    /**
     * 开始轮询远程开箱
     */
    public void startGetRemoteFromBackStage() {
        //TODO 每1s,去获取远程开箱的指令一次,后台应该注意，只发送一次，这样的指令，即1s之内完成指令的修改（或者当前端远程开箱之后，通知后台已经开箱了,另外，远程开箱 本地应该上传柜子的编号，确认是哪个柜子需要远程开箱）
        pauseRemote = false;
        mHandler.post(remoteRunnable);
    }

    /**
     * 暂停轮询远程开箱（实际上是暂停向服务器请求数据，轮询仍然在进行）
     */
    public void pauseGetRemoteFromBackStage() {
        pauseRemote = true;
    }

    /**
     * 请求远程开箱接口
     */
    private void getRemoteFromBackstage() {
        if (activity == null) {
            return;
        }
        activity.executeRequest(RequestFactory.getRemoteFromBackStageRequest(new Response.Listener<RemoteBean>() {
            @Override
            public void onResponse(RemoteBean remoteBean) {
                if (remoteBean != null) {
                    //notice
                    String notice = remoteBean.notice;
                    if (remoteFromBackstageCallback != null) {
                        remoteFromBackstageCallback.onNotice(notice);
                    }

                    String boxId = remoteBean.results;
                    L.d(TAG, "boxIdfake:" + boxId);
                    String cabinetId = ConfigManager.getCabinetId();
                    boxId = StringUtils.getRealBoxId(boxId, cabinetId);
                    L.d(TAG, "boxId:" + boxId);
                    if (!"00".equals(boxId)) {
                        BoxLogicManager.openBox(boxId);
                    }
                    //TODO 如果服务端不能在1s内完成修改，那么此时，应该停止对远程开箱接口的请求，等到开箱结束之后（这个开箱结束，怎么定义，如果定义为读取状态的话，有可能读取的时候，箱子又被关上了），给服务器发送远程开箱成功的通知，等待服务器返回修改成功的消息后，再开启对远程开箱接口的请求的轮询
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                activity.processVolleyError(volleyError);
            }
        }));
    }


    /**
     * 当有刷卡动作时，相应的操作，目前只能在主界面刷卡
     *
     * @param str
     */
    public void dealWithIC(String str) {
        str = StringUtils.deleteLineBreaks(str);
        L.d(TAG, "dealWithIC str:" + str);
        //判断CARDID,并判断身份
        if (Tools.isStringEmpty(str)) {
            ToastHelper.showShortToast(R.string.card_id_null);
            return;
        } else {
            //TODO test code
            ToastHelper.showShortToast("卡号:" + str);
        }

        CardInfo cardInfo = getCardInfoByCardId(str);
        //TODO 服务器应该返回int型的字段比如0代表管理员，或者admin代表管理员，或者给出所有的对应字符串,服务器目前返回的字符串不是UTF-8编码
        if (cardInfo == null) {
            ToastHelper.showShortToast(R.string.card_id_invalid);
            return;
        }

        if ("管理员".equals(cardInfo.identity)) {
            Bundle extras = new Bundle();
            //TODO cardInfo 传过去
            extras.putString(SCConstants.BUNDLE_KEY_CARD_ID, str);
            ActivityDispatcher.goAdmin(activity, extras);
        } else {
            if (ActivityStack.getInstance().currentActivity() instanceof MainActivity) {
                //TODO 开箱 学生最多2个箱子
                List<Box> boxList = cardInfo.box;
                if (boxList != null && boxList.size() > 0) {
                    for (Box box : boxList) {
                        if (box != null) {
                            String boxId = StringUtils.getRealBoxId(box.box_id, ConfigManager.getCabinetId());
                            BoxLogicManager.openBox(boxId);
                        }
                    }
                }
            } else {
                //TODO 目前只能在主界面进行刷卡操作
//                BroadCastDispatcher.sendOnICOutsideDataReceived(str, activity);
            }
        }
    }

    private CardInfo getCardInfoByCardId(String cardId) {
        CardInfoBean bean = ConfigManager.getCardInfoFromDB();
        CardInfo cardInfo = null;
        if (bean != null && bean.results != null) {
            List<CardInfo> cardInfos = bean.results.card_info;
            if (cardInfos != null && cardInfos.size() > 0) {
                for (int i = 0; i < cardInfos.size(); i++) {
                    if (cardInfos.get(i) != null) {
                        if (cardId.equals(cardInfos.get(i).card_id)) {
                            cardInfo = cardInfos.get(i);
                            break;
                        }
                    }
                }
            }
        }

        return cardInfo;
    }

    public Drawable getBg() {
        String url = MainBackgroundImageManager.getUrl();
        if (MainBackgroundImageManager.isMainBgExisted(url)) {
            String path = MainBackgroundImageManager.getMainBgFilePath(url);
            Drawable d = Drawable.createFromPath(path);
            return d;
        } else {
            return null;
        }
    }

    public interface RemoteFromBackstageCallback {
        void onNotice(String notice);
    }

//    private void doAction(String cardId) {
//        String cabinetId = getCabinetIdByCardId(cardId);
//        if (!Tools.isStringEmpty(cabinetId)) {
//            if (type != null) {
//                switch (type) {
//                    case SAVE:
//                    case GET:
//                        openLock(cabinetId);
//                        break;
//                    default:
//                        break;
//                }
//            }
//        }
//    }
//
//    private String getCabinetIdByCardId(String cardId) {
//        ArrayList<Card> list = DBManager.getInstance().getList();
//        if (list == null || list.size() <= 0) {
//            list = new CardTableManager().get();
//        }
//        if (list != null && list.size() > 0) {
//            for (int i = 0; i < list.size(); i++) {
//                if (list.get(i).cardId.equals(cardId)) {
//                    return list.get(i).cabinetId;
//                }
//            }
//        }
//
//        return "";
//    }

//    private void openLock(String cabinetId) {
//        sendCommand(obtainCommand(cabinetId));
//    }
//
//    private byte[] obtainCommand(String cabinetId) {
//        byte[] b = new byte[6];
//        int cabinetIdInt = Integer.valueOf(cabinetId);
//        int cabinetNo = 0;//柜子号
//        int lockNo = 0;//锁号
//        if (cabinetIdInt <= 4) {
//            cabinetNo = 1;
//            lockNo = cabinetIdInt - 4 * 0;
//        } else if (cabinetIdInt > 4 && cabinetIdInt <= 4 * 2) {
//            cabinetNo = 2;
//            lockNo = cabinetIdInt - 4 * 1;
//        } else if (cabinetIdInt > 4 * 2 && cabinetIdInt <= 4 * 3) {
//            cabinetNo = 3;
//            lockNo = cabinetIdInt - 4 * 2;
//        } else if (cabinetIdInt > 4 * 3 && cabinetIdInt <= 4 * 4) {
//            cabinetNo = 4;
//            lockNo = cabinetIdInt - 4 * 3;
//        } else if (cabinetIdInt > 4 * 4 && cabinetIdInt <= 4 * 5) {
//            cabinetNo = 5;
//            lockNo = cabinetIdInt - 4 * 4;
//        } else if (cabinetIdInt > 4 * 5 && cabinetIdInt <= 4 * 6) {
//            cabinetNo = 6;
//            lockNo = cabinetIdInt - 4 * 5;
//        } else if (cabinetIdInt > 4 * 6 && cabinetIdInt <= 4 * 7) {
//            cabinetNo = 7;
//            lockNo = cabinetIdInt - 4 * 6;
//        } else if (cabinetIdInt > 4 * 7 && cabinetIdInt <= 4 * 8) {
//            cabinetNo = 8;
//            lockNo = cabinetIdInt - 4 * 7;
//        } else if (cabinetIdInt > 4 * 8 && cabinetIdInt <= 4 * 9) {
//            cabinetNo = 9;
//            lockNo = cabinetIdInt - 4 * 8;
//        } else if (cabinetIdInt > 4 * 9 && cabinetIdInt <= 4 * 10) {
//            cabinetNo = 10;
//            lockNo = cabinetIdInt - 4 * 9;
//        } else if (cabinetIdInt > 4 * 10 && cabinetIdInt <= 4 * 11) {
//            cabinetNo = 11;
//            lockNo = cabinetIdInt - 4 * 10;
//        } else if (cabinetIdInt > 4 * 11 && cabinetIdInt <= 4 * 12) {
//            cabinetNo = 12;
//            lockNo = cabinetIdInt - 4 * 11;
//        } else if (cabinetIdInt > 4 * 12 && cabinetIdInt <= 4 * 13) {
//            cabinetNo = 13;
//            lockNo = cabinetIdInt - 4 * 12;
//        } else if (cabinetIdInt > 4 * 13 && cabinetIdInt <= 4 * 14) {
//            cabinetNo = 14;
//            lockNo = cabinetIdInt - 4 * 13;
//        }
//
//        if (lockNo == 1 || lockNo == 2) {
//            b[0] = (byte) ((cabinetNo - 1) * 2 + 1);
//        } else if (lockNo == 3 || lockNo == 4) {
//            b[0] = (byte) (cabinetNo * 2);
//        }
//        b[1] = (byte) 0xF2;
//        b[2] = (byte) 0x55;
//        if (lockNo % 2 == 0) {
//            b[3] = 0x04;
//        } else {
//            b[3] = 0x01;
//        }
//
//        // sum
//        int sum = 0;
//        for (int i = 0; i < 4; i++) {
//            sum += TypeUtil.byteToInt(b[i]);
//        }
//
//        if (sum > 255) {
//            b[4] = (byte) (sum >> 8);
//            L.d("high:" + TypeUtil.byteToInt(b[4]));
//            b[5] = (byte) ((sum << 8) >> 8);
//            L.d("low:" + TypeUtil.byteToInt(b[5]));
//        } else {
//            b[4] = (byte) 0x00;
//            b[5] = TypeUtil.intToByte(sum);
//        }
//
//        return b;
//    }
//
//    private void sendCommand(final byte[] b) {
//        Needle.onBackgroundThread().withTaskType(SCConstants.NEEDLE_TYPE_OPEN_LOCK)
//                .serially().execute(new Runnable() {
//
//            @Override
//            public void run() {
//                SerialDataSendHelper.getInstance().sendCommand(mOutputStreamLock, b);
//            }
//        });
//    }
//
//
//    public enum OperationType {
//        SAVE,
//        GET,
//    }
}
