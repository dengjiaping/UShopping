package com.brand.ushopping.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Objects;

/**
 * Created by Administrator on 2015/12/14.
 */
public class Charge implements Parcelable
{
    private long amount;
    private int amountRefunded;
    private int amountSettle;
    private String app;
    private String body;
    private String channel;
    private String clientIp;
    private String created;
    private Credential credential;
    private String currency;
    private Object extra;
    private String id;
    private boolean livemode;
    private Object metadata;
    private String object;
    private String orderNo;
    private boolean paid;
    private boolean refunded;
    private Refunds refunds;
    private String subject;
    private long timeExpire;

    private boolean success;
    private String msg;

    public Charge()
    {

    }

    protected Charge(Parcel in) {
        amount = in.readLong();
        amountRefunded = in.readInt();
        amountSettle = in.readInt();
        app = in.readString();
        body = in.readString();
        channel = in.readString();
        clientIp = in.readString();
        created = in.readString();
        currency = in.readString();
        id = in.readString();
        livemode = in.readByte() != 0;
        object = in.readString();
        orderNo = in.readString();
        paid = in.readByte() != 0;
        refunded = in.readByte() != 0;
        subject = in.readString();
        timeExpire = in.readLong();
        success = in.readByte() != 0;
        msg = in.readString();
    }

    public static final Creator<Charge> CREATOR = new Creator<Charge>() {
        @Override
        public Charge createFromParcel(Parcel in) {
            return new Charge(in);
        }

        @Override
        public Charge[] newArray(int size) {
            return new Charge[size];
        }
    };

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public int getAmountRefunded() {
        return amountRefunded;
    }

    public void setAmountRefunded(int amountRefunded) {
        this.amountRefunded = amountRefunded;
    }

    public int getAmountSettle() {
        return amountSettle;
    }

    public void setAmountSettle(int amountSettle) {
        this.amountSettle = amountSettle;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Credential getCredential() {
        return credential;
    }

    public void setCredential(Credential credential) {
        this.credential = credential;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Object getExtra() {
        return extra;
    }

    public void setExtra(Object extra) {
        this.extra = extra;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isLivemode() {
        return livemode;
    }

    public void setLivemode(boolean livemode) {
        this.livemode = livemode;
    }

    public Object getMetadata() {
        return metadata;
    }

    public void setMetadata(Object metadata) {
        this.metadata = metadata;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public boolean isRefunded() {
        return refunded;
    }

    public void setRefunded(boolean refunded) {
        this.refunded = refunded;
    }

    public Refunds getRefunds() {
        return refunds;
    }

    public void setRefunds(Refunds refunds) {
        this.refunds = refunds;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public long getTimeExpire() {
        return timeExpire;
    }

    public void setTimeExpire(long timeExpire) {
        this.timeExpire = timeExpire;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(amount);
        dest.writeInt(amountRefunded);
        dest.writeInt(amountSettle);
        dest.writeString(app);
        dest.writeString(body);
        dest.writeString(channel);
        dest.writeString(clientIp);
        dest.writeString(created);
        dest.writeString(currency);
        dest.writeString(id);
        dest.writeByte((byte) (livemode ? 1 : 0));
        dest.writeString(object);
        dest.writeString(orderNo);
        dest.writeByte((byte) (paid ? 1 : 0));
        dest.writeByte((byte) (refunded ? 1 : 0));
        dest.writeString(subject);
        dest.writeLong(timeExpire);
        dest.writeByte((byte) (success ? 1 : 0));
        dest.writeString(msg);
    }
}
