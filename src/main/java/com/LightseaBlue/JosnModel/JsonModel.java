package com.LightseaBlue.JosnModel;

import java.io.Serializable;

/**
   *   协议
 * @author LightseaBlue
 *
 */
public class JsonModel implements Serializable {
	private static final long serialVersionUID = -1085886377277231850L;
	private Integer code;  //状态码
	private Object obj;  //回传数据
	private String message;   //成功或者错误信息

	

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "JsonModel [code=" + code + ", obj=" + obj + ", message=" + message + "]";
	}


}
