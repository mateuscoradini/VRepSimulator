package br.com.pos.unicamp.vrep.exceptions;

public class VRepClientException extends RuntimeException {
	
	private String msg;

	/**
	 * 
	 */
	private static final long serialVersionUID = 6793127961397855844L;
	
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
