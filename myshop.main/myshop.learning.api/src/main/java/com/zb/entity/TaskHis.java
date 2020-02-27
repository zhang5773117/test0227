package com.zb.entity;

import java.io.Serializable;

public class TaskHis implements Serializable {
    private String id;
    private String create_time;
    private String update_time;
    private String delete_time;
    private String task_type;
    private String mq_exchange;
    private String mq_routingkey;
    private String request_body;
    private int version;
    private String status;
    private String errormsg;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getDelete_time() {
        return delete_time;
    }

    public void setDelete_time(String delete_time) {
        this.delete_time = delete_time;
    }

    public String getTask_type() {
        return task_type;
    }

    public void setTask_type(String task_type) {
        this.task_type = task_type;
    }

    public String getMq_exchange() {
        return mq_exchange;
    }

    public void setMq_exchange(String mq_exchange) {
        this.mq_exchange = mq_exchange;
    }

    public String getMq_routingkey() {
        return mq_routingkey;
    }

    public void setMq_routingkey(String mq_routingkey) {
        this.mq_routingkey = mq_routingkey;
    }

    public String getRequest_body() {
        return request_body;
    }

    public void setRequest_body(String request_body) {
        this.request_body = request_body;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrormsg() {
        return errormsg;
    }

    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
    }
}
