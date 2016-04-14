package com.brand.ushopping.utils;

/**
 * Created by viator42 on 15/5/4.
 * 系统常量
 */
public class StaticValues
{
    public final static int ICON_WIDTH = 255;
    public final static int ICON_HEIGHT = 255;

    public final static double PI = Math.PI; //3.14159265;  //π
    public final static double EARTH_RADIUS = 6378137;    //地球半径
    public final static double RAD = Math.PI / 180.0;   //   π/180

    public final static int RECOMMEND_PAGE_COUNT = 10;
    public final static int GOODS_PAGE_COUNT = 10;

    public final static int ADDRESS_ACTION_NEW = 1;
    public final static int ADDRESS_ACTION_UPDATE = 2;

    public final static float ALPHA_HALF = new Float(0.5);

    public final static int CONNECTION_TIMEOUT = 10000; //连接超时的时间 /毫秒

    //---------------------------微博登录--------------------------------
    public static final String APP_KEY = "866304116"; // 应用的APP_KEY
    public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";// 应用的回调页
    public static final String SCOPE = // 应用申请的 高级权限
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";

    //---------------------------QQ登录--------------------------------
    public static final String TENCENT_APP_ID = "1104902981";

    //---------------------------微信登录--------------------------------
    public static final String WX_APP_ID = "1104902981";

    // 付款方式：0 现金, 1 微信, 2 银联, 3 支付宝
    public static final int ORDER_PAY_CASH = 0;
    public static final int ORDER_PAY_WX = 1;
    public static final int ORDER_PAY_UNACP = 2;
    public static final int ORDER_PAY_ALIPAY = 3;

    //---------------------------ping++支付--------------------------------
    public static final int REQUEST_CODE_PAYMENT = 101;

    public static final String PAY_METHOD_CASH = "cash";
    public static final String PAY_METHOD_UPACP = "upacp";
    public static final String PAY_METHOD_ALIPAY = "alipay";
    public static final String PAY_METHOD_WX = "wx";

    public static final String PAYMENT_RESULT_SUCCESS = "success";
    public static final String PAYMENT_RESULT_FAIL = "fail";
    public static final String PAYMENT_RESULT_CANCEL = "cancel";
    public static final String PAYMENT_RESULT_INVALID = "invalid";

    //---------------------------银联--------------------------------
//    public static final String UNION_PAY_SERVER_MODE = "00";

    //订单分类flag      //0 未付款, 1 已付款, 2 已发货
    public static final int ORDER_FLAG_ALL = 9;
    public static final int ORDER_FLAG_UNPAY = 0;
    public static final int ORDER_FLAG_PAID = 1;
    public static final int ORDER_FLAG_DELIVERED = 2;
    public static final int ORDER_FLAG_CONFIRMED = 3;

    //  购买方式
    public static final int BOUTHT_TYPE_NORMAL = 1;         //普通
    public static final int BOUTHT_TYPE_RESERVATION = 2;    //到店预订
    public static final int BOUTHT_TYPE_TRYIT = 3;          //上门试衣

    // 购物车类型
    public static final int CART_TYPE_NORMAL = 1;         //普通
    public static final int CART_TYPE_RESERVATION = 2;    //到店预订
    public static final int CART_TYPE_TRYIT = 3;          //上门试衣

    // 预约订单分类flag   0 未付款, 1 已付款, 2 已发货
    public static final int RESERVATION_ORDER_FLAG_ALL = 9;
    public static final int RESERVATION_ORDER_FLAG_UNPAID = 0;
    public static final int RESERVATION_ORDER_FLAG_PAID = 1;
    public static final int RESERVATION_ORDER_FLAG_DELIVERED = 2;

    // 上门试衣订单分类flag flag: 0 未付款, 1 已付款, 2 已发货
    public static final int TRYOUT_ORDER_FLAG_ALL = 9;
    public static final int TRYOUT_ORDER_FLAG_UNPAID = 0;
    public static final int TRYOUT_ORDER_FLAG_PAID = 1;
    public static final int TRYOUT_ORDER_FLAG_DELIVERED = 2;

