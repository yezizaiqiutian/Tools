package com.gh.utils.net;

import java.io.Serializable;

/**
 * author: gh
 * time: 2016/10/5.
 * description:
 */
public class BaseModel<T> implements Serializable{
    public String error;
    public String msg;
    public T results;

    public boolean success(){
        return error.equals("false");
    }
}
