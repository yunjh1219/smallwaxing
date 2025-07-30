package com.example.smallwaxing.global.common;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@NoArgsConstructor
@Getter
public class SuccessResponse<T> {
	private int status;
	private String message;
	private T data;

	@Builder
	public SuccessResponse(int status, String message, @Nullable T data) {
		this.status = status;
		this.message = message;
		this.data = data;
	}
}
