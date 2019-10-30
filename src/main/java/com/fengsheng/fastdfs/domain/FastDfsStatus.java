package com.fengsheng.fastdfs.domain;

public enum FastDfsStatus {

	/** The success. */
	SUCCESS("success", "0"),

	/** The failure. */
	FAILURE("failure", "1"),

	/** The ok. */
	OK("ok", "2");

	FastDfsStatus(String message, String code) {
		this.message = message;
		this.code = code;
	}

	String code;

	String message;

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public static FastDfsStatus getDsesStatusByCode(final String code) {
		FastDfsStatus dsesStatus = null;

		if (null != code) {
			for (final FastDfsStatus key : FastDfsStatus.values()) {
				if (key.getCode().equals(code)) {
					dsesStatus = key;
					break;
				}
			}
		}

		return dsesStatus;
	}

}
