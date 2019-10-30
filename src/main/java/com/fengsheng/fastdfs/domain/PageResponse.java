package com.fengsheng.fastdfs.domain;

import java.util.List;

import lombok.Data;

/**
 * The Class TableResponse.
 *
 * @author gwm
 * @param <T> the generic type
 */
@Data
public class PageResponse<T> {

	/** The code. */
	private String code = FastDfsStatus.SUCCESS.getCode();

	/** The msg. */
	private String msg = FastDfsStatus.SUCCESS.getMessage();

	/** The count. */
	private Long count;

	/** The data. */
	private List<T> data;
	

}
