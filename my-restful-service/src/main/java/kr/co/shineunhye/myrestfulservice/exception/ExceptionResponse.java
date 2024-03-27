package kr.co.shineunhye.myrestfulservice.exception;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor //default 생성자를 생성
public class ExceptionResponse {
	private Date timestamp;
	private String message;
	private String deatils;
}
