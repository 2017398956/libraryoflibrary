package com.nfl.libraryoflibrary.utils;

import android.app.Activity;

/**
 * Created by fuli.niu on 2017/3/27.
 * 语音转文字工具
 */

public class Voice2CharacterTool {

//    private Activity activity;
//    private SpeechRecognizer mIat;
//    private RecognizerDialog iatDialog;
//    private StringBuffer sb;// 用于拼接说话内容，识别完成后需要清空 ；
//    private String lastSpeechContent;// 用于保存 sb 的最终内容 ；
//    private Voice2CharacterListener voice2CharacterListener;

    private Voice2CharacterTool() {
    }

    public Voice2CharacterTool(Activity activity) {
//        this();
//        this.activity = activity;
//        // 此接口在非主进程调用会返回null对象，如需在非主进程使用语音功能，
//        // 请使用参数：SpeechConstant.APPID +"=12345678," + SpeechConstant.FORCE_LOGIN +"=true"。
//        SpeechUtility.createUtility(activity, SpeechConstant.APPID + "=58d8be6c");
//        // 创建SpeechRecognizer对象，第二个参数：本地听写时传 InitListener
//        mIat = SpeechRecognizer.createRecognizer(activity, initListener);
//        // 创建SpeechRecognizer对象，第二个参数：本地听写时传 InitListener
//        iatDialog = new RecognizerDialog(activity, initListener);
//        initIflytek();
    }

    /**
     * 初始化讯飞相关参数
     */
    private void initIflytek() {
//        //2.设置听写参数，详见《科大讯飞MSC API手册(Android)》SpeechConstant类
//        mIat.setParameter(SpeechConstant.DOMAIN, "iat");
//        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
//        mIat.setParameter(SpeechConstant.ACCENT, "mandarin ");
//        // 设置回调接口
//        iatDialog.setListener(recognizerDialogListener);
    }

    //听写监听器
//    private RecognizerListener mRecoListener = new RecognizerListener() {
//
//        //开始录音
//        public void onBeginOfSpeech() {
//            sb = new StringBuffer();
//            iatDialog.show();
//        }
//
//        // 听写结果回调接口(返回Json格式结果，用户可参见附录12.1)；
//        // 一般情况下会通过onResults接口多次返回结果，完整的识别内容是多次结果的累加；
//        // 关于解析Json的代码可参见MscDemo中JsonParser类；
//        // isLast等于true时会话结束。
//        @Override
//        public void onResult(RecognizerResult result, boolean b) {
//        }
//
//        // 会话发生错误回调接口
//        @Override
//        public void onError(SpeechError error) {
//            error.getPlainDescription(true);//获取错误码描述
//            LogTool.i("讯飞识别错误:" + error.getMessage());
//            sb = null;
//            // if(null != voice2CharacterListener){
//            //     voice2CharacterListener.onFailure();
//            // }
//        }
//
//        //音量值0~30
//        @Override
//        public void onVolumeChanged(int i, byte[] bytes) {
//
//        }
//
//
//        //结束录音
//        public void onEndOfSpeech() {
//        }
//
//        //扩展用接口
//        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
//        }
//    };
//
//    private RecognizerDialogListener recognizerDialogListener = new RecognizerDialogListener() {
//        @Override
//        public void onResult(RecognizerResult recognizerResult, boolean b) {
//            Voice2CharacterBean voice2CharacterBean = (Voice2CharacterBean) GsonTool.string2Object(recognizerResult.getResultString(), Voice2CharacterBean.class);
//            if (null != voice2CharacterBean) {
//                List<Voice2CharacterBean.WsBean> wsBeanList = voice2CharacterBean.getWs();
//                for (Voice2CharacterBean.WsBean wsBean : wsBeanList) {
//                    if (null != wsBean) {
//                        List<Voice2CharacterBean.WsBean.CwBean> cwBeanList = wsBean.getCw();
//                        for (Voice2CharacterBean.WsBean.CwBean cwBean : cwBeanList) {
//                            if (null != cwBean) {
//                                // 当得分满足一定条件才记录该语句
//                                // if (cwBean.getSc() >= 60)
//                                //     sb.append(cwBean.getW());
//                                // else
//                                sb.append(cwBean.getW());
//                            }
//                        }
//                    }
//                }
//                if (voice2CharacterBean.isLs()) {
//                    /**
//                     * 如果语音识别结束，调用 {@link Voice2CharacterListener#onEndOfSpeech(String)}
//                     * 并将 {@link sb} 置为 null
//                     */
//                    if (null != voice2CharacterListener) {
//                        voice2CharacterListener.onEndOfSpeech(null == sb ? "" : sb.toString());
//                        voice2CharacterListener.onEndOfSpeechWithoutPunctuation(null == sb ? "" : sb.toString().replace("。", "").replace("，", ""));
//                    }
//                    sb = null;
//                }
//            }
//        }
//
//        @Override
//        public void onError(SpeechError speechError) {
//            if (null != voice2CharacterListener) {
//                voice2CharacterListener.onFailure();
//            }
//        }
//    };
//
//    private InitListener initListener = new InitListener() {
//
//        @Override
//        public void onInit(int i) {
//
//        }
//    };

    public void start() {
        // 开始听写
//        mIat.startListening(mRecoListener);
    }

    /**
     * 获得听写内容
     */
    public String getCharacters() {
//        return lastSpeechContent;
        return "";
    }

    /**
     * 语音识别回调
     */
    public static class Voice2CharacterListener {

        public Voice2CharacterListener() {

        }

        /**
         * 语音识别结束
         *
         * @param resultString 语音内容
         */
        public void onEndOfSpeech(String resultString) {
            LogTool.i("识别结果：" + resultString);
        }

        /**
         * 语音识别结束
         *
         * @param resultString 语音内容没有标点符号
         */
        public void onEndOfSpeechWithoutPunctuation(String resultString) {
            LogTool.i("识别结果：" + resultString);
        }

        /**
         * 识别失败
         */
        public void onFailure() {

        }
    }

    public void setVoice2CharacterListener(Voice2CharacterListener voice2CharacterListener) {
//        this.voice2CharacterListener = voice2CharacterListener;
    }

}