    // 订单商品删减flag
    public static final int ORDER_UPDATE_FLAG_REMOVED = -1;
    public static final int ORDER_UPDATE_FLAG_UNPAID = 0;
    public static final int ORDER_UPDATE_FLAG_PAID = 1;

    public static final int SYSTEM_TYPE_ANDROID = 1;

    public static final int BRAND_GOODS_TYPE_NEW = 1;
    public static final int BRAND_GOODS_TYPE_PRICE = 2;
    public static final int BRAND_GOODS_TYPE_SALE = 3;
    public static final int BRAND_GOODS_TYPE_UNDEF = 4;

    public static final int GOODS_VIEW_PAGE_INFO = 1;
    public static final int GOODS_VIEW_PAGE_DETAIL = 2;
    public static final int GOODS_VIEW_PAGE_COMMENT = 3;

    public static final int SELECT_DATE_TIME_INTERVAL = 30;

    //订单确认操作
    public static final int ORDER_COMFIRM_GEN_ORDER = 1;    //生成订单
    public static final int ORDER_COMFIRM_PAY = 2;          //订单支付

    public static final int SEARCH_MODE_NONE = 0;
    public static final int SEARCH_MODE_PROPERTY = 1;
    public static final int SEARCH_MODE_RESULT = 2;

    public static final String SEARCH_TYPE_NAME = "名称";
    public static final String SEARCH_TYPE_CODE = "货号";

    //订单退款状态
    public static final int CUSTOMER_FLAG_NONE = 9;
    public static final int CUSTOMER_FLAG_APPLY = 1;
    public static final int CUSTOMER_FLAG_SUBMITED = 2;
    public static final int CUSTOMER_FLAG_DONE = 3;

    //
    public static final int GOODS_COMPARE_TYPE_TIME = 1;
    public static final int GOODS_COMPARE_TYPE_PRICE = 2;
    public static final int GOODS_COMPARE_TYPE_SALE = 3;

    public static final int GOODS_FAVOURITE_ON = 1;
    public static final int GOODS_FAVOURITE_OFF = 0;

    public static final int BRAND_ENTER_TYPE_NORMAL = 1;
    public static final int BRAND_ENTER_TYPE_AROUND = 2;

    public final static int callAttributePopupNormal = 1;
    public final static int callAttributePopupAddToCart = 2;
    public final static int callAttributePopupBuy = 3;

    //
    public final static int MAIN_ACTIVITY_TAB_MAINPAGE = 1;
    public final static int MAIN_ACTIVITY_TAB_BRAND = 2;
    public final static int MAIN_ACTIVITY_TAB_THEME = 3;
    public final static int MAIN_ACTIVITY_TAB_MINE = 4;
    public final static int MAIN_ACTIVITY_TAB_CART = 5;

    //收货地址列表进入方式
    public final static int ADDRESSES_ENTER_MODE_EDIT = 1;
    public final static int ADDRESSES_ENTER_MODE_PICK = 2;

    public final static int CODE_ADDRESSES_PICK = 1001;
    public final static int CODE_VOUCHER_PICK = 1002;

    // 商品评价类型
    public final static int GOODS_EVALUATE_TYPE_NORMAL = 0; //正常
    public final static int GOODS_EVALUATE_TYPE_UNAMED = 1; //匿名

    //排序类型
    public final static int ARRENGE_NONE = 0;
    public final static int ARRENGE_TIME_ASC = 1;
    public final static int ARRENGE_TIME_DESC = 2;
    public final static int ARRENGE_PRICE_ASC = 3;
    public final static int ARRENGE_PRICE_DESC = 4;

    public final static int CACHE_LIFE = 60;

    public final static int VOUCHER_ENTER_LIST = 1;
    public final static int VOUCHER_ENTER_MINE = 2;
    public final static int VOUCHER_ENTER_PICK = 3;
    public final static int VOUCHER_ENTER_BUNDLE = 4;

    //优惠券地址
    public final static String voucherAddress = "/yhq.html";

    //活动类型
    public final static int ACTIVITY_FLAG_WEBPAGE = 0;  //网页
    public final static int ACTIVITY_FLAG_VOUCHER = 3;  //优惠券

    //注册handler
    public final static int GET_SMS_CODE_HANDLER = 1000;

}
