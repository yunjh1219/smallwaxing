package com.example.smallwaxing.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginRequestDto {
		@NotBlank(message = "아이디를 입력해주세요")
		private String userNum;

		@NotBlank(message = "비밀번호를 입력해주세요")
		private String password;


		@Builder
		public LoginRequestDto(String userNum, String password) {
			this.userNum = userNum;
			this.password = password;

		}
}
